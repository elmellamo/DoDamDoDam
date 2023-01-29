package com.example.dodamdodam.activity.album;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dodamdodam.R;
import com.example.dodamdodam.Utils.UniversalImageLoader;
import com.example.dodamdodam.activity.Login.BasicActivity;
import com.example.dodamdodam.adapter.AlbumMainListAdapter;
import com.example.dodamdodam.models.Post;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlbumMain extends BasicActivity {
    TextView myUid;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private Context mContext = AlbumMain.this;
    private String LOVERUID;
    private ArrayList<String> mFollowing = new ArrayList<>();
    private ArrayList<Post> mPosts = new ArrayList<>();
    private ImageButton album_add;
    private RecyclerView gridView;
    private RecyclerView.LayoutManager LayoutManager;

    public interface OnGridImageSelectedListener{
        void onGridImageSelected(Post post , int activityNumber);
    }
    OnGridImageSelectedListener mOnGridImageSelectedListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_main);
        album_add = findViewById(R.id.album_add);

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("도담도담");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        gridView = (RecyclerView)findViewById(R.id.gridView);
        gridView.setHasFixedSize(true);
        LayoutManager = new GridLayoutManager(AlbumMain.this, 3);
        gridView.setLayoutManager(LayoutManager);

        getFollowing();

        album_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myStartActivity(AlbumAdd.class);
            }
        });

    }

    public void onAttach(Context context) {
        try{
            mOnGridImageSelectedListener = (OnGridImageSelectedListener)getApplicationContext();
        }catch (ClassCastException e){
            Log.e("로그", "onAttach: ClassCastException: "+e.getMessage());
        }
    }
    private void getFollowing() {
        Log.e("로그", "나랑 짝꿍이랑 연결되는 중");

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference docRef = db.collection("users").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        LOVERUID = document.getData().get("lover").toString();
                    } else {
                        Log.e("로그", "지금 사용자의 lover가 없어요");
                    }
                } else {
                    Log.e("로그", "데베에서 유저 정보 가져 오는 거 실패 get failed with ", task.getException());
                }
            }
        });

        mFollowing.add(LOVERUID);
        mFollowing.add(user.getUid());


        getPosts();
        //getPosts 지금 여기서 가져와야함
    }

    private void getPosts(){
        Log.e("로그", "getPhotos: getting photos");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        for(int i = 0; i < mFollowing.size(); i++){
            final int count = i;

            Query query = reference
                    .child("user_posts")
                    .child(mFollowing.get(i))
                    .orderByChild("user_id")
                    .equalTo(mFollowing.get(i));

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                        Post post = new Post();
                        Map<String, Object> objectMap = (HashMap<String, Object>) singleSnapshot.getValue();

                        post.setCaption(objectMap.get("caption").toString());
                        post.setPost_id(objectMap.get("post_id").toString());
                        post.setUser_id(objectMap.get("user_id").toString());
                        post.setDate_created(objectMap.get("date_created").toString());


                        post.setImage_path((List<String>) objectMap.get("image_path"));

                        mPosts.add(post);
                    }

                    if(count >= mFollowing.size() -1){
                        //display our photos
                        Collections.sort(mPosts, new Comparator<Post>() {
                            @Override
                            public int compare(Post o1, Post o2) {
                                return o2.getDate_created().compareTo(o1.getDate_created());
                            }
                        });
                        setupGridView();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
    }

    private void setupGridView(){

        ArrayList<String> imgUrls = new ArrayList<>();
        for(int i=0 ; i<mPosts.size();i++){
            imgUrls.add(mPosts.get(i).getImage_path().get(0));
        }

        AlbumMainListAdapter adapter = new AlbumMainListAdapter(AlbumMain.this,R.layout.layout_grid_imageview,"",imgUrls);
        //gridView.setAdapter(adapter);
        //그냥...다시 어댑터 만들자..
        // 여기는 해당 아이템 눌렸을 때 다시 상세페이지 뜨도록 해야 함!mOnGridImageSelectedListener.onGridImageSelected(mPosts.get(position),4);
    }

    private void startToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
    private void initImageLoader(){
        UniversalImageLoader universalImageLoader = new UniversalImageLoader(mContext);
        ImageLoader.getInstance().init(universalImageLoader.getConfig());
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    private void myStartActivity(Class c){
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }

}