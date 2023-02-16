package com.dodamdodam.dodamdodam.activity.Login;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.dodamdodam.dodamdodam.R;
import com.dodamdodam.dodamdodam.activity.Question.QuestionMain;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends BasicActivity {
    private FirebaseAuth mAuth;
    private EditText idEditText, passwordEditText;
    private Button checkBtn;
    private String email_text, password_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //idEditText=findViewById(R.id.emailEditText);
        //passwordEditText=findViewById(R.id.passwordEditText);
        checkBtn = findViewById(R.id.checkBtn);
        TextInputLayout textInputLayout = findViewById(R.id.emailEditText);
        //textInputLayout.getEditText().setText("ur text");
        TextInputLayout textInputLayout2 = findViewById(R.id.passwordEditText);
        password_text = textInputLayout2.getEditText().getText().toString();

        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser()!=null){
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            DocumentReference document = db.collection("users")
                    .document(user.getUid());
            document.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                String mylover = (String) documentSnapshot.get("lover");
                                String myBirth = (String)documentSnapshot.get("birthday");
                                if(myBirth==null){
                                    myStartActivity(MemberInitActivity.class);
                                    startToast("회원정보를 입력해주세요");
                                }
                                else {
                                    if (mylover == null) {

                                        myStartActivity(FindLover.class);
                                    } else {
                                        myStartActivity(QuestionMain.class);
                                    }
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
        findViewById(R.id.checkBtn).setOnClickListener(onClickListener);
        findViewById(R.id.gotoPasswordResetBtn).setOnClickListener(onClickListener);
        findViewById(R.id.signupBtn).setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.checkBtn:
                    login();
                    break;
                case R.id.gotoPasswordResetBtn:
                    myStartActivity(PasswordResetActivity.class);
                    break;
                case R.id.signupBtn:
                    myStartActivity(SignUpActivity.class);
                    break;
            }
        }
    };

    private void login() {
        TextInputLayout textInputLayout = findViewById(R.id.emailEditText);
        String email = textInputLayout.getEditText().getText().toString();
        //String email = ((EditText) findViewById(R.id.textinput_edittext)).getText().toString();
        TextInputLayout textInputLayout2 = findViewById(R.id.passwordEditText);
        String password = textInputLayout2.getEditText().getText().toString();

        if (email.length() > 0 && password.length() > 0) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                startToast("로그인 성공!");


                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                DocumentReference document = db.collection("users")
                                        .document(user.getUid());
                                document.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                if (documentSnapshot.exists()) {
                                                    String mylover = (String) documentSnapshot.get("lover");
                                                    String myBirth = (String)documentSnapshot.get("birthday");
                                                    if(myBirth==null){
                                                        myStartActivity(MemberInitActivity.class);
                                                        startToast("회원정보를 입력해주세요");
                                                    }
                                                    else {
                                                        if (mylover == null) {
                                                            myStartActivity(FindLover.class);
                                                        } else {
                                                            myStartActivity(QuestionMain.class);
                                                        }
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




                            } else {
                                if (task.getException() != null) {
                                    startToast(task.getException().toString());
                                }
                            }
                        }
                    });
        } else {
            startToast("이메일 또는 비밀번호 입력해주세요!");
        }
    }



    private void startToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    //한번 로그인을 하였다면, signup login 등 이전 히스토리가 안 남게 하고 이제 backpressed를 한 번 해도 이전 로그인, 회원가입 화면이 뜨지 않고
    //바로 앱이 종료되도록 하는 것이 addFlags에 FLAG_ACTIVITY_CLEAR_TOP의 역할
    private void myStartActivity(Class c){
        Intent intent = new Intent(this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setMessage("도담도담 앱을 종료하시겠습니까?");
        builder.setPositiveButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setNegativeButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                moveTaskToBack(true); // 태스크를 백그라운드로 이동
                finishAndRemoveTask(); // 액티비티 종료 + 태스크 리스트에서 지우기
                finish();
            }
        });
        builder.show();
    }
}