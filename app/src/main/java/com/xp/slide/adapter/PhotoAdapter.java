package com.xp.slide.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xp.slide.R;

import java.util.ArrayList;
import java.util.List;


public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.BaseHolder> {
    private List<String> mDataList = new ArrayList<>();
    private Context mContext;

    @NonNull
    @Override
    public BaseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.home_rv_item_layout, null);
        return new PhotoHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
        holder.bindData(position);
    }

    @Override
    public int getItemCount() {
        return mDataList != null ? mDataList.size() : 0;
    }

    public void setDataList(int num) {
        if (mDataList != null) {
            mDataList.clear();
        } else {
            mDataList = new ArrayList<>();
        }
        for (int i = 0; i < num; i++) {
            mDataList.add("");
        }

        notifyDataSetChanged();
    }


    private class PhotoHolder extends BaseHolder {
        private ImageView itemIv;

        public PhotoHolder(@NonNull View itemView) {
            super(itemView);
            itemIv = itemView.findViewById(R.id.item_iv);
        }

        @Override
        public void bindData(int position) {
            itemIv.setImageResource((position + 1) % 2 == 1 ? R.mipmap.home_b : R.mipmap.home_g);
        }
    }

    abstract class BaseHolder extends RecyclerView.ViewHolder {

        public BaseHolder(@NonNull View itemView) {
            super(itemView);
        }

        abstract public void bindData(int position);
    }
}
