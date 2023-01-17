package com.example.dodamdodam.activity.album;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import com.bumptech.glide.Glide;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.Task;
import com.example.dodamdodam.R;
import com.example.dodamdodam.view.AlbumInfo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import static com.example.dodamdodam.Util.INTENT_PATH;
import static com.example.dodamdodam.Util.showToast;
import java.util.Objects;

public class AlbumAdd extends AppCompatActivity {
    private static final String TAG = "AlbumAdd";
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_add);

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("도담도담");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        findViewById(R.id.post_saves).setOnClickListener(onClickListener);
        findViewById(R.id.image_btn).setOnClickListener(onClickListener);
        findViewById(R.id.video_btn).setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.post_saves:
                    profileUpdate();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0: {
                if (resultCode == Activity.RESULT_OK) {
                    String profilePath = data.getStringExtra(INTENT_PATH);
                    LinearLayout parent = findViewById(R.id.contents_layout); //콘텐츠 넣을 레이아웃

                    ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT); //사진이든, 동영상이든 그 크기에 맞게 조절될 수 있도록

                    ImageView imageView = new ImageView(AlbumAdd.this);
                    imageView.setLayoutParams(layoutParams);
                    parent.addView(imageView); //레이아웃에 이미지뷰 넣어주기
                    Glide.with(this).load(profilePath).override(1000).into(imageView);

                    EditText editText = new EditText(AlbumAdd.this);
                    editText.setLayoutParams(layoutParams);
                    editText.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_CLASS_TEXT);
                    parent.addView(editText);
                }
                break;
            }
        }
    }

    private  void profileUpdate(){
        final String title = ((EditText)findViewById(R.id.title_edit)).getText().toString();
        final String contents = ((EditText)findViewById(R.id.contets_edit)).getText().toString();

        if(title.length()>0&&contents.length()>0){
            user = FirebaseAuth.getInstance().getCurrentUser();
            AlbumInfo albumInfo = new AlbumInfo(title, contents, user.getUid());
            uploader(albumInfo);
        }else{
            startToast("제목이나 내용이 빠지지 않았는지 확인해주세요.");
        }
    }

    private  void uploader(AlbumInfo albumInfo){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("posts").add(albumInfo)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot written with ID: "+documentReference.getId());
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
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void startGallery(Class c, String media){
        Intent intent = new Intent(this, c);
        intent.putExtra("media", media);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}