package com.dodamdodam.dodamdodam.activity.Question;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.dodamdodam.dodamdodam.R;
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

public class AnswerPop extends AppCompatActivity {

    private DatabaseReference databaseReference,userdatabaseReference,loveruidReference,databasesetting;
    private FirebaseUser user;
    private String TAG;
    private String LOVERUID,questionkey,answer1,answer2,List_Num,question,mynick,lovernick;
    private TextView tv_answer1_field,tv_answer2_field,tv_question_field,tv_question_title,tv_answer1_title,tv_answer2_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.answerpop);
        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Question");
        userdatabaseReference = FirebaseDatabase.getInstance().getReference("users");
        loveruidReference = userdatabaseReference.child(user.getUid());
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("users").document(user.getUid());
        databasesetting = FirebaseDatabase.getInstance().getReference("Setting");


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

        Intent intent =getIntent();
        List_Num = intent.getStringExtra("List_Num");

        databaseReference.child(List_Num).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot1) {
                for(DataSnapshot snapshot1 : datasnapshot1.getChildren()){
                    if(snapshot1.hasChild(user.getUid())&&snapshot1.hasChild(LOVERUID)) {//둘다 답변이 있다면?
                        tv_answer1_field=findViewById(R.id.tv_answer1_field);
                        tv_answer2_field=findViewById(R.id.tv_answer2_field);
                        tv_question_field=findViewById(R.id.tv_question_field);
                        tv_question_title=findViewById(R.id.tv_question_title);
                        questionkey=snapshot1.getKey();
                        answer1=snapshot1.child(user.getUid()).getValue().toString();
                        answer2=snapshot1.child(LOVERUID).getValue().toString();
                        tv_question_title.setText("우리의 "+List_Num+"번째 질문");
                        tv_question_field.setText(questionkey);
                        tv_answer1_field.setText(answer1);
                        tv_answer2_field.setText(answer2);
                        databasesetting.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                tv_answer1_title=findViewById(R.id.tv_answer1_title);
                                tv_answer2_title=findViewById(R.id.tv_answer2_title);
                                if(snapshot.child("mynickname").child(user.getUid()).getValue()!=null){
                                    mynick = snapshot.child("mynickname").child(user.getUid()).getValue().toString();
                                    tv_answer1_title.setText(mynick+"님의 답변");
                                    lovernick=snapshot.child("lovernickname").child(user.getUid()).getValue().toString();
                                    tv_answer2_title.setText(lovernick+"님의 답변");
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }
}
