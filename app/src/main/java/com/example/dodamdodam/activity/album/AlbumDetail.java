package com.example.dodamdodam.activity.album;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager2.widget.ViewPager2;

import com.example.dodamdodam.R;
import com.example.dodamdodam.Utils.FirebaseMethods;
import com.example.dodamdodam.activity.Login.BasicActivity;
import com.example.dodamdodam.adapter.MyAdapter;
import com.example.dodamdodam.models.DetailInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.relex.circleindicator.CircleIndicator3;

public class AlbumDetail  extends BasicActivity {
    private ViewPager2 mPager;
    private CircleIndicator3 mindicator;
    private TextView image_title, image_caption, image_time_posted;
    private String postId;
    private MyAdapter myAdapter;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseMethods mFirebaseMethods;
    private int postCount;
    private ArrayList<DetailInfo> detailInfos;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_detail);

        mPager = findViewById(R.id.pager_images);
        mindicator = findViewById(R.id.indicator);
        image_caption = findViewById(R.id.image_caption);
        image_title = findViewById(R.id.image_title);
        image_time_posted = findViewById(R.id.image_time_posted);
        setImage();

    }

    private void setImage(){
        Intent receivedIntent = getIntent();
        postId = (String) receivedIntent.getExtras().get("postId");

        Log.e("로그", postId);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase= FirebaseDatabase.getInstance();
        myRef=mFirebaseDatabase.getReference();

        Query query = myRef
                .child("posts")
                .child(postId)
                .orderByChild("post_id")
                .equalTo(postId);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    Map<String, Object> objectMap = (HashMap<String, Object>) singleSnapshot.getValue();

                    image_caption.setText(objectMap.get("caption").toString());
                    image_title.setText(objectMap.get("title").toString());
                    image_time_posted.setText(objectMap.get("date_created").toString());

                    postCount = ((List<String>) objectMap.get("image_path")).size();

                    Log.e("로그", objectMap.get("caption").toString());
                    detailInfos = new ArrayList<>();
                    for(int i=1; i<=postCount; i++){
                        DetailInfo detailInfo = new DetailInfo(postId, i);
                        detailInfos.add(detailInfo);
                    }


                    myAdapter = new MyAdapter(AlbumDetail.this, detailInfos);
                    mPager.setAdapter(myAdapter);
                    mindicator.setViewPager(mPager);
                    mindicator.createIndicators(postCount, 0);
                    mPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
                    mPager.setCurrentItem(0);
                    mPager.setOffscreenPageLimit(10);
                }}

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}
