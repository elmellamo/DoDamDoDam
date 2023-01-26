package com.example.dodamdodam.activity;

import android.app.Application;

public class FlagClass extends Application {
    private static String ourAnniversary;
    private static String myNickname;
    private static String loverNickname;

    @Override
    public void onCreate() {
        super.onCreate();
        myNickname = "";
    }

    public void setOurAnniversary(String flag){this.ourAnniversary = flag;}
    public String getOurAnniversary(){return ourAnniversary;}



   public void setmyNickname(String flag){this.myNickname = flag;}

    public String getmyNickname(){return myNickname;}


    public void setLoverNickname(String flag){this.loverNickname = flag;}

    public String getLoverNickname(){return loverNickname;}
}
