package com.example.dodamdodam.activity.album;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.dodamdodam.R;
import com.example.dodamdodam.view.AlbumInfo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class AlbumAdd extends AppCompatActivity {
    private static final String TAG = "AlbumAdd";
    private FirebaseUser user;
    private ArrayList<String> pathList = new ArrayList<>();
    private LinearLayout parent;
    private int pathCount,successCount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_add);

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("게시글 작성");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        parent = findViewById(R.id.contents_layout);

        findViewById(R.id.post_saves).setOnClickListener(onClickListener);
        findViewById(R.id.image_btn).setOnClickListener(onClickListener);
        findViewById(R.id.video_btn).setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.post_saves:
                    storageUpload();
                    break;
                case R.id.image_btn:
                    startGallery(GalleryActivity.class, "image");
                    break;
                case R.id.video_btn:
                    startGallery(GalleryActivity.class, "video");
                    break;
            }
        }
    };

    private void startGallery(Class c, String media){
        Intent intent = new Intent(this, c);
        intent.putExtra("media", media);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        resultLauncher.launch(intent);
    }

    ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>()
            {
                @Override
                public void onActivityResult(ActivityResult result)
                {
                    if (result.getResultCode() == Activity.RESULT_OK)
                    {
                        Intent data = result.getData();
                        assert data != null;
                        String profilePath = data.getStringExtra("profilePath");
                        pathList.add(profilePath);

                        //콘텐츠 넣을 레이아웃
                        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT); //사진이든, 동영상이든 그 크기에 맞게 조절될 수 있도록

                        ImageView imageView = new ImageView(AlbumAdd.this);
                        imageView.setLayoutParams(layoutParams);
                        Glide.with(AlbumAdd.this).load(profilePath).override(1000).into(imageView);
                        parent.addView(imageView); //레이아웃에 이미지뷰 넣어주기

                        EditText editText = new EditText(AlbumAdd.this);
                        editText.setLayoutParams(layoutParams);
                        editText.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_CLASS_TEXT);
                        parent.addView(editText);
                    }
                }
            });



    private  void storageUpload(){
        final String title = ((EditText)findViewById(R.id.title_edit)).getText().toString();

        if(title.length()>0){
            ArrayList<String> contentsList = new ArrayList<>();
            user = FirebaseAuth.getInstance().getCurrentUser();
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();

            for(int i=0 ; i<parent.getChildCount(); i++){
                View view = parent.getChildAt(i);
                if(view instanceof EditText){
                    String text = ((EditText)view).getText().toString();
                    if(text.length()>0){
                        contentsList.add(text);
                    }
                }else{
                    contentsList.add(pathList.get(pathCount));
                    final StorageReference mountainImagesRef = storageRef.child("users/" + user.getUid() + "/" + pathCount + ".jpg");
                    try {
                        InputStream stream = new FileInputStream(new File(pathList.get(pathCount)));
                        StorageMetadata metadata = new StorageMetadata.Builder().setCustomMetadata("index", "" + (contentsList.size() - 1)).build(); //인덱스정보 넘겨주기
                        UploadTask uploadTask = mountainImagesRef.putStream(stream, metadata);
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                final int index = Integer.parseInt(taskSnapshot.getMetadata().getCustomMetadata("index"));
                                mountainImagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        //Log.e("로그 : ","uri "+uri);
                                        contentsList.set(index, uri.toString());
                                        successCount++;
                                        if (successCount == pathList.size()) {
                                            AlbumInfo albumInfo = new AlbumInfo(title, contentsList, user.getUid(), new Date());
                                            storeUpload(albumInfo);
                                            /*for(int a=0; a<contentsList.size(); a++){
                                                Log.e("로그 : ", "콘텐츠: "+contentsList);
                                            }*/

                                        }
                                    }
                                });
                            }
                        });
                    } catch (FileNotFoundException e) {
                        Log.e("로그", "에러: " + e.toString());
                    }
                    pathCount++;
                }
            }
        }else{
            startToast("제목을 작성해주세요.");
        }
    }

    private  void storeUpload(AlbumInfo albumInfo){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("posts").add(albumInfo)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot written with ID: "+documentReference.getId());
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void startToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    private void myStartActivity(Class c){
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }
}