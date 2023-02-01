package com.example.dodamdodam.activity.album;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dodamdodam.R;
import com.example.dodamdodam.activity.Calendar.CalendarMain;
import com.example.dodamdodam.activity.Login.BasicActivity;
import com.example.dodamdodam.activity.Question.QuestionMain;
import com.example.dodamdodam.activity.Setting.SettingMain;
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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlbumMain extends BasicActivity {
    TextView myUid;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private Context mContext = AlbumMain.this;
    private String LOVERUID;
    private ArrayList<String> mFollowing;
    private ArrayList<Post> mPosts = new ArrayList<>();
    private ImageButton album_add, questionBtn,albumBtn,settingBtn, calendarBtn;
    private LinearLayout emptytxt;
    private GridView gridView;
    private RecyclerView.LayoutManager LayoutManager;
    private AlbumMainListAdapter adapter;

    public interface OnGridImageSelectedListener{
        void onGridImageSelected(Post post , int activityNumber);
    }
    OnGridImageSelectedListener mOnGridImageSelectedListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_main);
        album_add = findViewById(R.id.album_add);

        gridView = (GridView) findViewById(R.id.gridView);
        questionBtn = (ImageButton) findViewById(R.id.questionBtn);
        albumBtn = (ImageButton) findViewById(R.id.albumBtn);
        settingBtn = (ImageButton) findViewById(R.id.settingBtn);
        calendarBtn = (ImageButton) findViewById(R.id.calendarBtn2);
        emptytxt = findViewById(R.id.emptytxt);

        getFollowing();

        album_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myStartActivity(AlbumAdd.class);
            }
        });
        albumBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myStartActivity(AlbumMain.class);
            }
        });
        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myStartActivity(SettingMain.class);
            }
        });
        calendarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myStartActivity(CalendarMain.class);
            }
        });
        questionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myStartActivity(QuestionMain.class);
            }
        });

    }

    private void getFollowing() {
        Log.e("로그", "나랑 짝꿍이랑 연결되는 중");

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        mFollowing = new ArrayList<>();
        mFollowing.add(user.getUid());

        if(user!=null){
        DocumentReference docRef = db.collection("users").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        LOVERUID = document.getData().get("lover").toString();
                        Log.e("로그", "러버 유아이디>> "+LOVERUID);
                        mFollowing.add(LOVERUID);
                        getPosts();
                    } else {
                        Log.e("로그", "지금 사용자의 lover가 없어요");
                    }
                } else {
                    Log.e("로그", "데베에서 유저 정보 가져 오는 거 실패 get failed with ", task.getException());
                }
            }
        });

        //getPosts 지금 여기서 가져와야함
    }}

    private void getPosts(){
        Log.e("로그", "getPhotos: getting photos");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        //Log.e("로그", "팔로잉 개수>>>> "+mFollowing.size());
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
                        ArrayList<String> tmpPath = new ArrayList<>();
                        Map<String, Object> objectMap = (HashMap<String, Object>) singleSnapshot.getValue();

                        post.setCaption(objectMap.get("caption").toString());
                        post.setPost_id(objectMap.get("post_id").toString());
                        post.setUser_id(objectMap.get("user_id").toString());
                        post.setDate_created(objectMap.get("date_created").toString());
                        post.setImage_path((List<String>) objectMap.get("image_path"));

                        final int flag = Intent.FLAG_GRANT_READ_URI_PERMISSION;

                        for(int tmp=0; tmp<post.getImage_path().size(); tmp++){
                            getContentResolver().takePersistableUriPermission(Uri.parse(post.getImage_path().get(tmp)), flag);
                        }
                        mPosts.add(post);
                    }

                    if(count >= mFollowing.size() -1){
                        //display our photos
                        if(mPosts != null){
                            Log.e("로그", "여기까지는???");
                            mPosts.sort(new Comparator<Post>() {
                                @Override
                                public int compare(Post o1, Post o2) {
                                    return o2.getDate_created().compareTo(o1.getDate_created());
                                }
                            });
                            setupGridView();
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
    }

    private void setupGridView(){
        int gridWidth = getResources().getDisplayMetrics().widthPixels;
        int imageWidth = gridWidth/3;
        gridView.setColumnWidth(imageWidth);

        ArrayList<String> imgUrls = new ArrayList<>();
        for(int i=0 ; i<mPosts.size();i++){
            imgUrls.add(mPosts.get(i).getImage_path().get(0));
            Log.e("로그", "포스트 링크 >> "+ imgUrls.get(i));
        }

        adapter = new AlbumMainListAdapter(AlbumMain.this,R.layout.layout_grid_imageview,"",imgUrls);

        if(adapter.isEmpty()){
            emptytxt.setVisibility(adapter.getCount()==0?  View.VISIBLE : View.GONE);
        }
        gridView.setAdapter(adapter);

    }

    private void startToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
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