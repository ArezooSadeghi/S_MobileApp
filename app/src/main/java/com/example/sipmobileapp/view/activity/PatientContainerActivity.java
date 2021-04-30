package com.example.sipmobileapp.view.activity;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.example.sipmobileapp.view.fragment.PatientFragment;

public class PatientContainerActivity extends SingleFragmentActivity {

    @Override
    public Fragment createFragment() {
        return PatientFragment.newInstance();
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, PatientContainerActivity.class);
    }
}
