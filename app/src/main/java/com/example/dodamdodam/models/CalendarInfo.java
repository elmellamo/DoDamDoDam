package com.example.dodamdodam.models;

public class CalendarInfo {
    private String contents;
    private String publisher;
    private String publisherlover;

    public CalendarInfo(String contents, String publisher, String lover){
        this.contents = contents;
        this.publisher = publisher;
        this.publisherlover = publisherlover;
    }

    public String getContents(){

        return this.contents;
    }
    public void setContents(String contents){

        this.contents = contents;
    }

    public String getPublisher(){
        return this.publisher;
    }
    public void setPublisher(String publisher){ this.publisher = publisher; }

    public String getPublisherlover(){ return this.publisherlover; }
    public void setPublisherlover(String publisherlover){ this.publisherlover = publisherlover; }

}