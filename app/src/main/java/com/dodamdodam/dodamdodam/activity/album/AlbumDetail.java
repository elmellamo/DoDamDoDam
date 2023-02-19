package com.dodamdodam.dodamdodam.activity.album;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.dodamdodam.dodamdodam.R;
import com.dodamdodam.dodamdodam.Utils.FirebaseMethods;
import com.dodamdodam.dodamdodam.activity.Login.BasicActivity;
import com.dodamdodam.dodamdodam.adapter.MyAdapter;
import com.dodamdodam.dodamdodam.models.DetailInfo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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
    private TextView image_title, image_caption, image_time_posted, username;
    private ConstraintLayout delete_layout, modify_layout;
    private String postId, myId;
    private FirebaseUser user;
    private MyAdapter myAdapter;
    private DatabaseReference reference, dbSetting;
    private FirebaseMethods mFirebaseMethods;
    private int postCount;
    private ArrayList<DetailInfo> detailInfos;
    private String tmpTime;
    private ImageView ic_morebutton;
    private RelativeLayout buttonsBackgroundLayout;
    private StorageReference fileRef;
    private ArrayList<Integer> tmpCnt;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_detail);

        mPager = findViewById(R.id.pager_images);
        mindicator = findViewById(R.id.indicator);
        image_caption = findViewById(R.id.image_caption);
        image_title = findViewById(R.id.image_title);
        username = findViewById(R.id.username);
        image_time_posted = findViewById(R.id.image_time_posted);
        ic_morebutton = findViewById(R.id.ivEllipses);
        buttonsBackgroundLayout = findViewById(R.id.buttonsBackgroundLayout);
        user = FirebaseAuth.getInstance().getCurrentUser();
        delete_layout = findViewById(R.id.delete_layout);
        mFirebaseMethods = new FirebaseMethods(AlbumDetail.this);
        modify_layout = findViewById(R.id.modify_layout);

        setupImage();
        setImage();
        ic_morebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonsBackgroundLayout.setVisibility(View.VISIBLE);
            }
        });

        buttonsBackgroundLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (buttonsBackgroundLayout.getVisibility() == View.VISIBLE) {
                    buttonsBackgroundLayout.setVisibility(View.GONE);
                }
            }
        });

        modify_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AlbumDetail.this, AlbumModify.class);
                intent.putExtra("postId", postId);
                //intent.putExtra("date_created", )
                startActivity(intent);
            }
        });
        delete_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delPost();
            }
        });



    }

    private void setupImage(){
        Intent receivedIntent = getIntent();
        postId = (String) receivedIntent.getExtras().get("postId");

        Log.e("로그", "AlbumDetail 포스트 아이디>> "+postId);
    }

    private void setImage(){
        reference = FirebaseDatabase.getInstance().getReference();
        tmpCnt = new ArrayList<Integer>();

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
                    myId = objectMap.get("user_id").toString();
                    tmpTime = objectMap.get("date_created").toString();
                    String timestampDiff = getTimestampDifference(tmpTime);
                    if(!timestampDiff.equals("0")){
                        image_time_posted.setText(timestampDiff + " DAYS AGO");
                    }else{
                        image_time_posted.setText("TODAY");
                    }

                    postCount = ((List<String>) objectMap.get("image_path")).size();
                    tmpCnt.add(postCount);

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

                    setupName();
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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.KOREA);
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));//google 'android list of timezones'
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


    private void setupName(){
        dbSetting = FirebaseDatabase.getInstance().getReference("Setting");
        Log.e("로그", "내 uid>>>"+user.getUid());
        Log.e("로그", "작성자 uid>>>"+myId);



        if(user.getUid().equals(myId)){
            dbSetting.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.child("mynickname").child(user.getUid()).getValue()!=null){
                        String mynick = snapshot.child("mynickname").child(user.getUid()).getValue().toString();
                        username.setText(mynick);
                    }
                    else{
                        username.setText("나");
                        Toast.makeText(AlbumDetail.this, "설정에 가서 닉네임을 등록해주세요", Toast.LENGTH_SHORT).show();
                    }

                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }else{
            ic_morebutton.setVisibility(View.GONE);

            dbSetting.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.child("lovernickname").child(user.getUid()).getValue()!=null){
                        String lovernick = snapshot.child("lovernickname").child(user.getUid()).getValue().toString();
                        username.setText(lovernick);
                    }
                    else{
                        username.setText("짝꿍");
                        Toast.makeText(AlbumDetail.this, "설정에 가서 닉네임을 등록해주세요", Toast.LENGTH_SHORT).show();

                    }
                    //설정에 닉네임 등록했으면 출력, 아니면 상대방으로 출력
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void delPost() {
        Log.e("로그", "개수>>> "+ tmpCnt.get(0));
        for (int i = 1; i <= tmpCnt.get(0); i++) {
            fileRef = FirebaseStorage.getInstance().getReference().child("album/users/" + postId + "/photo" + i);
            fileRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            });
        }

        mFirebaseMethods.deletePost(postId);
}


}