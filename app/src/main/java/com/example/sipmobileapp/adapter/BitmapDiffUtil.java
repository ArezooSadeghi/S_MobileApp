package com.example.sipmobileapp.adapter;

import android.graphics.Bitmap;

import androidx.recyclerview.widget.DiffUtil;

import java.util.ArrayList;
import java.util.List;

public class BitmapDiffUtil extends DiffUtil.Callback {

    private List<Bitmap> oldBitmapList = new ArrayList<>();
    private List<Bitmap> newBitmapList = new ArrayList<>();

    public BitmapDiffUtil(List<Bitmap> oldBitmapList, List<Bitmap> newBitmapList) {
        this.oldBitmapList = oldBitmapList;
        this.newBitmapList = newBitmapList;
    }

    @Override
    public int getOldListSize() {
        return oldBitmapList == null ? 0 : oldBitmapList.size();
    }

    @Override
    public int getNewListSize() {
        return newBitmapList == null ? 0 : newBitmapList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return true;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldBitmapList.get(oldItemPosition) == newBitmapList.get(newItemPosition);
    }
}
