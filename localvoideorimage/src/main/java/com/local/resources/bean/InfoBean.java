package com.local.resources.bean;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/2.
 */

public class InfoBean implements Serializable {
    protected int id;
    protected String title;
    protected String path;
    protected String album;
    protected int albumId ;
    protected Bitmap bitmap;//当前缩略图


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public int getAlbumId() {
        return albumId;
    }

    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }
    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
    public Bitmap getBitmap() {
        return bitmap;
    }

}
