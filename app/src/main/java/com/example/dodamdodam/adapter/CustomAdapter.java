package com.example.dodamdodam.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.content.Context;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.dodamdodam.R;
import com.example.dodamdodam.view.RecyclerViewItem;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgView_item;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgView_item = (ImageView) itemView.findViewById(R.id.imgView_item);
        }
    }

    private ArrayList<RecyclerViewItem> mList = null;
    private Context mContext = null;

    public CustomAdapter(ArrayList<RecyclerViewItem> list, Context context) {
        this.mList = list;
        this.mContext = context;
    }

    @NonNull
    @Override
    public CustomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.item_add, parent, false);
        CustomAdapter.ViewHolder vh = new CustomAdapter.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecyclerViewItem item = mList.get(position);// 사진 없어서 기본 파일로 이미지 띄움
        Glide.with(mContext).load(item.getGalleryuri()).override(1000).into(holder.imgView_item);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}