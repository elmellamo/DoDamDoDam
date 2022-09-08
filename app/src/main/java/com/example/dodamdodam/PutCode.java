package com.example.dodamdodam;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class PutCode extends AppCompatActivity {
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
        String lovercode = ((EditText)findViewById(R.id.lovercode)).getText().toString();
        if (lovercode.length() > 0) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference document = db.collection("users").document(lovercode);
            document.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        String otherlover = (String) documentSnapshot.get("lover");
                        if (otherlover == null) {
                            if (user != null) {
                                db.collection("users").document(user.getUid()).update("lover", lovercode);
                                db.collection("users").document(lovercode).update("lover", user.getUid());
                                startToast("짝꿍과 연결되었습니다!");
                                myStartActivity(MainActivity.class);
                            }
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

    private void myStartActivity(Class c){
        Intent intent = new Intent(this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

}
