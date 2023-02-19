package com.dodamdodam.dodamdodam.models;

import java.util.ArrayList;
import java.util.Date;

public class AlbumInfo {
    private String title;
    private ArrayList<String> contents;
    private String publisher;
    private Date createdAt;

    public AlbumInfo(String title,ArrayList<String> contents, String publisher, Date createdAt){
        this.title = title;
        this.contents = contents;
        this.publisher = publisher;
        this.createdAt = createdAt;
    }

    public String getTitle(){return  this.title;}
    public void setTitle(String title){
        this.title = title;
    }
    public Date getCreatedAt(){return  this.createdAt;}
    public void setCreatedAt(Date createdAt){
        this.createdAt = createdAt;
    }
    public String getPublisher(){return  this.publisher;}
    public void setPublisher(String publisher){
        this.publisher = publisher;
    }
    public ArrayList<String> getContents(){
        return this.contents;
    }
    public void setContents(ArrayList<String> contents){ this.contents = contents; }

}