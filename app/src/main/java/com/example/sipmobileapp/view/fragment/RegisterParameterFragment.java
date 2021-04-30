package com.example.sipmobileapp.view.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.sipmobileapp.R;
import com.example.sipmobileapp.databinding.FragmentRegisterParameterBinding;

import java.util.ArrayList;
import java.util.List;

public class RegisterParameterFragment extends Fragment {
    private FragmentRegisterParameterBinding binding;

    private static final String ARGS_SICK_ID = "sickID";

    public static RegisterParameterFragment newInstance(int sickID) {
        RegisterParameterFragment fragment = new RegisterParameterFragment();
        Bundle args = new Bundle();
        args.putInt(ARGS_SICK_ID, sickID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        List<LinearLayout> linearLayoutList = new ArrayList<>();


        for (int i = 0; i < 5; i++) {
            LinearLayout linearLayout = new LinearLayout(getContext());
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams rootLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            rootLayoutParams.setMargins(8, 16, 16, 0);
            linearLayout.setLayoutParams(rootLayoutParams);
            TextView textView = new TextView(getContext());
            textView.setText("نام بیمار: ");
            textView.setTextSize(18);
            textView.setTextColor(Color.parseColor("#000000"));
            textView.setGravity(Gravity.RIGHT);
            EditText editText = new EditText(getContext());
            editText.setText("محمد امانی");
            editText.setGravity(Gravity.RIGHT);
            editText.setEms(13);
            editText.setSelection(editText.getText().length());
            linearLayout.addView(editText);
            linearLayout.addView(textView);
            linearLayoutList.add(linearLayout);
        }

        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_register_parameter,
                container,
                false);

        for (int i = 0; i < linearLayoutList.size(); i++) {
            binding.layoutRoot.addView(linearLayoutList.get(i));
        }

        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(200, LinearLayout.LayoutParams.WRAP_CONTENT);
        linearLayout.setLayoutParams(layoutParams);
        linearLayout.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams rootLayoutParams1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        rootLayoutParams1.setMargins(16, 32, 16, 0);
        linearLayout.setLayoutParams(rootLayoutParams1);

        Button btnCancel = new Button(getContext());
        Button btnRecord = new Button(getContext());

        btnCancel.setText("انصراف");
        btnRecord.setText("ضبط");

        linearLayout.addView(btnCancel);
        linearLayout.addView(btnRecord);

        binding.layoutRoot.addView(linearLayout);

      /*  ExtendedFloatingActionButton button = new ExtendedFloatingActionButton(getContext());
        button.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        button.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        button.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
        LinearLayout.LayoutParams rootLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        rootLayoutParams.setMargins(200, 700, 240, 0);
        button.setLayoutParams(rootLayoutParams);
        button.setText("ضبط");
        binding.layoutRoot.addView(button);*/

        return binding.getRoot();
    }
}