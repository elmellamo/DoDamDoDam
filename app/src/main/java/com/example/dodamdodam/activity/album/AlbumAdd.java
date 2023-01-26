package com.example.dodamdodam.activity.album;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.MimeTypeFilter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dodamdodam.R;
import com.example.dodamdodam.activity.Calendar.CalendarMain;
import com.example.dodamdodam.activity.Setting.SettingMain;
import com.example.dodamdodam.adapter.CustomAdapter;
import com.example.dodamdodam.view.AlbumInfo;
import com.example.dodamdodam.view.MediaType;
import com.example.dodamdodam.view.RecyclerViewItem;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
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
import java.util.Optional;

public class AlbumAdd extends AppCompatActivity {
    private static final String TAG = "AlbumAdd";
    private RecyclerView recyclerview;
    private RecyclerView.LayoutManager mLayoutManager;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();;
    private ArrayList<String> pathList = new ArrayList<>();
    private ArrayList<RecyclerViewItem> mRItems = new ArrayList<>();
    private CustomAdapter mAdapter;
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

        recyclerview = findViewById(R.id.addgallery_layout);

        findViewById(R.id.post_saves).setOnClickListener(onClickListener);
        findViewById(R.id.image_btn).setOnClickListener(onClickListener);

        EditText editText = new EditText(AlbumAdd.this);
        editText.setHint("내용을 입력하세요.");

        findViewById(R.id.questionBtn).setOnClickListener(onClickListener);
        findViewById(R.id.settingBtn).setOnClickListener(onClickListener);
        findViewById(R.id.calendarBtn2).setOnClickListener(onClickListener);
        findViewById(R.id.albumBtn).setOnClickListener(onClickListener);

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.post_saves:
                    storageUpload();
                    break;
                case R.id.image_btn:
                    mRItems.clear();
                    loadImage();
                    break;
                case R.id.questionBtn:
                    //myStartActivity(AlbumMain.class);
                    break;
                case R.id.settingBtn:
                    myStartActivity(SettingMain.class);
                    break;
                case R.id.calendarBtn2:
                    myStartActivity(CalendarMain.class);
                    break;
                case R.id.albumBtn:
                    myStartActivity(AlbumMain.class);
                    break;

            }
        }
    };

    private final ActivityResultLauncher<PickVisualMediaRequest> startForMultipleModeResult =
            registerForActivityResult(new ActivityResultContracts.PickMultipleVisualMedia(10), uris -> {
                if (!uris.isEmpty()) {
                    Log.e("로그", "Number of items selected: " + uris.size());

                    for (int i = 0; i < uris.size(); i++) {
                        final Uri currentUri = uris.get(i);
                        Log.e("로그", "onActivityResult: currentUri" + currentUri.toString());

                        handlePickerResponse(currentUri);
                    }

                    mAdapter = new CustomAdapter(mRItems, getApplicationContext());
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
                        RecyclerViewItem rItem = new RecyclerViewItem();
                        rItem.setPublisher(user.getUid());
                        rItem.setMcreatedAt(new Date());
                        rItem.setGalleryuri(currentUri);

                        mRItems.add(rItem);
                        break;
                }
            }, () -> Log.e("로그", "handlePickerResponse: error!"));
        }
    }



    private  void storageUpload(){
        final String title = ((EditText)findViewById(R.id.title_edit)).getText().toString();
        final String contents = ((EditText)findViewById(R.id.contets_edit)).getText().toString();

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
                                        if (pathList.size() == successCount) {
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
        mRItems.clear();
        mAdapter.notifyDataSetChanged();
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