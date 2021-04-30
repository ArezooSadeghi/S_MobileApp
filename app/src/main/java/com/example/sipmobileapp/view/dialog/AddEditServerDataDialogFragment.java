package com.example.sipmobileapp.view.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.sipmobileapp.R;
import com.example.sipmobileapp.databinding.FragmentAddEditServerDataDialogBinding;
import com.example.sipmobileapp.model.ServerData;
import com.example.sipmobileapp.viewmodel.LoginViewModel;

import java.util.List;

public class AddEditServerDataDialogFragment extends DialogFragment {
    private FragmentAddEditServerDataDialogBinding mBinding;
    private LoginViewModel mViewModel;

    private String mCenterName;

    private static final String ARGS_CENTER_NAME = "centerName";
    private static final String ARGS_IP_ADDRESS = "ipAddress";
    private static final String ARGS_PORT = "port";
    private static final String ARGS_IS_ADD = "isAdd";

    public static final String TAG = AddEditServerDataDialogFragment.class.getSimpleName();

    public static AddEditServerDataDialogFragment newInstance(String centerName, String ipAddress, String port, boolean isAdd) {
        AddEditServerDataDialogFragment fragment = new AddEditServerDataDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARGS_CENTER_NAME, centerName);
        args.putString(ARGS_IP_ADDRESS, ipAddress);
        args.putString(ARGS_PORT, port);
        args.putBoolean(ARGS_IS_ADD, isAdd);
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

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(
                getContext()),
                R.layout.fragment_add_edit_server_data_dialog,
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
        mCenterName = getArguments().getString(ARGS_CENTER_NAME);
        String ipAddress = getArguments().getString(ARGS_IP_ADDRESS);
        String port = getArguments().getString(ARGS_PORT);

        mBinding.edTxtCenterName.setText(mCenterName);
        mBinding.edTxtIpAddress.setText(ipAddress);
        mBinding.edTxtPort.setText(port);

        mBinding.edTxtCenterName.setSelection(mBinding.edTxtCenterName.getText().length());
        mBinding.edTxtIpAddress.setSelection(mBinding.edTxtIpAddress.getText().length());
        mBinding.edTxtPort.setSelection(mBinding.edTxtPort.getText().length());
    }

    private void handleClicked() {
        mBinding.fabOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String centerName = mBinding.edTxtCenterName.getText().toString();
                String ipAddress = mBinding.edTxtIpAddress.getText().toString();
                String port = mBinding.edTxtPort.getText().toString();
                if (centerName.isEmpty() || ipAddress.isEmpty() || port.isEmpty()) {
                    ErrorDialogFragment fragment = ErrorDialogFragment.newInstance(getString(R.string.fill_required_fields));
                    fragment.show(getChildFragmentManager(), ErrorDialogFragment.TAG);
                } else if (ipAddress.length() < 7 || !hasThreeDots(ipAddress) || hasEnglishLetter(ipAddress) || hasEnglishLetter(port)) {
                    ErrorDialogFragment fragment = ErrorDialogFragment.newInstance(getString(R.string.wrong_format));
                    fragment.show(getChildFragmentManager(), ErrorDialogFragment.TAG);
                } else {
                    if (isRepeatedCenterName(centerName) & !centerName.equals(mCenterName)) {
                        ErrorDialogFragment fragment = ErrorDialogFragment.newInstance(getString(R.string.repeated_center_name));
                        fragment.show(getChildFragmentManager(), ErrorDialogFragment.TAG);
                    } else {
                        ServerData serverData = new ServerData(centerName, convertPerDigitToEn(ipAddress), convertPerDigitToEn(port));
                        boolean isAdd = getArguments().getBoolean(ARGS_IS_ADD);
                        if (!isAdd) {
                            ServerData preServerData = mViewModel.getServerData(mCenterName);
                            mViewModel.deleteServerData(preServerData);
                        }
                        mViewModel.insertServerData(serverData);
                        mViewModel.getInsertNotifySpinner().setValue(true);
                        mViewModel.getInsertNotifyServerDataList().setValue(true);
                        dismiss();
                    }
                }
            }
        });

        mBinding.edTxtCenterName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionID, KeyEvent keyEvent) {
                if (actionID == 0 || actionID == EditorInfo.IME_ACTION_DONE) {
                    mBinding.edTxtIpAddress.requestFocus();
                }
                return false;
            }
        });

        mBinding.edTxtIpAddress.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionID, KeyEvent keyEvent) {
                if (actionID == 0 || actionID == EditorInfo.IME_ACTION_DONE) {
                    mBinding.edTxtPort.requestFocus();
                }
                return false;
            }
        });
    }

    private boolean hasEnglishLetter(String ipAddressOrPort) {
        String result = "";
        char[] chars = ipAddressOrPort.toCharArray();
        for (Character character : chars) {
            if (!String.valueOf(character).equals(".")) {
                result += String.valueOf(character);
            }
        }

        if (result.matches(".*[a-zA-Z]+.*")) {
            return true;
        } else {
            return false;
        }
    }

    private boolean hasThreeDots(String ipAddress) {
        int numberOfDots = 0;
        char[] chars = ipAddress.toCharArray();
        for (Character character : chars) {
            if (String.valueOf(character).equals(".")) {
                numberOfDots++;
            }
        }
        if (numberOfDots == 3) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isRepeatedCenterName(String centerName) {
        List<ServerData> serverDataList = mViewModel.getServerDataList();
        if (serverDataList != null & serverDataList.size() > 0) {
            for (ServerData serverData : serverDataList) {
                if (serverData.getCenterName().equals(centerName)) {
                    return true;
                }
            }
        }
        return false;
    }

    private String convertPerDigitToEn(String ipAddressOrPort) {
        return ipAddressOrPort.replaceAll("۰", "0").
                replaceAll("۱", "1").
                replaceAll("۲", "2").
                replaceAll("۳", "3").
                replaceAll("۴", "4").
                replaceAll("۵", "5").
                replaceAll("۶", "6").
                replaceAll("۷", "7").
                replaceAll("۸", "8").
                replaceAll("۹", "9").
                replaceAll(" ", "");
    }
}