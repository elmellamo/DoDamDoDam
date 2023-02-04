package com.example.dodamdodam.activity.Question;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dodamdodam.R;
import com.example.dodamdodam.activity.Calendar.CalendarMain;
import com.example.dodamdodam.activity.Setting.SettingMain;
import com.example.dodamdodam.activity.album.AlbumMain;
import com.example.dodamdodam.adapter.QuestionAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class QuestionList extends AppCompatActivity {

    private RecyclerView recyclerView;
    public ImageButton question_Btn,calendar_Btn,album_Btn, setting_Btn;
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
        databaseReference1=database1.getReference("UpdateDay");
        question_Btn = findViewById(R.id.question_main_btn);
        calendar_Btn=findViewById(R.id.calendarBtn_l);
        album_Btn = findViewById(R.id.albumBtn_l);
        setting_Btn = findViewById(R.id.settingBtn_l);

        Intent intent =getIntent();
        userUid = intent.getStringExtra("userUid");
        loverUid = intent.getStringExtra("loverUid");


        databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String num=snapshot.child("Num").child(userUid).getValue().toString();
                for(int i=1;i<=Integer.valueOf(num);i++) {
                    databaseReference.child(Integer.toString(i)).addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {

                            for (DataSnapshot snapshot1 : dataSnapshot1.getChildren()) {
                                if(snapshot1.hasChild(userUid)&&snapshot1.hasChild(loverUid)) {
                                    questionkey = snapshot1.getKey();
                                    if (snapshot1.hasChild(userUid)) {
                                        question = questionkey;
                                    } else question = null;

                                    if (snapshot1.hasChild(userUid)) {
                                        answer1 = snapshot1.child(userUid).getValue().toString();
                                    } else answer1 = null;
                                    if (snapshot1.hasChild(loverUid)) {
                                        answer2 = snapshot1.child(loverUid).getValue().toString();
                                    } else answer2 = null;
                                    QuestionListObject questionlistObject = new QuestionListObject(question, answer1, answer2);
                                    arrayList.add(questionlistObject);
                                }
                            }
                            questionAdapter.notifyDataSetChanged();
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        album_Btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                myStartActivity(AlbumMain.class);
            }
        });
        setting_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myStartActivity(SettingMain.class);
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
    }
    private void myStartActivity(Class c){
        Intent intent = new Intent(this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}

