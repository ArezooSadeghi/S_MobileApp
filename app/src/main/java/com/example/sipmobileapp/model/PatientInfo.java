package com.example.sipmobileapp.model;

import java.io.Serializable;

public class PatientInfo implements Serializable {

    private int sickID;
    private String Date;
    private String firstName;
    private String lastName;
    private String patientName;
    private String services;

    public int getSickID() {
        return sickID;
    }

    public void setSickID(int sickID) {
        this.sickID = sickID;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getServices() {
        return services;
    }

    public void setServices(String services) {
        this.services = services;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
