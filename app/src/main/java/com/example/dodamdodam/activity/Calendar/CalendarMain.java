package com.example.dodamdodam.activity.Calendar;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.dodamdodam.R;
import com.example.dodamdodam.activity.Login.BasicActivity;
import com.example.dodamdodam.activity.Question.QuestionMain;
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
import java.util.Calendar;
import java.util.Date;

public class CalendarMain extends BasicActivity {
    public String readDay = null;
    public String str = null;
    public CalendarView calendarView;
    public Button cha_Btn, del_Btn, save_Btn;
    public ImageButton question_Btn,album_Btn,setting_Btn,calendar_Btn;
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
    private int dday;
    public String ouranniversary;
    private TextView ddayTextView;
    private String daynow;
    private int dYear, dMonth, dDay;
    private String MYBIRTH, LOVERBIRTH;
    public int resultnum;
    public String MYNICK,LOVERNICK;
    public ConstraintLayout scroll_calendar;
    Dialog dilaog01; // 커스텀 다이얼로그
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_main);

        dilaog01 = new Dialog(CalendarMain.this);       // Dialog 초기화
        dilaog01.requestWindowFeature(Window.FEATURE_NO_TITLE); // 타이틀 제거
        dilaog01.setContentView(R.layout.dialog01);

        calendarView = findViewById(R.id.calendarView);
        diaryTextView = findViewById(R.id.diaryTextView);
        save_Btn = findViewById(R.id.save_Btn);
        del_Btn = findViewById(R.id.del_Btn);
        cha_Btn = findViewById(R.id.cha_Btn);
        todayText = findViewById(R.id.todaytext);

        loverText = findViewById(R.id.loverText);
        contextEditText = (EditText) findViewById(R.id.contextEditText);
        todayText.setVisibility(View.INVISIBLE);
        loverText.setVisibility(View.INVISIBLE);
        contextEditText.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent event) {
                // TODO Auto-generated method stub
                if (view.getId() ==R.id.contextEditText) {
                    view.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction()&MotionEvent.ACTION_MASK){
                        case MotionEvent.ACTION_UP:
                            view.getParent().requestDisallowInterceptTouchEvent(false);
                            break;
                    }
                }
                return false;
            }
        });
        question_Btn = findViewById(R.id.questionBtn);
        calendar_Btn=findViewById(R.id.calendarBtn2);
        album_Btn = findViewById(R.id.albumBtn);
        setting_Btn = findViewById(R.id.settingBtn);
        ddayTextView=findViewById(R.id.dday_TextView);
        ddayTextView.setVisibility(View.VISIBLE);
        //diaryTextView.setVisibility(View.VISIBLE);
        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Calendar");
        databaseReference2 = FirebaseDatabase.getInstance().getReference("Setting");
        userdatabaseReference = FirebaseDatabase.getInstance().getReference("users");
        loveruidReference = userdatabaseReference.child(user.getUid());
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("users").document(user.getUid());

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        LOVERUID = document.getData().get("lover").toString();
                        MYBIRTH = document.getData().get("birthday").toString();
                    } else {
                        Log.d(TAG, "없다없다없다없다");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        databaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {//이건 리얼타임
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("anniversary").child(user.getUid()).getValue()!=null){
                    ouranniversary = snapshot.child("anniversary").child(user.getUid()).getValue().toString();
                    long now = System.currentTimeMillis();
                    int ouranniversaryint = Integer.parseInt(ouranniversary);
                    dYear = ouranniversaryint/10000;
                    dMonth=(ouranniversaryint%10000)/100;
                    dDay = ouranniversaryint%100;
                    Calendar dCalendar = Calendar.getInstance();
                    dCalendar.set(dYear,dMonth-1,dDay);
                    long ddaytime = dCalendar.getTimeInMillis();
                    long dplusday=(now-ddaytime)/(24*60*60*1000);
                    resultnum = (int)dplusday+1;

                    if(LOVERUID!=null) {//이건 파이어스토어3
                        if (db.collection("users").document(LOVERUID) != null) {
                            DocumentReference docRef2 = db.collection("users").document(LOVERUID);
                            docRef2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task2) {

                                    if (task2.isSuccessful()) {
                                        DocumentSnapshot document2 = task2.getResult();
                                        if (document2.exists()) {
                                            LOVERBIRTH = document2.getData().get("birthday").toString();
                                            int dminusmy = dminusdayActivity(MYBIRTH);
                                            int dminuslover = dminusdayActivity(LOVERBIRTH);
                                            if(snapshot.child("lovernickname").child(user.getUid()).getValue()!=null){
                                                LOVERNICK = snapshot.child("lovernickname").child(user.getUid()).getValue().toString();
                                            }
                                            if(snapshot.child("mynickname").child(user.getUid()).getValue()!=null) {
                                                MYNICK = snapshot.child("mynickname").child(user.getUid()).getValue().toString();
                                            }
                                            ddayTextView.setText("우리가 만난지 "+"d+"+Integer.toString(resultnum)+
                                                    "일\n"+MYNICK+"의 생일 d"+dminusmy+"일\n"+LOVERNICK+"의 생일 d"
                                                    +dminuslover+"일");
                                            showToday();
                                        } else {
                                            Log.d(TAG, "없다없다없다없다");
                                            //ddayTextView.setText("상대방 생일1");

                                        }
                                    } else {
                                        Log.d(TAG, "get failed with ", task2.getException());
                                        //ddayTextView.setText("상대방 생일2");

                                    }
                                }
                            });
                        }
                    }
                    else{
                        //ddayTextView.setText("dfdf"+LOVERUID+MYBIRTH);
                    }

                }
                else{
                    ddayTextView.setText("설정에 가서 기념일을 입력해주십시오");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener()
        {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth)
            {
                loverText.setVisibility(View.VISIBLE);

                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month,dayOfMonth);
                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                if(month<9&&dayOfMonth<10) {
                    stringDateSelected = Integer.toString(year) +"0"+ Integer.toString(month + 1) +"0"+ Integer.toString(dayOfMonth);
                }
                else if (month>8 && dayOfMonth<10) {
                    stringDateSelected = Integer.toString(year) + Integer.toString(month + 1) +"0"+ Integer.toString(dayOfMonth);
                }
                else if (month<9 && dayOfMonth>9) {
                    stringDateSelected = Integer.toString(year) + "0"+Integer.toString(month + 1) + Integer.toString(dayOfMonth);
                }
                else {
                    stringDateSelected = Integer.toString(year) +Integer.toString(month + 1) + Integer.toString(dayOfMonth);
                }


                databaseReference.child(stringDateSelected).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.child(user.getUid()).getValue()!=null){
                            thisdayText = snapshot.child(user.getUid()).getValue().toString();
                            todayText.setText(snapshot.child(user.getUid()).getValue().toString());
                            contextEditText.setText(snapshot.child(user.getUid()).getValue().toString());
                            diaryTextView.setVisibility(View.VISIBLE);
                            save_Btn.setVisibility(View.INVISIBLE);
                            contextEditText.setVisibility(View.INVISIBLE);
                            todayText.setVisibility(View.VISIBLE);
                            cha_Btn.setVisibility(View.VISIBLE);
                            del_Btn.setVisibility(View.VISIBLE);
                            diaryTextView.setText(String.format("%d/%d/%d", year, month + 1, dayOfMonth));

                            ddayTextView.setVisibility(View.INVISIBLE);

                        }
                        else{
                            contextEditText.setText(null);
                            diaryTextView.setVisibility(View.VISIBLE);
                            save_Btn.setVisibility(View.VISIBLE);
                            contextEditText.setVisibility(View.VISIBLE);
                            todayText.setVisibility(View.INVISIBLE);
                            cha_Btn.setVisibility(View.INVISIBLE);
                            del_Btn.setVisibility(View.INVISIBLE);
                            diaryTextView.setText(String.format("%d/%d/%d", year, month + 1, dayOfMonth));
                            ddayTextView.setVisibility(View.INVISIBLE);




                        }

                        if(snapshot.child(LOVERUID).getValue()!=null){
                            loverText.setText(snapshot.child(LOVERUID).getValue().toString());
                            ddayTextView.setVisibility(View.INVISIBLE);

                        }
                        else{
                            loverText.setText("상대방의 일정이 비어있습니다.");
                            ddayTextView.setVisibility(View.INVISIBLE);

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
                ddayTextView.setVisibility(View.INVISIBLE);

                databaseReference.child(stringDateSelected).child(user.getUid()).setValue(contextEditText.getText().toString());
            }
        });
        todayText.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //showDialog(todayText.getText().toString());
                showDialog01(todayText.getText().toString());
            }
        });

        loverText.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //showDialog(loverText.getText().toString());
                showDialog01(loverText.getText().toString());
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
                ddayTextView.setVisibility(View.INVISIBLE);

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
                ddayTextView.setVisibility(View.INVISIBLE);

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

//    void showDialog(String str) {
//        AlertDialog.Builder msgBuilder = new AlertDialog.Builder(CalendarMain.this)
//                .setTitle("나의 일정")
//                .setMessage(str)
//                .setPositiveButton("닫기", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        //Toast.makeText(CalendarMain.this, "취소하였습니다", Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//        AlertDialog msgDlg = msgBuilder.create();
//        msgDlg.show();
//
//        TextView textView = (TextView) dialog.findViewById(android.R.id.message);
//        Typeface face = Typeface.createFromAsset(getAssets(),"font/font_1");
//        textView.setTypeface(face);
//
//    }


    private void myStartActivity(Class c){
        Intent intent = new Intent(this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void showToday(){
        long now2 = System.currentTimeMillis();
        Date date = new Date(now2);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String todayString = dateFormat.format(date);
        int todayint=Integer.parseInt(todayString);
        int tyear = todayint/10000;
        int tmonth = (todayint%10000)/100;
        int tday = todayint%100;
        diaryTextView.setText(tyear+"/"+tmonth+"/"+tday);
    }

    private int dminusdayActivity(String STR){
        long now2 = System.currentTimeMillis();
        //int todayint = (int)(now2/(24*60*60*1000));

        Date date = new Date(now2);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String todayString = dateFormat.format(date);
        int todayint=Integer.parseInt(todayString);

        int birthdayint = Integer.parseInt(STR);
        while(birthdayint<todayint){
            birthdayint=birthdayint+10000;
        }
        //todayText.setText(Integer.toString(birthdayint)+Integer.toString(todayint));
        int dYear2 = birthdayint/10000;
        int dMonth2=(birthdayint%10000)/100;
        int dDay2 = birthdayint%100;
        Calendar dCalendar2 = Calendar.getInstance();
        dCalendar2.set(dYear2,dMonth2-1,dDay2);
        long ddaytime2 = dCalendar2.getTimeInMillis();
        long dplusday2 = (now2-ddaytime2)/(24*60*60*1000);
        return (int)dplusday2;

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
    public void showDialog01(String str){
        TextView tmptext = dilaog01.findViewById(R.id.dialogtextView);
        tmptext.setMovementMethod(new ScrollingMovementMethod());
        tmptext.setText(str);
        dilaog01.show();
        dilaog01.findViewById(R.id.yesBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dilaog01.dismiss();          // 앱 종료
            }
        });
    }
}