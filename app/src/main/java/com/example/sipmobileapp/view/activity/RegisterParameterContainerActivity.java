package com.example.sipmobileapp.view.activity;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.example.sipmobileapp.view.fragment.RegisterParameterFragment;

public class RegisterParameterContainerActivity extends SingleFragmentActivity {

    private static final String EXTRA_SICK_ID = "sickID";

    @Override
    public Fragment createFragment() {
        int sickID = getIntent().getIntExtra(EXTRA_SICK_ID, 0);
        return RegisterParameterFragment.newInstance(sickID);
    }

    public static Intent newIntent(Context context, int sickID) {
        Intent intent = new Intent(context, RegisterParameterContainerActivity.class);
        intent.putExtra(EXTRA_SICK_ID, sickID);
        return intent;
    }
}