package cn.xcom.helper.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.internal.http.multipart.FilePart;
import com.android.internal.http.multipart.Part;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import cn.xcom.helper.HelperApplication;
import cn.xcom.helper.R;
import cn.xcom.helper.adapter.GridViewAdapter;
import cn.xcom.helper.bean.UserInfo;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.record.AudioPlayer;
import cn.xcom.helper.record.RecordActivity;
import cn.xcom.helper.record.SoundView;
import cn.xcom.helper.utils.AudioManager;
import cn.xcom.helper.utils.GalleryFinalUtil;
import cn.xcom.helper.utils.PicturePickerDialog;
import cn.xcom.helper.utils.PushImage;
import cn.xcom.helper.utils.PushImageUtil;
import cn.xcom.helper.utils.SingleVolleyRequest;
import cn.xcom.helper.utils.StringJoint;
import cn.xcom.helper.utils.StringPostRequest;
import cn.xcom.helper.utils.VolleyRequest;

/**
 * 发布便民圈
 */
public class ReleaseConvenienceActivity extends BaseActivity implements View.OnClickListener {
    private static final int SOUND_CODE = 111;
    private Context context;
    private List<PhotoInfo> addImageList;
    private RelativeLayout back;
    private List<String> nameList;//添加相册选取完返回的的list
    private TextView cnnvenience_release, convenience_phone;
    private EditText description;
    private GridView gridview;
    private LinearLayout image_linearLayout;
    private ImageView tupian;
    private ImageView voice;
    private View view_line;
    private GalleryFinalUtil galleryFinalUtil;
    private GridViewAdapter gridViewAdapter;
    private final int REQUEST_CODE_CAMERA = 1000;
    private final int REQUEST_CODE_GALLERY = 1001;
    private AudioManager audioManager;
    private UserInfo userInfo;//得到用户的userid
    private KProgressHUD hud;
    private String soundPath;
    private SoundView soundView;
    private String descriptionString;
    private String soundName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_convenience);
        initView();
    }

    private void initView() {
        context = this;
        userInfo = new UserInfo();
        userInfo.readData(context);
        addImageList = new ArrayList();
        nameList = new ArrayList<>();
        galleryFinalUtil = new GalleryFinalUtil(9);
        back = (RelativeLayout) findViewById(R.id.back);
        back.setOnClickListener(this);
        view_line = findViewById(R.id.view_line);
        cnnvenience_release = (TextView) findViewById(R.id.cnnvenience_release);
        cnnvenience_release.setOnClickListener(this);
        convenience_phone = (TextView) findViewById(R.id.convenience_phone);
        convenience_phone.setText(userInfo.getUserPhone());
        description = (EditText) findViewById(R.id.description);
        gridview = (GridView) findViewById(R.id.gridview);
        image_linearLayout = (LinearLayout) findViewById(R.id.image_linearLayout);
        tupian = (ImageView) findViewById(R.id.tupian);
        tupian.setOnClickListener(this);
        voice = (ImageView) findViewById(R.id.voice);
        voice.setOnClickListener(this);
        soundView = (SoundView) findViewById(R.id.sound_view);
        hud = KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(true);
    }

    private void submit() {
        descriptionString = description.getText().toString().trim();
        if (TextUtils.isEmpty(descriptionString)) {
            Toast.makeText(this, "描述不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        //上传声音
        if (!TextUtils.isEmpty(soundPath)) {
            uploadSound();
            return;
        }
        //上传图片
        if (addImageList.size() == 0) {
            uploadConvenience();
        } else {
            uploadImgs();
        }
    }

    /**
     * 上传声音
     */
    private void uploadSound() {
        hud.show();
        String url = NetConstant.UPLOAD_RECORD;
        File f = new File(soundPath);
        List<Part> list = new ArrayList<>();
        try {
            FilePart filePart = new FilePart("upfile", f);
            list.add(filePart);
            VolleyRequest request = new VolleyRequest(url, list.toArray(new Part[list.size()]), new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    Log.d("uplaod_sound", s);
                    try {
                        JSONObject j = new JSONObject(s);
                        if (j.getString("status").equals("success")) {
                            soundName = j.getString("data");
                        }

                        if (addImageList.size() == 0) {
                            uploadConvenience();
                        } else {
                            uploadImgs();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    hud.dismiss();
                    Toast.makeText(context, "上传录音失败", Toast.LENGTH_SHORT).show();
                }
            });
            SingleVolleyRequest.getInstance(context).addToRequestQueue(request);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 上传图片
     */
    private void uploadImgs() {
        if (!hud.isShowing()) {
            hud.show();
        }
        //先上传图片再发布
        new PushImageUtil().setPushIamge(getApplication(), addImageList, nameList, new PushImage() {
            @Override
            public void success(boolean state) {
                uploadConvenience();
            }

            @Override
            public void error() {
                Toast.makeText(getApplication(), "上传图片失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 最后上传便民圈
     */
    private void uploadConvenience() {
        if (!hud.isShowing()) {
            hud.show();
        }
        //Toast.makeText(getApplication(), "图片上传成功", Toast.LENGTH_SHORT).show();
        //发布任务
        String url = NetConstant.CONVENIENCE_RELEASE;
        StringPostRequest request = new StringPostRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (hud != null) {
                    hud.dismiss();
                }
                Log.d("我的发布", s);
                try {
                    JSONObject object = new JSONObject(s);
                    if (object.optString("status").equals("success")) {
                        Toast.makeText(getApplication(), "发布成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplication(), object.getString("data"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                HelperApplication.getInstance().trendsBack = true;
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (hud != null) {
                    hud.dismiss();
                }
                Toast.makeText(getApplication(), "网络错误，检查您的网络", Toast.LENGTH_SHORT).show();
            }
        });

        request.putValue("userid", userInfo.getUserId());
        request.putValue("phone", convenience_phone.getText().toString());
        request.putValue("type", "1");
        request.putValue("title", convenience_phone.getText().toString());
        request.putValue("content", descriptionString);
        if (addImageList.size() > 0) {
            String s = StringJoint.arrayJointchar(nameList, ",");
            request.putValue("picurl", s);
        }else{
            request.putValue("picurl", "");
        }
        if (!TextUtils.isEmpty(soundName)) {
            request.putValue("sound", soundName);
            request.putValue("soundtime", soundView.getSoundTime() + "");
        } else {
            request.putValue("sound", "");
            request.putValue("soundtime", "");
        }
        request.putValue("latitude", String.valueOf(HelperApplication.getInstance().mLocLat));
        request.putValue("longitude", String.valueOf(HelperApplication.getInstance().mLocLon));
        request.putValue("address", HelperApplication.getInstance().mLocAddress);
        Log.e("发布便民圈", String.valueOf(HelperApplication.getInstance().mLocLat) + HelperApplication.getInstance().mLocAddress);
        SingleVolleyRequest.getInstance(getApplication()).addToRequestQueue(request);

    }


    public void showPicturePicker(View view) {
        PicturePickerDialog picturePickerDialog = new PicturePickerDialog(this);
        picturePickerDialog.show(new PicturePickerDialog.PicturePickerCallBack() {
            @Override
            public void onPhotoClick() {

                Toast.makeText(context, "拍 照", Toast.LENGTH_SHORT).show();
                //获取拍照权限
                if (galleryFinalUtil.openCamera(ReleaseConvenienceActivity.this, (ArrayList<PhotoInfo>) addImageList, REQUEST_CODE_CAMERA, mOnHanlderResultCallback)) {
                    return;
                } else {
                    String[] perms = {"android.permission.CAMERA"};
                    int permsRequestCode = 200;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(perms, permsRequestCode);
                    }
                }
            }

            @Override
            public void onAlbumClick() {
                galleryFinalUtil.openAblum(ReleaseConvenienceActivity.this, (ArrayList<PhotoInfo>) addImageList, REQUEST_CODE_GALLERY, mOnHanlderResultCallback);

                Toast.makeText(context, "相册选择", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 选择图片后 返回的图片数据放在list里面
     */
    private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {
        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
            if (resultList != null) {
                //  photoWithPaths.clear();
                addImageList.clear();
                addImageList.addAll(resultList);
                //  photoWithPaths.addAll(GetImageUtil.getImgWithPaths(resultList));
                Log.d("======haha", addImageList.size() + "");
                gridViewAdapter = new GridViewAdapter(ReleaseConvenienceActivity.this, (ArrayList<PhotoInfo>) addImageList);
                gridview.setAdapter(gridViewAdapter);
                view_line.setVisibility(View.VISIBLE);

            } else {
                view_line.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            Toast.makeText(ReleaseConvenienceActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tupian:
                showPicturePicker(v);
                break;
            case R.id.cnnvenience_release:
                submit();
                break;
            case R.id.voice:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                        != PackageManager.PERMISSION_GRANTED) {
                    //申请WRITE_EXTERNAL_STORAGE权限
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 1);
                } else {
                    startActivityForResult(new Intent(this, RecordActivity.class), SOUND_CODE);
                }
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SOUND_CODE) {
                //不是第一次录音
                if(!TextUtils.isEmpty(soundPath)){
                    soundView.delete();
                }
                soundPath = data.getStringExtra("path");
                int time = data.getIntExtra("time",0);
                soundView.setVisibility(View.VISIBLE);
                soundView.init(soundPath,time);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
                startActivityForResult(new Intent(this, RecordActivity.class), SOUND_CODE);
            } else {
                // Permission Denied
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(AudioPlayer.isPlaying){
            AudioPlayer.getInstance().stopPlay();
        }
    }
}
