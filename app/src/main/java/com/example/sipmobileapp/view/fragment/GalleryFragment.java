package com.example.sipmobileapp.view.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.sipmobileapp.R;
import com.example.sipmobileapp.adapter.BitmapAdapter;
import com.example.sipmobileapp.databinding.FragmentGalleryBinding;
import com.example.sipmobileapp.eventbus.UpdateEvent;
import com.example.sipmobileapp.model.AttachInfo;
import com.example.sipmobileapp.model.AttachResult;
import com.example.sipmobileapp.model.ServerData;
import com.example.sipmobileapp.utils.SipMobileAppPreferences;
import com.example.sipmobileapp.view.activity.FullScreenImageContainerActivity;
import com.example.sipmobileapp.view.dialog.AttachmentDialogFragment;
import com.example.sipmobileapp.view.dialog.ErrorDialogFragment;
import com.example.sipmobileapp.viewmodel.AttachmentViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GalleryFragment extends Fragment {
    private FragmentGalleryBinding binding;
    private AttachmentViewModel viewModel;

    private int patientID, index;
    private boolean flag;
    private BitmapAdapter adapter;
    private List<AttachInfo> attachInfoList = new ArrayList<>();
    private List<Bitmap> oldBitmapList = new ArrayList<>();
    private List<Bitmap> newBitmapList = new ArrayList<>();
    private Map<Bitmap, String> mapBitmap = new HashMap<>();
    private Map<Uri, String> mapUri = new HashMap<>();

    private static final int TABLET_SPAN_COUNT = 4;
    private static final int PHONE_SPAN_COUNT = 3;
    private static final int REQUEST_CODE_CAMERA_PERMISSION = 0;
    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION = 1;

    private static final String ARGS_PATIENT_ID = "patientID";

    private static final String TAG = GalleryFragment.class.getSimpleName();

    public static GalleryFragment newInstance(int patientID) {
        GalleryFragment fragment = new GalleryFragment();
        Bundle args = new Bundle();
        args.putInt(ARGS_PATIENT_ID, patientID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createViewModel();

        patientID = getArguments().getInt(ARGS_PATIENT_ID);

        if (hasWriteExternalStoragePermission()) {
            getPatientAttachmentList(patientID);
        } else {
            requestWriteExternalStoragePermission();
        }
    }

    private void createViewModel() {
        viewModel = new ViewModelProvider(requireActivity()).get(AttachmentViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_gallery,
                container,
                false);

        initViews();
        handleClicked();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupObserver();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        int attachID = SipMobileAppPreferences.getAttachId(getContext());
        if (attachID != 0) {
            Bitmap bitmap = null;
            File file = new File(Environment.getExternalStorageDirectory(), "Attachments");
            if (file.exists()) {
                File[] files = file.listFiles();
                if (files.length != 0) {
                    for (File f : files) {
                        if (f.getName().contains(String.valueOf(attachID))) {
                            bitmap = BitmapFactory.decodeFile(f.getAbsolutePath());
                            f.delete();
                        }
                    }
                }
            }

            List<Bitmap> bitmaps = new ArrayList<>();
            for (Bitmap b : newBitmapList) {
                if (!b.sameAs(bitmap)) {
                    bitmaps.add(b);
                }
            }

            adapter.updateBitmapList(bitmaps);
            binding.recyclerViewAttachmentFile.setAdapter(adapter);
            newBitmapList.clear();
            newBitmapList.addAll(bitmaps);

            SipMobileAppPreferences.setAttachId(getContext(), 0);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(sticky = true)
    public void updateGallery(UpdateEvent updateEvent) {
        getAttachInfo(updateEvent.getNewAttachID());

        Bitmap bitmap = null;
        File file = new File(Environment.getExternalStorageDirectory(), "Attachments");
        if (file.exists()) {
            File[] files = file.listFiles();
            if (files.length != 0) {
                for (File f : files) {
                    if (f.getName().contains(String.valueOf(updateEvent.getOldAttachID()))) {
                        bitmap = BitmapFactory.decodeFile(f.getAbsolutePath());
                        f.delete();
                    }
                }
            }
        }

        List<Bitmap> bitmaps = new ArrayList<>();
        for (Bitmap b : newBitmapList) {
            if (!b.sameAs(bitmap)) {
                bitmaps.add(b);
            }
        }

        adapter.updateBitmapList(bitmaps);
        binding.recyclerViewAttachmentFile.setAdapter(adapter);
        newBitmapList.clear();
        newBitmapList.addAll(bitmaps);

        EventBus.getDefault().removeStickyEvent(updateEvent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_CAMERA_PERMISSION:
                if (grantResults == null || grantResults.length == 0) {
                    return;
                }
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    viewModel.getAllowPermission().setValue(true);
                } else {
                    ErrorDialogFragment fragment = ErrorDialogFragment.newInstance(getString(R.string.no_access_camera_permission));
                    fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
                }
                break;
            case REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION:
                if (grantResults == null || grantResults.length == 0) {
                    return;
                }
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getPatientAttachmentList(patientID);
                } else {
                    binding.progressBarLoading.setVisibility(View.GONE);
                    ErrorDialogFragment fragment = ErrorDialogFragment.newInstance(getString(R.string.no_access_storage_permission));
                    fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
                }
                break;
        }
    }

    private boolean hasWriteExternalStoragePermission() {
        return (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }

    private void getPatientAttachmentList(int patientID) {
        String centerName = SipMobileAppPreferences.getCenterName(getContext());
        String userLoginKey = SipMobileAppPreferences.getUserLoginKey(getContext());
        ServerData serverData = viewModel.getServerData(centerName);
        viewModel.getPatientAttachmentListService(serverData.getIPAddress() + ":" + serverData.getPort());
        viewModel.patientAttachmentList(userLoginKey, patientID);
    }

    private void requestWriteExternalStoragePermission() {
        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION);
    }

    private void initViews() {
        if (isTablet()) {
            binding.recyclerViewAttachmentFile.setLayoutManager(new GridLayoutManager(getContext(), TABLET_SPAN_COUNT));
        } else {
            binding.recyclerViewAttachmentFile.setLayoutManager(new GridLayoutManager(getContext(), PHONE_SPAN_COUNT));
        }
    }

    private void handleClicked() {
        binding.fabAddNewFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AttachmentDialogFragment fragment = AttachmentDialogFragment.newInstance(patientID);
                fragment.show(getParentFragmentManager(), AttachmentDialogFragment.TAG);
            }
        });
    }

    private void setupObserver() {
        viewModel.getRequestPermission().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isRequestPermission) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA_PERMISSION);
            }
        });

        viewModel.getPatientAttachmentListResultSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<AttachResult>() {
            @Override
            public void onChanged(AttachResult attachResult) {
                if (attachResult.getAttachs() != null & attachResult.getAttachs().length == 0) {
                    binding.progressBarLoading.setVisibility(View.GONE);
                } else {
                    for (int i = 0; i < attachResult.getAttachs().length; i++) {
                        Bitmap bitmap = readFromStorage(attachResult.getAttachs()[i]);
                        if (bitmap != null) {
                            binding.progressBarLoading.setVisibility(View.GONE);
                            binding.recyclerViewAttachmentFile.setVisibility(View.VISIBLE);
                            setupAdapter();
                        }
                    }

                    if (index < attachInfoList.size()) {
                        getAttachInfo(attachResult.getAttachs()[index].getAttachID());
                    }
                }
            }
        });

        viewModel.getErrorPatientAttachmentListResultSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String message) {
                binding.progressBarLoading.setVisibility(View.GONE);
                ErrorDialogFragment fragment = ErrorDialogFragment.newInstance(message);
                fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
            }
        });

        viewModel.getAttachInfoResultSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<AttachResult>() {
            @Override
            public void onChanged(AttachResult attachResult) {
                GalleryAsyncTask galleryAsyncTask = new GalleryAsyncTask();
                galleryAsyncTask.execute(attachResult.getAttachs()[0]);
            }
        });

        viewModel.getErrorAttachInfoResultSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String message) {
                binding.progressBarLoading.setVisibility(View.GONE);
                ErrorDialogFragment fragment = ErrorDialogFragment.newInstance(message);
                fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
            }
        });

        viewModel.getNoConnectivitySingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String message) {
                binding.progressBarLoading.setVisibility(View.GONE);
                ErrorDialogFragment fragment = ErrorDialogFragment.newInstance(message);
                fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
            }
        });

        viewModel.getTimeOutExceptionHappenSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isTimeOutExceptionHappen) {
                binding.progressBarLoading.setVisibility(View.GONE);
                ErrorDialogFragment fragment = ErrorDialogFragment.newInstance(getString(R.string.error_connection));
                fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
            }
        });

        viewModel.getUpdateGallery().observe(getViewLifecycleOwner(), new Observer<AttachResult>() {
            @Override
            public void onChanged(AttachResult attachResult) {
                getAttachInfo(attachResult.getAttachs()[0].getAttachID());
            }
        });

        viewModel.getTestShowFullScreenImage().observe(getViewLifecycleOwner(), new Observer<Map<Uri, String>>() {
            @Override
            public void onChanged(Map<Uri, String> map) {
                Uri uri = null;
                String imageName = "";
                for (Map.Entry<Uri, String> entry : map.entrySet()) {
                    uri = entry.getKey();
                    imageName = entry.getValue();
                }
                imageName = imageName.replace(".jpg", "");
                int attachID = Integer.valueOf(imageName);

                Intent intent = FullScreenImageContainerActivity.newIntent(getContext(), uri, attachID);
                startActivity(intent);
            }
        });

        viewModel.getDeleteImageFromGallery().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer attachID) {
                Bitmap bitmap = null;
                File file = new File(Environment.getExternalStorageDirectory(), "Attachments");
                if (file.exists()) {
                    File[] files = file.listFiles();
                    if (files.length != 0) {
                        for (File f : files) {
                            if (f.getName().contains(String.valueOf(attachID))) {
                                bitmap = BitmapFactory.decodeFile(f.getAbsolutePath());
                                f.delete();
                            }
                        }
                    }
                }

                List<Bitmap> bitmaps = new ArrayList<>();
                for (Bitmap b : newBitmapList) {
                    if (!b.sameAs(bitmap)) {
                        bitmaps.add(b);
                    }
                }

                adapter.updateBitmapList(bitmaps);
                binding.recyclerViewAttachmentFile.setAdapter(adapter);
                newBitmapList.clear();
                newBitmapList.addAll(bitmaps);
            }
        });
    }

    private Bitmap readFromStorage(AttachInfo attachInfo) {
        Bitmap bitmap = null;
        File file = new File(Environment.getExternalStorageDirectory(), "Attachments");
        if (file.exists()) {
            File[] files = file.listFiles();

            if (files.length == 0) {
                if (!hasAttachInfo(attachInfo.getAttachID())) {
                    attachInfoList.add(attachInfo);
                }
            } else {
                for (File f : files) {
                    if (f.getName().equals(attachInfo.getAttachID() + ".jpg")) {
                        flag = true;
                        bitmap = BitmapFactory.decodeFile(f.getAbsolutePath());
                        if (adapter == null) {
                            oldBitmapList.add(bitmap);
                            newBitmapList.addAll(oldBitmapList);
                        } else {
                            newBitmapList.add(bitmap);
                        }
                        mapBitmap.put(bitmap, f.getName());
                        mapUri.put(Uri.fromFile(f), f.getName());
                        break;
                    }
                }
                if (!flag) {
                    if (!hasAttachInfo(attachInfo.getAttachID())) {
                        attachInfoList.add(attachInfo);
                    }
                } else {
                    flag = false;
                }
            }
        } else {
            if (!hasAttachInfo(attachInfo.getAttachID())) {
                attachInfoList.add(attachInfo);
            }
        }
        return bitmap;
    }

    private boolean hasAttachInfo(int attachID) {
        for (AttachInfo attachInfo : attachInfoList) {
            if (attachInfo.getAttachID() == attachID) {
                return true;
            }
        }
        return false;
    }

    private AttachInfo writeToStorage(AttachInfo attachInfo) throws IOException {
        File fileDir = new File(Environment.getExternalStorageDirectory(), "Attachments");
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }

        File file = new File(fileDir, attachInfo.getAttachID() + ".jpg");

        if (attachInfo.getFileBase64() != null & attachInfo.getDeleteUserID() == 0) {
            byte[] stringAsBytes = Base64.decode(attachInfo.getFileBase64(), 0);
            FileOutputStream fileOutputStream = new FileOutputStream(file, true);
            fileOutputStream.write(stringAsBytes);
            fileOutputStream.flush();
            fileOutputStream.close();
        }
        return attachInfo;
    }

    private void setupAdapter() {
        if (adapter == null) {
            adapter = new BitmapAdapter(getContext(), oldBitmapList, viewModel);
        } else {
            adapter.updateBitmapList(newBitmapList);
        }
        adapter.setMapBitmap(mapBitmap);
        adapter.setMapUri(mapUri);
        binding.recyclerViewAttachmentFile.setAdapter(adapter);
    }

    private void getAttachInfo(int attachID) {
        String centerName = SipMobileAppPreferences.getCenterName(getContext());
        String userLoginKey = SipMobileAppPreferences.getUserLoginKey(getContext());
        ServerData serverData = viewModel.getServerData(centerName);
        viewModel.getAttachInfoService(serverData.getIPAddress() + ":" + serverData.getPort());
        viewModel.attachInfo(userLoginKey, attachID);
    }

    private class GalleryAsyncTask extends AsyncTask<AttachInfo, Void, AttachInfo> {

        @Override
        protected AttachInfo doInBackground(AttachInfo... attachInfoArray) {
            AttachInfo attachInfo = null;
            try {
                attachInfo = writeToStorage(attachInfoArray[0]);
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
            return attachInfo;
        }

        @Override
        protected void onPostExecute(AttachInfo attachInfo) {
            if (attachInfo != null) {
                Bitmap bitmap = readFromStorage(attachInfo);
                if (bitmap != null) {
                    binding.progressBarLoading.setVisibility(View.GONE);
                    binding.recyclerViewAttachmentFile.setVisibility(View.VISIBLE);
                    setupAdapter();
                }
            }
            index++;
            if (index < attachInfoList.size()) {
                getAttachInfo(attachInfoList.get(index).getAttachID());
            } else {
                binding.progressBarLoading.setVisibility(View.GONE);
            }
        }
    }

    public boolean isTablet() {
        boolean xlarge = ((getContext().getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
        boolean large = ((getContext().getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
        return (xlarge || large);
    }
}