package com.example.dodamdodam.activity.album;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dodamdodam.R;
import com.example.dodamdodam.view.AlbumInfo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

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
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.post_saves:
                    profileUpdate();
                    break;
            }
        }
    };

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

    private void startToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    private void myStartActivity(Class c){
        Intent intent = new Intent(this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}