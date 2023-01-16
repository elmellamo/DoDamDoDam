package com.example.dodamdodam.activity.Login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dodamdodam.R;
import com.example.dodamdodam.view.MemberInfo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class MemberInitActivity extends AppCompatActivity {
    private static final String TAG = "MemberInitActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_init);

        findViewById(R.id.checkBtn).setOnClickListener(onClickListener);
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("도담도담");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.checkBtn:
                    profileUpdate();
                    break;
            }
        }
    };

    private void profileUpdate() {
        String name = ((EditText) findViewById(R.id.nameEditText)).getText().toString();
        final String birthDay = ((EditText) findViewById(R.id.birthDayEditText)).getText().toString();

        if (name.length()>0 && birthDay.length()>5) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            MemberInfo memberInfo = new MemberInfo(name, birthDay, null);

            if(user!=null){
                db.collection("users").document(user.getUid()).set(memberInfo)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                //myStartActivity(FindLover.class);
                                startToast("회원정보 등록 성공!");
                                myStartActivity(FindLover.class);
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                startToast("회원정보 등록 실패 ㅠ");
                                Log.w(TAG, "Error writing document", e);
                            }
                        });
            }else{
                startToast("회원정보를 입력해주세요.");
            }


//            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
//                    .setDisplayName(name)
//                    .build();
//            if(user!=null){
//                user.updateProfile(profileUpdates)
//                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                                if(task.isSuccessful()){
//                                    startToast("회원정보 등록에 성공하였습니다.");
//                                    finish();
//                                }
//                            }
//                        });
//                }



            } else {
                startToast("회원정보를 입력해주세요.");
            }
    }

    private void myStartActivity(Class c){
        Intent intent = new Intent(this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void startToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}