package com.example.dodamdodam.view;

public class MemberInfo {
    private String name;
    private String birthday;
    private String lover;

    public MemberInfo(String name, String birthday, String lover){
        this.name = name;
        this.birthday = birthday;
        this.lover = lover;
    }

    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getBirthday(){
        return this.birthday;
    }
    public void setBirthday(String birthday){ this.birthday = birthday; }

    public String getLover(){ return this.lover; }
    public void setLover(String lover){ this.lover = lover; }

}
