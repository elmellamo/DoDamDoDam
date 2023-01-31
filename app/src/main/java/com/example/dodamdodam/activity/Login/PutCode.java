package com.example.dodamdodam.activity.Login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dodamdodam.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PutCode extends BasicActivity {
    TextView myUid;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_put_code);
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

                            myStartActivity(MainActivity.class);
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
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    private void myStartActivity(Class c){
        Intent intent = new Intent(this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

}