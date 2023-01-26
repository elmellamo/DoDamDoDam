package com.example.dodamdodam.activity.Setting;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dodamdodam.R;
import com.example.dodamdodam.activity.Calendar.CalendarMain;
import com.example.dodamdodam.activity.Login.SignUpActivity;
import com.example.dodamdodam.activity.album.AlbumMain;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SettingMain extends AppCompatActivity {
    public Button chabtn_1,chabtn_2,chabtn_3;
    public EditText editText_1,editText_2,editText_3;
    private DatabaseReference databaseReference;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_main);
        chabtn_1 = findViewById(R.id.chaBtn1);
        chabtn_2 = findViewById(R.id.chaBtn2);
        chabtn_3 = findViewById(R.id.chaBtn3);
        editText_1=findViewById(R.id.ourAnniversaryEditText);
        editText_2=findViewById(R.id.myNickNameEditText);
        editText_3=findViewById(R.id.loverNickNameEditText);
        user = FirebaseAuth.getInstance().getCurrentUser();

        databaseReference = FirebaseDatabase.getInstance().getReference("Setting");
        databaseReference.child("anniversary").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(user.getUid()).getValue()!=null){
                    editText_1.setText(snapshot.child(user.getUid()).getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseReference.child("mynickname").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(user.getUid()).getValue()!=null){
                    editText_2.setText(snapshot.child(user.getUid()).getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseReference.child("lovernickname").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(user.getUid()).getValue()!=null){
                    editText_3.setText(snapshot.child(user.getUid()).getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        chabtn_1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                databaseReference.child("anniversary").child(user.getUid()).setValue(editText_1.getText().toString());
            }
        });

        chabtn_2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                databaseReference.child("mynickname").child(user.getUid()).setValue(editText_2.getText().toString());
            }
        }); chabtn_3.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                databaseReference.child("lovernickname").child(user.getUid()).setValue(editText_3.getText().toString());
            }
        });


    }


}
