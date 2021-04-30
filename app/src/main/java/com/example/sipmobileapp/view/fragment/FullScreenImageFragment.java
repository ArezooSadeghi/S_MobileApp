package com.example.sipmobileapp.view.fragment;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.example.sipmobileapp.R;
import com.example.sipmobileapp.databinding.FragmentFullScreenImageBinding;
import com.example.sipmobileapp.eventbus.UpdateEvent;
import com.example.sipmobileapp.model.AttachParameter;
import com.example.sipmobileapp.model.AttachResult;
import com.example.sipmobileapp.model.ServerData;
import com.example.sipmobileapp.utils.SipMobileAppPreferences;
import com.example.sipmobileapp.view.dialog.DeleteQuestionDialogFragment;
import com.example.sipmobileapp.view.dialog.ErrorDialogFragment;
import com.example.sipmobileapp.view.dialog.SaveChangeDialogFragment;
import com.example.sipmobileapp.view.dialog.SuccessRemoveAttachDialogFragment;
import com.example.sipmobileapp.viewmodel.AttachmentViewModel;
import com.theartofdev.edmodo.cropper.CropImage;

import org.greenrobot.eventbus.EventBus;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class FullScreenImageFragment extends Fragment {
    private FragmentFullScreenImageBinding binding;
    private AttachmentViewModel viewModel;

    private Uri uri;
    private int attachID, oldAttachID;
    private Bitmap bitmap;

    private static final String ARGS_IMAGE = "image";
    private static final String ARGS_ATTACH_ID = "attachID";

    public static final String TAG = FullScreenImageFragment.class.getSimpleName();

    public static FullScreenImageFragment newInstance(Uri image, int attachID) {
        FullScreenImageFragment fragment = new FullScreenImageFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARGS_IMAGE, image);
        args.putInt(ARGS_ATTACH_ID, attachID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        attachID = getArguments().getInt(ARGS_ATTACH_ID);
        createViewModel();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_full_screen_image,
                container,
                false);

        initViews();
        handleClicked();

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        String uriAsString = SipMobileAppPreferences.getUri(getContext());
        if (uriAsString != null) {
            try {
                binding.imgViewOk.setVisibility(View.VISIBLE);
                binding.imgViewCancel.setVisibility(View.VISIBLE);
                binding.imgViewDelete.setVisibility(View.GONE);
                binding.imgViewEdit.setVisibility(View.GONE);

                Uri uri = Uri.parse(uriAsString);
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                binding.imgViewFullScreen.setImage(ImageSource.bitmap(bitmap));
            } catch (IOException exception) {
                Log.e(TAG, exception.getMessage());
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupObserver();
    }

    private void createViewModel() {
        viewModel = new ViewModelProvider(requireActivity()).get(AttachmentViewModel.class);
    }

    private void initViews() {
        uri = getArguments().getParcelable(ARGS_IMAGE);
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
        } catch (IOException exception) {
            Log.e(TAG, exception.getMessage());
        }
        binding.imgViewFullScreen.setImage(ImageSource.bitmap(bitmap));
    }

    private void handleClicked() {
        binding.imgViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteQuestionDialogFragment fragment = DeleteQuestionDialogFragment.newInstance("آیا می خواهید فایل مربوطه را حذف کنید؟");
                fragment.show(getParentFragmentManager(), DeleteQuestionDialogFragment.TAG);
            }
        });

        binding.imgViewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity(uri)
                        .setAllowFlipping(false)
                        .setCropMenuCropButtonTitle(getString(R.string.crop))
                        .start((Activity) getContext());
            }
        });

        binding.imgViewOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveChangeDialogFragment fragment = SaveChangeDialogFragment.newInstance("آیا می خواهید تغییرات را ذخیره کنید؟");
                fragment.show(getParentFragmentManager(), SaveChangeDialogFragment.TAG);
            }
        });

        binding.imgViewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SipMobileAppPreferences.setUri(getContext(), null);
                getActivity().finish();
            }
        });
    }

    private void setupObserver() {
        viewModel.getDeleteAttachResultSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<AttachResult>() {
            @Override
            public void onChanged(AttachResult attachResult) {
                if (SipMobileAppPreferences.getUri(getContext()) != null) {
                    SipMobileAppPreferences.setUri(getContext(), null);
                    oldAttachID = attachResult.getAttachs()[0].getAttachID();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String base64 = convertBitmapToStringBase64(bitmap);
                            viewModel.getCropAddAttachment().postValue(base64);
                        }
                    }).start();
                } else {
                    SuccessRemoveAttachDialogFragment fragment = SuccessRemoveAttachDialogFragment.newInstance(attachResult.getAttachs()[0].getAttachID(), "فایل با موفقیت حذف شد");
                    fragment.show(getParentFragmentManager(), SuccessRemoveAttachDialogFragment.TAG);
                }
            }
        });

        viewModel.getErrorDeleteAttachResultSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String message) {
                ErrorDialogFragment fragment = ErrorDialogFragment.newInstance(message);
                fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
            }
        });

        viewModel.getNoConnectivitySingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String message) {
                ErrorDialogFragment fragment = ErrorDialogFragment.newInstance(message);
                fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
            }
        });

        viewModel.getTimeOutExceptionHappenSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isTimeOutExceptionHappen) {
                ErrorDialogFragment fragment = ErrorDialogFragment.newInstance(getString(R.string.error_connection));
                fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
            }
        });

        viewModel.getYesDelete().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean yesDelete) {
                String centerName = SipMobileAppPreferences.getCenterName(getContext());
                String userLoginKey = SipMobileAppPreferences.getUserLoginKey(getContext());
                ServerData serverData = viewModel.getServerData(centerName);
                viewModel.getDeleteAttachService(serverData.getIPAddress() + ":" + serverData.getPort());
                viewModel.deleteAttach(userLoginKey, attachID);
            }
        });

        viewModel.getFinish().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean finished) {
                getActivity().finish();
            }
        });

        viewModel.getCropAddAttachment().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String stringBase64) {
                AttachParameter attachParameter = new AttachParameter();
                int sickID = SipMobileAppPreferences.getPatientId(getContext());
                attachParameter.setSickID(sickID);
                attachParameter.setImage(stringBase64);
                attachParameter.setAttachTypeID(1);
                attachParameter.setImageTypeID(2);

                String centerName = SipMobileAppPreferences.getCenterName(getContext());
                String userLoginKey = SipMobileAppPreferences.getUserLoginKey(getContext());
                ServerData serverData = viewModel.getServerData(centerName);
                viewModel.getAddAttachService(serverData.getIPAddress() + ":" + serverData.getPort());
                viewModel.addAttach(userLoginKey, attachParameter);
            }
        });

        viewModel.getAddAttachResultSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<AttachResult>() {
            @Override
            public void onChanged(AttachResult attachResult) {
                EventBus.getDefault().postSticky(new UpdateEvent(oldAttachID, attachResult.getAttachs()[0].getAttachID()));
                getActivity().finish();
            }
        });

        viewModel.getYesSaveChange().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean saveChange) {
                binding.progressBarLoading.setVisibility(View.VISIBLE);
                binding.imgViewOk.setEnabled(false);
                binding.imgViewCancel.setEnabled(false);

                String centerName = SipMobileAppPreferences.getCenterName(getContext());
                String userLoginKey = SipMobileAppPreferences.getUserLoginKey(getContext());
                ServerData serverData = viewModel.getServerData(centerName);
                viewModel.getDeleteAttachService(serverData.getIPAddress() + ":" + serverData.getPort());
                viewModel.deleteAttach(userLoginKey, attachID);
            }
        });
    }

    private String convertBitmapToStringBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        System.gc();

        return Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
    }
}