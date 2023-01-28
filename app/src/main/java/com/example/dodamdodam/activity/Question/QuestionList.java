package com.example.dodamdodam.activity.Question;


import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dodamdodam.R;

import com.example.dodamdodam.adapter.QuestionAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;
import java.util.function.LongToIntFunction;

public class QuestionList extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<QuestionListObject> arrayList;
    private FirebaseDatabase database;
    private FirebaseDatabase database1;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReference1;
    private QuestionAdapter questionAdapter;
    private String num,questionkey,question,answer1,answer2,userUid,loverUid,answer11,answer22;
    private int how_many_question;
    private String Num;
    private TextView uiduid;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_list);

        recyclerView = (RecyclerView) findViewById(R.id.RecyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>();
        questionAdapter = new QuestionAdapter(arrayList);

        database = FirebaseDatabase.getInstance();
        database1 = FirebaseDatabase.getInstance();
        recyclerView.setAdapter((questionAdapter));
        databaseReference = database.getReference("Question");



        Intent intent =getIntent();
        userUid = intent.getStringExtra("userUid");
        loverUid = intent.getStringExtra("loverUid");
        Intent intent1 =getIntent();
        Num=intent1.getStringExtra("Num");
        how_many_question=Integer.valueOf(Num);
        answer2=loverUid;
        for(int i=1;i<=how_many_question;i++) {
            databaseReference.child(Integer.toString(i)).addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {

                    for (DataSnapshot snapshot1 : dataSnapshot1.getChildren()) {
                        questionkey = snapshot1.getKey();
                        if (snapshot1.hasChild(userUid)) {
                            question = questionkey;
                        } else question = null;

                        if (snapshot1.hasChild(userUid)) {
                            answer1 = snapshot1.child(userUid).getValue().toString();
                        }
                        else answer1=null;
                        if (snapshot1.hasChild(loverUid)) {
                            answer2 = snapshot1.child(loverUid).getValue().toString();
                        }
                        else answer2=null;
                        QuestionListObject questionlistObject = new QuestionListObject(question, answer1, answer2);
                        arrayList.add(questionlistObject);

                    }
                    questionAdapter.notifyDataSetChanged();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });

        }
    }


}