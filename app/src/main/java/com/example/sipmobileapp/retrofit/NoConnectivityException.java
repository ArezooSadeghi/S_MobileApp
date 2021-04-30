package com.example.sipmobileapp.retrofit;

import androidx.annotation.Nullable;

import java.io.IOException;

public class NoConnectivityException extends IOException {

    @Nullable
    @Override
    public String getMessage() {
        return "عدم اتصال به اینترنت";
    }
}
