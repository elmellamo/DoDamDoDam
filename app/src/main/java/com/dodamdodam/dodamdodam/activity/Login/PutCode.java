package com.dodamdodam.dodamdodam.activity.Login;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.dodamdodam.dodamdodam.R;
import com.dodamdodam.dodamdodam.activity.Question.QuestionMain;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PutCode extends BasicActivity {
    TextView myUid;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private EditText putlovercode;
    private Button connectBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_put_code);
        putlovercode = findViewById(R.id.putlovercode);
        connectBtn = findViewById(R.id.connectBtn);
        putlovercode.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if((keyEvent.getAction()==keyEvent.ACTION_DOWN)&& i == KeyEvent.KEYCODE_ENTER){
                    connectBtn.performClick();
                    return true;
                }
                return false;
            }
        });


        findViewById(R.id.connectBtn).setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.connectBtn:
                    putLoverCode();
                    break;
            }
        }
    };

    private void putLoverCode() {

        String loveruid = ((EditText)findViewById(R.id.putlovercode)).getText().toString();
        if (loveruid.length() > 0) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference document = db.collection("users")
                    .document(loveruid);
            document.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                String otherlover = (String) documentSnapshot.get("lover");
                                if (otherlover == null) {
                                    db.collection("users").document(user.getUid()).update("lover", loveruid);
                                    db.collection("users").document(loveruid).update("lover", user.getUid());
                                    startToast("짝꿍과 연결되었습니다!");
                                    long now=System.currentTimeMillis();
                                    Date date =new Date(now);
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                                    String getTime = sdf.format(date);
                                    DatabaseReference database = FirebaseDatabase.getInstance().getReference("UpdateDay");
                                    database.child("Num").child(user.getUid()).setValue("1");
                                    database.child("Info").child(user.getUid()).setValue(getTime);
                                    database.child("Num").child(loveruid).setValue("1");
                                    database.child("Info").child(loveruid).setValue(getTime);


                                    myStartActivity(QuestionMain.class);
                                } else {
                                    startToast("해당 짝꿍은 연결되어 있는 사람이 있어요.");
                                }
                            } else
                                startToast("짝꿍이 존재하지 않아요.");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            startToast("Failed to fetch");
                        }
                    });
        }
    }

    private void startToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        finish();
//    }
    private void myStartActivity(Class c){
        Intent intent = new Intent(this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(PutCode.this);
        builder.setMessage("도담도담 앱을 종료하시겠습니까?");
        builder.setPositiveButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setNegativeButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                moveTaskToBack(true); // 태스크를 백그라운드로 이동
                finishAndRemoveTask(); // 액티비티 종료 + 태스크 리스트에서 지우기
                finish();
            }
        });
        builder.show();
    }
}