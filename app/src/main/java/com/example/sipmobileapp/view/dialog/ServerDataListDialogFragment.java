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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.sipmobileapp.R;
import com.example.sipmobileapp.adapter.ServerDataAdapter;
import com.example.sipmobileapp.databinding.FragmentServerDataListDialogBinding;
import com.example.sipmobileapp.model.ServerData;
import com.example.sipmobileapp.viewmodel.LoginViewModel;

import java.util.List;

public class ServerDataListDialogFragment extends DialogFragment {
    private FragmentServerDataListDialogBinding mBinding;
    private LoginViewModel mViewModel;

    public static final String TAG = ServerDataListDialogFragment.class.getSimpleName();

    public static ServerDataListDialogFragment newInstance() {
        ServerDataListDialogFragment fragment = new ServerDataListDialogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createViewModel();
        setupObserver();
    }

    private void createViewModel() {
        mViewModel = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(
                getContext()),
                R.layout.fragment_server_data_list_dialog,
                null,
                false);

        initViews();
        setupAdapter();
        handleClicked();

        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setView(mBinding.getRoot())
                .create();

        return dialog;
    }

    private void setupObserver() {
        mViewModel.getInsertNotifyServerDataList().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isServerDataInsert) {
                setupAdapter();
            }
        });

        mViewModel.getEditClicked().observe(this, new Observer<ServerData>() {
            @Override
            public void onChanged(ServerData serverData) {
                AddEditServerDataDialogFragment fragment = AddEditServerDataDialogFragment.newInstance(serverData.getCenterName(), serverData.getIPAddress(), serverData.getPort(), false);
                fragment.show(getChildFragmentManager(), AddEditServerDataDialogFragment.TAG);
            }
        });

        mViewModel.getDeleteClicked().observe(this, new Observer<ServerData>() {
            @Override
            public void onChanged(ServerData serverData) {
                mViewModel.deleteServerData(serverData);
                mViewModel.getDeleteNotifySpinner().setValue(true);
                setupAdapter();
                List<ServerData> serverDataList = mViewModel.getServerDataList();
                if (serverDataList.size() == 0) {
                    RequiredServerDataDialogFragment fragment = RequiredServerDataDialogFragment.newInstance();
                    fragment.show(getParentFragmentManager(), RequiredServerDataDialogFragment.TAG);
                    dismiss();
                }
            }
        });
    }

    private void initViews() {
        mBinding.recyclerViewServerData.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.recyclerViewServerData.addItemDecoration(new DividerItemDecoration(
                mBinding.recyclerViewServerData.getContext(),
                DividerItemDecoration.VERTICAL));
    }

    private void setupAdapter() {
        List<ServerData> serverDataList = mViewModel.getServerDataList();
        ServerDataAdapter adapter = new ServerDataAdapter(getContext(), serverDataList, mViewModel);
        mBinding.recyclerViewServerData.setAdapter(adapter);
    }

    private void handleClicked() {
        mBinding.fabAddServerData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddEditServerDataDialogFragment fragment = AddEditServerDataDialogFragment.newInstance("", "", "", true);
                fragment.show(getChildFragmentManager(), AddEditServerDataDialogFragment.TAG);
            }
        });
    }
}