package com.example.dodamdodam.Utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.dodamdodam.R;
import com.example.dodamdodam.activity.album.AlbumMain;
import com.example.dodamdodam.models.Post;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

public class FirebaseMethods {
    private static final String TAG = "FirebaseMethods";
    private Context mContext;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private String userID;
    private StorageReference mStorageReference;
    public int uploadkey;
    private List<String> pleaseUpload;

    public FirebaseMethods(Context context) {
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        mStorageReference = FirebaseStorage.getInstance().getReference();
        mContext = context;

        if (mAuth.getCurrentUser() != null) {
            userID = mAuth.getCurrentUser().getUid();
        }
    }

    public int getPostCount(DataSnapshot dataSnapshot) {
        int count = 0;
        for (DataSnapshot ds : dataSnapshot.child(mContext.getString(R.string.dbname_user_posts))
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).getChildren()) {

            count++;
        }
        return count;
    }

    public void uploadNewPost(final String caption, int count, ArrayList<Uri> imgUrl) {
        Log.e("로그", "uploadNewPhoto: uploading NEW photo.");

        String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        uploadkey=1;
        Bitmap bm = null;
        pleaseUpload = new ArrayList<String>();

        for(int i=1; i<=imgUrl.size(); i++){
            final StorageReference storageReference = mStorageReference
                    .child("album/users/" + user_id + "/photo" + (count + i));

            pleaseUpload.add(imgUrl.get(i - 1).toString());

            try {
                Log.e("로그", "알려줄게요>>> "+imgUrl.get(i - 1).toString());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    bm = ImageDecoder.decodeBitmap(ImageDecoder.createSource(mContext.getContentResolver(), imgUrl.get(i - 1)));
                } else {
                    bm = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), imgUrl.get(i - 1));
                }
                byte[] bytes = ImageManager.getBytesFromBitmap(bm, 100);
                UploadTask uploadTask = null;
                uploadTask = storageReference.putBytes(bytes);
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Uri firebaseUrl = uri;
                                Log.e("로그", "photo upload success");
                                Toast.makeText(mContext, "photo upload success", Toast.LENGTH_SHORT).show();

                                //새로운 앨범 게시물을 'posts'랑 'user_posts' 카테고리에 추가
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("로그", "onFailure: Photo upload failed.");
                        Toast.makeText(mContext, "Photo upload failed ", Toast.LENGTH_SHORT).show();
                        uploadkey = 0;
                    }
                });
            }catch (IOException e){
                e.printStackTrace();
            }
        }

        if(uploadkey==1){
            if(caption==null){
                addAlbumToDatabase(null, pleaseUpload);
                Log.e("로그", "내용 없슈!!!");
            }else{
                addAlbumToDatabase(caption, pleaseUpload);
                Log.e("로그", "진짜진짜 성공!!!");
            }


            Intent intent = new Intent(mContext, AlbumMain.class);
            mContext.startActivity(intent);
        }
    }

    private void addAlbumToDatabase(String caption, List<String> imgurl){
        Log.e("로그", "addPhotoToDatabase: 데이터 베이스에 사진 추가");

        String newPostKey = myRef.child(mContext.getString(R.string.dbname_posts)).push().getKey();
        Post post = new Post();
        post.setCaption(caption);
        post.setDate_created(getTimeStamp());
        post.setImage_path(imgurl);
        post.setUser_id(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
        post.setPost_id(newPostKey);

        // 데이터베이스에 넣기
        assert newPostKey != null;
        myRef.child(mContext.getString(R.string.dbname_user_posts)).
                child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(newPostKey).setValue(post);
        myRef.child(mContext.getString(R.string.dbname_posts)).child(newPostKey).setValue(post);
    }

    private String getTimeStamp(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.KOREA);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        return simpleDateFormat.format(new Date());
    }
}