package com.example.sipmobileapp.view.dialog;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.sipmobileapp.R;
import com.example.sipmobileapp.databinding.FragmentAttachmentDialogBinding;
import com.example.sipmobileapp.model.AttachParameter;
import com.example.sipmobileapp.model.AttachResult;
import com.example.sipmobileapp.model.ServerData;
import com.example.sipmobileapp.utils.SipMobileAppPreferences;
import com.example.sipmobileapp.viewmodel.AttachmentViewModel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public class AttachmentDialogFragment extends DialogFragment {
    private FragmentAttachmentDialogBinding binding;
    private AttachmentViewModel viewModel;

    private File photoFile;
    private Uri uri;
    private Bitmap bitmap;
    private int numberOfRotate, patientID;

    private static final String ARGS_PATIENT_ID = "patientID";
    private static final String AUTHORITY = "com.example.sipmobileapp.fileProvider";

    private static final int REQUEST_CODE_PICK_PHOTO = 0;
    private static final int REQUEST_CODE_TAKE_PHOTO = 1;

    public static final String TAG = AttachmentDialogFragment.class.getSimpleName();

    public static AttachmentDialogFragment newInstance(int patientID) {
        AttachmentDialogFragment fragment = new AttachmentDialogFragment();
        Bundle args = new Bundle();
        args.putInt(ARGS_PATIENT_ID, patientID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        patientID = getArguments().getInt(ARGS_PATIENT_ID);

        createViewModel();

        setupObserver();
    }

    private void createViewModel() {
        viewModel = new ViewModelProvider(requireActivity()).get(AttachmentViewModel.class);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(
                getContext()),
                R.layout.fragment_attachment_dialog,
                null,
                false);

        handleClicked();

        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setView(binding.getRoot())
                .create();

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_TAKE_PHOTO) {
                uri = FileProvider.getUriForFile(getContext(), AUTHORITY, photoFile);
                if (uri != null) {
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                        if (bitmap != null) {
                            BitmapFactory.Options options = new BitmapFactory.Options();
                            options.inJustDecodeBounds = true;
                            BitmapFactory.decodeFile(photoFile.getAbsolutePath(), options);

                            int imageHeight = options.outHeight;
                            int imageWidth = options.outWidth;

                            options.inJustDecodeBounds = false;

                            if (imageWidth > imageHeight) {
                                Matrix matrix = new Matrix();
                                matrix.postRotate(90);
                                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                                binding.imgViewAttachment.setImageBitmap(bitmap);
                            } else {
                                binding.imgViewAttachment.setImageBitmap(bitmap);
                            }
                        }
                    } catch (IOException e) {
                        Log.e(TAG, e.getMessage());
                    } finally {
                        getActivity().revokeUriPermission(uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    }
                }
            } else if (requestCode == REQUEST_CODE_PICK_PHOTO) {
                uri = data.getData();
                if (uri != null) {
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                        binding.imgViewAttachment.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        Log.e(TAG, e.getMessage());
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setupObserver() {
        viewModel.getAllowPermission().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isPermissionAllow) {
                openCamera();
            }
        });

        viewModel.getAddAttachResultSingleLiveEvent().observe(this, new Observer<AttachResult>() {
            @Override
            public void onChanged(AttachResult attachResult) {
                viewModel.getUpdateGallery().setValue(attachResult);
                binding.progressLoading.setVisibility(View.GONE);
                binding.imgViewClose.setEnabled(true);
                binding.imgViewSend.setEnabled(true);
                binding.imgViewCamera.setEnabled(true);
                binding.edTextDescription.setEnabled(true);
                binding.imgViewScreenRotation.setEnabled(true);
                binding.imgViewAttachFromGallery.setEnabled(true);
                SuccessAttachDialogFragment fragment = SuccessAttachDialogFragment.newInstance("فایل با موفقیت ارسال شد");
                fragment.show(getParentFragmentManager(), SuccessAttachDialogFragment.TAG);
            }
        });

        viewModel.getErrorAddAttachResultSingleLiveEvent().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String message) {
                binding.progressLoading.setVisibility(View.GONE);
                binding.imgViewClose.setEnabled(true);
                binding.imgViewSend.setEnabled(true);
                binding.imgViewCamera.setEnabled(true);
                binding.edTextDescription.setEnabled(true);
                binding.imgViewScreenRotation.setEnabled(true);
                binding.imgViewAttachFromGallery.setEnabled(true);
                ErrorDialogFragment fragment = ErrorDialogFragment.newInstance(message);
                fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
            }
        });

        viewModel.getNoConnectivitySingleLiveEvent().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String message) {
                binding.progressLoading.setVisibility(View.GONE);
                binding.imgViewClose.setEnabled(true);
                binding.imgViewSend.setEnabled(true);
                binding.imgViewCamera.setEnabled(true);
                binding.edTextDescription.setEnabled(true);
                binding.imgViewScreenRotation.setEnabled(true);
                binding.imgViewAttachFromGallery.setEnabled(true);
                ErrorDialogFragment fragment = ErrorDialogFragment.newInstance(message);
                fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
            }
        });

        viewModel.getTimeOutExceptionHappenSingleLiveEvent().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isTimeOutExceptionHappen) {
                binding.progressLoading.setVisibility(View.GONE);
                binding.imgViewClose.setEnabled(true);
                binding.imgViewSend.setEnabled(true);
                binding.imgViewCamera.setEnabled(true);
                binding.edTextDescription.setEnabled(true);
                binding.imgViewScreenRotation.setEnabled(true);
                binding.imgViewAttachFromGallery.setEnabled(true);
                ErrorDialogFragment fragment = ErrorDialogFragment.newInstance(getString(R.string.error_connection));
                fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
            }
        });

        viewModel.getShowAttachAgainDialog().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean showAttachAgainDialog) {
                AttachAgainDialogFragment fragment = AttachAgainDialogFragment.newInstance();
                fragment.show(getParentFragmentManager(), AttachAgainDialogFragment.TAG);
            }
        });

        viewModel.getNoAttachAgain().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean noAttachAgain) {
                dismiss();
            }
        });

        viewModel.getYesAttachAgain().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean yesAttachAgain) {
                uri = null;
                binding.imgViewAttachment.setImageResource(0);
                binding.edTextDescription.setText("");
            }
        });
    }

    private void handleClicked() {
        binding.imgViewAttachFromGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, getString(R.string.select_image)), REQUEST_CODE_PICK_PHOTO);
            }
        });

        binding.imgViewScreenRotation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (uri == null) {
                    ErrorDialogFragment fragment = ErrorDialogFragment.newInstance(getString(R.string.no_selected_file));
                    fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
                } else {
                    switch (numberOfRotate) {
                        case 0:
                            Matrix matrix = new Matrix();
                            matrix.postRotate(90);
                            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                            binding.imgViewAttachment.setImageBitmap(bitmap);
                            numberOfRotate++;
                            return;
                        case 1:
                            Matrix matrix1 = new Matrix();
                            matrix1.postRotate(180);
                            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix1, true);
                            binding.imgViewAttachment.setImageBitmap(bitmap);
                            numberOfRotate++;
                            return;
                        case 2:
                            Matrix matrix2 = new Matrix();
                            matrix2.postRotate(270);
                            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix2, true);
                            binding.imgViewAttachment.setImageBitmap(bitmap);
                            numberOfRotate = 0;
                            return;
                    }
                }
            }
        });

        binding.imgViewCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hasCameraPermission()) {
                    openCamera();
                } else {
                    viewModel.getRequestPermission().setValue(true);
                }
            }
        });

        binding.imgViewSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (uri == null) {
                    ErrorDialogFragment fragment = ErrorDialogFragment.newInstance(getString(R.string.no_selected_file));
                    fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
                } else {
                    AttachmentAsyncTask attachmentAsyncTask = new AttachmentAsyncTask();
                    attachmentAsyncTask.execute(bitmap);
                }
            }
        });

        binding.imgViewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            File fileDir = getActivity().getFilesDir();
            String fileName = "img_" + new Date().getTime() + ".jpg";
            photoFile = new File(fileDir, fileName);

            if (photoFile != null) {
                Uri uri = FileProvider.getUriForFile(getContext(), AUTHORITY, photoFile);

                List<ResolveInfo> activities = getActivity().getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);

                for (ResolveInfo activity : activities) {
                    getActivity().grantUriPermission(activity.activityInfo.packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }

                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(intent, REQUEST_CODE_TAKE_PHOTO);
            }
        }
    }

    private boolean hasCameraPermission() {
        return (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED);
    }

    private String convertBitmapToStringBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        System.gc();

        return Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
    }

    public class AttachmentAsyncTask extends AsyncTask<Bitmap, Void, String> {

        @Override
        protected void onPreExecute() {
            binding.progressLoading.setVisibility(View.VISIBLE);
            binding.imgViewClose.setEnabled(false);
            binding.imgViewSend.setEnabled(false);
            binding.imgViewCamera.setEnabled(false);
            binding.edTextDescription.setEnabled(false);
            binding.imgViewScreenRotation.setEnabled(false);
            binding.imgViewAttachFromGallery.setEnabled(false);
        }

        @Override
        protected String doInBackground(Bitmap... bitmaps) {
            String stringBase64 = convertBitmapToStringBase64(bitmap);
            return stringBase64;
        }

        @Override
        protected void onPostExecute(String stringBase64) {
            AttachParameter attachParameter = new AttachParameter();
            String description = binding.edTextDescription.getText().toString();
            attachParameter.setDescription(description);
            attachParameter.setSickID(patientID);
            attachParameter.setImage(stringBase64);
            attachParameter.setAttachTypeID(1);
            attachParameter.setImageTypeID(2);

            String centerName = SipMobileAppPreferences.getCenterName(getContext());
            String userLoginKey = SipMobileAppPreferences.getUserLoginKey(getContext());
            ServerData serverData = viewModel.getServerData(centerName);
            viewModel.getAddAttachService(serverData.getIPAddress() + ":" + serverData.getPort());
            viewModel.addAttach(userLoginKey, attachParameter);
        }
    }
}