package com.example.sipmobileapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sipmobileapp.R;
import com.example.sipmobileapp.databinding.PatientAdapterItemBinding;
import com.example.sipmobileapp.model.PatientInfo;
import com.example.sipmobileapp.utils.Converter;
import com.example.sipmobileapp.viewmodel.PatientViewModel;
import com.skydoves.powermenu.OnMenuItemClickListener;
import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;

import java.util.List;

public class PatientAdapter extends RecyclerView.Adapter<PatientAdapter.PatientHolder> {
    private Context context;
    private List<PatientInfo> patientInfoList;
    private PatientViewModel viewModel;

    public PatientAdapter(Context context, List<PatientInfo> patientInfoList, PatientViewModel viewModel) {
        this.context = context;
        this.patientInfoList = patientInfoList;
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public PatientHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PatientHolder(DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.patient_adapter_item,
                parent,
                false));
    }

    @Override
    public void onBindViewHolder(@NonNull PatientHolder holder, int position) {
        PatientInfo patientInfo = patientInfoList.get(position);
        holder.bindPatientInfo(patientInfo);

        holder.binding.imgButtonMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.getShowDialog().setValue(patientInfo);
            }
        });

        /*holder.binding.imgButtonMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PowerMenu powerMenu = new PowerMenu.Builder(context)
                        .addItem(new PowerMenuItem("مشاهده مستندات", R.drawable.magnifier))
                        .addItem(new PowerMenuItem("جوابدهی"))
                        .addItem(new PowerMenuItem("ثبت پارامترها"))
                        .setSize(330, 400)
                        .build();

                powerMenu.setOnMenuItemClickListener(new OnMenuItemClickListener<PowerMenuItem>() {
                    @Override
                    public void onItemClick(int position, PowerMenuItem item) {
                        switch (position) {
                            case 0:
                                viewModel.getNavigateToGallery().setValue(patientInfo);
                                powerMenu.dismiss();
                                break;
                            case 1:
                                viewModel.getNavigateToAnswer().setValue(patientInfo.getSickID());
                                powerMenu.dismiss();
                            case 2:
                                viewModel.getNavigateToRegisterParameter().setValue(patientInfo.getSickID());
                                powerMenu.dismiss();
                        }
                    }
                });
                powerMenu.showAsDropDown(view);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return patientInfoList == null ? 0 : patientInfoList.size();
    }

    public class PatientHolder extends RecyclerView.ViewHolder {
        private PatientAdapterItemBinding binding;

        public PatientHolder(PatientAdapterItemBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }

        public void bindPatientInfo(PatientInfo patientInfo) {
            binding.txtPatientID.setText(String.valueOf(patientInfo.getSickID()));
            binding.txtDate.setText(patientInfo.getDate());
            binding.txtPatientName.setText(Converter.convert(patientInfo.getPatientName()));
            binding.txtServices.setText(Converter.convert(patientInfo.getServices()));
        }
    }
}
