package com.example.tcp.testdome;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.tcp.testdome.adapter.VidoeAdapter;
import com.local.resources.LocalResource;
import com.local.resources.bean.InfoBean;
import com.local.resources.bean.VideoInfo;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2017/3/6.
 */

public class VideoListActivity extends AppCompatActivity implements LocalResource.GetAlbumList {
    private final static int R_GAVE = 1;
    private GridView voide_itemImage;
    private ArrayList<VideoInfo> videoInfoList;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_video_list);
        voide_itemImage = (GridView)findViewById(R.id.voide_itemImage);
        setData();
    }


    protected void setData() {
        if(!startGave(R_GAVE,Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            LocalResource localResource = new LocalResource(VideoListActivity.this.getApplicationContext(),this);
            localResource.execute(LocalResource.VOIDE);
        }
    }

    private void setVoideItem(){
        VidoeAdapter vidoeAdapter = new VidoeAdapter(VideoListActivity.this,videoInfoList) ;
        voide_itemImage.setAdapter(vidoeAdapter);
        voide_itemImage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String filePath = videoInfoList.get(position).getPath();
                Toast.makeText(VideoListActivity.this,""+filePath,Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==R_GAVE&&grantResults[0] == PackageManager.PERMISSION_GRANTED){
            LocalResource localResource = new LocalResource(VideoListActivity.this.getApplicationContext(),this);
            localResource.execute(LocalResource.VOIDE);
        }else if(requestCode==R_GAVE&&grantResults[0] == PackageManager.PERMISSION_DENIED){
           Toast.makeText(VideoListActivity.this,"请开启相关权限",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public <T extends InfoBean> void getAlbumList(List<T> list) {
        videoInfoList = (ArrayList<VideoInfo>) list;
        new Thread(runnable).start();
    }

    private Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            setVoideItem();
        }
    };

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if(videoInfoList!=null){
                for(int i =0;i<videoInfoList.size();i++){
                    Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(videoInfoList.get(i).getPath(), MediaStore.Video.Thumbnails.MINI_KIND);

                    if(bitmap==null){
                        videoInfoList.remove(i);
                    }else
                        videoInfoList.get(i).setBitmap( bitmap );
                }
                handler.obtainMessage().sendToTarget();
            }
        }
    };


    /**
     * 权限管理
     * **/
    public boolean startGave(int R_GAVE,String ...key){
        if(Build.VERSION.SDK_INT <23){
            return false;
        }
        List<String> list = new ArrayList<>();
        String [] p;
        if(key!=null){
            for(String permission:key){
                if(checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED)
                {
                    list.add(permission);
                }
            }
        }
        if(list!=null&&list.size()>0){
            p= new String[list.size()];
            for(int i =0;i<list.size();i++){
                p[i] = list.get(i);
            }
            this.requestPermissions(p,R_GAVE);
            list.clear();
            return true;
        }else return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
        handler = null;
        if(videoInfoList!=null){
            for(VideoInfo videoInfo:videoInfoList){
                Bitmap bitmap = videoInfo.getBitmap();
                if(bitmap!=null){
                    bitmap.recycle();
                    bitmap=null;
                }
            }
            videoInfoList.clear();
            videoInfoList = null;
            System.gc();
        }
    }
}
