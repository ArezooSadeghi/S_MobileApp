package com.example.sipmobileapp.model;

public class AttachResult {

    private String error;
    private String errorCode;
    private AttachInfo[] attachs;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public AttachInfo[] getAttachs() {
        return attachs;
    }

    public void setAttachs(AttachInfo[] attachs) {
        this.attachs = attachs;
    }
}
