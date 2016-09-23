package cn.xcom.helper.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
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
import com.baoyz.actionsheet.ActionSheet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import cn.xcom.helper.R;
import cn.xcom.helper.adapter.ChoosePhotoListAdapter;
import cn.xcom.helper.adapter.GridViewAdapter;
import cn.xcom.helper.bean.UserInfo;
import cn.xcom.helper.constant.NetConstant;

import cn.xcom.helper.entity.PhotoWithPath;

import cn.xcom.helper.utils.DateUtil;
import cn.xcom.helper.utils.GalleryFinalUtil;
import cn.xcom.helper.utils.GetImageUtil;
import cn.xcom.helper.utils.PushImage;
import cn.xcom.helper.utils.PushImageUtil;
import cn.xcom.helper.utils.SingleVolleyRequest;
import cn.xcom.helper.utils.StringPostRequest;
import cn.xcom.helper.utils.VolleyRequest;
import cn.xcom.helper.utils.WheelView;



public class ReleaseActivity extends BaseActivity implements View.OnClickListener {
    private Context context;
    private RelativeLayout rl_i_help_back;
    private List<PhotoInfo> addImageList;//添加相册选取完返回的的list
    private ArrayList<PhotoWithPath> photoWithPaths;
    private EditText goodName;
    private EditText description;
    private GalleryFinalUtil galleryFinalUtil;
    private ImageView iv_authorized_handheld_ID_card;
    private ImageView tupian,ima_dismiss,id_dismiss11;
    private LinearLayout divide ;
    private EditText ed_price;
    private EditText ed_oldprice,ed_location;
    private GridView gridView;
    private GridViewAdapter gridViewAdapter;
    private LinearLayout location,image_linearLayout;
    private LinearLayout transport;
    private TextView telephone,tv_divide,text_transport;
    private LinearLayout release;
    private Button bt_login_release;
    private ChoosePhotoListAdapter choosePhotoListAdapter;
    private final int REQUEST_CODE_CAMERA = 1000;
    private final int REQUEST_CODE_GALLERY = 1001;
    private final int REQUEST_CODE_CROP = 1002;
    private final int REQUEST_CODE_EDIT = 1003;
    private final int  PHONE_PHOTO=1;
    private final int  TAKE_PHOTO=2;
    private final int  RESULT_PHOTO=3;//裁剪后的头像
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS=4;
    private  String type="123";
    String a[]={"同城自取","送货上门","凭劵消费"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_release);
        context=this;
        initView();

        Intent intent=getIntent();
         String type=intent.getStringExtra("id");
        String s1="";
        String s2=intent.getStringExtra("judge");
//        String userid=intent.getStringExtra("userid");
//        Log.d("++++ciid",userid);
        if (s2!=null&&s2.equals("返回")){
            s1= intent.getStringExtra("dictionary");
            ima_dismiss.setVisibility(View.INVISIBLE);
            tv_divide.setText(s1);
        }

    }
    private void initView() {
        addImageList=new ArrayList();
        photoWithPaths = new ArrayList<>();
        galleryFinalUtil=new GalleryFinalUtil(9);
        rl_i_help_back = (RelativeLayout) findViewById(R.id.rl_i_help_back);
        rl_i_help_back.setOnClickListener(this);
        goodName = (EditText) findViewById(R.id.goodName);
        description = (EditText) findViewById(R.id.description);
        ima_dismiss= (ImageView) findViewById(R.id.ima_dissmis);
        ima_dismiss.setOnClickListener(this);
        id_dismiss11= (ImageView) findViewById(R.id.id_dismiss11);
        tv_divide= (TextView) findViewById(R.id.text_divide);
        tv_divide.setOnClickListener(this);
        //iv_authorized_handheld_ID_card = (ImageView) findViewById(R.id.iv_authorized_handheld_ID_card);
        tupian = (ImageView) findViewById(R.id.tupian);
        tupian.setOnClickListener(this);
        text_transport= (TextView) findViewById(R.id.text_transport);
        ed_location= (EditText) findViewById(R.id.ed_location);
        //  delete= (ImageView) findViewById(R.id.delete);
     //   delete.setOnClickListener(this);
        divide = (LinearLayout) findViewById(R.id.divide);
        divide.setOnClickListener(this);

        ed_price = (EditText) findViewById(R.id.ed_price);
        ed_oldprice = (EditText) findViewById(R.id.ed_oldprice);
        location = (LinearLayout) findViewById(R.id.location);
        image_linearLayout= (LinearLayout) findViewById(R.id.image_linearLayout);
        image_linearLayout.setOnClickListener(this);
        transport = (LinearLayout) findViewById(R.id.transport);
        transport.setOnClickListener(this);
        telephone = (TextView) findViewById(R.id.telephone);
        release = (LinearLayout) findViewById(R.id.release);
        bt_login_release = (Button) findViewById(R.id.bt_login_release);
        bt_login_release.setOnClickListener(this);
        gridView= (GridView) findViewById(R.id.gridview);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_login_release:
                submit();
                break;
            case R.id.tupian:
                showActionSheet();
                break;
            case R.id.rl_i_help_back:
                backImage();
                break;
           case R.id.divide:
               startActivity(new Intent(getBaseContext(), DivideActivity.class));
               finish();
                break;
            case R.id.text_divide:
                startActivity(new Intent(getBaseContext(),DivideActivity.class));
                finish();
                break;
            case R.id.transport:
                popoDialog(a);
                break;
        }
    }
       //提交发布
    private void submit() {

        final UserInfo info=new UserInfo();
         info.readData(context);
        Log.d("++++ciid", info.getUserId());



        // validate
        final String goodNameString = goodName.getText().toString().trim();
        if (TextUtils.isEmpty(goodNameString)) {
            Toast.makeText(this, "goodNameString不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        final String descriptionString = description.getText().toString().trim();
        if (TextUtils.isEmpty(descriptionString)) {
            Toast.makeText(this, "descriptionString不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        final String price = ed_price.getText().toString().trim();
        if (TextUtils.isEmpty(price)) {
            Toast.makeText(this, "price不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        final String oldprice = ed_oldprice.getText().toString().trim();
        if (TextUtils.isEmpty(oldprice)) {
            Toast.makeText(this, "oldprice不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        final String location = ed_location.getText().toString().trim();
        if (TextUtils.isEmpty(oldprice)) {
            Toast.makeText(this, "ed_location不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
       //先上传图片再发布
        new PushImageUtil().setPushIamge(getApplication(), addImageList, new PushImage() {
            @Override
            public void success(boolean state) {
                Toast.makeText(getApplication(),"上传成功",Toast.LENGTH_SHORT).show();
                //发布任务
                String url=NetConstant.RELEASE;
                StringPostRequest request=new StringPostRequest(url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.d("++++我的发布",s);
                        Toast.makeText(getApplication(),"发布成功",Toast.LENGTH_SHORT).show();

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getApplication(),"网络错误，检查您的网络",Toast.LENGTH_SHORT).show();
                    }
                });
                request.putValue("userid",info.getUserId());
                request.putValue("goodsname",goodNameString);
                request.putValue("type",type);
                request.putValue("price",price);
                request.putValue("oprice",oldprice);
                request.putValue("description",descriptionString);
                request.putValue("unit","个");
                request.putValue("address","北京市朝阳区");
                request.putValue("longitude",location);
                request.putValue("latitude",location);
                request.putValue("delivery",text_transport.toString().trim());


                SingleVolleyRequest.getInstance(getApplication()).addToRequestQueue(request);

            }

            @Override
            public void error() {
                Toast.makeText(getApplication(),"上传失败",Toast.LENGTH_SHORT).show();
                String url=NetConstant.RELEASE;
                StringPostRequest request=new StringPostRequest(url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.d("++++我的发布",s);
                        Toast.makeText(getApplication(),"发布成功",Toast.LENGTH_SHORT).show();

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getApplication(),"网络错误，检查您的网络",Toast.LENGTH_SHORT).show();
                    }
                });
                request.putValue("userid",info.getUserId());
                request.putValue("goodsname",goodNameString);
                request.putValue("type",type);
                request.putValue("price",price);
                request.putValue("oprice",oldprice);
                request.putValue("description",descriptionString);
                request.putValue("unit","个");
                request.putValue("address","北京市朝阳区");
                request.putValue("longitude",location);
                request.putValue("latitude",location);
                request.putValue("delivery",text_transport.toString().trim());


                SingleVolleyRequest.getInstance(getApplication()).addToRequestQueue(request);
            }
        });



    }
    //向服务器上传图片,以文件的形式
    public void updatePhoto(File f){
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
                Log.d("+++头像上传", s);
                Toast.makeText(getBaseContext(),"头像上传成功",Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getBaseContext(),"头像上传失败",Toast.LENGTH_SHORT).show();
            }
        });

        SingleVolleyRequest.getInstance(this).addToRequestQueue(request);
    }


//    @Override
//    protected void onActivityResult(int requestCode,
//                                    int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode!= RESULT_OK){
//            return;
//        }
//        if (requestCode==TAKE_PHOTO){
//           startPhoneZoom(data.getData());
//        }
//        else if (requestCode==PHONE_PHOTO){
//            String state = Environment.getExternalStorageState();
//            if (state.equals(Environment.MEDIA_MOUNTED)) {
//                File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
//                File tempFile = new File(path, "51head.jpg");
//                startPhoneZoom(Uri.fromFile(tempFile));
//            } else {
//                Toast.makeText(getApplicationContext(), "未找到存储卡，无法存储照片！", Toast.LENGTH_SHORT).show();
//            }
//        }
//        //裁剪返回位图
//        else if(requestCode==RESULT_PHOTO){
//            if (data!=null) {
//                Bundle bundle = data.getExtras();
//                if (bundle != null) {
//                    Bitmap bitmap = bundle.getParcelable("data");
//                     addImageList.add(bitmap);
//                    Log.d("+++集合大小", addImageList.size() + "");
//                    convertBitmap(bitmap);
//                    gridViewAdapter =new GridViewAdapter(getBaseContext(),addImageList);
//                    gridView.setAdapter(gridViewAdapter);
//                    gridViewAdapter.notifyDataSetChanged();
//
//
//                }
//            }
//        }
//
//    }

    public void convertBitmap(Bitmap bitmap){
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
            String s= DateUtil.getDateToString(System.currentTimeMillis());
            Log.d("+++当前时间", s);
            Log.d("+++文件名", fileName);
            updatePhoto(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 弹出选择框
     */
    private void showActionSheet() {
        ActionSheet.createBuilder(ReleaseActivity.this, getSupportFragmentManager())
                .setCancelButtonTitle("取消")
                .setOtherButtonTitles("打开相册", "拍照")
                .setCancelableOnTouchOutside(true)
                .setListener(new ActionSheet.ActionSheetListener() {
                    @Override
                    public void onDismiss(ActionSheet actionSheet, boolean isCancel) {

                    }

                    @Override
                    public void onOtherButtonClick(ActionSheet actionSheet, int index) {

                        switch (index) {
                            case 0:
                                galleryFinalUtil.openAblum(ReleaseActivity.this, (ArrayList<PhotoInfo>) addImageList, REQUEST_CODE_GALLERY, mOnHanlderResultCallback);
                                break;
                            case 1:
                                //获取拍照权限
                                if (galleryFinalUtil.openCamera(ReleaseActivity.this, (ArrayList<PhotoInfo>) addImageList, REQUEST_CODE_CAMERA, mOnHanlderResultCallback)) {
                                    return;
                                } else {
                                    String[] perms = {"android.permission.CAMERA"};
                                    int permsRequestCode = 200;
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermissions(perms, permsRequestCode);
                                    }
                                }
                                break;

                            default:
                                break;
                        }
                    }
                })
                .show();
    }
    /**
     * 选择图片后 返回的图片数据
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
                gridViewAdapter = new GridViewAdapter(ReleaseActivity.this, (ArrayList<PhotoInfo>) addImageList);
                gridView.setAdapter(gridViewAdapter);

            }
        }
        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            Toast.makeText(ReleaseActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
        }
    };

    //弹出相册相机对话框
    public void showDialog(){
        new AlertDialog.Builder(this,AlertDialog.THEME_HOLO_LIGHT)
                .setNegativeButton("相机", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(),
                                Manifest.permission.CAMERA);
                        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            String state = Environment.getExternalStorageState();
                            if (state.equals(Environment.MEDIA_MOUNTED)) {
                                File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                                File file = new File(path, "51head.jpg");
                                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                            }
                            startActivityForResult(cameraIntent, PHONE_PHOTO);
                        } else if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                            // Should we show an explanation?
                            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,
                                    Manifest.permission.CAMERA)) {

                                // Show an expanation to the user *asynchronously* -- don't block
                                // this thread waiting for the user's response! After the user
                                // sees the explanation, try again to request the permission.

                            } else {

                                // No explanation needed, we can request the permission.

                                ActivityCompat.requestPermissions((Activity) context,
                                        new String[]{Manifest.permission.CAMERA},
                                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                                // app-defined int constant. The callback method gets the
                                // result of the request.
                            }
                        }
//                        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                        File file = FileUitlity.getInstance(getApplicationContext()).makeDir("head_camera");
//                        capter = file.getPath()
//                                + File.separatorChar
//                                + System.currentTimeMillis()
//                                + ".jpg";
//                        camera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(capter)));
//                        camera.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
//                        startActivityForResult(camera, PHONE_PHOTO);
                    }
                }).setPositiveButton("相册", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //  takephoto();
            }
        }).show();
    }
    //弹出对话框的方法
    public void popoDialog(String [] a){
        View outerView = LayoutInflater.from(getBaseContext())
                .inflate(R.layout.wheel_view, null);

        WheelView wv = (WheelView) outerView
                .findViewById(R.id.wheel_view_wv);

        wv.setOffset(2);// 偏移量
        wv.setItems(Arrays.asList(a));// 实际内容
        wv.setSeletion(0);// 设置默认被选中的项目
        wv.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                id_dismiss11.setVisibility(View.INVISIBLE);
                text_transport.setText(item);
            }
        });
        // 展示弹出框
        new android.support.v7.app.AlertDialog.Builder(ReleaseActivity.this)
                .setTitle("请选择类型").setView(outerView)
                .setPositiveButton("确定", null).show();


    }
    /*
     将相册返回的图片的路径转换为bitmap格式；
     */
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

    //点击返回键
    public void backImage(){
        finish();
    }
}
