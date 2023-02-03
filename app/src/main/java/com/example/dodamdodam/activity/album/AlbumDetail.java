package com.example.dodamdodam.activity.album;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager2.widget.ViewPager2;

import com.example.dodamdodam.R;
import com.example.dodamdodam.activity.Login.BasicActivity;
import com.example.dodamdodam.adapter.MyAdapter;
import com.example.dodamdodam.models.DetailInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import me.relex.circleindicator.CircleIndicator3;

public class AlbumDetail  extends BasicActivity {
    private ViewPager2 mPager;
    private CircleIndicator3 mindicator;
    private TextView image_title, image_caption, image_time_posted;
    private String postId;
    private MyAdapter myAdapter;
    private DatabaseReference reference;
    private int postCount;
    private ArrayList<DetailInfo> detailInfos;
    private String tmpTime;
    private String timestampDiff;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_detail);

        mPager = findViewById(R.id.pager_images);
        mindicator = findViewById(R.id.indicator);
        image_caption = findViewById(R.id.image_caption);
        image_title = findViewById(R.id.image_title);
        image_time_posted = findViewById(R.id.image_time_posted);
        setupImage();
        setImage();

    }

    private void setupImage(){
        Intent receivedIntent = getIntent();
        postId = (String) receivedIntent.getExtras().get("postId");

        Log.e("로그", "AlbumDetail 포스트 아이디>> "+postId);
    }

    private void setImage(){
        reference = FirebaseDatabase.getInstance().getReference();

        Query query = reference
                .child("posts")
                .child(postId);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    Map<String, Object> objectMap=(HashMap<String,Object>)dataSnapshot.getValue();

                    Log.e("로그", "되고 있나요?");
                    image_caption.setText(objectMap.get("caption").toString());
                    image_title.setText(objectMap.get("title").toString());

                    tmpTime = objectMap.get("date_created").toString();
                    String timestampDiff = getTimestampDifference(tmpTime);
                    if(!timestampDiff.equals("0")){
                        image_time_posted.setText(timestampDiff + " DAYS AGO");
                    }else{
                        image_time_posted.setText("TODAY");
                    }

                    postCount = ((List<String>) objectMap.get("image_path")).size();

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
                }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("로그", "안되고 있네요ㅎㅎ");
            }
        });
    }

    private String getTimestampDifference(String date_created){
        Log.e("로그", "getTimestampDifference: getting timestamp difference.");

        String difference = "";
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.CANADA);
        sdf.setTimeZone(TimeZone.getTimeZone("Canada/Pacific"));//google 'android list of timezones'
        Date today = c.getTime();
        sdf.format(today);
        Date timestamp;
        final String photoTimestamp = date_created;
        try{
            timestamp = sdf.parse(photoTimestamp);
            difference = String.valueOf(Math.round(((today.getTime() - timestamp.getTime()) / 1000 / 60 / 60 / 24 )));
        }catch (ParseException e){
            Log.e("로그", "getTimestampDifference: ParseException: " + e.getMessage() );
            difference = "0";
        }
        return difference;
    }


}