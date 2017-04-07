package com.local.resources.bean;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/2.
 * video information
 * 视频相关信息
 */

    public class VideoInfo extends InfoBean implements Serializable {
        private static final long serialVersionUID = -7920222595800367956L;

        private String artist;
        private String displayName;
        private String mimeType;
        private long size;
        private long duration;



    public VideoInfo(int id, String title, String album, String artist, String displayName, String mimeType,
                     String path, long size, long duration) {
            super();
            this.id = id;

            this.title = title;
            this.album = album;
            this.artist = artist;
            this.displayName = displayName;
            this.mimeType = mimeType;
            this.path = path;
            this.size = size;
            this.duration = duration;
        }

        public String getArtist() {
            return artist;
        }
        public void setArtist(String artist) {
            this.artist = artist;
        }
        public String getDisplayName() {
            return displayName;
        }
        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }
        public String getMimeType() {
            return mimeType;
        }
        public void setMimeType(String mimeType) {
            this.mimeType = mimeType;
        }
        public long getSize() {
            return size;
        }
        public void setSize(long size) {
            this.size = size;
        }
        public long getDuration() {
            return duration;
        }
        public void setDuration(long duration) {
            this.duration = duration;
        }
        public static long getSerialversionuid() {
            return serialVersionUID;
        }

    }
