package com.example.dodamdodam.models;

import android.net.Uri;

import java.util.ArrayList;
import java.util.Date;

public class RecyclerViewItem {
    private ArrayList<Uri> galleryuri;
    private String publisher;
    private Date mcreatedAt;

    public RecyclerViewItem(){

    }

    public RecyclerViewItem(ArrayList<Uri> galleryuri, String publisher, Date mcreatedAt){
        this.galleryuri = galleryuri;
        this.publisher = publisher;
        this.mcreatedAt = mcreatedAt;
    }

    public  ArrayList<Uri> getGalleryuri(){return  this.galleryuri;}
    public void setGalleryuri(ArrayList<Uri> galleryuri){this.galleryuri = galleryuri;}

    public String getPublisher() {return this.publisher;}
    public void setPublisher(String publisher) {this.publisher = publisher;}

    public Date getMcreatedAt(){return this.mcreatedAt;}
    public void setMcreatedAt(Date mcreatedAt){this.mcreatedAt = mcreatedAt;}

}