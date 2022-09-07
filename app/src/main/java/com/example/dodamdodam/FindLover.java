package com.example.dodamdodam;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

public class FindLover extends AppCompatActivity {
    TextView myUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_lover);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        myUid = (TextView)findViewById(R.id.myUID);

        if(user!=null){
                String uid = user.getUid();
                myUid.setText(uid);
        }



        findViewById(R.id.sharebtn).setOnClickListener(onClickListener);
        findViewById(R.id.loverbtn).setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.sharebtn:
                    //공유 창은 일단 구현 안 하는걸로
                    break;
                case R.id.loverbtn:
                    myStartActivity(PasswordResetActivity.class);
                    break;
            }
        }
    };

    private void myStartActivity(Class c){
        Intent intent = new Intent(this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
