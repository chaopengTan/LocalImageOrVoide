package com.local.resources.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/2.
 *
 * Picture information
 *
 * 每一个图片的信息
 */

public class ImageBean extends InfoBean implements Serializable {
    public ImageBean(int id, String title, String path, String album, int albumId){
        this.id = id;
        this.title = title;
        this.path = path;
        this.album = album;
        this.albumId = albumId;
    }
}
