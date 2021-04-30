package com.example.sipmobileapp.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.sipmobileapp.database.SipMobileAppDBHelper;
import com.example.sipmobileapp.database.SipMobileAppSchema;
import com.example.sipmobileapp.model.AttachParameter;
import com.example.sipmobileapp.model.AttachResult;
import com.example.sipmobileapp.model.PatientResult;
import com.example.sipmobileapp.model.ServerData;
import com.example.sipmobileapp.model.UserParameter;
import com.example.sipmobileapp.model.UserResult;
import com.example.sipmobileapp.retrofit.AttachResultDeserializer;
import com.example.sipmobileapp.retrofit.NoConnectivityException;
import com.example.sipmobileapp.retrofit.PatientResultDeserializer;
import com.example.sipmobileapp.retrofit.RetrofitInstance;
import com.example.sipmobileapp.retrofit.SipMobileAppService;
import com.example.sipmobileapp.retrofit.UserResultDeserializer;
import com.example.sipmobileapp.viewmodel.SingleLiveEvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SipMobileAppRepository {
    public static SipMobileAppRepository sInstance;
    private Context context;
    private SQLiteDatabase mDatabase;
    private SipMobileAppService userLoginService, searchService, patientAttachmentListService, attachInfoService, addAttachService, deleteAttachService;

    public static final String TAG = SipMobileAppRepository.class.getSimpleName();

    private SingleLiveEvent<String> noConnectivitySingleLiveEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<Boolean> timeOutExceptionHappenSingleLiveEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<String> wrongAddressSingleLiveEvent = new SingleLiveEvent<>();

    private SingleLiveEvent<UserResult> userLoginSingleLiveEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<String> errorUserLoginSingleLiveEvent = new SingleLiveEvent<>();

    private SingleLiveEvent<PatientResult> searchSingleLiveEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<String> errorSearchSingleLiveEvent = new SingleLiveEvent<>();

    private SingleLiveEvent<AttachResult> patientAttachmentListResultSingleLiveEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<String> errorPatientAttachmentListResultSingleLiveEvent = new SingleLiveEvent<>();

    private SingleLiveEvent<AttachResult> attachInfoSingleLiveEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<String> errorAttachInfoSingleLiveEvent = new SingleLiveEvent<>();

    private SingleLiveEvent<AttachResult> addAttachSingleLiveEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<String> errorAddAttachSingleLiveEvent = new SingleLiveEvent<>();

    private SingleLiveEvent<AttachResult> deleteAttachSingleLiveEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<String> errorDeleteAttachSingleLiveEvent = new SingleLiveEvent<>();

    private SipMobileAppRepository(Context context) {
        this.context = context;
        SipMobileAppDBHelper helper = new SipMobileAppDBHelper(context);
        mDatabase = helper.getWritableDatabase();
    }

    public static SipMobileAppRepository getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new SipMobileAppRepository(context.getApplicationContext());
        }
        return sInstance;
    }

    public void getUserLoginService(String newBaseUrl) {
        RetrofitInstance.getNewBaseUrl(newBaseUrl);
        userLoginService = RetrofitInstance.userLoginRetrofitInstance(new TypeToken<UserResult>() {
        }.getType(), new UserResultDeserializer(), context).create(SipMobileAppService.class);
    }

    public void getSearchService(String newBaseUrl) {
        RetrofitInstance.getNewBaseUrl(newBaseUrl);
        searchService = RetrofitInstance.searchRetrofitInstance(new TypeToken<PatientResult>() {
        }.getType(), new PatientResultDeserializer(), context).create(SipMobileAppService.class);
    }

    public void getPatientAttachmentListService(String newBaseUrl) {
        RetrofitInstance.getNewBaseUrl(newBaseUrl);
        patientAttachmentListService = RetrofitInstance.patientAttachmentListRetrofitInstance(new TypeToken<AttachResult>() {
        }.getType(), new AttachResultDeserializer(), context).create(SipMobileAppService.class);
    }

    public void getAttachInfoService(String newBaseUrl) {
        RetrofitInstance.getNewBaseUrl(newBaseUrl);
        attachInfoService = RetrofitInstance.attachInfoRetrofitInstance(new TypeToken<AttachResult>() {
        }.getType(), new AttachResultDeserializer(), context).create(SipMobileAppService.class);
    }

    public void getAddAttachService(String newBaseUrl) {
        RetrofitInstance.getNewBaseUrl(newBaseUrl);
        addAttachService = RetrofitInstance.addAttachRetrofitInstance(new TypeToken<AttachResult>() {
        }.getType(), new AttachResultDeserializer(), context).create(SipMobileAppService.class);
    }

    public void getDeleteAttachService(String newBaseUrl) {
        RetrofitInstance.getNewBaseUrl(newBaseUrl);
        deleteAttachService = RetrofitInstance.deleteAttachRetrofitInstance(new TypeToken<AttachResult>() {
        }.getType(), new AttachResultDeserializer(), context).create(SipMobileAppService.class);
    }

    public SingleLiveEvent<UserResult> getUserLoginSingleLiveEvent() {
        return userLoginSingleLiveEvent;
    }

    public SingleLiveEvent<String> getErrorUserLoginSingleLiveEvent() {
        return errorUserLoginSingleLiveEvent;
    }

    public SingleLiveEvent<String> getNoConnectivitySingleLiveEvent() {
        return noConnectivitySingleLiveEvent;
    }

    public SingleLiveEvent<Boolean> getTimeOutExceptionHappenSingleLiveEvent() {
        return timeOutExceptionHappenSingleLiveEvent;
    }

    public SingleLiveEvent<String> getWrongAddressSingleLiveEvent() {
        return wrongAddressSingleLiveEvent;
    }

    public SingleLiveEvent<PatientResult> getSearchSingleLiveEvent() {
        return searchSingleLiveEvent;
    }

    public SingleLiveEvent<String> getErrorSearchSingleLiveEvent() {
        return errorSearchSingleLiveEvent;
    }

    public SingleLiveEvent<AttachResult> getPatientAttachmentListResultSingleLiveEvent() {
        return patientAttachmentListResultSingleLiveEvent;
    }

    public SingleLiveEvent<String> getErrorPatientAttachmentListResultSingleLiveEvent() {
        return errorPatientAttachmentListResultSingleLiveEvent;
    }

    public SingleLiveEvent<AttachResult> getAttachInfoSingleLiveEvent() {
        return attachInfoSingleLiveEvent;
    }

    public SingleLiveEvent<String> getErrorAttachInfoSingleLiveEvent() {
        return errorAttachInfoSingleLiveEvent;
    }

    public SingleLiveEvent<AttachResult> getAddAttachSingleLiveEvent() {
        return addAttachSingleLiveEvent;
    }

    public SingleLiveEvent<String> getErrorAddAttachSingleLiveEvent() {
        return errorAddAttachSingleLiveEvent;
    }

    public SingleLiveEvent<AttachResult> getDeleteAttachSingleLiveEvent() {
        return deleteAttachSingleLiveEvent;
    }

    public SingleLiveEvent<String> getErrorDeleteAttachSingleLiveEvent() {
        return errorDeleteAttachSingleLiveEvent;
    }

    public void insertServerData(ServerData serverData) {
        ContentValues values = new ContentValues();

        values.put(SipMobileAppSchema.ServerDataTable.Cols.CENTER_NAME, serverData.getCenterName());
        values.put(SipMobileAppSchema.ServerDataTable.Cols.IP_ADDRESS, serverData.getIPAddress());
        values.put(SipMobileAppSchema.ServerDataTable.Cols.PORT, serverData.getPort());

        mDatabase.insert(SipMobileAppSchema.ServerDataTable.NAME, null, values);
    }

    public List<ServerData> getServerDataList() {
        List<ServerData> serverDataList = new ArrayList<>();
        Cursor cursor = mDatabase.query(
                SipMobileAppSchema.ServerDataTable.NAME,
                null,
                null,
                null,
                null,
                null,
                null);

        if (cursor == null || cursor.getCount() == 0) {
            return serverDataList;
        }

        try {

            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {

                String centerName = cursor.getString(cursor.getColumnIndex(SipMobileAppSchema.ServerDataTable.Cols.CENTER_NAME));
                String ipAddress = cursor.getString(cursor.getColumnIndex(SipMobileAppSchema.ServerDataTable.Cols.IP_ADDRESS));
                String port = cursor.getString(cursor.getColumnIndex(SipMobileAppSchema.ServerDataTable.Cols.PORT));

                ServerData serverData = new ServerData(centerName, ipAddress, port);

                serverDataList.add(serverData);

                cursor.moveToNext();
            }

        } finally {
            cursor.close();
        }
        return serverDataList;
    }

    public ServerData getServerData(String centerName) {
        ServerData serverData = new ServerData();
        String selection = "centerName=?";
        String[] selectionArgs = {centerName};
        Cursor cursor = mDatabase.query(
                SipMobileAppSchema.ServerDataTable.NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null);

        if (cursor == null || cursor.getCount() == 0) {
            return serverData;
        }

        try {

            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {

                String ipAddress = cursor.getString(cursor.getColumnIndex(SipMobileAppSchema.ServerDataTable.Cols.IP_ADDRESS));
                String port = cursor.getString(cursor.getColumnIndex(SipMobileAppSchema.ServerDataTable.Cols.PORT));

                serverData.setCenterName(centerName);
                serverData.setIPAddress(ipAddress);
                serverData.setPort(port);

                cursor.moveToNext();
            }

        } finally {
            cursor.close();
        }
        return serverData;
    }

    public void deleteServerData(ServerData serverData) {
        String whereClause = "centerName=?";
        String whereArgs[] = {serverData.getCenterName()};
        mDatabase.delete(SipMobileAppSchema.ServerDataTable.NAME, whereClause, whereArgs);
    }

    public void userLogin(UserParameter userParameter) {
        userLoginService.userLogin(userParameter).enqueue(new Callback<UserResult>() {
            @Override
            public void onResponse(Call<UserResult> call, Response<UserResult> response) {
                if (response.isSuccessful()) {
                    userLoginSingleLiveEvent.setValue(response.body());
                } else {
                    Gson gson = new GsonBuilder().create();
                    UserResult userResult = new UserResult();
                    try {
                        userResult = gson.fromJson(response.errorBody().string(), UserResult.class);
                        errorUserLoginSingleLiveEvent.setValue(userResult.getError());
                    } catch (IOException e) {
                        Log.e(TAG, e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<UserResult> call, Throwable t) {
                if (t instanceof NoConnectivityException) {
                    noConnectivitySingleLiveEvent.setValue(t.getMessage());
                } else if (t instanceof SocketTimeoutException) {
                    timeOutExceptionHappenSingleLiveEvent.setValue(true);
                } else {
                    wrongAddressSingleLiveEvent.setValue("سرور موجود نمی باشد");
                }
            }
        });
    }

    public void search(String userLoginKey, String patientName) {
        searchService.search(userLoginKey, patientName).enqueue(new Callback<PatientResult>() {
            @Override
            public void onResponse(Call<PatientResult> call, Response<PatientResult> response) {
                if (response.isSuccessful()) {
                    searchSingleLiveEvent.setValue(response.body());
                } else {
                    Gson gson = new GsonBuilder().create();
                    PatientResult patientResult = new PatientResult();
                    try {
                        patientResult = gson.fromJson(response.errorBody().string(), PatientResult.class);
                        errorSearchSingleLiveEvent.setValue(patientResult.getError());
                    } catch (IOException e) {
                        Log.e(TAG, e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<PatientResult> call, Throwable t) {
                if (t instanceof NoConnectivityException) {
                    noConnectivitySingleLiveEvent.setValue(t.getMessage());
                } else if (t instanceof SocketTimeoutException) {
                    timeOutExceptionHappenSingleLiveEvent.setValue(true);
                }
            }
        });
    }

    public void patientAttachmentList(String userLoginKey, int sickID) {
        patientAttachmentListService.patientAttachmentList(userLoginKey, sickID).enqueue(new Callback<AttachResult>() {
            @Override
            public void onResponse(Call<AttachResult> call, Response<AttachResult> response) {
                if (response.isSuccessful()) {
                    patientAttachmentListResultSingleLiveEvent.setValue(response.body());
                } else {
                    Gson gson = new GsonBuilder().create();
                    AttachResult attachResult = new AttachResult();
                    try {
                        attachResult = gson.fromJson(response.errorBody().string(), AttachResult.class);
                        errorPatientAttachmentListResultSingleLiveEvent.setValue(attachResult.getError());
                    } catch (IOException e) {
                        Log.e(TAG, e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<AttachResult> call, Throwable t) {
                if (t instanceof NoConnectivityException) {
                    noConnectivitySingleLiveEvent.setValue(t.getMessage());
                } else if (t instanceof SocketTimeoutException) {
                    timeOutExceptionHappenSingleLiveEvent.setValue(true);
                }
            }
        });
    }

    public void attachInfo(String userLoginKey, int attachID) {
        attachInfoService.attachInfo(userLoginKey, attachID).enqueue(new Callback<AttachResult>() {
            @Override
            public void onResponse(Call<AttachResult> call, Response<AttachResult> response) {
                if (response.isSuccessful()) {
                    attachInfoSingleLiveEvent.setValue(response.body());
                } else {
                    Gson gson = new GsonBuilder().create();
                    AttachResult attachResult = new AttachResult();
                    try {
                        attachResult = gson.fromJson(response.errorBody().string(), AttachResult.class);
                        errorAttachInfoSingleLiveEvent.setValue(attachResult.getError());
                    } catch (IOException e) {
                        Log.e(TAG, e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<AttachResult> call, Throwable t) {
                if (t instanceof NoConnectivityException) {
                    noConnectivitySingleLiveEvent.setValue(t.getMessage());
                } else if (t instanceof SocketTimeoutException) {
                    timeOutExceptionHappenSingleLiveEvent.setValue(true);
                }
            }
        });
    }

    public void addAttach(String userLoginKey, AttachParameter attachParameter) {
        addAttachService.addAttach(userLoginKey, attachParameter).enqueue(new Callback<AttachResult>() {
            @Override
            public void onResponse(Call<AttachResult> call, Response<AttachResult> response) {
                if (response.isSuccessful()) {
                    addAttachSingleLiveEvent.setValue(response.body());
                } else {
                    Gson gson = new GsonBuilder().create();
                    AttachResult attachResult = new AttachResult();
                    try {
                        attachResult = gson.fromJson(response.errorBody().string(), AttachResult.class);
                        errorAddAttachSingleLiveEvent.setValue(attachResult.getError());
                    } catch (IOException e) {
                        Log.e(TAG, e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<AttachResult> call, Throwable t) {
                if (t instanceof NoConnectivityException) {
                    noConnectivitySingleLiveEvent.setValue(t.getMessage());
                } else if (t instanceof SocketTimeoutException) {
                    timeOutExceptionHappenSingleLiveEvent.setValue(true);
                }
            }
        });
    }

    public void deleteAttach(String userLoginKey, int attachID) {
        deleteAttachService.deleteAttach(userLoginKey, attachID).enqueue(new Callback<AttachResult>() {
            @Override
            public void onResponse(Call<AttachResult> call, Response<AttachResult> response) {
                if (response.isSuccessful()) {
                    deleteAttachSingleLiveEvent.setValue(response.body());
                } else {
                    Gson gson = new GsonBuilder().create();
                    AttachResult attachResult = new AttachResult();
                    try {
                        attachResult = gson.fromJson(response.errorBody().string(), AttachResult.class);
                        errorDeleteAttachSingleLiveEvent.setValue(attachResult.getError());
                    } catch (IOException e) {
                        Log.e(TAG, e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<AttachResult> call, Throwable t) {
                if (t instanceof NoConnectivityException) {
                    noConnectivitySingleLiveEvent.setValue(t.getMessage());
                } else if (t instanceof SocketTimeoutException) {
                    timeOutExceptionHappenSingleLiveEvent.setValue(true);
                }
            }
        });
    }
}
