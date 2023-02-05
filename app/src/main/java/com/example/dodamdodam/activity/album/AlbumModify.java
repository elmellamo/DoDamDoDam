package com.example.dodamdodam.activity.album;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dodamdodam.R;
import com.example.dodamdodam.Utils.FirebaseMethods;
import com.example.dodamdodam.adapter.ModifyAdapter;
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

public class AlbumModify extends AppCompatActivity {
    private EditText mod_title, mod_contets_edit;
    private RecyclerView recyclerview;
    private FirebaseAuth mAuth;
    private FirebaseMethods mFirebaseMethods;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private int postCount;
    private ArrayList<Integer> tmpCnt;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<DetailInfo> detailInfos;
    private ModifyAdapter modAdapter;
    private Button mod_save;
    private String createdAt;
    private List<String> mod_path;

    private String postId;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_modify);

        mFirebaseMethods = new FirebaseMethods(AlbumModify.this);

        mod_title = findViewById(R.id.mod_title);
        mod_contets_edit = findViewById(R.id.mod_contets_edit);
        recyclerview = findViewById(R.id.mod_gallery_layout);
        mod_save = findViewById(R.id.mod_save);

        setupFirebaseAuth();
        setupId();
        bringPast();

        mod_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modstorageUpload();
            }
        });
    }

    private void modstorageUpload(){
        final String mod_title = ((EditText)findViewById(R.id.mod_title)).getText().toString();
        final String mod_contents = ((EditText)findViewById(R.id.mod_contets_edit)).getText().toString();

        mFirebaseMethods.modifyPost(mod_title, mod_contents, postId);
    }


    private void setupFirebaseAuth(){
        Log.e("로그", "setupFirebaseAuth: setting up firebase auth");

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase= FirebaseDatabase.getInstance();
        myRef=mFirebaseDatabase.getReference();
        //Log.e("로그", "onDataChange: image count : "+postCount);


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //postCount = mFirebaseMethods.getPostCount(dataSnapshot);
                //Log.e("로그", "onDataChange: post count : "+postCount);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setupId(){
        Intent receivedIntent = getIntent();
        postId = (String) receivedIntent.getExtras().get("postId");

        Log.e("로그", "Albummodify 포스트 아이디>> "+postId);
    }

    private void bringPast(){
        Query query = myRef
                .child("posts")
                .child(postId);
        tmpCnt = new ArrayList<Integer>();
        mod_path = new ArrayList<String>();

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Map<String, Object> objectMap=(HashMap<String,Object>)dataSnapshot.getValue();

                Log.e("로그", "되고 있나요?");
                mod_contets_edit.setText(objectMap.get("caption").toString());
                mod_title.setText(objectMap.get("title").toString());
                createdAt = objectMap.get("date_created").toString();
                mod_path = (List<String>) objectMap.get("image_path");
                Log.e("로그", "title>>"+objectMap.get("title").toString());

                postCount = ((List<String>) objectMap.get("image_path")).size();
                tmpCnt.add(postCount);

                detailInfos = new ArrayList<>();
                for(int i=1; i<=postCount; i++){
                    DetailInfo detailInfo = new DetailInfo(postId, i);
                    detailInfos.add(detailInfo);
                }


                modAdapter = new ModifyAdapter(AlbumModify.this, detailInfos);
                recyclerview.setAdapter(modAdapter);
                mLayoutManager = new LinearLayoutManager(AlbumModify.this, RecyclerView.HORIZONTAL, false);
                recyclerview.setLayoutManager(mLayoutManager);
                recyclerview.setHasFixedSize(true);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("로그", "안되고 있네요ㅎㅎ");
            }
        }
        );
    }
}
