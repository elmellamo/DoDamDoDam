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
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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

    public void deletePost(String postId){
        myRef.child("user_posts").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(postId).removeValue();
        myRef.child("posts").child(postId).removeValue();

        Toast.makeText(mContext, "해당 포스트가 삭제되었습니다.", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(mContext, AlbumMain.class);
        mContext.startActivity(intent);
    }



    public List<String> changeString(ArrayList<Uri> imgUri){
        pleaseUpload = new ArrayList<String>();

        for(int i=1; i<=imgUri.size(); i++){
            pleaseUpload.add(imgUri.get(i - 1).toString());
        }

        return pleaseUpload;
    }

    public void uploadNewPost(final String title, final String caption, String postId, ArrayList<Uri> imgUrl) {
        Log.e("로그", "포스트 업로드에 성공하였습니다.");

        String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        uploadkey=1;
        Bitmap bm = null;

        for(int i=1; i<=imgUrl.size(); i++){
            final StorageReference storageReference = mStorageReference
                    .child("album/users/" + postId+"/photo" + i);

            try {
                Log.e("로그", "알려줄게요>>> "+imgUrl.get(i - 1).toString());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    bm = ImageDecoder.decodeBitmap(ImageDecoder.createSource(mContext.getContentResolver(), imgUrl.get(i - 1)));
                } else {
                    bm = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), imgUrl.get(i - 1));
                }
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] bytes = baos.toByteArray(); //ImageManager.getBytesFromBitmap(bm, 100);
                UploadTask uploadTask = null;
                uploadTask = storageReference.putBytes(bytes);
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                //Uri firebaseUrl = uri;
                                Log.e("로그", "photo upload success");
                                Toast.makeText(mContext, "앨범이 성공적으로 등록되었습니다.", Toast.LENGTH_SHORT).show();

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
                //addAlbumToDatabase(title, null, pleaseUpload);
                Log.e("로그", "내용 없슈!!!");
            }else{
                //addAlbumToDatabase(title, caption, pleaseUpload);
                Log.e("로그", "진짜진짜 성공!!!");
            }


            Intent intent = new Intent(mContext, AlbumMain.class);
            mContext.startActivity(intent);
        }
    }

    public String addAlbumToDatabase(String title, String caption, List<String> imgurl){
        Log.e("로그", "addPhotoToDatabase: 데이터 베이스에 사진 추가");

        String newPostKey = myRef.child(mContext.getString(R.string.dbname_posts)).push().getKey();
        Post post = new Post();
        post.setTitle(title);
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

        return newPostKey;
    }

    public void modifyPost(String mod_title, String mod_contents, String postId){

        HashMap<String, Object> result = new HashMap<>();
        result.put("caption", mod_contents);
        result.put("post_id", postId);
        result.put("title", mod_title);
        result.put("user_id", FirebaseAuth.getInstance().getCurrentUser().getUid());


        Query query = myRef
                .child("posts")
                .child(postId);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Map<String, Object> objectMap=(HashMap<String,Object>)dataSnapshot.getValue();

                Log.e("로그", "되고 있나요?");
                result.put("image_path", objectMap.get("image_path"));
                result.put("date_created", objectMap.get("date_created"));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("로그", "안되고 있네요ㅎㅎ");
            }
        });


        myRef.child("posts").child(postId).updateChildren(result).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(mContext, "해당 포스트가 수정에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        myRef.child("user_posts").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(postId).updateChildren(result).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(mContext, "해당 포스트가 수정되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(mContext, "해당 포스트가 수정에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                });

        Intent intent = new Intent(mContext, AlbumMain.class);
        mContext.startActivity(intent);

    }

    private String getTimeStamp(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.KOREA);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        return simpleDateFormat.format(new Date());
    }
}