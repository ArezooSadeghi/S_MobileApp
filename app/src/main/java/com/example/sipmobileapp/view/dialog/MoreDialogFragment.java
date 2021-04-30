package com.example.sipmobileapp.view.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.sipmobileapp.R;
import com.example.sipmobileapp.databinding.FragmentMoreDialogBinding;
import com.example.sipmobileapp.model.PatientInfo;
import com.example.sipmobileapp.utils.SipMobileAppPreferences;
import com.example.sipmobileapp.view.activity.GalleryContainerActivity;
import com.example.sipmobileapp.view.activity.RegisterParameterContainerActivity;
import com.example.sipmobileapp.viewmodel.PatientViewModel;

public class MoreDialogFragment extends DialogFragment {
    private FragmentMoreDialogBinding binding;
    private PatientViewModel viewModel;

    private PatientInfo patientInfo;

    private static final String ARGS_PATIENT_INFO = "patientInfo";

    public static final String TAG = MoreDialogFragment.class.getSimpleName();

    public static MoreDialogFragment newInstance(PatientInfo patientInfo) {
        MoreDialogFragment fragment = new MoreDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARGS_PATIENT_INFO, patientInfo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        patientInfo = (PatientInfo) getArguments().getSerializable(ARGS_PATIENT_INFO);

        createViewModel();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow()
                .getAttributes().windowAnimations = R.style.DialogAnimation;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                LayoutInflater.from(getContext()),
                R.layout.fragment_more_dialog,
                null,
                false);

        handleEvents();

        AlertDialog dialog = new AlertDialog
                .Builder(getContext())
                .setView(binding.getRoot())
                .create();

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }

    private void createViewModel() {
        viewModel = new ViewModelProvider(requireActivity()).get(PatientViewModel.class);
    }

    private void handleEvents() {
        binding.btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        binding.btnSeeDocuments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SipMobileAppPreferences.setPatientId(getContext(), patientInfo.getSickID());
                Intent intent = GalleryContainerActivity.newIntent(getContext(), patientInfo.getSickID());
                startActivity(intent);
                dismiss();
            }
        });

        binding.btnRegisterParameter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Intent intent = RegisterParameterContainerActivity.newIntent(getContext(), patientInfo.getSickID());
                startActivity(intent);
                dismiss();*/
            }
        });
    }
}