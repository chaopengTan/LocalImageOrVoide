package com.local.resources;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;


import com.local.resources.bean.ImageFolderBean;
import com.local.resources.bean.ImageBean;
import com.local.resources.bean.InfoBean;
import com.local.resources.bean.VideoInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/2.
 */

public class LocalResource extends AsyncTask<Object, Object, Object> {
    public static final String VOIDE = "V";
    public static final String IMAGE="M";
        final String TAG = getClass().getSimpleName();
        ContentResolver cr;
        // 缩略图列表
        HashMap<String, String> thumbnailList = new HashMap<String, String>();
        Map<String,Object> image = new HashMap<>();

        private GetAlbumList getAlbumList;

        private String Type;

        public LocalResource (Context context,GetAlbumList getAlbumList){
            cr = context.getContentResolver();
            this.getAlbumList = getAlbumList;
        }


        /** * 得到缩略图，这里主要得到的是图片的ID值 */
        private void getThumbnail() {
            String[] projection = { MediaStore.Images.Thumbnails._ID, MediaStore.Images.Thumbnails.IMAGE_ID,
                    MediaStore.Images.Thumbnails.DATA};
            Cursor cursor1 = MediaStore.Images.Thumbnails.queryMiniThumbnails(cr, MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
                    MediaStore.Images.Thumbnails.MINI_KIND, projection);
            getThumbnailColumnData(cursor1);
            cursor1.close();
        }

        /** * 从数据库中得到缩略图 * @param cur */
        private void getThumbnailColumnData(Cursor cur) {
            if (cur.moveToFirst()) {
                int image_id;
                String image_path;
                int image_idColumn = cur.getColumnIndex(MediaStore.Images.Thumbnails.IMAGE_ID);
                int dataColumn = cur.getColumnIndex(MediaStore.Images.Thumbnails.DATA);
                do {
                    image_id = cur.getInt(image_idColumn);
                    image_path = cur.getString(dataColumn);
                    thumbnailList.put("" + image_id, image_path);
                } while (cur.moveToNext());
            }
        }

        /** * 得到图片集 */
        ArrayList<ImageFolderBean> imageBeanArrayList = new ArrayList<>();
        private void buildImagesBucketList() {
            // 构造缩略图索引
            getThumbnail();
            // 构造相册索引
            String columns[] = new String[] { MediaStore.Images.Media._ID, MediaStore.Images.Media.BUCKET_ID,
                    MediaStore.Images.Media.PICASA_ID, MediaStore.Images.Media.DATA, MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.TITLE,
                    MediaStore.Images.Media.SIZE, MediaStore.Images.Media.BUCKET_DISPLAY_NAME,MediaStore.Images.ImageColumns.SIZE

            };
            // 得到一个游标
            Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null,
                    MediaStore.Images.Media.DATE_MODIFIED+" desc");
            if (cur.moveToFirst()) {
                // 获取指定列的索引
                int photoIDIndex = cur.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
                int photoPathIndex = cur.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                int bucketDisplayNameIndex = cur.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
                int bucketIdIndex = cur.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_ID);
                int photoName = cur.getColumnIndexOrThrow( MediaStore.Images.Media.DISPLAY_NAME);

                /** * Description:这里增加了一个判断：判断照片的名 * 字是否合法，例如.jpg .png等没有名字的格式 * 如果图片名字是不合法的，直接过滤掉 */
                do {
                    if (cur.getString(photoPathIndex).substring(
                            cur.getString(photoPathIndex).lastIndexOf("/")+1,
                            cur.getString(photoPathIndex).lastIndexOf("."))
                            .replaceAll(" ", "").length()<=0)
                    {
                        Log.d(TAG, "出现了异常图片的地址：cur.getString(photoPathIndex)="+cur.getString(photoPathIndex));
                    }else {

//                        String _id = cur.getString(photoIDIndex);//图片id
                        int _id = cur.getInt(photoIDIndex);
                        String name = cur.getString(photoName);//图片名字
                        String path = cur.getString(photoPathIndex);//图片路径
                        String bucketName = cur.getString(bucketDisplayNameIndex);//图片所属相册名字
                        int bucketId = cur.getInt(bucketIdIndex);//图片所属相册Id
                        ImageFolderBean imageBean = (ImageFolderBean) image.get(bucketId+"");
                        if(imageBean == null){
                            imageBean = new ImageFolderBean(bucketName,bucketId);
                            image.put(bucketId+"",imageBean);
                            imageBeanArrayList.add(imageBean);
                            imageBean.setPath(path);//取一个图片作为封面图
                        }

                        ImageBean imageBeanItem = new ImageBean(_id,name,path,bucketName,bucketId);
                        ArrayList<ImageBean> arrayList =imageBean.getImageBeanItemArrayList();
                        if(arrayList == null){
                            arrayList = new ArrayList<>();
                            imageBean.setImageBeanItemArrayList(arrayList);
                        }
                        arrayList.add(imageBeanItem);
                    }
                } while (cur.moveToNext());
            }
            cur.close();

        }
        /** * 得到原始图像路径 * @param image_id * @return */
        public String getOriginalImagePath(String image_id) {
            String path = null;
            String[] projection = { MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA };
            Cursor cursor = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection,
                    MediaStore.Images.Media._ID + "=" + image_id, null, MediaStore.Images.Media.DATE_MODIFIED+" desc");
            if (cursor != null) {
                cursor.moveToFirst();
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
            return path;
        }



    /**
     * 获取视频文件
     * **/
    List<VideoInfo> voideList = new ArrayList<VideoInfo>();
    public void getVoideList() {
            Cursor cursor = cr.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null,null, null);
            if (cursor != null) {

                while (cursor.moveToNext()) {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID));
                    String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));  //视频文件的标题内容
                    String album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.ALBUM));
                    String artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.ARTIST));
                    String displayName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME));
                    String mimeType = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE));
                    String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));  //
                    long duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
                    long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));
                    VideoInfo videoinfo = new VideoInfo(id, title, album, artist, displayName, mimeType, path, size, duration);
                    voideList.add(videoinfo);
                }
                cursor.close();
            }
    }



        //回调接口，当完成相册和图片的获取之后，调用该接口的方法传递数据，这种方法很常用，大家务必掌握
     public interface GetAlbumList{
           public <T extends InfoBean>void getAlbumList(List<T> list);
     }

        @Override
     protected Object doInBackground(Object... params) {
            Type = params[0].toString();
            if(Type.equals(VOIDE)){
                getVoideList();
            }else if(Type.equals(IMAGE)){
                buildImagesBucketList();
            }
          return null;
     }

        @Override
     protected void onPostExecute(Object result) {
        super.onPostExecute(result);
        if(getAlbumList!=null&&Type.equals(VOIDE)){
             getAlbumList.getAlbumList(voideList);
        }else if(imageBeanArrayList!=null &&Type.equals(IMAGE)){
            getAlbumList.getAlbumList(imageBeanArrayList);
        }
     }
    }

