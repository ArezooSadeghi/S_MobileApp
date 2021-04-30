package com.example.sipmobileapp.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sipmobileapp.utils.SipMobileAppPreferences;
import com.example.sipmobileapp.view.fragment.LoginFragment;

public class LoginContainerActivity extends SingleFragmentActivity {

    @Override
    public Fragment createFragment() {
        return LoginFragment.newInstance();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (SipMobileAppPreferences.getUserLoginKey(this) == null) {
            super.onCreate(savedInstanceState);
        } else {
            super.onCreate(savedInstanceState);
            Intent intent = PatientContainerActivity.newIntent(this);
            startActivity(intent);
            finish();
        }
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, LoginContainerActivity.class);
    }
}