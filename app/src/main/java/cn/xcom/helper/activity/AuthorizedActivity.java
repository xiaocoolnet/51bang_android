package cn.xcom.helper.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.xcom.helper.R;
import cn.xcom.helper.bean.UserInfo;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.net.HelperAsyncHttpClient;
import cn.xcom.helper.utils.LogUtils;
import cz.msebera.android.httpclient.Header;

/**
 * Created by zhuchongkun on 16/6/3.
 * 身份认证页
 */
public class AuthorizedActivity extends BaseActivity implements View.OnClickListener{
    private String TAG="AuthorizedActivity";
    private Context mContext;
    private RelativeLayout rl_back;
    private ImageView iv_city,iv_handheld_ID_card,iv_ID_card,iv_driving_license;
    private TextView tv_city;
    private EditText et_name,et_ID,et_contact_name,et_contact_phone;
    private Button bt_next;
    private ImageLoader imageLoader=ImageLoader.getInstance();
    private DisplayImageOptions options;
    private static final int PHOTO_REQUEST_CAMERA=1;//拍照
    private static final int PHOTO_REQUEST_ALBUM=2;//相册
    private static final int PHOTO_REQUEST_CUT=3;//剪裁
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS=4;
    private int flag=0;
    private ProgressDialog dialog;
    private UserInfo userInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_authorized);
        mContext=this;
        initView();
    }

    private void initView(){
        rl_back= (RelativeLayout) findViewById(R.id.rl_authorized_back);
        rl_back.setOnClickListener(this);
        iv_city= (ImageView) findViewById(R.id.iv_authorized_city);
        iv_city.setOnClickListener(this);
        iv_handheld_ID_card= (ImageView) findViewById(R.id.iv_authorized_handheld_ID_card);
        iv_handheld_ID_card.setOnClickListener(this);
        iv_ID_card= (ImageView) findViewById(R.id.iv_authorized_ID_card);
        iv_ID_card.setOnClickListener(this);
        iv_driving_license= (ImageView) findViewById(R.id.iv_authorized_driving_license);
        iv_driving_license.setOnClickListener(this);
        tv_city= (TextView) findViewById(R.id.tv_authorized_city);
        et_name= (EditText) findViewById(R.id.et_authorized_name);
        et_ID= (EditText) findViewById(R.id.et_authorized_ID);
        et_contact_name= (EditText) findViewById(R.id.et_authorized_emergency_contact_person_name);
        et_contact_phone= (EditText) findViewById(R.id.et_authorized_emergency_contact_person_phone);
        bt_next= (Button) findViewById(R.id.bt_authorized_next);
        bt_next.setOnClickListener(this);
        userInfo=new UserInfo();
        dialog=new ProgressDialog(mContext,AlertDialog.THEME_HOLO_LIGHT);
        options=new DisplayImageOptions.Builder()
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .showImageOnLoading(R.mipmap.ic_authorized_photo)
                .showImageOnFail(R.mipmap.ic_authorized_photo)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .considerExifParams(true).build();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setData();
    }

    private void setData() {
        userInfo.readData(mContext);
        tv_city.setText(userInfo.getUserCity());
        et_name.setText(userInfo.getUserRealName());
        et_contact_name.setText(userInfo.getUserContactName());
        et_contact_phone.setText(userInfo.getUserContactPhone());
        et_ID.setText(userInfo.getUserID());
        imageLoader.displayImage(NetConstant.NET_DISPLAY_IMG+userInfo.getUserHandIDCard(),iv_handheld_ID_card,options);
        imageLoader.displayImage(NetConstant.NET_DISPLAY_IMG+userInfo.getUserIDCard(),iv_ID_card,options);
        imageLoader.displayImage(NetConstant.NET_DISPLAY_IMG+userInfo.getUserDrivingLicense(),iv_driving_license,options);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_authorized_back:
                finish();
                break;
            case R.id.iv_authorized_city:
                Intent intent=new Intent(mContext,SetCityActivity.class);
                startActivityForResult(intent,4);
                break;
            case R.id.iv_authorized_handheld_ID_card:
                flag=1;
                showPickDialog();
                break;
            case R.id.iv_authorized_ID_card:
                flag=2;
                showPickDialog();
                break;
            case R.id.iv_authorized_driving_license:
                flag=3;
                showPickDialog();
                break;
            case R.id.bt_authorized_next:
                startActivity(new Intent(mContext,SkillListActivity.class));
                break;
        }

    }
    private void showPickDialog(){
        new AlertDialog.Builder(this,AlertDialog.THEME_HOLO_LIGHT)
                .setNegativeButton("相册", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent albumIntent=new Intent();
                        albumIntent.setType("image/*");
                        albumIntent.setAction(Intent.ACTION_PICK);
                        startActivityForResult(albumIntent,PHOTO_REQUEST_ALBUM);
                    }
                }).setPositiveButton("拍照", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                int permissionCheck = ContextCompat.checkSelfPermission(mContext,
                        Manifest.permission.CAMERA);
                if(permissionCheck== PackageManager.PERMISSION_GRANTED){
                    Intent cameraIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    String state = Environment.getExternalStorageState();
                    if (state.equals(Environment.MEDIA_MOUNTED)){
                        File path=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                        File file=new File(path,"51helper.jpg");
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                    }
                    startActivityForResult(cameraIntent,PHOTO_REQUEST_CAMERA);
                }else if (permissionCheck== PackageManager.PERMISSION_DENIED){
                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext,
                            Manifest.permission.CAMERA)) {

                        // Show an expanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.

                    } else {

                        // No explanation needed, we can request the permission.

                        ActivityCompat.requestPermissions((Activity) mContext,
                                new String[]{Manifest.permission.CAMERA},
                                MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    }
                }

            }
        }).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 4);
        intent.putExtra("aspectY", 3);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 400);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

    /**
     * 保存裁剪之后的图片数据
     *
     * @param data
     */
    private void getImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            storeImageToSDCARD(photo);
        }else{

        }
    }

    /**
     * storeImageToSDCARD 将bitmap存放到sdcard中
     * */
    public void storeImageToSDCARD(Bitmap bm) {
        File appDir = new File(Environment.getExternalStorageDirectory(), "51helper");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            uploadImg(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void uploadImg(File f){
//        http://bang.xiaocool.net/index.php?g=apps&m=index&a=uploadimg
        dialog.setMessage("正在上传。。。");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show();
        RequestParams params=new RequestParams();
        try {
            params.put("upfile",f);
        }catch (FileNotFoundException e){

        }
        LogUtils.e(TAG,"--statusCode->"+params.toString());
        HelperAsyncHttpClient.post(NetConstant.NET_UPLOAD_IMG,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                LogUtils.e(TAG,"--statusCode->"+statusCode+"==>"+response.toString());
                if (response!=null){
                    try {
                        String state=response.getString("status");
                        if (state.equals("success")){
                            LogUtils.e(TAG,"--flag->"+flag);
                            if(flag==1){
                                userInfo.setUserHandIDCard(response.getString("data"));
                                imageLoader.displayImage(NetConstant.NET_DISPLAY_IMG+userInfo.getUserHandIDCard(),iv_handheld_ID_card,options);
                            }else if(flag==2){
                                userInfo.setUserIDCard(response.getString("data"));
                                imageLoader.displayImage(NetConstant.NET_DISPLAY_IMG+userInfo.getUserIDCard(),iv_ID_card,options);
                            }else if(flag==3){
                                userInfo.setUserDrivingLicense(response.getString("data"));
                                imageLoader.displayImage(NetConstant.NET_DISPLAY_IMG+userInfo.getUserDrivingLicense(),iv_driving_license,options);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                LogUtils.e(TAG,"--statusCode->"+statusCode+"==>"+responseString);
                dialog.dismiss();
            }
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode!=RESULT_CANCELED){
            switch (requestCode) {
                case PHOTO_REQUEST_CAMERA:// 相册
                    // 判断存储卡是否可以用，可用进行存储
                    String state = Environment.getExternalStorageState();
                    if (state.equals(Environment.MEDIA_MOUNTED)) {
                        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                        File tempFile = new File(path, "51helper.jpg");
                        startPhotoZoom(Uri.fromFile(tempFile));
                    } else {
                        Toast.makeText(getApplicationContext(), "未找到存储卡，无法存储照片！", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case PHOTO_REQUEST_ALBUM:// 图库
                    startPhotoZoom(data.getData());
                    break;

                case PHOTO_REQUEST_CUT: // 图片缩放完成后
                    if (data != null) {
                        getImageToView(data);
                    }
                    break;
                case 4:
                    if (data!=null){
                        tv_city.setText(data.getStringExtra("city"));
                    }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveDate();
    }
    private void saveDate(){
        userInfo.setUserCity(tv_city.getText().toString());
        userInfo.setUserRealName(et_name.getText().toString());
        userInfo.setUserContactName(et_contact_name.getText().toString());
        userInfo.setUserContactPhone(et_contact_phone.getText().toString());
        userInfo.setUserID(et_ID.getText().toString());
        userInfo.writeData(mContext);
    }
}
