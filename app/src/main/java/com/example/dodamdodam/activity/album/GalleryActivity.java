package com.example.dodamdodam.activity.album;

import static com.example.dodamdodam.Util.GALLERY_IMAGE;
import static com.example.dodamdodam.Util.GALLERY_VIDEO;
import static com.example.dodamdodam.Util.INTENT_MEDIA;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dodamdodam.R;
import com.example.dodamdodam.adapter.GalleryAdapter;

import java.util.ArrayList;
import java.util.Objects;

public class GalleryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("갤러리");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        permissionCheck();


    }
    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    Log.d("로그", "드디어!!! 성공!!");
                    recyclerInit();
                } else {
                    startToast("거절했으므로 종료합니다.");
                    finish();
                }
            });


    private void permissionCheck(){
        if (ContextCompat.checkSelfPermission(GalleryActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) ==  PackageManager.PERMISSION_GRANTED) {
            recyclerInit();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                startToast("이미 거절한 전적이 있습니다. 다시 해보겠습니다!");
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
            } else {
                // You can directly ask for the permission.
                // The registered ActivityResultCallback gets the result of this request.
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        }
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void recyclerInit(){
        final int numberOfColumns = 4;

        RecyclerView galleryrecyclerView = findViewById(R.id.galleryrecyclerview);
        galleryrecyclerView.setHasFixedSize(true);
        galleryrecyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));

        RecyclerView.Adapter mAdapter = new GalleryAdapter(this, getImagesPath(this));
        galleryrecyclerView.setAdapter(mAdapter);
    }


    private void startToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    public ArrayList<String> getImagesPath(Activity activity) {
        Uri uri;
        ArrayList<String> listOfAllImages = new ArrayList<String>();
        Cursor cursor;
        int column_index_data;
        String PathOfImage = null;
        String[] projection;

        Intent intent = getIntent();
        final int media = intent.getIntExtra(INTENT_MEDIA, GALLERY_IMAGE);
        if(media == GALLERY_VIDEO){
            uri = android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            projection = new String[] { MediaStore.MediaColumns.DATA, MediaStore.Video.Media.BUCKET_DISPLAY_NAME };
        }else{
            uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            projection = new String[] { MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME };
        }

        cursor = activity.getContentResolver().query(uri, projection, null, null, null);
        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);

        while (cursor.moveToNext()) {
            PathOfImage = cursor.getString(column_index_data);
            listOfAllImages.add(PathOfImage);
        }
        return listOfAllImages;
    }
}