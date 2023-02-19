package com.dodamdodam.dodamdodam.activity.Login;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.dodamdodam.dodamdodam.R;
import com.dodamdodam.dodamdodam.models.MemberInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private static final String TAG = "SignUpActivity"; //앱에 뜨게 하려고
    private EditText emailEditText,passwordEditText,passwordCheckEditText;
    private Button signUpBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        passwordCheckEditText = findViewById(R.id.passwordCheckEditText);
        signUpBtn = findViewById(R.id.signUpBtn);
        emailEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if((keyEvent.getAction()==keyEvent.ACTION_DOWN)&& i == KeyEvent.KEYCODE_ENTER){
                    passwordEditText.performClick();
                    return true;
                }
                return false;
            }
        });

        passwordEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if((keyEvent.getAction()==keyEvent.ACTION_DOWN)&& i == KeyEvent.KEYCODE_ENTER){
                    passwordCheckEditText.performClick();
                    return true;
                }
                return false;
            }
        });
        passwordCheckEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if((keyEvent.getAction()==keyEvent.ACTION_DOWN)&& i == KeyEvent.KEYCODE_ENTER){
                    signUpBtn.performClick();
                    return true;
                }
                return false;
            }
        });



        mAuth = FirebaseAuth.getInstance(); //유저를 받아오기 위해서

        findViewById(R.id.signUpBtn).setOnClickListener(onClickListener);

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.signUpBtn:
                    signUp();
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


                                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    DocumentReference document = db.collection("users")
                                            .document(user.getUid());
                                    MemberInfo memberInfo = new MemberInfo(null, null, null);

                                    db.collection("users").document(user.getUid()).set(memberInfo);


                                    myStartActivity(MemberInitActivity.class);
                                    finish();
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

    private void myStartActivity(Class c){
        Intent intent = new Intent(this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void startToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

//    @Override
//    public void onBackPressed() {
//        final AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
//        builder.setMessage("도담도담 앱을 종료하시겠습니까?");
//        builder.setPositiveButton("아니오", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//
//            }
//        });
//        builder.setNegativeButton("네", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                moveTaskToBack(true); // 태스크를 백그라운드로 이동
//                finishAndRemoveTask(); // 액티비티 종료 + 태스크 리스트에서 지우기
//                android.os.Process.killProcess(android.os.Process.myPid()); // 앱 프로세스 종료
//            }
//        });
//        builder.show();
//    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}