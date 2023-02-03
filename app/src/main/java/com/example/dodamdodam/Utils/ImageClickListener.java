package com.example.dodamdodam.Utils;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.example.dodamdodam.activity.album.AlbumDetail;

public class ImageClickListener implements View.OnClickListener {
    private Context context;
    private String postId;

    public ImageClickListener(Context context, String postId){
        this.context = context;
        this.postId = postId;
    }


    @Override
    public void onClick(View view) {
        Intent intent = new Intent(context, AlbumDetail.class);
        intent.putExtra("postId", postId);
        context.startActivity(intent);
    }
}