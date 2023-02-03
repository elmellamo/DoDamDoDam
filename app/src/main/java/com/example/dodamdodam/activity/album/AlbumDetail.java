package com.example.dodamdodam.activity.album;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.viewpager2.widget.ViewPager2;

import com.example.dodamdodam.R;
import com.example.dodamdodam.Utils.FirebaseMethods;
import com.example.dodamdodam.activity.Login.BasicActivity;
import com.example.dodamdodam.adapter.MyAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import me.relex.circleindicator.CircleIndicator3;

public class AlbumDetail  extends BasicActivity {
    private ViewPager2 mPager;
    private CircleIndicator3 mindicator;
    private String postId;
    private MyAdapter myAdapter;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseMethods mFirebaseMethods;
    private int postCount;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_detail);

        mPager = findViewById(R.id.pager_images);
        mindicator = findViewById(R.id.indicator);
        setImage();
        myAdapter = new MyAdapter(this, postId, postCount);
        mPager.setAdapter(myAdapter);
        mindicator.setViewPager(mPager);
        mindicator.createIndicators(postCount, 0);
        mPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        mPager.setCurrentItem(0);
        mPager.setOffscreenPageLimit(10);
    }

    private void setImage(){
        Intent receivedIntent = getIntent();
        postId = (String) receivedIntent.getExtras().get("postId");

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase= FirebaseDatabase.getInstance();
        myRef=mFirebaseDatabase.getReference();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postCount = mFirebaseMethods.getPictureCount(dataSnapshot, postId);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


}
