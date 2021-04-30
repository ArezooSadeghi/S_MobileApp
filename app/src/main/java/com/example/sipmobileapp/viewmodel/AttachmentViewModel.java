package com.example.sipmobileapp.viewmodel;

import android.app.Application;
import android.graphics.Bitmap;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.sipmobileapp.model.AttachParameter;
import com.example.sipmobileapp.model.AttachResult;
import com.example.sipmobileapp.model.ServerData;
import com.example.sipmobileapp.repository.SipMobileAppRepository;

import java.util.Map;

public class AttachmentViewModel extends AndroidViewModel {
    private SipMobileAppRepository repository;

    private SingleLiveEvent<Boolean> requestPermission = new SingleLiveEvent<>();
    private SingleLiveEvent<Boolean> allowPermission = new SingleLiveEvent<>();

    private SingleLiveEvent<String> convertBitmapToStringBase64 = new SingleLiveEvent<>();

    private SingleLiveEvent<String> noConnectivitySingleLiveEvent;
    private SingleLiveEvent<Boolean> timeOutExceptionHappenSingleLiveEvent;

    private SingleLiveEvent<AttachResult> patientAttachmentListResultSingleLiveEvent;
    private SingleLiveEvent<String> errorPatientAttachmentListResultSingleLiveEvent;

    private SingleLiveEvent<AttachResult> attachInfoResultSingleLiveEvent;
    private SingleLiveEvent<String> errorAttachInfoResultSingleLiveEvent;

    private SingleLiveEvent<AttachResult> addAttachResultSingleLiveEvent;
    private SingleLiveEvent<String> errorAddAttachResultSingleLiveEvent;

    private SingleLiveEvent<AttachResult> deleteAttachResultSingleLiveEvent;
    private SingleLiveEvent<String> errorDeleteAttachResultSingleLiveEvent;

    private SingleLiveEvent<Boolean> showAttachAgainDialog = new SingleLiveEvent<>();
    private SingleLiveEvent<Boolean> noAttachAgain = new SingleLiveEvent<>();
    private SingleLiveEvent<Boolean> yesAttachAgain = new SingleLiveEvent<>();

    private SingleLiveEvent<AttachResult> updateGallery = new SingleLiveEvent<>();

    private SingleLiveEvent<Map<Bitmap, String>> showFullScreenImage = new SingleLiveEvent<>();
    private SingleLiveEvent<Map<Uri, String>> testShowFullScreenImage = new SingleLiveEvent<>();

    private SingleLiveEvent<Integer> deleteImageFromGallery = new SingleLiveEvent<>();

    private SingleLiveEvent<Boolean> yesDelete = new SingleLiveEvent<>();

    private SingleLiveEvent<Boolean> finish = new SingleLiveEvent<>();

    private SingleLiveEvent<String> cropAddAttachment = new SingleLiveEvent<>();

    private SingleLiveEvent<Boolean> yesSaveChange = new SingleLiveEvent<>();

    public AttachmentViewModel(@NonNull Application application) {
        super(application);

        repository = SipMobileAppRepository.getInstance(getApplication());

        noConnectivitySingleLiveEvent = repository.getNoConnectivitySingleLiveEvent();
        timeOutExceptionHappenSingleLiveEvent = repository.getTimeOutExceptionHappenSingleLiveEvent();

        patientAttachmentListResultSingleLiveEvent = repository.getPatientAttachmentListResultSingleLiveEvent();
        errorPatientAttachmentListResultSingleLiveEvent = repository.getErrorPatientAttachmentListResultSingleLiveEvent();

        attachInfoResultSingleLiveEvent = repository.getAttachInfoSingleLiveEvent();
        errorAttachInfoResultSingleLiveEvent = repository.getErrorAttachInfoSingleLiveEvent();

        addAttachResultSingleLiveEvent = repository.getAddAttachSingleLiveEvent();
        errorAddAttachResultSingleLiveEvent = repository.getErrorAddAttachSingleLiveEvent();

        deleteAttachResultSingleLiveEvent = repository.getDeleteAttachSingleLiveEvent();
        errorDeleteAttachResultSingleLiveEvent = repository.getErrorDeleteAttachSingleLiveEvent();
    }

    public SingleLiveEvent<Boolean> getRequestPermission() {
        return requestPermission;
    }

    public SingleLiveEvent<Boolean> getAllowPermission() {
        return allowPermission;
    }

    public SingleLiveEvent<String> getConvertBitmapToStringBase64() {
        return convertBitmapToStringBase64;
    }

    public SingleLiveEvent<String> getNoConnectivitySingleLiveEvent() {
        return noConnectivitySingleLiveEvent;
    }

    public SingleLiveEvent<Boolean> getTimeOutExceptionHappenSingleLiveEvent() {
        return timeOutExceptionHappenSingleLiveEvent;
    }

    public SingleLiveEvent<AttachResult> getPatientAttachmentListResultSingleLiveEvent() {
        return patientAttachmentListResultSingleLiveEvent;
    }

    public SingleLiveEvent<String> getErrorPatientAttachmentListResultSingleLiveEvent() {
        return errorPatientAttachmentListResultSingleLiveEvent;
    }

    public SingleLiveEvent<AttachResult> getAttachInfoResultSingleLiveEvent() {
        return attachInfoResultSingleLiveEvent;
    }

    public SingleLiveEvent<String> getErrorAttachInfoResultSingleLiveEvent() {
        return errorAttachInfoResultSingleLiveEvent;
    }

    public SingleLiveEvent<AttachResult> getAddAttachResultSingleLiveEvent() {
        return addAttachResultSingleLiveEvent;
    }

    public SingleLiveEvent<String> getErrorAddAttachResultSingleLiveEvent() {
        return errorAddAttachResultSingleLiveEvent;
    }

    public SingleLiveEvent<Boolean> getShowAttachAgainDialog() {
        return showAttachAgainDialog;
    }

    public SingleLiveEvent<Boolean> getNoAttachAgain() {
        return noAttachAgain;
    }

    public SingleLiveEvent<Boolean> getYesAttachAgain() {
        return yesAttachAgain;
    }

    public SingleLiveEvent<AttachResult> getUpdateGallery() {
        return updateGallery;
    }

    public SingleLiveEvent<Map<Bitmap, String>> getShowFullScreenImage() {
        return showFullScreenImage;
    }

    public SingleLiveEvent<AttachResult> getDeleteAttachResultSingleLiveEvent() {
        return deleteAttachResultSingleLiveEvent;
    }

    public SingleLiveEvent<String> getErrorDeleteAttachResultSingleLiveEvent() {
        return errorDeleteAttachResultSingleLiveEvent;
    }

    public SingleLiveEvent<Integer> getDeleteImageFromGallery() {
        return deleteImageFromGallery;
    }

    public SingleLiveEvent<Map<Uri, String>> getTestShowFullScreenImage() {
        return testShowFullScreenImage;
    }

    public SingleLiveEvent<Boolean> getYesDelete() {
        return yesDelete;
    }

    public SingleLiveEvent<Boolean> getFinish() {
        return finish;
    }

    public SingleLiveEvent<String> getCropAddAttachment() {
        return cropAddAttachment;
    }

    public SingleLiveEvent<Boolean> getYesSaveChange() {
        return yesSaveChange;
    }

    public ServerData getServerData(String centerName) {
        return repository.getServerData(centerName);
    }

    public void getPatientAttachmentListService(String newBaseUrl) {
        repository.getPatientAttachmentListService(newBaseUrl);
    }

    public void getAttachInfoService(String newBaseUrl) {
        repository.getAttachInfoService(newBaseUrl);
    }

    public void getAddAttachService(String newBaseUrl) {
        repository.getAddAttachService(newBaseUrl);
    }

    public void getDeleteAttachService(String newBaseUrl) {
        repository.getDeleteAttachService(newBaseUrl);
    }

    public void patientAttachmentList(String userLoginKey, int sickID) {
        repository.patientAttachmentList(userLoginKey, sickID);
    }

    public void attachInfo(String userLoginKey, int attachID) {
        repository.attachInfo(userLoginKey, attachID);
    }

    public void addAttach(String userLoginKey, AttachParameter attachParameter) {
        repository.addAttach(userLoginKey, attachParameter);
    }

    public void deleteAttach(String userLoginKey, int attachID) {
        repository.deleteAttach(userLoginKey, attachID);
    }
}
