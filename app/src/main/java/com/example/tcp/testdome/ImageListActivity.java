package com.example.tcp.testdome;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.example.tcp.testdome.adapter.ImageFolderAdapter;
import com.example.tcp.testdome.adapter.VidoeAdapter;
import com.local.resources.LocalResource;
import com.local.resources.bean.ImageFolderBean;
import com.local.resources.bean.InfoBean;
import com.local.resources.bean.VideoInfo;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2017/3/6.
 */

public class ImageListActivity extends AppCompatActivity implements LocalResource.GetAlbumList {
    private final static int R_GAVE = 1;
    private GridView imageFolder,image;
    private ArrayList<ImageFolderBean> imageFolderBeen;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_imagelist);
        imageFolder = (GridView)findViewById(R.id.imageFolder);
        image = (GridView)findViewById(R.id.image);
        setData();
    }


    protected void setData() {
        if(!startGave(R_GAVE,Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            LocalResource localResource = new LocalResource(ImageListActivity.this.getApplicationContext(),this);
            localResource.execute(LocalResource.IMAGE);
        }
    }

    ImageFolderAdapter imageAdapter;
    private void setVoideItem(){
        ImageFolderAdapter imageFolderAdapter = new ImageFolderAdapter(ImageListActivity.this,imageFolderBeen,true) ;
        imageFolder.setAdapter(imageFolderAdapter);
        imageFolder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(imageAdapter==null){
                    imageAdapter = new ImageFolderAdapter(ImageListActivity.this,imageFolderBeen.get(position).getImageBeanItemArrayList(),false);
                    image.setAdapter(imageAdapter);
                }else{
                    imageAdapter.imageBeen = imageFolderBeen.get(position).getImageBeanItemArrayList();
                    imageAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==R_GAVE&&grantResults[0] == PackageManager.PERMISSION_GRANTED){
            LocalResource localResource = new LocalResource(ImageListActivity.this.getApplicationContext(),this);
            localResource.execute(LocalResource.VOIDE);
        }else if(requestCode==R_GAVE&&grantResults[0] == PackageManager.PERMISSION_DENIED){
           Toast.makeText(ImageListActivity.this,"请开启相关权限",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public <T extends InfoBean> void getAlbumList(List<T> list) {
        imageFolderBeen = (ArrayList<ImageFolderBean>) list;
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
            if(imageFolderBeen!=null){
                BitmapFactory.Options opts = new BitmapFactory.Options();
                opts.inSampleSize = 2;
                for(int i =0;i<imageFolderBeen.size();i++){
                    ImageFolderBean imageFolderBean = imageFolderBeen.get(i);
                    Bitmap bitmap = BitmapFactory.decodeFile(imageFolderBean.getPath(), opts);
                    if(bitmap!=null){
                        imageFolderBean.setBitmap(bitmap);
                        for(int a = 0;a<imageFolderBean.getImageBeanItemArrayList().size();a++){
                            Log.d("Path",imageFolderBean.getImageBeanItemArrayList().get(a).getPath());
                            Bitmap bitmaps = BitmapFactory.decodeFile(imageFolderBean.getImageBeanItemArrayList().get(a).getPath(), opts);
                            if(bitmaps!=null){
                                imageFolderBean.getImageBeanItemArrayList().get(a).setBitmap(bitmaps);
                            }else{
                                imageFolderBean.getImageBeanItemArrayList().remove(a);
                            }
                        }

                    }else{
                        imageFolderBeen.remove(i);
                    }
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
        if(imageFolderBeen!=null){
            imageFolderBeen.clear();
            imageFolderBeen = null;
            System.gc();
        }
    }
}
