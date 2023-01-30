package com.example.dodamdodam.activity.Setting;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dodamdodam.activity.Login.LoginActivity;
import com.google.firebase.firestore.DocumentReference;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dodamdodam.R;
import com.example.dodamdodam.activity.Calendar.CalendarMain;
import com.example.dodamdodam.activity.Login.MainActivity;
import com.example.dodamdodam.activity.Login.SignUpActivity;
import com.example.dodamdodam.activity.Question.QuestionMain;
import com.example.dodamdodam.activity.album.AlbumMain;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SettingMain extends AppCompatActivity {
    public Button chabtn_1,chabtn_2,chabtn_3;
    public Button savebtn_1,savebtn_2,savebtn_3,askbtn,withdrawbtn;
    public EditText editText_1,editText_2,editText_3;
    public TextView textView_1,textView_2,textView_3;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    public ImageButton question_Btn,album_Btn,calendar_Btn;
    public String TAG,LOVERUID2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_main);
        chabtn_1 = findViewById(R.id.chaBtn1);
        chabtn_2 = findViewById(R.id.chaBtn2);
        chabtn_3 = findViewById(R.id.chaBtn3);
        savebtn_1=findViewById(R.id.saveBtn1);
        savebtn_2=findViewById(R.id.saveBtn2);
        savebtn_3=findViewById(R.id.saveBtn3);
        editText_1=findViewById(R.id.ourAnniversaryEditText);
        editText_2=findViewById(R.id.myNickNameEditText);
        editText_3=findViewById(R.id.loverNickNameEditText);
        textView_1=findViewById(R.id.ourAnniversaryTextView);
        textView_2=findViewById(R.id.myNickNameTextView);
        textView_3=findViewById(R.id.loverNickNameTextView);
        question_Btn=findViewById(R.id.questionBtn);
        calendar_Btn=findViewById(R.id.calendarBtn2);
        album_Btn=findViewById(R.id.albumBtn);
        askbtn=findViewById(R.id.askBtn);
        withdrawbtn=findViewById(R.id.withdrawBtn);
        user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference document = db.collection("users").document(user.getUid());

        databaseReference = FirebaseDatabase.getInstance().getReference("Setting");
        databaseReference.child("anniversary").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(user.getUid()).getValue()!=null){
                    textView_1.setText(snapshot.child(user.getUid()).getValue().toString());
                    editText_1.setVisibility(View.INVISIBLE);
                    textView_1.setVisibility(View.VISIBLE);
                    savebtn_1.setVisibility(View.INVISIBLE);
                    chabtn_1.setVisibility(View.VISIBLE);
                }
                else{
                    editText_1.setText(null);
                    editText_1.setVisibility(View.VISIBLE);
                    textView_1.setVisibility(View.INVISIBLE);
                    savebtn_1.setVisibility(View.VISIBLE);
                    chabtn_1.setVisibility(View.INVISIBLE);
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
                    textView_2.setText(snapshot.child(user.getUid()).getValue().toString());
                    editText_2.setVisibility(View.INVISIBLE);
                    textView_2.setVisibility(View.VISIBLE);
                    savebtn_2.setVisibility(View.INVISIBLE);
                    chabtn_2.setVisibility(View.VISIBLE);
                }
                else{
                    editText_2.setText(null);
                    editText_2.setVisibility(View.VISIBLE);
                    textView_2.setVisibility(View.INVISIBLE);
                    savebtn_2.setVisibility(View.VISIBLE);
                    chabtn_2.setVisibility(View.INVISIBLE);
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
                    textView_3.setText(snapshot.child(user.getUid()).getValue().toString());
                    editText_3.setVisibility(View.INVISIBLE);
                    textView_3.setVisibility(View.VISIBLE);
                    savebtn_3.setVisibility(View.INVISIBLE);
                    chabtn_3.setVisibility(View.VISIBLE);
                }
                else{
                    editText_3.setText(null);
                    editText_3.setVisibility(View.VISIBLE);
                    textView_3.setVisibility(View.INVISIBLE);
                    savebtn_3.setVisibility(View.VISIBLE);
                    chabtn_3.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        savebtn_1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                databaseReference.child("anniversary").child(user.getUid()).setValue(editText_1.getText().toString());
                textView_1.setText(editText_1.getText().toString());
                savebtn_1.setVisibility(View.INVISIBLE);
                chabtn_1.setVisibility(View.VISIBLE);
                editText_1.setVisibility(View.INVISIBLE);
                textView_1.setVisibility(View.VISIBLE);
            }
        });

        savebtn_2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                databaseReference.child("mynickname").child(user.getUid()).setValue(editText_2.getText().toString());

                textView_2.setText(editText_2.getText().toString());
                savebtn_2.setVisibility(View.INVISIBLE);
                chabtn_2.setVisibility(View.VISIBLE);
                editText_2.setVisibility(View.INVISIBLE);
                textView_2.setVisibility(View.VISIBLE);
            }
        });
        savebtn_3.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                databaseReference.child("lovernickname").child(user.getUid()).setValue(editText_3.getText().toString());
                textView_3.setText(editText_3.getText().toString());
                savebtn_3.setVisibility(View.INVISIBLE);
                chabtn_3.setVisibility(View.VISIBLE);
                editText_3.setVisibility(View.INVISIBLE);
                textView_3.setVisibility(View.VISIBLE);
            }
        });



        chabtn_1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                textView_1.setVisibility(View.INVISIBLE);
                editText_1.setVisibility(View.VISIBLE);
                savebtn_1.setVisibility(View.VISIBLE);
                chabtn_1.setVisibility(View.INVISIBLE);
            }
        });

        chabtn_2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                textView_2.setVisibility(View.INVISIBLE);
                editText_2.setVisibility(View.VISIBLE);
                savebtn_2.setVisibility(View.VISIBLE);
                chabtn_2.setVisibility(View.INVISIBLE);            }
        }); chabtn_3.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                textView_3.setVisibility(View.INVISIBLE);
                editText_3.setVisibility(View.VISIBLE);
                savebtn_3.setVisibility(View.VISIBLE);
                chabtn_3.setVisibility(View.INVISIBLE);            }
        });

        album_Btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                myStartActivity(AlbumMain.class);
            }
        });
        question_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myStartActivity(QuestionMain.class);
            }
        });
        calendar_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myStartActivity(CalendarMain.class);
            }
        });

        askbtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent email = new Intent(Intent.ACTION_SEND);
                email.setType("plain/text");
                String[] address = {"dobbydavid1@naver.com"};
                email.putExtra(Intent.EXTRA_EMAIL, address);
                email.putExtra(Intent.EXTRA_SUBJECT, "도담도담 1대1 문의");
                email.putExtra(Intent.EXTRA_TEXT, "아이디 : \n문의사항 : ");
                startActivity(email);

            }
        });

        withdrawbtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                showDialog();
            }
        });


    }
    private void myStartActivity(Class c){
        Intent intent = new Intent(this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    private void startToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    void showDialog() {
        AlertDialog.Builder msgBuilder = new AlertDialog.Builder(SettingMain.this)
                .setTitle("탈퇴하기")
                .setMessage("탈퇴 시 되돌릴 수 없습니다")
                .setPositiveButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(SettingMain.this, "취소하였습니다", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("탈퇴", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(), "길게 출력 Hello World!", Toast.LENGTH_LONG).show();
                        WITHDRAW();
                        //startToast("탈퇴를 진행하겠습니다\n1분 내외의 시간이 소요됩니다");
                    }
                });
        AlertDialog msgDlg = msgBuilder.create();
        msgDlg.show();
    }
    void WITHDRAW(){
        //user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("users").document(user.getUid());

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        LOVERUID2 = document.getData().get("lover").toString();
                        db.collection("users").document(LOVERUID2)
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error deleting document", e);
                                    }
                                });

                        db.collection("users").document(user.getUid())
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error deleting document", e);
                                    }
                                });




                    }
                    else {
                        Log.d(TAG, "없다없다없다없다");
                    }
                }
                else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User account deleted.");
                        }
                    }
                });


        FirebaseAuth.getInstance().signOut();
        myStartActivity(LoginActivity.class);
    }
}