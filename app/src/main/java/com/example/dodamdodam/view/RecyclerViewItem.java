package com.example.dodamdodam.view;

import android.net.Uri;

import java.util.Date;

public class RecyclerViewItem {
    private Uri galleryuri;
    private String publisher;
    private Date mcreatedAt;

    public RecyclerViewItem(){

    }

    public RecyclerViewItem(Uri galleryuri, String publisher, Date mcreatedAt){
        this.galleryuri = galleryuri;
        this.publisher = publisher;
        this.mcreatedAt = mcreatedAt;
    }

    public  Uri getGalleryuri(){return  this.galleryuri;}
    public void setGalleryuri(Uri galleryuri){this.galleryuri = galleryuri;}

    public String getPublisher() {return this.publisher;}
    public void setPublisher(String publisher) {this.publisher = publisher;}

    public Date getMcreatedAt(){return this.mcreatedAt;}
    public void setMcreatedAt(Date mcreatedAt){this.mcreatedAt = mcreatedAt;}

}