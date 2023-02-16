package com.dodamdodam.dodamdodam.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.dodamdodam.dodamdodam.R;
import com.dodamdodam.dodamdodam.Utils.ImageClickListener;
import com.dodamdodam.dodamdodam.Utils.SquareImageView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class AlbumMainListAdapter extends ArrayAdapter {
    private int postCount;
    private Context mContext;
    private LayoutInflater mInflater;
    private int layoutResource;
    private String mAppend;
    private ArrayList<String> postIDs;
    public AlbumMainListAdapter(Context context, int layoutResource, String mAppend, ArrayList<String> postIDs) {
        super(context,layoutResource, postIDs);
        this.mContext = context;
        this.mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.layoutResource = layoutResource;
        this.mAppend = mAppend;
        this.postIDs = postIDs;
    }

    private static class ViewHolder{
        SquareImageView profileImage;
        ProgressBar mProgressBar;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final ViewHolder viewHolder;
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        // 뷰홀더 패턴 //
        if(convertView == null){
            convertView =mInflater.inflate(layoutResource,parent,false);
            viewHolder= new ViewHolder();
            viewHolder.mProgressBar = (ProgressBar)convertView.findViewById(R.id.circle_progress_bar);
            viewHolder.profileImage = (SquareImageView)convertView.findViewById(R.id.gridImageView);

            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        String postId = (String)getItem(position);

        boolean isPost = postId !=null;
        if(isPost) {
            StorageReference pathReference = storageReference.child("album/users/" + postId + "/photo1");
            pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(viewHolder.profileImage.getContext())
                            .load(uri)
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    viewHolder.mProgressBar.setVisibility(View.GONE);
                                    return false;
                                }
                            })
                            .into(viewHolder.profileImage);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });

            ImageClickListener imageClickListener = new ImageClickListener(mContext, postId);
            viewHolder.profileImage.setOnClickListener(imageClickListener);


        }

        return convertView;
    }

    public int getCount(){
        return super.getCount();
    }
}