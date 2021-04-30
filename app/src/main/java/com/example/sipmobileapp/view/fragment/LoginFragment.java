package com.example.sipmobileapp.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.sipmobileapp.R;
import com.example.sipmobileapp.databinding.FragmentLoginBinding;
import com.example.sipmobileapp.model.ServerData;
import com.example.sipmobileapp.model.UserParameter;
import com.example.sipmobileapp.model.UserResult;
import com.example.sipmobileapp.utils.SipMobileAppPreferences;
import com.example.sipmobileapp.view.activity.PatientContainerActivity;
import com.example.sipmobileapp.view.dialog.ErrorDialogFragment;
import com.example.sipmobileapp.view.dialog.RequiredServerDataDialogFragment;
import com.example.sipmobileapp.view.dialog.ServerDataListDialogFragment;
import com.example.sipmobileapp.viewmodel.LoginViewModel;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.List;

public class LoginFragment extends Fragment {
    private FragmentLoginBinding mBinding;
    private LoginViewModel mViewModel;

    private String lastValueSpinner;

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
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
        mViewModel = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_login,
                container,
                false);

        if (mViewModel.getServerDataList().size() == 0 || mViewModel.getServerDataList() == null) {
            RequiredServerDataDialogFragment fragment = RequiredServerDataDialogFragment.newInstance();
            fragment.show(getParentFragmentManager(), RequiredServerDataDialogFragment.TAG);
        } else {
            setupSpinner();
        }

        handleClicked();

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupObserver();
    }

    private void setupSpinner() {
        List<ServerData> serverDataList = mViewModel.getServerDataList();
        List<String> centerNameList = new ArrayList<>();
        for (ServerData serverData : serverDataList) {
            centerNameList.add(serverData.getCenterName());
        }
        mBinding.spinnerServerData.setItems(centerNameList);
        if (centerNameList.size() > 0) {
            lastValueSpinner = centerNameList.get(0);
        }
    }

    private void handleClicked() {
        mBinding.imgBtnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ServerDataListDialogFragment fragment = ServerDataListDialogFragment.newInstance();
                fragment.show(getParentFragmentManager(), ServerDataListDialogFragment.TAG);
            }
        });

        mBinding.edTxtUserName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionID, KeyEvent keyEvent) {
                if (actionID == 0 || actionID == EditorInfo.IME_ACTION_DONE) {
                    mBinding.edTxtPassword.requestFocus();
                }
                return false;
            }
        });

        mBinding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mViewModel.getServerDataList() == null || mViewModel.getServerDataList().size() == 0) {
                    RequiredServerDataDialogFragment fragment = RequiredServerDataDialogFragment.newInstance();
                    fragment.show(getParentFragmentManager(), RequiredServerDataDialogFragment.TAG);
                } else {
                    mBinding.loadingLayout.setVisibility(View.VISIBLE);
                    mBinding.edTxtUserName.setEnabled(false);
                    mBinding.edTxtPassword.setEnabled(false);
                    mBinding.btnLogin.setEnabled(false);

                    String userName = mBinding.edTxtUserName.getText().toString();
                    String password = mBinding.edTxtPassword.getText().toString();

                    UserParameter userParameter = new UserParameter(userName, password);
                    ServerData serverData = mViewModel.getServerData(lastValueSpinner);

                    mViewModel.getUserLoginService(serverData.getIPAddress() + ":" + serverData.getPort());
                    mViewModel.userLogin(userParameter);
                }
            }
        });

        mBinding.spinnerServerData.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                lastValueSpinner = (String) item;
            }
        });
    }

    private void setupObserver() {
        mViewModel.getInsertNotifySpinner().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isInsertServerData) {
                setupSpinner();
            }
        });

        mViewModel.getDeleteNotifySpinner().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isDeleteServerData) {
                setupSpinner();
            }
        });

        mViewModel.getUserLoginSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<UserResult>() {
            @Override
            public void onChanged(UserResult userResult) {
                SipMobileAppPreferences.setUserLoginKey(getContext(), userResult.getUsers()[0].getUserLoginKey());
                SipMobileAppPreferences.setCenterName(getContext(), lastValueSpinner);
                SipMobileAppPreferences.setUsername(getContext(), userResult.getUsers()[0].getUser_Name());
                Intent intent = PatientContainerActivity.newIntent(getContext());
                startActivity(intent);
                getActivity().finish();
            }
        });

        mViewModel.getErrorUserLoginSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String message) {
                mBinding.loadingLayout.setVisibility(View.GONE);
                mBinding.edTxtUserName.setEnabled(true);
                mBinding.edTxtPassword.setEnabled(true);
                mBinding.btnLogin.setEnabled(true);

                ErrorDialogFragment fragment = ErrorDialogFragment.newInstance(message);
                fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
            }
        });

        mViewModel.getNoConnectivitySingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String message) {
                mBinding.loadingLayout.setVisibility(View.GONE);
                mBinding.edTxtUserName.setEnabled(true);
                mBinding.edTxtPassword.setEnabled(true);
                mBinding.btnLogin.setEnabled(true);

                ErrorDialogFragment fragment = ErrorDialogFragment.newInstance(message);
                fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
            }
        });

        mViewModel.getTimeOutExceptionHappenSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isTimeOutExceptionHappen) {
                mBinding.loadingLayout.setVisibility(View.GONE);
                mBinding.edTxtUserName.setEnabled(true);
                mBinding.edTxtPassword.setEnabled(true);
                mBinding.btnLogin.setEnabled(true);

                ErrorDialogFragment fragment = ErrorDialogFragment.newInstance(getString(R.string.error_connection));
                fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
            }
        });

        mViewModel.getWrongAddressSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String message) {
                mBinding.loadingLayout.setVisibility(View.GONE);
                mBinding.edTxtUserName.setEnabled(true);
                mBinding.edTxtPassword.setEnabled(true);
                mBinding.btnLogin.setEnabled(true);

                ErrorDialogFragment fragment = ErrorDialogFragment.newInstance(message);
                fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
            }
        });
    }
}