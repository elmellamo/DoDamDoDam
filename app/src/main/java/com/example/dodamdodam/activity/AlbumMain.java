package com.example.dodamdodam.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dodamdodam.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AlbumMain extends AppCompatActivity {
    TextView myUid;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_main);
        findViewById(R.id.album_add).setOnClickListener(onClickListener);

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("도담도담");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.album_add:
                    myStartActivity(PutCode.class); ////여기에는 앨범 추가 사진이랑 간단 설명 넣을 시 갈 액티비티 넣기
                    break;
            }
        }
    };

    private void startToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    private void myStartActivity(Class c){
        Intent intent = new Intent(this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

}