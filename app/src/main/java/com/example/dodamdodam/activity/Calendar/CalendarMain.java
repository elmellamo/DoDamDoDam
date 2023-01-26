package com.example.dodamdodam.activity.Calendar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import com.example.dodamdodam.R;
import com.example.dodamdodam.activity.Login.BasicActivity;
import com.example.dodamdodam.activity.Login.SignUpActivity;
import com.example.dodamdodam.activity.Setting.SettingMain;
import com.example.dodamdodam.activity.album.AlbumMain;
import com.example.dodamdodam.activity.Question.QuestionMain;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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


import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;

import androidx.annotation.NonNull;


public class CalendarMain extends BasicActivity {
    public String readDay = null;
    public String str = null;
    public CalendarView calendarView;
    public Button cha_Btn, del_Btn, save_Btn;
    public ImageButton question_Btn,album_Btn,setting_Btn;
    public TextView diaryTextView, todayText, loverText;
    public EditText contextEditText;
    private String stringDateSelected;
    private DatabaseReference databaseReference,databaseReference2;
    private FirebaseUser user;
    private DatabaseReference userdatabaseReference;
    private DatabaseReference loveruidReference;
    private DatabaseReference loveruidcodeReference;
    private String LOVERUID;
    private String thisdayText;
    private String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_main);
        calendarView = findViewById(R.id.calendarView);
        diaryTextView = findViewById(R.id.diaryTextView);
        save_Btn = findViewById(R.id.save_Btn);
        del_Btn = findViewById(R.id.del_Btn);
        cha_Btn = findViewById(R.id.cha_Btn);
        todayText = findViewById(R.id.todaytext);
        loverText = findViewById(R.id.loverText);
        contextEditText = findViewById(R.id.contextEditText);
        question_Btn = findViewById(R.id.questionBtn);
        album_Btn = findViewById(R.id.albumBtn);
        setting_Btn = findViewById(R.id.settingBtn);

        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Calendar");
        databaseReference2 = FirebaseDatabase.getInstance().getReference("Question");
        userdatabaseReference = FirebaseDatabase.getInstance().getReference("users");
        loveruidReference = userdatabaseReference.child(user.getUid());
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        //LOVERUID = db.collection("users").document(user.getUid()).collection("lover").get().toString();

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
        });




        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener()
        {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth)
            {
                stringDateSelected=Integer.toString(year)+Integer.toString(month+1)+Integer.toString(dayOfMonth);
                databaseReference.child(stringDateSelected).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.child(user.getUid()).getValue()!=null){
                            thisdayText = snapshot.child(user.getUid()).getValue().toString();
                            todayText.setText(snapshot.child(user.getUid()).getValue().toString());
                            diaryTextView.setVisibility(View.VISIBLE);
                            save_Btn.setVisibility(View.INVISIBLE);
                            contextEditText.setVisibility(View.INVISIBLE);
                            todayText.setVisibility(View.VISIBLE);
                            cha_Btn.setVisibility(View.VISIBLE);
                            del_Btn.setVisibility(View.VISIBLE);
                            diaryTextView.setText(String.format("%d / %d / %d", year, month + 1, dayOfMonth));
                            //todayText.setText(snapshot.getKey().toString());
                        }
                        else{
                            contextEditText.setText(null);
                            diaryTextView.setVisibility(View.VISIBLE);
                            save_Btn.setVisibility(View.VISIBLE);
                            contextEditText.setVisibility(View.VISIBLE);
                            todayText.setVisibility(View.INVISIBLE);
                            cha_Btn.setVisibility(View.INVISIBLE);
                            del_Btn.setVisibility(View.INVISIBLE);
                            diaryTextView.setText(String.format("%d / %d / %d", year, month + 1, dayOfMonth));
                        }

                        if(snapshot.child(LOVERUID).getValue()!=null){
                            loverText.setText(snapshot.child(LOVERUID).getValue().toString());
                        }
                        else{
                            loverText.setText("상대방의 일정이 비어있습니다.");
                             }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



            }
        });
        save_Btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                str = contextEditText.getText().toString();
                todayText.setText(str);
                save_Btn.setVisibility(View.INVISIBLE);
                cha_Btn.setVisibility(View.VISIBLE);
                del_Btn.setVisibility(View.VISIBLE);
                contextEditText.setVisibility(View.INVISIBLE);
                todayText.setVisibility(View.VISIBLE);
                todayText.setMovementMethod(new ScrollingMovementMethod());

                databaseReference.child(stringDateSelected).child(user.getUid()).setValue(contextEditText.getText().toString());
            }
        });

        cha_Btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                contextEditText.setVisibility(View.VISIBLE);
                todayText.setVisibility(View.INVISIBLE);
                contextEditText.setText(thisdayText);
                save_Btn.setVisibility(View.VISIBLE);
                cha_Btn.setVisibility(View.INVISIBLE);
                del_Btn.setVisibility(View.INVISIBLE);
                todayText.setText(contextEditText.getText());
            }

        });
        del_Btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                todayText.setVisibility(View.INVISIBLE);
                contextEditText.setText(null);
                contextEditText.setVisibility(View.VISIBLE);
                save_Btn.setVisibility(View.VISIBLE);
                cha_Btn.setVisibility(View.INVISIBLE);
                del_Btn.setVisibility(View.INVISIBLE);
                databaseReference.child(stringDateSelected).child(user.getUid()).setValue(null);
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
    }



    private void myStartActivity(Class c){
        Intent intent = new Intent(this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

}