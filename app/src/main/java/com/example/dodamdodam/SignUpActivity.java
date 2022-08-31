package com.example.dodamdodam;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private static final String TAG = "SignUpActivity"; //앱에 뜨게 하려고

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance(); //유저를 받아오기 위해서

        findViewById(R.id.signUpBtn).setOnClickListener(onClickListener);
        findViewById(R.id.goto_login).setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.signUpBtn:
                    signUp();
                    break;
                case R.id.goto_login:
                    StartLoginActivity();
                    break;
            }
        }
    };

    private void signUp() { //회원 가입 로직을 처리하는 함수
        String email = ((EditText) findViewById(R.id.emailEditText)).getText().toString();
        //editText 붙인 이유 : getText를 사용하려면 일반 View는 getText를 사용 못함
        //editText나 textView에서만 사용 가능 > 그래서 형변환 해줌
        String password = ((EditText) findViewById(R.id.passwordEditText)).getText().toString();
        String passwordCheck = ((EditText) findViewById(R.id.passwordCheckEditText)).getText().toString();

        if (email.length() > 0 && password.length() > 0 && passwordCheck.length() > 0) {
            if (password.equals(passwordCheck)) {


                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) { //회원가입 성공시
                                    startToast("회원가입 성공!");
                                } else { //회원가입 실패시
                                    if (task.getException() != null) {
                                        startToast(task.getException().toString());
                                    }
                                }
                            }
                        });
            } else {
                startToast("비밀번호가 일치하지 않습니다.");
            }
        }
        else {
            startToast("이메일 또는 비밀번호를 입력해 주세요.");
        }
    }

    //리스너에서는 인텐트 못 걸어줘서 따로 함수 만드는 것
    private void StartLoginActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void startToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
    //mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener에서는 Toast.makeToast 사용 못해서 함수 선언
}
