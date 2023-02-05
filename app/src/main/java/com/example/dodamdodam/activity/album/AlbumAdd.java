package com.example.dodamdodam.activity.album;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.MimeTypeFilter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dodamdodam.R;
import com.example.dodamdodam.Utils.FirebaseMethods;
import com.example.dodamdodam.adapter.CustomAdapter;
import com.example.dodamdodam.models.MediaType;
import com.example.dodamdodam.models.RecyclerViewItem;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class AlbumAdd extends AppCompatActivity {
    private static final String TAG = "AlbumAdd";
    private RecyclerView recyclerview;
    private RecyclerView.LayoutManager mLayoutManager;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();;
    private ArrayList<Uri> pathList = new ArrayList<>();
    private ArrayList<Uri> changePath   = new ArrayList<>();
    private List<String> stringUri;
    private String postId;
    private RecyclerViewItem rItem = new RecyclerViewItem();
    private CustomAdapter mAdapter;
    private int postCount =0;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseMethods mFirebaseMethods;
    private Button post_saves;
    private ImageButton image_btn;
    private RelativeLayout deleteBackgroundLayout;
    private ConstraintLayout delete_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_add);

        mFirebaseMethods = new FirebaseMethods(AlbumAdd.this);

        recyclerview = findViewById(R.id.addgallery_layout);
        post_saves = findViewById(R.id.post_saves);
        image_btn = findViewById(R.id.image_btn);
        deleteBackgroundLayout = findViewById(R.id.deleteBackgroundLayout);
        delete_layout = findViewById(R.id.delete_layout);

        setupFirebaseAuth();

        post_saves.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                storageUpload();
            }
        });
        image_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pathList.clear();
                changePath.clear();
                loadImage();
            }
        });


    }


    private final ActivityResultLauncher<PickVisualMediaRequest> startForMultipleModeResult =
            registerForActivityResult(new ActivityResultContracts.PickMultipleVisualMedia(10), uris -> {
                if (!uris.isEmpty()) {
                    Log.e("로그", "Number of items selected: " + uris.size());
                    final int flag = Intent.FLAG_GRANT_READ_URI_PERMISSION;

                    for (int i = 0; i < uris.size(); i++) {
                        final Uri currentUri = uris.get(i);
                        Log.e("로그", "onActivityResult: currentUri" + currentUri.toString());

                        handlePickerResponse(currentUri);
                        //getContentResolver().takePersistableUriPermission(currentUri, flag);
                    }

                    for(int i=0; i<uris.size(); i++){
                        changePath.add(pathList.get(i));
                    }
                    rItem.setPublisher(user.getUid());
                    rItem.setMcreatedAt(new Date());
                    rItem.setGalleryuri(changePath);

                    mAdapter = new CustomAdapter(rItem, getApplicationContext());
                    mAdapter.setOnItemClickListener(new CustomAdapter.OnItemClickEventListener() {
                        @Override
                        public void onItemClick(View a_view, int a_position) {
                            final Uri item = rItem.getGalleryuri().get(a_position);
                            //startToast("해당 아이템 정보>>>"+item);
                            deleteBackgroundLayout.setVisibility(View.VISIBLE);
                            deleteBackgroundLayout.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (deleteBackgroundLayout.getVisibility() == View.VISIBLE) {
                                            deleteBackgroundLayout.setVisibility(View.GONE);
                                        }
                                    }
                            });

                            delete_layout.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    deleteBackgroundLayout.setVisibility(View.GONE);
                                    rItem.getGalleryuri().remove(a_position);
                                    startToast("해당 사진이 삭제되었습니다.");
                                    mAdapter.notifyItemRemoved(a_position);
                                }
                            });

                        }
                    });

                    recyclerview.setAdapter(mAdapter);
                    mLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
                    recyclerview.setLayoutManager(mLayoutManager);
                    recyclerview.setHasFixedSize(true);

                } else {
                    Log.e("로그", "No media selected");
                }
            });



    private void loadImage(){
        try {
            Log.e("로그", "startForMultipleModeResult 런치 시작!");
            startForMultipleModeResult.launch(new PickVisualMediaRequest());
        } catch (ActivityNotFoundException ex) {
            showDialog(ex.getLocalizedMessage());
        }
    }

    @NonNull
    private Optional<MediaType> getMediaType(Uri currentUri) {
        final String type = getContentResolver().getType(currentUri);
        final String mimeType = MimeTypeMap.getSingleton().getExtensionFromMimeType(type);
        Log.e("로그", "type: " + type + " mimeType: " + mimeType); // type: video/mp4 mimeType: mp4
        Optional<MediaType> result = Optional.empty();
        try {
            final String[] MIME_TYPES = new String[]{"image/*", "video/*"};
            Log.e("로그", "MimeTypeFilter.matches " + MimeTypeFilter.matches(type, MIME_TYPES));
            result = MediaType.mediaFind(type);
        } catch (IllegalArgumentException ex) {
            showDialog(ex.getLocalizedMessage());
        }
        result.ifPresent(ret -> Log.e("로그", "getMimeType: " + ret.getMediaType())); // video/mp4
        return result;
    }

    private void handlePickerResponse(Uri currentUri) {
        Log.d(TAG, "handlePickerResponse: currentUri: " + currentUri);
        final Optional<MediaType> clipMimeType = getMediaType(currentUri); // get MIME_TYPE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            clipMimeType.ifPresentOrElse(type -> {
                switch (type) {
                    case IMAGE_GIF:
                    case IMAGE_PNG:
                    case IMAGE_JPG:
                    case VIDEO_MP4:
                    case VIDEO_WEBM:
                        pathList.add(currentUri);
                        break;
                }
            }, () -> Log.e("로그", "handlePickerResponse: error!"));
        }
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

    private void storageUpload(){
        final String title = ((EditText)findViewById(R.id.title_edit)).getText().toString();
        final String contents = ((EditText)findViewById(R.id.contets_edit)).getText().toString();
        if(title.length()>0 && !pathList.isEmpty()){

            stringUri = new ArrayList<String>();

            stringUri = mFirebaseMethods.changeString(changePath);
            postId = mFirebaseMethods.addAlbumToDatabase(title, contents, stringUri);
            mFirebaseMethods.uploadNewPost(title, contents, postId, changePath);

        }else if(title.length()==0){
            startToast("제목을 작성해주세요.");
        }else if(pathList.isEmpty()){
            startToast("앨범에 등록할 사진을 선택해주세요.");
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        pathList.clear();
        changePath.clear();
        //mAdapter.notifyDataSetChanged();
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

    private void showDialog(String message) {
        new MaterialAlertDialogBuilder(this)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> dialogInterface.dismiss())
                .create().show();
        Log.d(TAG, message);
    }
}