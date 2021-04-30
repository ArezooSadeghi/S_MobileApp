package com.example.sipmobileapp.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sipmobileapp.utils.SipMobileAppPreferences;
import com.example.sipmobileapp.view.fragment.FullScreenImageFragment;
import com.theartofdev.edmodo.cropper.CropImage;

public class FullScreenImageContainerActivity extends SingleFragmentActivity {

    public static final String EXTRA_IMAGE = "image";
    public static final String ARGS_ATTACH_ID = "attachID";

    @Override
    public Fragment createFragment() {
        Uri image = getIntent().getParcelableExtra(EXTRA_IMAGE);
        int attachID = getIntent().getIntExtra(ARGS_ATTACH_ID, 0);
        return FullScreenImageFragment.newInstance(image, attachID);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == Activity.RESULT_OK) {
                Uri resultUri = result.getUri();
                SipMobileAppPreferences.setUri(this, resultUri.toString());
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public static Intent newIntent(Context context, Uri image, int attachID) {
        Intent intent = new Intent(context, FullScreenImageContainerActivity.class);
        intent.putExtra(EXTRA_IMAGE, image);
        intent.putExtra(ARGS_ATTACH_ID, attachID);
        return intent;
    }

    @Override
    public void onBackPressed() {
        SipMobileAppPreferences.setUri(this, null);
        super.onBackPressed();
    }
}