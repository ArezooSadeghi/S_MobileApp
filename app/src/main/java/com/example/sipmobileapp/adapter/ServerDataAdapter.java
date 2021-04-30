package com.example.sipmobileapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sipmobileapp.R;
import com.example.sipmobileapp.databinding.ServerDataAdapterItemBinding;
import com.example.sipmobileapp.model.ServerData;
import com.example.sipmobileapp.viewmodel.LoginViewModel;

import java.util.List;

public class ServerDataAdapter extends RecyclerView.Adapter<ServerDataAdapter.ServerDataHolder> {
    private Context mContext;
    private List<ServerData> mServerDataList;
    private LoginViewModel mViewModel;

    public ServerDataAdapter(Context context, List<ServerData> serverDataList, LoginViewModel viewModel) {
        mContext = context;
        mServerDataList = serverDataList;
        mViewModel = viewModel;
    }

    @NonNull
    @Override
    public ServerDataHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ServerDataHolder(DataBindingUtil.inflate(
                LayoutInflater.from(mContext),
                R.layout.server_data_adapter_item,
                parent,
                false));
    }

    @Override
    public void onBindViewHolder(@NonNull ServerDataHolder holder, int position) {
        ServerData serverData = mServerDataList.get(position);
        holder.bindServerData(serverData);

        holder.mBinding.imgViewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewModel.getEditClicked().setValue(serverData);
            }
        });

        holder.mBinding.imgViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewModel.getDeleteClicked().setValue(serverData);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mServerDataList == null ? 0 : mServerDataList.size();
    }

    public class ServerDataHolder extends RecyclerView.ViewHolder {
        private ServerDataAdapterItemBinding mBinding;

        public ServerDataHolder(ServerDataAdapterItemBinding binding) {
            super(binding.getRoot());

            mBinding = binding;
        }

        public void bindServerData(ServerData serverData) {
            mBinding.txtCenterName.setText(serverData.getCenterName());
            mBinding.txtIpAddress.setText(serverData.getIPAddress());
        }
    }
}
