package com.example.dodamdodam.activity.Question;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dodamdodam.R;
import com.example.dodamdodam.activity.Calendar.CalendarMain;
import com.example.dodamdodam.activity.Setting.SettingMain;
import com.example.dodamdodam.activity.album.AlbumMain;
import com.google.android.gms.tasks.OnCompleteListener;
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

import java.text.SimpleDateFormat;
import java.util.Date;


public class QuestionMain extends AppCompatActivity {

    private DatabaseReference databaseReference,database,databasesetting;
    private FirebaseUser user;
    private DatabaseReference userdatabaseReference;
    private DatabaseReference loveruidReference;
    private DatabaseReference loveruidcodeReference;
    private String LOVERUID;


    private String TAG;
    private Button ques_submit_btn,ques_show_btn;
    private ImageButton question_list_btn;
    public String str_ans = null;
    private EditText et_ques;

    private TextView show_question,tv_show_answer1,tv_show_answer2,tv_today_question,tv_no_answer,tv_happy;

    public String questionkey,getTime;

    private ImageView r1,r2,r3;
    private int num,number;
    private long now;
    public ImageButton ALBUMBTN,CALENDARBTN,SETTINGBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_main);
        ALBUMBTN = findViewById(R.id.albumBtn);
        SETTINGBTN = findViewById(R.id.settingBtn);
        CALENDARBTN = findViewById(R.id.calendarBtn2);

        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Question");
        userdatabaseReference = FirebaseDatabase.getInstance().getReference("users");
        loveruidReference = userdatabaseReference.child(user.getUid());
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        database = FirebaseDatabase.getInstance().getReference("UpdateDay");
        databasesetting = FirebaseDatabase.getInstance().getReference("Setting");
        DocumentReference docRef = db.collection("users").document(user.getUid());

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        LOVERUID = document.getData().get("lover").toString();

                    } else {
                        Log.d(TAG, "없다없다없다없다");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });//LoverUID받아오기
/////////////////////////////////////////////////////////////////////////////////////////////////////////
        tv_show_answer1=findViewById(R.id.tv_show_answer1);
        tv_show_answer2=findViewById(R.id.tv_show_answer2);
        et_ques=(EditText)findViewById(R.id.et_question);
        str_ans=et_ques.getText().toString();
        ques_submit_btn=findViewById(R.id.ques_submit_btn);

        tv_today_question=findViewById(R.id.tv_today_question);
        tv_no_answer=findViewById(R.id.tv_no_answer);
        tv_happy=findViewById(R.id.tv_happy);
        show_question=findViewById(R.id.tv_show_question);

        r1=findViewById(R.id.rabbit);
        r2=findViewById(R.id.sadrabbit);
        r3=findViewById(R.id.happyrabbit);
        now=System.currentTimeMillis();
        Date date =new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        getTime = sdf.format(date);

        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.child("Info").child(user.getUid()).getValue()!=null){
                if((snapshot.child("Info").child(user.getUid()).getValue().toString()).equals(getTime)==false){//하루지났을때

                    num=Integer.valueOf(snapshot.child("Num").child(user.getUid()).getValue().toString());
                    databaseReference.child(Integer.toString(num)).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot datasnapshot1) {
                            for(DataSnapshot snapshot1 : datasnapshot1.getChildren()){
                                if(snapshot1.hasChild(user.getUid())&&snapshot1.hasChild(LOVERUID)) {//둘다 답변이 있다면?
                                    database.child("Num").child(user.getUid()).setValue(Integer.toString(num + 1));
                                    tv_today_question.setVisibility(View.VISIBLE);
                                    tv_happy.setVisibility(View.INVISIBLE);
                                    tv_no_answer.setVisibility(View.INVISIBLE);
                                    r1.setVisibility(View.VISIBLE);
                                    r2.setVisibility(View.INVISIBLE);
                                    r3.setVisibility(View.INVISIBLE);
                                    et_ques.setVisibility(View.VISIBLE);
                                    ques_submit_btn.setVisibility(View.VISIBLE);
                                    tv_show_answer1.setVisibility(View.INVISIBLE);
                                    tv_show_answer2.setVisibility(View.INVISIBLE);
                                }
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                    database.child("Info").child(user.getUid()).setValue(getTime);

                }
                else{//날짜 안지남
                    num=Integer.valueOf(snapshot.child("Num").child(user.getUid()).getValue().toString());
                    databaseReference.child(Integer.toString(num)).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot childSnapshot:snapshot.getChildren()){
                                questionkey=childSnapshot.getKey();
                                show_question.setText(questionkey);

                                if(childSnapshot.hasChild(user.getUid())){//내 답변이 등록되있을떄
                                    et_ques.setVisibility(View.INVISIBLE);
                                    ques_submit_btn.setVisibility(View.INVISIBLE);
                                    databasesetting.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if(snapshot.child("mynickname").child(user.getUid()).getValue()!=null){
                                                String mynick = snapshot.child("mynickname").child(user.getUid()).getValue().toString();
                                                tv_show_answer1.setText(mynick+"의 답변 : "+childSnapshot.child(user.getUid()).getValue().toString());
                                            }
                                            else{
                                                tv_show_answer1.setText("나의 답변 : "+childSnapshot.child(user.getUid()).getValue().toString());
                                                Toast.makeText(QuestionMain.this, "설정에 가서 닉네임을 등록해주세요", Toast.LENGTH_SHORT).show();

                                            }

                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                    if(childSnapshot.hasChild(LOVERUID)) {//상대도 답변했음

                                        //tv_show_answer2.setText("너 : " + childSnapshot.child(LOVERUID).getValue().toString());
                                        tv_happy.setVisibility(View.VISIBLE);
                                        r1.setVisibility(View.INVISIBLE);
                                        r2.setVisibility(View.INVISIBLE);
                                        r3.setVisibility(View.VISIBLE);
                                        tv_today_question.setVisibility(View.INVISIBLE);
                                        tv_no_answer.setVisibility(View.INVISIBLE);
                                        tv_show_answer2.setVisibility(View.VISIBLE);
                                        databasesetting.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if(snapshot.child("lovernickname").child(user.getUid()).getValue()!=null){
                                                    String lovernick = snapshot.child("lovernickname").child(user.getUid()).getValue().toString();
                                                    tv_show_answer2.setText(lovernick+"의 답변 : "+childSnapshot.child(LOVERUID).getValue().toString());
                                                }
                                                else{
                                                    tv_show_answer2.setText("상대방의 답변 : "+childSnapshot.child(LOVERUID).getValue().toString());
                                                    Toast.makeText(QuestionMain.this, "설정에 가서 닉네임을 등록해주세요", Toast.LENGTH_SHORT).show();

                                                }
                                                //설정에 닉네임 등록했으면 출력, 아니면 상대방으로 출력
                                            }
                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }
                                    else{//상대 답변없을때
                                        tv_happy.setVisibility(View.INVISIBLE);
                                        tv_today_question.setVisibility(View.INVISIBLE);
                                        tv_no_answer.setVisibility(View.VISIBLE);
                                        r1.setVisibility(View.INVISIBLE);
                                        r2.setVisibility(View.VISIBLE);
                                        r3.setVisibility(View.INVISIBLE);
                                        tv_show_answer2.setText("상 대 방 이   아 직   답 변 하 지   않 았 어 요...");
                                        tv_show_answer2.setVisibility(View.VISIBLE);
                                        //너 :
                                        //보다는 그냥 원래 하던 답변하지 않았다가 나을거 같아서 일단 바꿈
                                    }
                                    tv_show_answer1.setVisibility(View.VISIBLE);

                                }
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }}

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

////////////////질문 띄우기//////////////////////////////////////////////////////////////////////////////////////////////////////////
        ques_show_btn=findViewById(R.id.ques_show_btn);
        ques_show_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                database.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        number=Integer.valueOf(snapshot.child("Num").child(user.getUid()).getValue().toString());

                        databaseReference.child(Integer.toString(number)).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot childSnapshot:snapshot.getChildren()){
                                    questionkey=childSnapshot.getKey();
                                    show_question.setText(questionkey);
                                    database.child("Info").child(user.getUid()).setValue(getTime);
                                    if(childSnapshot.hasChild(LOVERUID)){
                                        tv_show_answer2.setText("너 : "+childSnapshot.child(LOVERUID).getValue().toString());
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        ///답변 입력하기///////////////////////////////////////////////////////////////////////


            ques_submit_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    database.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            number=Integer.valueOf(snapshot.child("Num").child(user.getUid()).getValue().toString());
                            et_ques=(EditText)findViewById(R.id.et_question);
                            str_ans=et_ques.getText().toString();
                            databaseReference.child(Integer.toString(number)).child(questionkey).child(user.getUid()).setValue(str_ans);
                            Toast myToast = Toast.makeText(getApplicationContext(),"답변이 등록되었습니다!", Toast.LENGTH_SHORT);
                            myToast.show();
                            et_ques.setVisibility(View.INVISIBLE);
                            ques_submit_btn.setVisibility(View.INVISIBLE);
                            database.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    number=Integer.valueOf(snapshot.child("Num").child(user.getUid()).getValue().toString());

                                    databaseReference.child(Integer.toString(number)).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            for(DataSnapshot childSnapshot:snapshot.getChildren()){
                                                tv_show_answer1=findViewById(R.id.tv_show_answer1);
                                                tv_show_answer2=findViewById(R.id.tv_show_answer2);
                                                databasesetting.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        if(snapshot.child("lovernickname").child(user.getUid()).getValue()!=null){
                                                            String mynick = snapshot.child("mynickname").child(LOVERUID).getValue().toString();
                                                            tv_show_answer1.setText(mynick+"의 답변 : "+childSnapshot.child(user.getUid()).getValue().toString());
                                                        }
                                                        else{

                                                            Toast.makeText(QuestionMain.this, "설정에 가서 닉네임을 등록해주세요", Toast.LENGTH_SHORT).show();

                                                        }

                                                        tv_show_answer1.setVisibility(View.VISIBLE);
                                                        //설정에 닉네임 등록했으면 출력, 아니면 상대방으로 출력
                                                    }
                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });
                                                if(childSnapshot.hasChild(LOVERUID)){
                                                    databasesetting.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            if(snapshot.child("lovernickname").child(user.getUid()).getValue()!=null){
                                                                String lovernick = snapshot.child("lovernickname").child(user.getUid()).getValue().toString();
                                                                String mynick = snapshot.child("mynickname").child(LOVERUID).getValue().toString();
                                                                tv_show_answer2.setText(lovernick+"의 답변 : "+childSnapshot.child(LOVERUID).getValue().toString());
                                                                tv_show_answer1.setText(mynick+"의 답변 : "+childSnapshot.child(user.getUid()).getValue().toString());
                                                            }
                                                            else{
                                                                tv_show_answer2.setText("상대방의 답변 : "+childSnapshot.child(LOVERUID).getValue().toString());
                                                                Toast.makeText(QuestionMain.this, "설정에 가서 닉네임을 등록해주세요", Toast.LENGTH_SHORT).show();

                                                            }

                                                            tv_show_answer2.setVisibility(View.VISIBLE);
                                                            tv_show_answer1.setVisibility(View.VISIBLE);
                                                            r3.setVisibility(View.VISIBLE);
                                                            r1.setVisibility(View.INVISIBLE);
                                                            r2.setVisibility(View.INVISIBLE);
                                                            //설정에 닉네임 등록했으면 출력, 아니면 상대방으로 출력
                                                        }
                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });

                                                }
                                                else{
                                                    tv_show_answer2.setText("상대방이 아직 답변하지 않았었요...");
                                                    tv_show_answer2.setVisibility(View.VISIBLE);
                                                    r2.setVisibility(View.VISIBLE);
                                                    r3.setVisibility(View.INVISIBLE);
                                                    r1.setVisibility(View.INVISIBLE);
                                                }
                                            }
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }
            });






////////밑의 버튼들/////////////////////////////////////////////////////////////////
        question_list_btn=findViewById(R.id.question_list_btn);
        question_list_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QuestionMain.this, QuestionList.class);
                intent.putExtra("userUid",user.getUid());
                intent.putExtra("loverUid",LOVERUID);
                startActivity(intent);
            }
        });

        ALBUMBTN.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                myStartActivity(AlbumMain.class);
            }
        });
        SETTINGBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myStartActivity(SettingMain.class);
            }
        });
        CALENDARBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myStartActivity(CalendarMain.class);
            }
        });
    }
    private void myStartActivity(Class c){
        Intent intent = new Intent(this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("도담도담 앱을 종료하시겠습니까?");
        builder.setPositiveButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setNegativeButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                moveTaskToBack(true);
                finishAndRemoveTask();
                finish();
            }
        });
        builder.show();
    }
}