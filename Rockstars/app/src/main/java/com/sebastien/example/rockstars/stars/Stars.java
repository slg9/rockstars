package com.sebastien.example.rockstars.stars;

import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * Created by Sebastien on 26/03/2016.
 */
public class Stars {


    int id;
    String picture;
    String fullname;
    String status;
    Boolean bookmark;

    public Stars(int id,String picture, String fullname, String status, Boolean bookmark) {
        this.id=id;
        this.picture = picture;
        this.fullname = fullname;
        this.status = status;
        this.bookmark = bookmark;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        status = status;
    }

    public Boolean getBookmark() {
        return bookmark;
    }

    public void setBookmark(Boolean bookmark) {
        this.bookmark = bookmark;
    }


}
