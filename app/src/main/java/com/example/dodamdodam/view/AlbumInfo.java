package com.example.dodamdodam.view;

public class AlbumInfo {
    private String title;
    private String contents;
    private String publisher;

    public AlbumInfo(String title, String contents, String publisher){
        this.title = title;
        this.contents = contents;
        this.publisher = publisher;
    }

    public String getTitle(){return  this.title;}
    public void setTitle(String title){
        this.title = title;
    }
    public String getPublisher(){return  this.publisher;}
    public void setPublisher(String publisher){
        this.publisher = publisher;
    }
    public String getContents(){
        return this.contents;
    }
    public void setContents(String contents){ this.contents = contents; }

}
