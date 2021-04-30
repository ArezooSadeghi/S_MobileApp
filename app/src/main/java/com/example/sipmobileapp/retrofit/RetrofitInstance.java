package com.example.sipmobileapp.retrofit;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {
    public static String BASE_URL = "";

    public static Retrofit userLoginRetrofitInstance(Type type, Object typeAdapter, Context context) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new ConnectivityInterceptor(context))
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .cache(null)
                .build();

        return new Retrofit.Builder()
                .baseUrl("http://" + BASE_URL + "/api/v1/users/login/")
                .addConverterFactory(createConverter(type, typeAdapter))
                .client(client)
                .build();
    }

    public static Retrofit searchRetrofitInstance(Type type, Object typeAdapter, Context context) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new ConnectivityInterceptor(context))
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .cache(null)
                .build();

        return new Retrofit.Builder()
                .baseUrl("http://" + BASE_URL + "/api/v1/patients/search/")
                .addConverterFactory(createConverter(type, typeAdapter))
                .client(client)
                .build();
    }

    public static Retrofit patientAttachmentListRetrofitInstance(Type type, Object typeAdapter, Context context) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new ConnectivityInterceptor(context))
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .cache(null)
                .build();

        return new Retrofit.Builder()
                .baseUrl("http://" + BASE_URL + "/api/v1/attachs/ListBySickID/")
                .addConverterFactory(createConverter(type, typeAdapter))
                .client(client)
                .build();
    }

    public static Retrofit attachInfoRetrofitInstance(Type type, Object typeAdapter, Context context) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new ConnectivityInterceptor(context))
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .cache(null)
                .build();

        return new Retrofit.Builder()
                .baseUrl("http://" + BASE_URL + "/api/v1/attachs/Info/")
                .addConverterFactory(createConverter(type, typeAdapter))
                .client(client)
                .build();
    }

    public static Retrofit addAttachRetrofitInstance(Type type, Object typeAdapter, Context context) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new ConnectivityInterceptor(context))
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .cache(null)
                .build();

        return new Retrofit.Builder()
                .baseUrl("http://" + BASE_URL + "/api/v1/attachs/Add/")
                .addConverterFactory(createConverter(type, typeAdapter))
                .client(client)
                .build();
    }

    public static Retrofit deleteAttachRetrofitInstance(Type type, Object typeAdapter, Context context) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new ConnectivityInterceptor(context))
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .cache(null)
                .build();

        return new Retrofit.Builder()
                .baseUrl("http://" + BASE_URL + "/api/v1/attachs/Delete/")
                .addConverterFactory(createConverter(type, typeAdapter))
                .client(client)
                .build();
    }

    public static Converter.Factory createConverter(Type type, Object typeAdapter) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(type, typeAdapter);
        Gson gson = gsonBuilder.create();
        return GsonConverterFactory.create(gson);
    }

    public static void getNewBaseUrl(String newBaseUrl) {
        BASE_URL = newBaseUrl;
    }
}
