package com.example.dodamdodam.models;

public class DetailInfo {
    private String postId;
    private int postCnt;

    public DetailInfo(String postId, int postCnt){
        this.postId = postId;
        this.postCnt = postCnt;
    }

    public String getPostId(){return  this.postId;}
    public void setPostId(String postId){
        this.postId = postId;
    }

    public int getPostCnt(){return  this.postCnt;}
    public void setPostCnt(int postCnt){
        this.postCnt = postCnt;
    }

}
