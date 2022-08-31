package com.example.dodamdodam;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*로그아웃 화면 만들지 않기로 했으니 로그아웃 버튼은 만들지 않았지만 혹시 언제 쓸지 몰라 코드 남김 (3강 22:22)
        버튼을 만들고 리스너에 FirebaseAuth.getInstance().signOut(); 을 쓰고 로그아웃 후 어떻게 할지 코드 짜면 됨

        유튜브에서는 로그아웃 버튼을 따로 만들었기 때문에 로그아웃 시 signup 화면으로 넘어가게 만들어서
        signup 화면에서는 또다시 backpressed를 누르면 로그인한 화면이 나와 조치를 취해줬지만, 우리 앱에서는
        그럴 일이 없을 것 같아 구현하지 않음*/
        
        //로그인 되지 않았다면, 회원가입 화면으로 넘어가기

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user == null){
            myStartActivity(SignUpActivity.class);
        }
        else{
            for(UserInfo profile : user.getProviderData()){
                String name =profile.getDisplayName();
                if(name != null){
                    if(name.length()==0){
                        myStartActivity(MemberInitActivity.class);
                    }
                }
            }
        }

    }


    private void myStartActivity(Class c){
        Intent intent = new Intent(this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    
    //메인 화면에서 back 버튼을 누르면 앱이 종료되도록 설정
    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("도담도담 앱을 종료하시겠습니까?");
        builder.setPositiveButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setNegativeButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        builder.show();
    }
}