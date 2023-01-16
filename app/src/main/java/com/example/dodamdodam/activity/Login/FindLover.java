package com.example.dodamdodam.activity.Login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dodamdodam.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FindLover extends BasicActivity {
    TextView myUid;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_lover);

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("도담도담");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


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
                    shareMyUid();
                    break;
                case R.id.loverbtn:
                    myStartActivity(PutCode.class);
                    break;
            }
        }
    };

    private void shareMyUid(){
        if(user!=null){
            String uid = user.getUid();
            Intent Sharing_intent = new Intent(Intent.ACTION_SEND);
            Sharing_intent.setType("text/plain");

            String Test_Message = uid;

            Sharing_intent.putExtra(Intent.EXTRA_TEXT, Test_Message);

            Intent Sharing = Intent.createChooser(Sharing_intent, "공유하기");
            startActivity(Sharing);
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