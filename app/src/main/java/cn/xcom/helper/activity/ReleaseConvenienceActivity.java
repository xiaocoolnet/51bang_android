package cn.xcom.helper.activity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
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

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.List;

import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import cn.xcom.helper.R;
import cn.xcom.helper.adapter.GridViewAdapter;
import cn.xcom.helper.bean.UserInfo;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.utils.AudioManager;
import cn.xcom.helper.utils.GalleryFinalUtil;
import cn.xcom.helper.utils.PicturePickerDialog;
import cn.xcom.helper.utils.PushImage;
import cn.xcom.helper.utils.PushImageUtil;
import cn.xcom.helper.utils.SingleVolleyRequest;
import cn.xcom.helper.utils.StringJoint;
import cn.xcom.helper.utils.StringPostRequest;

public class ReleaseConvenienceActivity extends BaseActivity implements View.OnClickListener{
    private Context context;
    private List<PhotoInfo> addImageList;
    private RelativeLayout back;
    private List<String> nameList;//添加相册选取完返回的的list
    private TextView cnnvenience_release,convenience_phone;
    private EditText description;
    private GridView gridview;
    private LinearLayout image_linearLayout;
    private ImageView tupian;
    private Button voice_button;
    private ImageView voice;
    private GalleryFinalUtil galleryFinalUtil;
    private GridViewAdapter gridViewAdapter;
    private final int REQUEST_CODE_CAMERA = 1000;
    private final int REQUEST_CODE_GALLERY = 1001;
    private AudioManager audioManager;
    private UserInfo userInfo;//得到用户的userid


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_convenience);
        initView();
    }

    private void initView() {
        context = this;
        userInfo=new UserInfo();
        userInfo.readData(context);
        addImageList = new ArrayList();
        nameList = new ArrayList<>();
        galleryFinalUtil = new GalleryFinalUtil(9);
        back = (RelativeLayout) findViewById(R.id.back);
        back.setOnClickListener(this);
        cnnvenience_release = (TextView) findViewById(R.id.cnnvenience_release);
        cnnvenience_release.setOnClickListener(this);
        voice_button= (Button) findViewById(R.id.voice_button);
        convenience_phone= (TextView) findViewById(R.id.convenience_phone);
        description = (EditText) findViewById(R.id.description);
        gridview = (GridView) findViewById(R.id.gridview);
        image_linearLayout = (LinearLayout) findViewById(R.id.image_linearLayout);
        tupian = (ImageView) findViewById(R.id.tupian);
        tupian.setOnClickListener(this);
        voice = (ImageView) findViewById(R.id.voice);
    }

    private void submit() {
        // validate
        final String descriptionString = description.getText().toString().trim();
        if (TextUtils.isEmpty(descriptionString)) {
            Toast.makeText(this, "descriptionString不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        //先上传图片再发布
        new PushImageUtil().setPushIamge(getApplication(), addImageList, nameList, new PushImage() {
            @Override
            public void success(boolean state) {

                Toast.makeText(getApplication(), "图片上传成功", Toast.LENGTH_SHORT).show();

                //发布任务
                String url = NetConstant.CONVENIENCE_RELEASE;
                StringPostRequest request = new StringPostRequest(url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.d("===我的发布", s);
                        Toast.makeText(getApplication(), "发布成功", Toast.LENGTH_SHORT).show();

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getApplication(), "网络错误，检查您的网络", Toast.LENGTH_SHORT).show();
                    }
                });
                String s = StringJoint.arrayJointchar(nameList, ",");
                Log.d("2222233", s + "");
                Log.d("2222233", nameList.size() + "");
                request.putValue("userid", userInfo.getUserId());
                request.putValue("phone", convenience_phone.getText().toString());
                request.putValue("type", "1");
                request.putValue("title", convenience_phone.getText().toString());
                request.putValue("content",descriptionString);
                request.putValue("picurl", s);
                request.putValue("sound", "");
                request.putValue("soundtime", "");
                SingleVolleyRequest.getInstance(getApplication()).addToRequestQueue(request);

            }

            @Override
            public void error() {
                Toast.makeText(getApplication(), "上传失败", Toast.LENGTH_SHORT).show();
                String url = NetConstant.RELEASE;
                StringPostRequest request = new StringPostRequest(url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.d("++++我的发布", s);
                        Toast.makeText(getApplication(), "发布成功", Toast.LENGTH_SHORT).show();

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getApplication(), "网络错误，检查您的网络", Toast.LENGTH_SHORT).show();
                    }
                });
//                request.putValue("userid", info.getUserId());
//                request.putValue("goodsname", goodNameString);
//                request.putValue("type", type);
//                request.putValue("price", price);
//                request.putValue("oprice", oldprice);
//                request.putValue("description", descriptionString);
//                request.putValue("unit", "个");
//                request.putValue("address", "北京市朝阳区");
//                request.putValue("longitude", location);
//                request.putValue("latitude", location);
//                request.putValue("delivery", text_transport.getText().toString());
                SingleVolleyRequest.getInstance(getApplication()).addToRequestQueue(request);
            }
        });
    // TODO validate success, do something


}
    public void showPicturePicker(View view){
        PicturePickerDialog picturePickerDialog = new PicturePickerDialog(this);
        picturePickerDialog.show(new PicturePickerDialog.PicturePickerCallBack() {
            @Override
            public void onPhotoClick() {

                Toast.makeText(context,"拍 照",Toast.LENGTH_SHORT).show();
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

                Toast.makeText(context,"相册选择",Toast.LENGTH_SHORT).show();
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

            }
        }
        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            Toast.makeText(ReleaseConvenienceActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
        }
    };
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.tupian:
                showPicturePicker(v);
                break;
            case R.id.cnnvenience_release:
                submit();
                break;
        }

    }






}
