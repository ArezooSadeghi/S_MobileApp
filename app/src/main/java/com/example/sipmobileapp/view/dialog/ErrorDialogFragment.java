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

import com.example.sipmobileapp.R;
import com.example.sipmobileapp.databinding.FragmentErrorDialogBinding;

public class ErrorDialogFragment extends DialogFragment {
    private FragmentErrorDialogBinding mBinding;

    private static final String ARGS_MESSAGE = "message";

    public static final String TAG = ErrorDialogFragment.class.getSimpleName();

    public static ErrorDialogFragment newInstance(String message) {
        ErrorDialogFragment fragment = new ErrorDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARGS_MESSAGE, message);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(
                getContext()),
                R.layout.fragment_error_dialog,
                null,
                false);

        initViews();
        handleClicked();

        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setView(mBinding.getRoot())
                .create();

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }

    private void initViews() {
        String message = getArguments().getString(ARGS_MESSAGE);
        mBinding.txtMessage.setText(message);
    }

    private void handleClicked() {
        mBinding.imgCloseWindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        mBinding.btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
}