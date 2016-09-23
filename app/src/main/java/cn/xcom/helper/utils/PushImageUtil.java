package cn.xcom.helper.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.android.internal.http.multipart.FilePart;
import com.android.internal.http.multipart.Part;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.finalteam.galleryfinal.model.PhotoInfo;
import cn.xcom.helper.constant.NetConstant;


/**
 * Created by Administrator on 2016/8/25.
 */
public class PushImageUtil {
    private static final int ADD_KEY = 4;
    private static final int ADD_IMG_KEY1 = 101;
    private static final int ADD_IMG_KEY2 = 102;
    private static final int ADD_IMG_KEY3 = 103;
    private static final int ADD_IMG_KEY4 = 104;
    private static final int ADD_IMG_KEY5 = 105;
    private static final int ADD_IMG_KEY6 = 106;
    private static final int ADD_IMG_KEY7 = 107;
    private static final int ADD_IMG_KEY8 = 108;
    private static final int ADD_IMG_KEY9 = 109;
    private static Context mContext;
    private int imgNums = 0;
    private int key = 0;
    private List<PhotoInfo> photoWithPaths;
    private boolean isOk;
    private PushImage pushIamge;

    public void setPushIamge(Context context,List<PhotoInfo> p,PushImage pushIamge) {
        this.photoWithPaths = p;
        this.mContext = context;
        this.pushIamge = pushIamge;
        if (photoWithPaths.size()>0){
            pushImage(photoWithPaths.get(imgNums),ADD_IMG_KEY1);
        }else {
            pushIamge.error();
        }

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ADD_IMG_KEY1:
                    if (msg.obj != null) {
                      if (JsonResult.JSONparser(mContext, String.valueOf((JSONObject)msg.obj))){
                          imgNums = 1;
                          if (imgNums < photoWithPaths.size()) {
                              pushImage(photoWithPaths.get(imgNums),ADD_IMG_KEY2);
                          }else {
                              pushIamge.success(true);
                              isOk = true;
                          }
                      }else {
                          pushIamge.error();
                          Toast.makeText(mContext, "发送失败", Toast.LENGTH_SHORT).show();
                      }
                    }
                    break;
                case ADD_IMG_KEY2:
                    if (msg.obj != null) {
                        if (JsonResult.JSONparser(mContext, String.valueOf((JSONObject)msg.obj))){
                            imgNums = 2;
                            if (imgNums < photoWithPaths.size()) {
                                pushImage(photoWithPaths.get(imgNums),ADD_IMG_KEY3);

                            }else {
                                pushIamge.success(true);
                                isOk = true;
                            }
                        }else {
                            pushIamge.error();
                            Toast.makeText(mContext, "发送失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                case ADD_IMG_KEY3:
                    if (msg.obj != null) {
                        if (JsonResult.JSONparser(mContext, String.valueOf((JSONObject)msg.obj))){
                            imgNums = 3;
                            if (imgNums < photoWithPaths.size()) {
                                pushImage(photoWithPaths.get(imgNums),ADD_IMG_KEY4);

                            }else {
                                pushIamge.success(true);
                                isOk = true;
                            }
                        }else {
                            pushIamge.error();
                            Toast.makeText(mContext, "发送失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                case ADD_IMG_KEY4:
                    if (msg.obj != null) {
                        if (JsonResult.JSONparser(mContext, String.valueOf((JSONObject)msg.obj))){
                            imgNums = 4;
                            if (imgNums < photoWithPaths.size()) {
                                pushImage(photoWithPaths.get(imgNums),ADD_IMG_KEY5);

                            }else {
                                pushIamge.success(true);
                                isOk = true;
                            }
                        }else {
                            pushIamge.error();
                            Toast.makeText(mContext, "发送失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                case ADD_IMG_KEY5:
                    if (msg.obj != null) {
                        if (JsonResult.JSONparser(mContext, String.valueOf((JSONObject)msg.obj))){
                            imgNums = 5;
                            if (imgNums < photoWithPaths.size()) {
                                pushImage(photoWithPaths.get(imgNums),ADD_IMG_KEY6);

                            }else {
                                pushIamge.success(true);
                                isOk = true;
                            }
                        }else {
                            pushIamge.error();
                            Toast.makeText(mContext, "发送失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                case ADD_IMG_KEY6:
                    if (msg.obj != null) {
                        if (JsonResult.JSONparser(mContext, String.valueOf((JSONObject)msg.obj))){
                            imgNums = 6;
                            if (imgNums < photoWithPaths.size()) {
                                pushImage(photoWithPaths.get(imgNums),ADD_IMG_KEY7);

                            }else {
                                pushIamge.success(true);
                                isOk = true;
                            }
                        }else {
                            pushIamge.error();
                            Toast.makeText(mContext, "发送失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                case ADD_IMG_KEY7:
                    if (msg.obj != null) {
                        if (JsonResult.JSONparser(mContext, String.valueOf((JSONObject)msg.obj))){
                            imgNums = 7;
                            if (imgNums < photoWithPaths.size()) {
                                pushImage(photoWithPaths.get(imgNums),ADD_IMG_KEY8);

                            }else {
                                pushIamge.success(true);
                                isOk = true;
                            }
                        }else {
                            pushIamge.error();
                            Toast.makeText(mContext, "发送失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                case ADD_IMG_KEY8:
                    if (msg.obj != null) {
                        if (JsonResult.JSONparser(mContext, String.valueOf((JSONObject)msg.obj))){
                            imgNums = 8;
                            if (imgNums < photoWithPaths.size()) {
                                pushImage(photoWithPaths.get(imgNums),ADD_IMG_KEY9);

                            }else {
                                pushIamge.success(true);
                                isOk = true;
                            }
                        }else {
                            pushIamge.error();
                            Toast.makeText(mContext, "发送失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                case ADD_IMG_KEY9:
                    if (msg.obj != null) {
                        if (JsonResult.JSONparser(mContext, String.valueOf((JSONObject)msg.obj))){
                            imgNums = 9;
                            isOk = true;
                            pushIamge.success(true);

                        }else {
                            pushIamge.error();
                            Toast.makeText(mContext, "发送失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;

            }
        }
    };

    private void pushImage(PhotoInfo photoWithPath,int  addImgKey) {
        pushImg(photoWithPath.getPhotoPath(), addImgKey);
    }

    public void pushImg(final String picPath,int  addImgKey){
        convertBitmap(convertToBitmap(picPath, 200, 200), addImgKey);

    }
    public void updatePhoto(final File f,final int KEY){

        new Thread(){
            Message msg = Message.obtain();
            @Override
            public void run() {
                List<Part> list=new ArrayList<Part>();
                try {
                    list.add(new FilePart("upfile",f));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                String url1= NetConstant.NET_UPLOAD_IMG;
                VolleyRequest request=new VolleyRequest(url1, list.toArray(new Part[list.size()]),new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject obj = new JSONObject(s);
                            msg.what = KEY;
                            msg.obj = obj;
                            key=KEY;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }finally {
                            handler.sendMessage(msg);
                        }
                        Log.d("+++头像上传", s);

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                });

                SingleVolleyRequest.getInstance(mContext).addToRequestQueue(request);
            }
        }.start();

    }
    public void convertBitmap(Bitmap bitmap,int  addImgKey){
        File appDir = new File(Environment.getExternalStorageDirectory(), "51helper");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        Random random=new Random();
        String fileName ="yyy"+ random.nextInt(10000)+System.currentTimeMillis() + ".jpg";

        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            updatePhoto(file,addImgKey);
            ArrayList<String> arrayList=new ArrayList<>();
              //  Log.d("====图片集合key",key+"");
                arrayList.add(fileName);
            Log.d("====图片集合jjj", addImgKey + "");
            Log.d("====图片集合", arrayList.size() + "");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public Bitmap convertToBitmap(String path, int w, int h) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        // 设置为ture只获取图片大小
        opts.inJustDecodeBounds = true;
        opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
        // 返回为空
        BitmapFactory.decodeFile(path, opts);
        int width = opts.outWidth;
        int height = opts.outHeight;
        float scaleWidth = 0.f, scaleHeight = 0.f;
        if (width > w || height > h) {
            // 缩放
            scaleWidth = ((float) width) / w;
            scaleHeight = ((float) height) / h;
        }
        opts.inJustDecodeBounds = false;
        float scale = Math.max(scaleWidth, scaleHeight);
        opts.inSampleSize = (int)scale;
        WeakReference<Bitmap> weak = new WeakReference<Bitmap>(BitmapFactory.decodeFile(path, opts));
        return Bitmap.createScaledBitmap(weak.get(), w, h, true);
    }

}
