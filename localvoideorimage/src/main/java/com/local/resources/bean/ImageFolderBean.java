package com.local.resources.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/2.
 * top-level  -Album
 * 最顶层的相册即文件夹
 */

public class ImageFolderBean extends InfoBean implements Serializable {
    public ImageFolderBean(String album, int albumId){
        this.album = album;
        this.albumId = albumId;
    }

    public ArrayList<ImageBean> getImageBeanItemArrayList() {
        return imageBeanItemArrayList;
    }

    public void setImageBeanItemArrayList(ArrayList<ImageBean> imageBeanItemArrayList) {
        this.imageBeanItemArrayList = imageBeanItemArrayList;
    }

    private ArrayList<ImageBean> imageBeanItemArrayList;

}
