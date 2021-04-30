package com.example.sipmobileapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.sipmobileapp.model.ServerData;
import com.example.sipmobileapp.model.UserParameter;
import com.example.sipmobileapp.model.UserResult;
import com.example.sipmobileapp.repository.SipMobileAppRepository;

import java.util.List;

public class LoginViewModel extends AndroidViewModel {
    private SipMobileAppRepository mRepository;

    private SingleLiveEvent<Boolean> insertNotifySpinner = new SingleLiveEvent<>();
    private SingleLiveEvent<Boolean> insertNotifyServerDataList = new SingleLiveEvent<>();

    private SingleLiveEvent<ServerData> editClicked = new SingleLiveEvent<>();

    private SingleLiveEvent<ServerData> deleteClicked = new SingleLiveEvent<>();
    private SingleLiveEvent<Boolean> deleteNotifySpinner = new SingleLiveEvent<>();

    private SingleLiveEvent<String> noConnectivitySingleLiveEvent;
    private SingleLiveEvent<Boolean> timeOutExceptionHappenSingleLiveEvent;
    private SingleLiveEvent<String> wrongAddressSingleLiveEvent;

    private SingleLiveEvent<UserResult> userLoginSingleLiveEvent;
    private SingleLiveEvent<String> errorUserLoginSingleLiveEvent;

    public LoginViewModel(@NonNull Application application) {
        super(application);

        mRepository = SipMobileAppRepository.getInstance(getApplication());

        noConnectivitySingleLiveEvent = mRepository.getNoConnectivitySingleLiveEvent();
        timeOutExceptionHappenSingleLiveEvent = mRepository.getTimeOutExceptionHappenSingleLiveEvent();
        wrongAddressSingleLiveEvent = mRepository.getWrongAddressSingleLiveEvent();

        userLoginSingleLiveEvent = mRepository.getUserLoginSingleLiveEvent();
        errorUserLoginSingleLiveEvent = mRepository.getErrorUserLoginSingleLiveEvent();
    }

    public SingleLiveEvent<Boolean> getInsertNotifySpinner() {
        return insertNotifySpinner;
    }

    public SingleLiveEvent<Boolean> getInsertNotifyServerDataList() {
        return insertNotifyServerDataList;
    }

    public SingleLiveEvent<ServerData> getEditClicked() {
        return editClicked;
    }

    public SingleLiveEvent<ServerData> getDeleteClicked() {
        return deleteClicked;
    }

    public SingleLiveEvent<Boolean> getDeleteNotifySpinner() {
        return deleteNotifySpinner;
    }

    public SingleLiveEvent<String> getNoConnectivitySingleLiveEvent() {
        return noConnectivitySingleLiveEvent;
    }

    public SingleLiveEvent<Boolean> getTimeOutExceptionHappenSingleLiveEvent() {
        return timeOutExceptionHappenSingleLiveEvent;
    }

    public SingleLiveEvent<UserResult> getUserLoginSingleLiveEvent() {
        return userLoginSingleLiveEvent;
    }

    public SingleLiveEvent<String> getErrorUserLoginSingleLiveEvent() {
        return errorUserLoginSingleLiveEvent;
    }

    public SingleLiveEvent<String> getWrongAddressSingleLiveEvent() {
        return wrongAddressSingleLiveEvent;
    }

    public List<ServerData> getServerDataList() {
        return mRepository.getServerDataList();
    }

    public ServerData getServerData(String centerName) {
        return mRepository.getServerData(centerName);
    }

    public void getUserLoginService(String newBaseUrl) {
        mRepository.getUserLoginService(newBaseUrl);
    }

    public void insertServerData(ServerData serverData) {
        mRepository.insertServerData(serverData);
    }

    public void deleteServerData(ServerData serverData) {
        mRepository.deleteServerData(serverData);
    }

    public void userLogin(UserParameter userParameter) {
        mRepository.userLogin(userParameter);
    }
}
