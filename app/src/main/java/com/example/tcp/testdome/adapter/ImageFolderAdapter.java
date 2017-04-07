package com.example.tcp.testdome.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tcp.testdome.R;
import com.local.resources.bean.ImageBean;
import com.local.resources.bean.ImageFolderBean;
import com.local.resources.bean.InfoBean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/7.
 */

public class ImageFolderAdapter <T extends InfoBean>  extends BaseAdapter{
    private boolean isFolderImage;//是否是相册文件夹？
    private LayoutInflater inflater;

    private ArrayList<ImageFolderBean> imageFolderBeen;
    public ArrayList<ImageBean> imageBeen;
    public  ImageFolderAdapter (Context mContext, ArrayList<T> videoInfoList,boolean isFolderImage){
        inflater = LayoutInflater.from(mContext);
        if(isFolderImage){
            imageFolderBeen = (ArrayList<ImageFolderBean>) videoInfoList;
        }else{
            imageBeen  = (ArrayList<ImageBean>) videoInfoList;
        }
        this.isFolderImage = isFolderImage;
    }
    @Override
    public int getCount() {
        if(isFolderImage){
            return imageFolderBeen == null? 0: imageFolderBeen.size();
        }else{
            return imageBeen == null? 0: imageBeen.size();
        }
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemImage itemImage;
        if(convertView==null){
            convertView = inflater.inflate(R.layout.adapter_image_folder_item,null);
            itemImage = new ItemImage();
            itemImage.image = (ImageView) convertView.findViewById( R.id.image);
            itemImage.videoName = (TextView)convertView.findViewById(R.id.folderName);
            convertView.setTag(itemImage);
        }else{
            itemImage = (ItemImage) convertView.getTag();
        }

        if(isFolderImage){
            itemImage.image.setImageBitmap(imageFolderBeen.get(position).getBitmap());
            itemImage.image.setBackgroundResource(R.drawable.folder_item_image_bg);
            itemImage.videoName.setText(imageFolderBeen.get(position).getAlbum()+"("+imageFolderBeen.get(position).getImageBeanItemArrayList().size()+")");
        }else{
            itemImage.image.setImageBitmap(imageBeen.get(position).getBitmap());
        }
        return convertView;
    }

    class ItemImage{
         ImageView image;
         TextView videoName;
    }
}
