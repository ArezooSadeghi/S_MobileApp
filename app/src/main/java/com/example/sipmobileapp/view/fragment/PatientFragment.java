package com.example.sipmobileapp.view.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.sipmobileapp.R;
import com.example.sipmobileapp.adapter.PatientAdapter;
import com.example.sipmobileapp.databinding.FragmentPatientBinding;
import com.example.sipmobileapp.model.PatientInfo;
import com.example.sipmobileapp.model.PatientResult;
import com.example.sipmobileapp.model.ServerData;
import com.example.sipmobileapp.utils.SipMobileAppPreferences;
import com.example.sipmobileapp.view.activity.AnswerContainerActivity;
import com.example.sipmobileapp.view.activity.GalleryContainerActivity;
import com.example.sipmobileapp.view.activity.LoginContainerActivity;
import com.example.sipmobileapp.view.activity.RegisterParameterContainerActivity;
import com.example.sipmobileapp.view.dialog.ErrorDialogFragment;
import com.example.sipmobileapp.view.dialog.MoreDialogFragment;
import com.example.sipmobileapp.viewmodel.PatientViewModel;
import com.skydoves.powermenu.OnMenuItemClickListener;
import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;

import java.util.Arrays;
import java.util.List;

public class PatientFragment extends Fragment {
    private FragmentPatientBinding binding;
    private PatientViewModel viewModel;

    public static PatientFragment newInstance() {
        PatientFragment fragment = new PatientFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createViewModel();
    }

    private void createViewModel() {
        viewModel = new ViewModelProvider(this).get(PatientViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_patient,
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

    private void initViews() {
        binding.recyclerViewPatient.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewPatient.addItemDecoration(new DividerItemDecoration(
                binding.recyclerViewPatient.getContext(),
                DividerItemDecoration.VERTICAL));
    }

    private void handleClicked() {
        binding.imgButtonMore.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {
                PowerMenu powerMenu = new PowerMenu.Builder(getContext())
                        .addItem(new PowerMenuItem(getString(R.string.log_out)))
                        .build();

                powerMenu.setOnMenuItemClickListener(new OnMenuItemClickListener<PowerMenuItem>() {
                    @Override
                    public void onItemClick(int position, PowerMenuItem item) {
                        switch (position) {
                            case 0:
                                SipMobileAppPreferences.setUserLoginKey(getContext(), null);
                                Intent intent = LoginContainerActivity.newIntent(getContext());
                                startActivity(intent);
                                getActivity().finish();
                                break;
                        }
                    }
                });
                powerMenu.showAsDropDown(view);
            }
        });

        binding.btnSearch.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                String centerName = SipMobileAppPreferences.getCenterName(getContext());
                ServerData serverData = viewModel.getServerData(centerName);
                viewModel.getSearchService(serverData.getIPAddress() + ":" + serverData.getPort());
                String userLoginKey = SipMobileAppPreferences.getUserLoginKey(getContext());
                viewModel.search(userLoginKey, binding.edTextSearch.getText().toString());
            }
        });
    }

    private void setupObserver() {
        viewModel.getSearchSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<PatientResult>() {
            @Override
            public void onChanged(PatientResult patientResult) {
                if (patientResult.getPatients() == null || patientResult.getPatients().length == 0) {
                    binding.progressBarLoading.setVisibility(View.GONE);
                    binding.recyclerViewPatient.setVisibility(View.GONE);
                    binding.txtNoPatient.setVisibility(View.VISIBLE);
                } else {
                    binding.progressBarLoading.setVisibility(View.GONE);
                    binding.txtNoPatient.setVisibility(View.GONE);
                    binding.recyclerViewPatient.setVisibility(View.VISIBLE);
                    setupAdapter(patientResult.getPatients());
                }
            }
        });

        viewModel.getErrorSearchSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String message) {
                binding.progressBarLoading.setVisibility(View.GONE);
                binding.recyclerViewPatient.setVisibility(View.GONE);
                binding.txtNoPatient.setVisibility(View.VISIBLE);
                ErrorDialogFragment fragment = ErrorDialogFragment.newInstance(message);
                fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
            }
        });

       /* viewModel.getNavigateToGallery().observe(getViewLifecycleOwner(), new Observer<PatientInfo>() {
            @Override
            public void onChanged(PatientInfo patientInfo) {
                SipMobileAppPreferences.setPatientId(getContext(), patientInfo.getSickID());
                Intent intent = GalleryContainerActivity.newIntent(getContext(), patientInfo.getSickID());
                startActivity(intent);
            }
        });*/

        viewModel.getNoConnectivitySingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String message) {
                binding.progressBarLoading.setVisibility(View.GONE);
                binding.recyclerViewPatient.setVisibility(View.GONE);
                binding.txtNoPatient.setVisibility(View.VISIBLE);
                ErrorDialogFragment fragment = ErrorDialogFragment.newInstance(message);
                fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
            }
        });

        viewModel.getTimeOutExceptionHappenSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isTimeOutExceptionHappen) {
                binding.progressBarLoading.setVisibility(View.GONE);
                binding.recyclerViewPatient.setVisibility(View.GONE);
                binding.txtNoPatient.setVisibility(View.VISIBLE);
                ErrorDialogFragment fragment = ErrorDialogFragment.newInstance(getString(R.string.error_connection));
                fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
            }
        });

       /* viewModel.getNavigateToAnswer().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer patientID) {
                Intent intent = AnswerContainerActivity.newIntent(getContext(), patientID);
                startActivity(intent);
            }
        });

        viewModel.getNavigateToRegisterParameter().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer sickID) {
                Intent intent = RegisterParameterContainerActivity.newIntent(getContext(), sickID);
                startActivity(intent);
            }
        });*/

        viewModel.getShowDialog().observe(getViewLifecycleOwner(), new Observer<PatientInfo>() {
            @Override
            public void onChanged(PatientInfo patientInfo) {
                MoreDialogFragment fragment = MoreDialogFragment.newInstance(patientInfo);
                fragment.show(getParentFragmentManager(), MoreDialogFragment.TAG);
            }
        });
    }

    private void setupAdapter(PatientInfo[] patients) {
        binding.recyclerViewPatient.setVisibility(View.VISIBLE);
        List<PatientInfo> patientInfoList = Arrays.asList(patients);
        PatientAdapter adapter = new PatientAdapter(getContext(), patientInfoList, viewModel);
        binding.recyclerViewPatient.setAdapter(adapter);
    }
}