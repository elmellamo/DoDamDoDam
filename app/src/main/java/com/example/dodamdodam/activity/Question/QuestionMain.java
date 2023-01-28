package com.example.dodamdodam.activity.Question;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;

import com.example.dodamdodam.R;
import com.example.dodamdodam.activity.Login.BasicActivity;
import com.google.android.gms.tasks.OnCompleteListener;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.OnCompleteListener;
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



import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import com.example.dodamdodam.R;
import com.example.dodamdodam.activity.Login.BasicActivity;
import com.google.android.gms.tasks.OnCompleteListener;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;


public class QuestionMain extends AppCompatActivity {

    private DatabaseReference databaseReference,database;
    private FirebaseUser user;
    private DatabaseReference userdatabaseReference;
    private DatabaseReference loveruidReference;
    private DatabaseReference loveruidcodeReference;
    private String LOVERUID;


    private String TAG;
    private Button question_list_btn,ques_submit_btn,check_btn;
    public String str_ans = null;
    private EditText et_ques;

    private TextView show_question,show_num,show_number,show_num1;

    public String questionkey,getTime;


    private int num;
    private long now;





    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_main);

        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Question");
        userdatabaseReference = FirebaseDatabase.getInstance().getReference("users");
        loveruidReference = userdatabaseReference.child(user.getUid());
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("users").document(user.getUid());

        database = FirebaseDatabase.getInstance().getReference("UpdateDay");


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


        show_question=findViewById(R.id.tv_show_question);

        now=System.currentTimeMillis();
        Date date =new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        getTime = sdf.format(date);




        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(user.getUid())&&snapshot.hasChild(LOVERUID)){//시작했을때
                    if(snapshot.child("Info").child(user.getUid()).getValue().toString()!=getTime){//하루지났을때
                        num=Integer.valueOf(snapshot.child("Num").child(user.getUid()).getValue().toString())+1;
                        database.child("Num").child(user.getUid()).setValue(Integer.toString(num));



                        databaseReference.child(Integer.toString(num)).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot childSnapshot:snapshot.getChildren()){
                                    questionkey=childSnapshot.getKey();
                                    show_question.setText(questionkey);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });



                        et_ques=(EditText)findViewById(R.id.et_question);
                        str_ans=et_ques.getText().toString();
                        ques_submit_btn=findViewById(R.id.ques_submit_btn);
                        ques_submit_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                et_ques=(EditText)findViewById(R.id.et_question);
                                str_ans=et_ques.getText().toString();

                                databaseReference.child(Integer.toString(num)).child(questionkey).child(user.getUid()).setValue(str_ans);

                            }
                        });

                        Intent intent1 = new Intent(QuestionMain.this, QuestionList.class);
                        intent1.putExtra("Num",Integer.toString(num));

                    }
                    else{
                        num=Integer.valueOf(snapshot.child("Num").child(user.getUid()).getValue().toString());


                        databaseReference.child(Integer.toString(num)).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot childSnapshot:snapshot.getChildren()){
                                    questionkey=childSnapshot.getKey();
                                    show_question.setText(questionkey);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });




                        et_ques=(EditText)findViewById(R.id.et_question);
                        str_ans=et_ques.getText().toString();
                        ques_submit_btn=findViewById(R.id.ques_submit_btn);
                        ques_submit_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                et_ques=(EditText)findViewById(R.id.et_question);
                                str_ans=et_ques.getText().toString();

                                databaseReference.child(Integer.toString(num)).child(questionkey).child(user.getUid()).setValue(str_ans);

                            }
                        });
                    }
                    Intent intent1 = new Intent(QuestionMain.this, QuestionList.class);
                    intent1.putExtra("Num",Integer.toString(num));

                }
                else{//둘중에 하나라도 시작 안했을때
                    database.child("Info").child(user.getUid()).setValue(getTime);
                    database.child("Num").child(user.getUid()).setValue("1");
                    database.child("Info").child(LOVERUID).setValue(getTime);
                    database.child("Num").child(LOVERUID).setValue("1");
                    //number = snapshot.child("Num").child(user.getUid()).getValue(Number.class);
                    num=Integer.valueOf(snapshot.child("Num").child(user.getUid()).getValue().toString());
                    databaseReference.child(Integer.toString(num)).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot childSnapshot:snapshot.getChildren()){
                                questionkey=childSnapshot.getKey();
                                show_question.setText(questionkey);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });



                    et_ques=(EditText)findViewById(R.id.et_question);
                    str_ans=et_ques.getText().toString();
                    ques_submit_btn=findViewById(R.id.ques_submit_btn);
                    ques_submit_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            et_ques=(EditText)findViewById(R.id.et_question);
                            str_ans=et_ques.getText().toString();

                            databaseReference.child(Integer.toString(num)).child(questionkey).child(user.getUid()).setValue(str_ans);

                        }
                    });



                    Intent intent1 = new Intent(QuestionMain.this, QuestionList.class);
                    intent1.putExtra("Num",Integer.toString(num));


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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

        /*


        databaseReference.child(number).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot childSnapshot:snapshot.getChildren()){
                    questionkey=childSnapshot.getKey();
                    show_question.setText(questionkey);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });









        //데이터입력
        et_ques=(EditText)findViewById(R.id.et_question);
        str_ans=et_ques.getText().toString();
        ques_submit_btn=findViewById(R.id.ques_submit_btn);
        ques_submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_ques=(EditText)findViewById(R.id.et_question);
                str_ans=et_ques.getText().toString();

                databaseReference.child(number).child(questionkey).child(user.getUid()).setValue(str_ans);

            }
        });





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

*/

    }

}
