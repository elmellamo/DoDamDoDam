package com.example.dodamdodam.activity.Question;



import android.os.Bundle;
import android.util.Log;

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

public class QuestionList extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<QuestionListObject> arrayList;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    private String num,questionkey,answer1,answer2;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_list);

        recyclerView = findViewById(R.id.RecyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>();

        database = FirebaseDatabase.getInstance();

        databaseReference = database.getReference("Question");

        num="1";
/*
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    questionkey=snapshot.getKey();

                    QuestionListObject questionObject = snapshot.getValue(QuestionListObject.class);


                    //questionObject.setQues(questionkey);
                    //questionObject.setAns1(questionkey);
                    //questionObject.setAns2(questionkey);
                    arrayList.add(questionkey)
                    arrayList.add(questionObject);
                    num=String.valueOf(Integer.valueOf(num)+1);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("QuestionList", String.valueOf(databaseError.toException()));
            }
        });


        adapter=new QuestionAdapter(arrayList, this);
        recyclerView.setAdapter(adapter);


*/
    }


}