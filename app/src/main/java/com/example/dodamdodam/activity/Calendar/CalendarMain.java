package com.example.dodamdodam.activity.Calendar;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.dodamdodam.R;
import com.example.dodamdodam.activity.Login.BasicActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.annotation.SuppressLint;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;

import androidx.annotation.NonNull;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Calendar;

public class CalendarMain extends BasicActivity {
    public String readDay = null;
    public String str = null;
    public CalendarView calendarView;
    public Button cha_Btn, del_Btn, save_Btn;
    public TextView diaryTextView, todayText;
    public EditText contextEditText;
    private String stringDateSelected;
    private DatabaseReference databaseReference;
    private FirebaseUser user;

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
        contextEditText = findViewById(R.id.contextEditText);
        databaseReference = FirebaseDatabase.getInstance().getReference("Calendar");

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener()
        {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth)
            {
                stringDateSelected=Integer.toString(year)+Integer.toString(month+1)+Integer.toString(dayOfMonth);
                databaseReference.child(stringDateSelected).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.getValue()!=null){
                            todayText.setText(snapshot.getValue().toString());
                            diaryTextView.setVisibility(View.VISIBLE);
                            save_Btn.setVisibility(View.INVISIBLE);
                            contextEditText.setVisibility(View.INVISIBLE);
                            todayText.setVisibility(View.VISIBLE);
                            cha_Btn.setVisibility(View.VISIBLE);
                            del_Btn.setVisibility(View.VISIBLE);
                            diaryTextView.setText(String.format("%d / %d / %d", year, month + 1, dayOfMonth));

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
                databaseReference.child(stringDateSelected).setValue(contextEditText.getText().toString());
            }
        });

        cha_Btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                contextEditText.setVisibility(View.VISIBLE);
                todayText.setVisibility(View.INVISIBLE);
                contextEditText.setText(str);

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
                contextEditText.setText("");
                contextEditText.setVisibility(View.VISIBLE);
                save_Btn.setVisibility(View.VISIBLE);
                cha_Btn.setVisibility(View.INVISIBLE);
                del_Btn.setVisibility(View.INVISIBLE);
                databaseReference.child(stringDateSelected).setValue(null);
            }
        });

    }

}