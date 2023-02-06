package com.example.dodamdodam.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.dodamdodam.R;
import com.example.dodamdodam.models.RecyclerViewItem;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    public interface OnItemClickEventListener{
        void onItemClick(View a_view, int a_position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgView_item;
        ImageView delete_sign;
        public ViewHolder(@NonNull View itemView, final OnItemClickEventListener a_itemClickListener) {
            super(itemView);
            imgView_item = (ImageView) itemView.findViewById(R.id.imgView_item);
            delete_sign = (ImageView) itemView.findViewById(R.id.del_sign);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        a_itemClickListener.onItemClick(view, position);
                    }
                }
            });
        }
    }

    private RecyclerViewItem mList = null;
    private Context mContext = null;
    private OnItemClickEventListener mItemClickListener;

    public void setOnItemClickListener(OnItemClickEventListener a_listener){
        mItemClickListener = a_listener;
    }

    public CustomAdapter(RecyclerViewItem list, Context context) {
        this.mList = list;
        this.mContext = context;
    }

    @NonNull
    @Override
    public CustomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.item_add, parent, false);
        CustomAdapter.ViewHolder vh = new CustomAdapter.ViewHolder(view, mItemClickListener);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(mContext).load(mList.getGalleryuri().get(position)).override(1000).into(holder.imgView_item);
    }

    @Override
    public int getItemCount() {
        return mList.getGalleryuri().size();
    }
}