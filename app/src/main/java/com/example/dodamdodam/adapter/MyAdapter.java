package com.example.dodamdodam.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.dodamdodam.R;
import com.example.dodamdodam.Utils.SquareImageView;
import com.example.dodamdodam.models.DetailInfo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<DetailInfo> detailInfos;
    private DetailInfo detailInfo;

    public MyAdapter(Context context, ArrayList<DetailInfo> detailInfos){
        this.detailInfos = detailInfos;
        this.mContext = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        SquareImageView imgView_item;
        ProgressBar mProgressBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgView_item = (SquareImageView) itemView.findViewById(R.id.gridImageView);
            mProgressBar = (ProgressBar) itemView.findViewById(R.id.circle_progress_bar);
        }
    }

    @NonNull
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.layout_viewpager_grid, parent, false);
        MyAdapter.ViewHolder vh = new MyAdapter.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.ViewHolder holder, int position) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();

        detailInfo = detailInfos.get(position);


        StorageReference pathReference = storageReference.child("album/users/" + detailInfo.getPostId() + "/photo"+detailInfo.getPostCnt());
        pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(mContext)
                        .load(uri)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                holder.mProgressBar.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .into(holder.imgView_item);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return detailInfos.size();
    }
}