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

public class FindLover extends AppCompatActivity {
    TextView myUid;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_lover);

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
                    putLoverCode();
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

    private void putLoverCode(){
        Dialog dialog = new Dialog(FindLover.this, android.R.style.Theme_Material_Light_Dialog);
        dialog.setContentView(R.layout.dialog_put_code);
        //커스텀 다이얼로그
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        //다이얼로그 크기 조절하기
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes((WindowManager.LayoutParams) params);

        String lovercode = ((EditText)findViewById(R.id.lovercode)).getText().toString();

        Button btn_ok = dialog.findViewById(R.id.btn_ok);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(lovercode.length()>0){
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference document = db.collection("users").document(lovercode);
                document.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            String otherlover = (String)documentSnapshot.get("lover");
                            if(otherlover == null){
                                if(user!=null){
                                db.collection("users").document(user.getUid()).update("lover", lovercode);
                                db.collection("users").document(lovercode).update("lover", user.getUid());
                                startToast("짝꿍과 연결되었습니다!");
                                myStartActivity(MainActivity.class);}
                            }
                            else{
                                startToast("해당 짝꿍은 연결되어 있는 사람이 있어요.");
                            }
                        }
                        else
                            startToast("짝꿍이 존재하지 않아요.");
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                startToast("Failed to fetch");
                            }
                        });
                dialog.dismiss();//dialog 종료
            }}
            });
        dialog.show();
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
