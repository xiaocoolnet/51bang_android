package cn.xcom.helper.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import cn.xcom.helper.utils.RegexUtil;
import cn.xcom.helper.view.CircleImageView;
import cz.msebera.android.httpclient.Header;

/**
 * Created by zhuchongkun on 16/6/16.
 * 修改资料页
 */
public class EditPersonalActivity extends BaseActivity implements View.OnClickListener ,RadioGroup.OnCheckedChangeListener{
    private String TAG="EditPersonalActivity";
    private Context mContext;
    private RelativeLayout rl_back;
    private TextView tv_phone,tv_nickname;
    private LinearLayout ll_update_name, ll_update_password,ll_update_phone;
    private CircleImageView iv_head;
    private RadioGroup rg_gender;
    private RadioButton rb_male,rb_female;
    private ImageLoader imageLoader=ImageLoader.getInstance();
    private DisplayImageOptions options;
    private int gender;
    private UserInfo userInfo;
    private static final int PHOTO_REQUEST_CAMERA=1;//拍照
    private static final int PHOTO_REQUEST_ALBUM=2;//相册
    private static final int PHOTO_REQUEST_CUT=3;//剪裁
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS=4;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_edit_personal);
        mContext=this;
        initView();
    }

    private void initView(){
        rl_back= (RelativeLayout) findViewById(R.id.rl_edit_personal_back);
        rl_back.setOnClickListener(this);
        iv_head= (CircleImageView) findViewById(R.id.iv_edit_personal_head);
        iv_head.setOnClickListener(this);
        tv_phone= (TextView) findViewById(R.id.tv_edit_personal_phone);
        tv_nickname= (TextView) findViewById(R.id.tv_edit_personal_nickname);
        ll_update_name= (LinearLayout) findViewById(R.id.ll_edit_personal_update_name);
        ll_update_name.setOnClickListener(this);
        ll_update_password= (LinearLayout) findViewById(R.id.ll_edit_personal_update_password);
        ll_update_password.setOnClickListener(this);
        ll_update_phone= (LinearLayout) findViewById(R.id.ll_edit_personal_update_phone);
        ll_update_phone.setOnClickListener(this);
        rg_gender= (RadioGroup) findViewById(R.id.radioGroup_edit_personal_gender);
        rb_male= (RadioButton) findViewById(R.id.radioButton_edit_personal_man);
        rb_female= (RadioButton) findViewById(R.id.radioButton_edit_personal_woman);
        rg_gender.setOnCheckedChangeListener(this);
        options=new DisplayImageOptions.Builder()
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .showImageOnLoading(R.mipmap.ic_deafult_head)
                .showImageOnFail(R.mipmap.ic_deafult_head)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .considerExifParams(true).build();
    }

    @Override
    protected void onResume() {
        super.onResume();
        userInfo=new UserInfo(mContext);
        displayDate();
    }

    private void displayDate(){
        if (userInfo!=null){
            imageLoader.displayImage(NetConstant.NET_DISPLAY_IMG+userInfo.getUserImg(),iv_head,options);
            if (userInfo.getUserGender().equals("0")){
                rb_female.setChecked(true);
            }else if (userInfo.getUserGender().equals("1")){
                rb_male.setChecked(true);
            }
            tv_phone.setText(userInfo.getUserPhone());
            tv_nickname.setText(userInfo.getUserName());
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_edit_personal_back:
                finish();
                break;
            case R.id.iv_edit_personal_head:
                requestPermission();
                break;
            case R.id.ll_edit_personal_update_name:
                startActivity(new Intent(mContext,UpdateNameActivity.class));
                break;
            case R.id.ll_edit_personal_update_password:
                startActivity(new Intent(mContext,ResetPasswordActivity.class));
                break;
            case R.id.ll_edit_personal_update_phone:
                startActivity(new Intent(mContext,UpdatePhoneActivity.class));
                break;
        }

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (group.getCheckedRadioButtonId()){
            case R.id.radioButton_edit_personal_man:
                gender=1;
                updateGender();
                break;
            case R.id.radioButton_edit_personal_woman:
                gender=0;
                updateGender();
                break;
        }
    }

    private void updateGender(){
        if (!userInfo.getUserGender().equals(""+gender)){
            RequestParams params =new RequestParams();
            params.put("userid",userInfo.getUserId());
            params.put("sex",gender);
            HelperAsyncHttpClient.get(NetConstant.NET_UPDATE_GENDER,params,new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    LogUtils.e(TAG,"--statusCode->"+statusCode+"==>"+response.toString());
                    if (response!=null){
                        try {
                            String state=response.getString("status");
                            if (state.equals("success")){
                                userInfo.setUserGender(""+gender);
                                userInfo.writeData(mContext);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
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
                        File file=new File(path,"51head.jpg");
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


        if (requestCode == REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showPickDialog();
            } else {
                //用户勾选了不再询问
                //提示用户手动打开权限
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Toast.makeText(this, "访问权限已被禁止", Toast.LENGTH_SHORT).show();
                }
            }
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
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 340);
        intent.putExtra("outputY", 340);
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
                            userInfo.setUserImg(response.getString("data"));
                            imageLoader.displayImage(NetConstant.NET_DISPLAY_IMG+userInfo.getUserImg(),iv_head,options);
                            updateHead();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                LogUtils.e(TAG,"--statusCode->"+statusCode+"==>"+responseString);
            }
        });
    }
    private void updateHead(){
        RequestParams params=new RequestParams();
        params.put("userid",userInfo.getUserId());
        params.put("avatar",userInfo.getUserImg());
        HelperAsyncHttpClient.get(NetConstant.NET_UPDATE_HEAD,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                LogUtils.e(TAG,"--statusCode->"+statusCode+"==>"+response.toString());
                if (response!=null){
                    try {
                        String state=response.getString("status");
                        if (state.equals("success")){
                            userInfo.writeData(mContext);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                LogUtils.e(TAG,"--statusCode->"+statusCode+"==>"+responseString);
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
                        File tempFile = new File(path, "51head.jpg");
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
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private int REQUEST_CODE = 1111;

    private void requestPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // 第一次请求权限时，用户如果拒绝，下一次请求shouldShowRequestPermissionRationale()返回true
            // 向用户解释为什么需要这个权限
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(EditPersonalActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE);
            } else {
                //申请权限
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE);
            }
        } else{
            showPickDialog();
        }
    }



}
