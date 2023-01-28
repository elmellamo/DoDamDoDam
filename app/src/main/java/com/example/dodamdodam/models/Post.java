package com.example.dodamdodam.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Post implements Parcelable {
    private String caption;
    private String date_created;
    private List<String> image_path;
    private String post_id;
    private String user_id;

    public Post() {
    }

    public Post(String caption, String date_created, List<String> image_path, String post_id,
                 String user_id) {
        this.caption = caption;
        this.date_created = date_created;
        this.image_path = image_path;
        this.post_id = post_id;
        this.user_id = user_id;
    }

    protected Post(Parcel in) {
        caption = in.readString();
        date_created = in.readString();
        in.readStringList(image_path);
        post_id = in.readString();
        user_id = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(caption);
        dest.writeString(date_created);
        dest.writeStringList(image_path);
        dest.writeString(post_id);
        dest.writeString(user_id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Post> CREATOR = new Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };

    public static Creator<Post> getCREATOR() {
        return CREATOR;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }

    public List<String> getImage_path() {
        return image_path;
    }

    public void setImage_path(List<String> image_path) {
        this.image_path = image_path;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }


    public String getUser_id() { return user_id; }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    @Override
    public String toString() {

        return "Post{" +
                "caption='" + caption + '\'' +
                ", date_created='" + date_created + '\'' +
                ", image_path='" + image_path + '\'' +
                ", post_id='" + post_id + '\'' +
                ", user_id='" + user_id +
                '}';
    }
}
