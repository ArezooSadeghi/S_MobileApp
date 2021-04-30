package com.example.sipmobileapp.view.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.sipmobileapp.R;
import com.example.sipmobileapp.databinding.FragmentSuccessDialogBinding;
import com.example.sipmobileapp.viewmodel.AttachmentViewModel;

public class SuccessAttachDialogFragment extends DialogFragment {
    public static final String ARGS_MESSAGE = "message";
    private FragmentSuccessDialogBinding binding;
    private AttachmentViewModel viewModel;

    public static final String TAG = SuccessAttachDialogFragment.class.getSimpleName();

    public static SuccessAttachDialogFragment newInstance(String message) {
        SuccessAttachDialogFragment fragment = new SuccessAttachDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARGS_MESSAGE, message);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createViewModel();
    }

    private void createViewModel() {
        viewModel = new ViewModelProvider(requireActivity()).get(AttachmentViewModel.class);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(
                getContext()),
                R.layout.fragment_success_dialog,
                null,
                false);

        initViews();
        handleClicked();

        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setView(binding.getRoot())
                .create();

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }

    private void initViews() {
        String message = getArguments().getString(ARGS_MESSAGE);
        binding.txtMessage.setText(message);
    }

    private void handleClicked() {
        binding.imgCloseWindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.getShowAttachAgainDialog().setValue(true);
                dismiss();
            }
        });
    }
}