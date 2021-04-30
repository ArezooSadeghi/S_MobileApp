package com.example.sipmobileapp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sipmobileapp.R;
import com.example.sipmobileapp.databinding.BitmapAdapterItemBinding;
import com.example.sipmobileapp.viewmodel.AttachmentViewModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BitmapAdapter extends RecyclerView.Adapter<BitmapAdapter.BitmapHolder> {
    private Context context;
    private List<Bitmap> bitmapList;
    private AttachmentViewModel viewModel;
    private Map<Bitmap, String> mapBitmap;
    private Map<Uri, String> mapUri;

    public BitmapAdapter(Context context, List<Bitmap> bitmapList, AttachmentViewModel viewModel) {
        this.context = context;
        this.bitmapList = bitmapList;
        this.viewModel = viewModel;
    }

    public void setMapBitmap(Map<Bitmap, String> mapBitmap) {
        this.mapBitmap = mapBitmap;
    }

    public void setMapUri(Map<Uri, String> mapUri) {
        this.mapUri = mapUri;
    }

    @NonNull
    @Override
    public BitmapHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BitmapHolder(DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.bitmap_adapter_item,
                parent,
                false));
    }

    @Override
    public void onBindViewHolder(@NonNull BitmapHolder holder, int position) {
        holder.bindBitmap(bitmapList.get(position));
        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap bitmap = bitmapList.get(position);
                String imageName = mapBitmap.get(bitmap);
                Uri uri = null;
                for (Map.Entry<Uri, String> entry : mapUri.entrySet()) {
                    if (entry.getValue().equals(imageName)) {
                        uri = entry.getKey();
                    }
                }
                Map<Uri, String> mapUri = new HashMap<>();
                mapUri.put(uri, imageName);
                viewModel.getTestShowFullScreenImage().setValue(mapUri);
            }
        });
    }

    public void updateBitmapList(List<Bitmap> newBitmapList) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new BitmapDiffUtil(bitmapList, newBitmapList));
        diffResult.dispatchUpdatesTo(this);
        bitmapList.clear();
        bitmapList.addAll(newBitmapList);
    }

    @Override
    public int getItemCount() {
        return bitmapList == null ? 0 : bitmapList.size();
    }

    public class BitmapHolder extends RecyclerView.ViewHolder {
        private BitmapAdapterItemBinding binding;

        public BitmapHolder(BitmapAdapterItemBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }

        public void bindBitmap(Bitmap bitmap) {
            Glide.with(context).load(bitmap).into(binding.imgView);
        }
    }
}
