package com.example.sipmobileapp.view.fragment;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.sipmobileapp.R;
import com.example.sipmobileapp.databinding.FragmentAnswerBinding;
import com.example.sipmobileapp.utils.SipMobileAppPreferences;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class AnswerFragment extends Fragment {
    private FragmentAnswerBinding binding;

    private int patientID;
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;

    private static final int PORT = 4010;
    private static final String APP_VERSION = "1.0";
    private static final String ARGS_PATIENT_ID = "patientID";
    private static final String SERVER_IP = "192.168.1.105";

    public static final String TAG = AnswerFragment.class.getSimpleName();

    public static AnswerFragment newInstance(int patientID) {
        AnswerFragment fragment = new AnswerFragment();
        Bundle args = new Bundle();
        args.putInt(ARGS_PATIENT_ID, patientID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        patientID = getArguments().getInt(ARGS_PATIENT_ID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_answer,
                container,
                false);

        openConnection();
        initViews();
        handleClicked();

        return binding.getRoot();
    }

    private void openConnection() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket socket = new Socket(SERVER_IP, PORT);
                    String deviceName = Build.MODEL;
                    String appName = getString(R.string.app_name);
                    String userName = SipMobileAppPreferences.getUsername(getContext());
                    String data = deviceName + ";" + appName + ";" + APP_VERSION + ";" + userName;
                    byte[] bData = data.getBytes("UTF-8");
                    byte[] bLength = CreateIntArray(bData.length);

                    byte[] bPacket = new byte[12 + bData.length];
                    int tag = 103;
                    byte[] bTag = CreateIntArray(tag);

                    System.arraycopy(bTag, 0, bPacket, 4, 4);
                    System.arraycopy(bLength, 0, bPacket, 8, 4);
                    System.arraycopy(bData, 0, bPacket, 12, bData.length);

                    dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    dataOutputStream.write(bPacket);
                    dataOutputStream.flush();

                    dataInputStream = new DataInputStream(socket.getInputStream());
                    new Thread(new Runnable() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void run() {
                            byte[] bytes = new byte[10000];
                            try {
                                int count = dataInputStream.read(bytes);
                                Log.d("Arezoo", "Count: " + count);

                                int index = 4;
                                byte[] bTagBigIndian = new byte[4];
                                System.arraycopy(bytes, index, bTagBigIndian, 0, bTag.length);
                                byte[] bTagLittleIndian = convertToLittleEndian(bTagBigIndian);
                                int tag = ByteBuffer.wrap(bTagLittleIndian).getInt();
                                Log.d("Arezoo", "Tag: " + tag);


                                index += 4;
                                byte[] bDataLengthBigIndian = new byte[4];
                                System.arraycopy(bytes, index, bDataLengthBigIndian, 0, bDataLengthBigIndian.length);
                                byte[] bDataLengthLittleEndian = convertToLittleEndian(bDataLengthBigIndian);
                                int dataLength = ByteBuffer.wrap(bDataLengthLittleEndian).getInt();
                                Log.d("Arezoo", "DataLength: " + dataLength);


                                index += 4;
                                byte[] bVersionBigEndian = new byte[8];
                                System.arraycopy(bytes, index, bVersionBigEndian, 0, bVersionBigEndian.length);
                                byte[] bVersionLittleEndian = convertToLittleEndian(bVersionBigEndian);
                                float version = ByteBuffer.wrap(bVersionLittleEndian).getFloat();
                                Log.d("Arezoo", "version: " + version);


                                index += 8;
                                byte[] bErrorCodeBigIndian = new byte[4];
                                System.arraycopy(bytes, index, bErrorCodeBigIndian, 0, bErrorCodeBigIndian.length);
                                byte[] bErrorCodeLittleIndian = convertToLittleEndian(bErrorCodeBigIndian);
                                int errorCode = ByteBuffer.wrap(bErrorCodeLittleIndian).getInt();
                                Log.d("Arezoo", "errorCode: " + errorCode);


                                index += 4;
                                byte[] bErrorLengthBigIndian = new byte[4];
                                System.arraycopy(bytes, index, bErrorLengthBigIndian, 0, bErrorLengthBigIndian.length);
                                byte[] bErrorLengthLittleIndian = convertToLittleEndian(bErrorLengthBigIndian);
                                int errorLength = ByteBuffer.wrap(bErrorLengthLittleIndian).getInt();
                                Log.d("Arezoo", "errorLength: " + errorLength);


                                index += 4;
                                byte[] bError = new byte[errorLength];
                                System.arraycopy(bytes, index, bError, 0, bError.length);
                                String error = new String(bError, StandardCharsets.UTF_16LE);
                                Log.d("Arezoo", "error: " + error);


                                index += bError.length;
                                byte[] bEPLengthBigIndian = new byte[4];
                                System.arraycopy(bytes, index, bEPLengthBigIndian, 0, bEPLengthBigIndian.length);
                                byte[] bEPLengthLittleIndian = convertToLittleEndian(bEPLengthBigIndian);
                                int EPLength = ByteBuffer.wrap(bEPLengthLittleIndian).getInt();
                                Log.d("Arezoo", "EPLength: " + EPLength);


                                index += 4;
                                byte[] bEP = new byte[EPLength];
                                System.arraycopy(bytes, index, bEP, 0, bEP.length);
                                String EP = new String(bEP, StandardCharsets.UTF_16LE);
                                Log.d("Arezoo", "EP: " + EP);

                            } catch (IOException exception) {
                                Log.e(TAG, exception.getMessage());
                            }
                        }
                    }).start();

                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        }).start();
    }

    private void initViews() {
        int patientID = getArguments().getInt(ARGS_PATIENT_ID);
        binding.txtPatientID.setText(String.valueOf(patientID));
    }

    private void handleClicked() {
        binding.txtAnswer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = charSequence.toString();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String data = patientID + ";" + text;
                            byte[] bData = data.getBytes("UTF-8");
                            byte[] bLength = CreateIntArray(bData.length);

                            byte[] bPacket = new byte[12 + bData.length];
                            int tag = 2000001;
                            byte[] bTag = CreateIntArray(tag);

                            System.arraycopy(bTag, 0, bPacket, 4, 4);
                            System.arraycopy(bLength, 0, bPacket, 8, 4);
                            System.arraycopy(bData, 0, bPacket, 12, bData.length);

                            dataOutputStream.write(bPacket);
                            dataOutputStream.flush();
                        } catch (IOException exception) {
                            Log.e(TAG, exception.getMessage());
                        } catch (NullPointerException exception) {
                            Log.e(TAG, exception.getMessage());
                        }
                    }
                }).start();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private byte[] CreateIntArray(int value) {
        byte[] bData = ByteBuffer.allocate(4).putInt(value).array();
        byte[] bData2 = new byte[4];
        bData2[0] = bData[3];
        bData2[1] = bData[2];
        bData2[2] = bData[1];
        bData2[3] = bData[0];
        return bData2;
    }

    private byte[] convertToLittleEndian(byte[] bigIndian) {
        byte[] littleIndian = new byte[bigIndian.length];
        for (int i = 0; i < littleIndian.length; i++) {
            littleIndian[i] = bigIndian[littleIndian.length - 1 - i];
        }
        return littleIndian;
    }
}

