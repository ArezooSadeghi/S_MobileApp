package com.example.sipmobileapp.retrofit;

import com.example.sipmobileapp.model.AttachParameter;
import com.example.sipmobileapp.model.AttachResult;
import com.example.sipmobileapp.model.PatientResult;
import com.example.sipmobileapp.model.UserParameter;
import com.example.sipmobileapp.model.UserResult;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface SipMobileAppService {

    @POST(".")
    Call<UserResult> userLogin(@Body UserParameter userParameter);

    @GET(".")
    Call<PatientResult> search(@Header("userLoginKey") String userLoginKey, @Query("patientName") String patientName);

    @GET(".")
    Call<AttachResult> patientAttachmentList(@Header("userLoginKey") String userLoginKey, @Query("sickID") int sickID);

    @GET(".")
    Call<AttachResult> attachInfo(@Header("userLoginKey") String userLoginKey, @Query("attachID") int attachID);

    @POST(".")
    Call<AttachResult> addAttach(@Header("userLoginKey") String userLoginKey, @Body AttachParameter attachParameter);

    @DELETE(".")
    Call<AttachResult> deleteAttach(@Header("userLoginKey") String userLoginKey, @Query("attachID") int attachID);
}
