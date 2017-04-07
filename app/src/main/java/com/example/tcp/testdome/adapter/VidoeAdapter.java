package com.example.tcp.testdome.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tcp.testdome.ImageListActivity;
import com.example.tcp.testdome.R;
import com.local.resources.bean.ImageFolderBean;
import com.local.resources.bean.VideoInfo;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/7.
 */

public class VidoeAdapter extends BaseAdapter{
    private ArrayList<VideoInfo> mVideoInfoList;
    private LayoutInflater inflater;
    public VidoeAdapter(Context mContext,ArrayList<VideoInfo> videoInfoList){
        this.mVideoInfoList = videoInfoList;
        inflater = LayoutInflater.from(mContext);
    }



    @Override
    public int getCount() {
        return mVideoInfoList == null? 0: mVideoInfoList.size();
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
            convertView = inflater.inflate(R.layout.adapter_item,null);
            itemImage = new ItemImage();
            itemImage.image = (ImageView) convertView.findViewById( R.id.image);
            itemImage.videoName = (TextView)convertView.findViewById(R.id.videoName);
            convertView.setTag(itemImage);
        }else{
            itemImage = (ItemImage) convertView.getTag();
        }
        itemImage.image.setImageBitmap(mVideoInfoList.get(position).getBitmap());
        itemImage.videoName.setText(mVideoInfoList.get(position).getDisplayName());
        return convertView;
    }

    class ItemImage{
         ImageView image;
         TextView videoName;
    }
}
