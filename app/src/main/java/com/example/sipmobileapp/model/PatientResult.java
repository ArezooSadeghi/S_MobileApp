package com.example.sipmobileapp.model;

public class PatientResult {

    private String error;
    private String errorCode;
    private PatientInfo[] patients;

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

    public PatientInfo[] getPatients() {
        return patients;
    }

    public void setPatients(PatientInfo[] patients) {
        this.patients = patients;
    }
}
