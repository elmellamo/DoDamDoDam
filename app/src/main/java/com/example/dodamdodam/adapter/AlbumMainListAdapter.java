package com.example.dodamdodam.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
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
import com.example.dodamdodam.R;
import com.example.dodamdodam.Utils.SquareImageView;

import java.util.ArrayList;

public class AlbumMainListAdapter extends ArrayAdapter {

        private Context mContext;
        private LayoutInflater mInflater;
        private int layoutResource;
        private String mAppend;
        private ArrayList<String> imgURLs;

        public AlbumMainListAdapter(Context context, int layoutResource, String mAppend, ArrayList<String> imgURLs) {
            super(context,layoutResource, imgURLs);
            this.mContext = context;
            this.mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.layoutResource = layoutResource;
            this.mAppend = mAppend;
            this.imgURLs = imgURLs;
        }

        private static class ViewHolder{
            SquareImageView profileImage;
            ProgressBar mProgressBar;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            final ViewHolder viewHolder;
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

            String imgURL = (String)getItem(position);

            boolean isPost = imgURL !=null;
            if(isPost){
                Glide.with(viewHolder.profileImage.getContext())
                        .load(imgURL)
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

//            imageLoader.displayImage(mAppend + imgURL, viewHolder.profileImage, new ImageLoadingListener() {
//                @Override
//                public void onLoadingStarted(String imageUri, View view) {
//                    if(viewHolder.mProgressBar != null){
//                        viewHolder.mProgressBar.setVisibility(View.VISIBLE);
//                    }
//                }
//
//                @Override
//                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//                    if(viewHolder.mProgressBar != null){
//                        viewHolder.mProgressBar.setVisibility(View.GONE);
//                    }
//                }
//
//                @Override
//                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                    if(viewHolder.mProgressBar != null){
//                        viewHolder.mProgressBar.setVisibility(View.GONE);
//                    }
//                }
//
//                @Override
//                public void onLoadingCancelled(String imageUri, View view) {
//                    if(viewHolder.mProgressBar != null){
//                        viewHolder.mProgressBar.setVisibility(View.GONE);
//                    }
//                }
//            });

            return convertView;
        }

        public int getCount(){
            return super.getCount();
        }
    }

