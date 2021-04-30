package com.example.sipmobileapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SipMobileAppPreferences {

    private static final String USER_LOGIN_KEY = "userLoginKey";
    private static final String CENTER_NAME = "centerName";
    private static final String USERNAME = "userName";
    private static final String ATTACH_ID = "attachID";
    private static final String PATIENT_ID = "patientID";
    private static final String URI = "uri";

    public static String getUserLoginKey(Context context) {
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.getString(USER_LOGIN_KEY, null);
    }

    public static void setUserLoginKey(Context context, String userLoginKey) {
        SharedPreferences preferences = getSharedPreferences(context);
        preferences.edit().putString(USER_LOGIN_KEY, userLoginKey).commit();
    }

    public static String getCenterName(Context context) {
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.getString(CENTER_NAME, null);
    }

    public static void setCenterName(Context context, String centerName) {
        SharedPreferences preferences = getSharedPreferences(context);
        preferences.edit().putString(CENTER_NAME, centerName).commit();
    }

    public static String getUsername(Context context) {
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.getString(USERNAME, null);
    }

    public static void setUsername(Context context, String userName) {
        SharedPreferences preferences = getSharedPreferences(context);
        preferences.edit().putString(USERNAME, userName).commit();
    }

    public static int getAttachId(Context context) {
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.getInt(ATTACH_ID, 0);
    }

    public static void setAttachId(Context context, int attachID) {
        SharedPreferences preferences = getSharedPreferences(context);
        preferences.edit().putInt(ATTACH_ID, attachID).commit();
    }

    public static String getUri(Context context) {
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.getString(URI, null);
    }

    public static void setUri(Context context, String uri) {
        SharedPreferences preferences = getSharedPreferences(context);
        preferences.edit().putString(URI, uri).commit();
    }

    public static int getPatientId(Context context) {
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.getInt(PATIENT_ID, 0);
    }

    public static void setPatientId(Context context, int patientID) {
        SharedPreferences preferences = getSharedPreferences(context);
        preferences.edit().putInt(PATIENT_ID, patientID).commit();
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(
                context.getPackageName(),
                context.MODE_PRIVATE);
    }
}
