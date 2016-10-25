package cn.xcom.helper.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import cn.xcom.helper.R;
import cn.xcom.helper.adapter.GridViewAdapter;
import cn.xcom.helper.bean.UserInfo;
import cn.xcom.helper.constant.HelperConstant;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.net.HelperAsyncHttpClient;
import cn.xcom.helper.utils.GalleryFinalUtil;
import cn.xcom.helper.utils.PushImage;
import cn.xcom.helper.utils.PushImageUtil;
import cn.xcom.helper.utils.SPUtils;
import cz.msebera.android.httpclient.Header;

/**
 * Created by zhuchongkun on 16/6/12.
 * 投保页
 */
public class InsureActivity extends BaseActivity implements View.OnClickListener{
    private final int REQUEST_CODE_GALLERY = 1001;

    private String TAG="InsureActivity";
    private Context mContext;
    private RelativeLayout rl_back,rl_state;
    private TextView tv_state;
    private Button bt_insure;
    private UserInfo userInfo;
    private ImageView insureImg;
    private GalleryFinalUtil galleryFinalUtil;
    private ArrayList<PhotoInfo> mPhotoList;
    private List<String> nameList;
    private ProgressDialog progressDialog;
    private String time;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_insure);
        mContext=this;
        userInfo = new UserInfo(this);
        initView();
        galleryFinalUtil = new GalleryFinalUtil(1);
        mPhotoList = new ArrayList<>();
        nameList = new ArrayList<>();
        checkInsurance();
    }

    private void initView(){
        rl_back= (RelativeLayout) findViewById(R.id.rl_insure_back);
        rl_back.setOnClickListener(this);
        rl_state= (RelativeLayout) findViewById(R.id.rl_insure_state);
        tv_state= (TextView) findViewById(R.id.tv_insure_state);
        bt_insure= (Button) findViewById(R.id.bt_insure);
        bt_insure.setOnClickListener(this);
        insureImg  = (ImageView) findViewById(R.id.iv_insure);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_insure_back:
                finish();
                break;
            case R.id.bt_insure:
                galleryFinalUtil.openAblum(mContext,mPhotoList,REQUEST_CODE_GALLERY,mOnHanlderResultCallback);
                break;
        }
    }

    private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {
        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
            if (resultList != null) {
                mPhotoList.clear();
                mPhotoList.addAll(resultList);
//                uploadImg();
                Intent intent = new Intent(mContext,InsureDetailActivity.class);
                intent.putExtra("url",mPhotoList.get(0).getPhotoPath());
                startActivityForResult(intent,1);
            }
        }
        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            Toast.makeText(InsureActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
        }
    };

    private void checkInsurance(){
        RequestParams params=new RequestParams();
        params.put("userid",userInfo.getUserId());
        HelperAsyncHttpClient.get(NetConstant.Check_Insurance, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if(response.optString("status").equals("success")){
                    String data = response.optString("data");
                    SPUtils.put(mContext, HelperConstant.IS_INSURANCE,response.optString("data"));
                    if(data.equals("-1")){
                        rl_state.setBackgroundResource(R.color.colorTheme);
                        tv_state.setText("认证中");
                        bt_insure.setVisibility(View.GONE);
                        insureImg.setVisibility(View.GONE);
                    }else if(data.equals("1")){
                        rl_state.setBackgroundResource(R.color.colorTheme);
                        tv_state.setText(getString(R.string.tv_insure_state_yes));
                        bt_insure.setVisibility(View.GONE);
                        insureImg.setVisibility(View.GONE);
                    }else {
                        rl_state.setBackgroundResource(R.color.colorTextGray);
                        tv_state.setText(getString(R.string.tv_insure_state_no));
                        bt_insure.setVisibility(View.VISIBLE);
                    }
                }else{
                    rl_state.setBackgroundResource(R.color.colorTextGray);
                    tv_state.setText(getString(R.string.tv_insure_state_no));
                    bt_insure.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });

    }

    private void uploadImg(){
        progressDialog.setMessage("正在上传图片");
        progressDialog.show();
        new PushImageUtil().setPushIamge(getApplication(),mPhotoList,nameList, new PushImage() {

            @Override
            public void success(boolean state) {
                updateInsure();
            }

            @Override
            public void error() {
                progressDialog.dismiss();
            }
        });


    }

    private void updateInsure(){
        RequestParams params=new RequestParams();
        params.put("userid",userInfo.getUserId());
        params.put("photo",nameList.get(0));
        params.put("expirydate",time);
        HelperAsyncHttpClient.get(NetConstant.Check_Insurance, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                progressDialog.dismiss();
                Toast.makeText(mContext,"上传成功",Toast.LENGTH_SHORT).show();
                rl_state.setBackgroundResource(R.color.colorTheme);
                tv_state.setText("认证中");
                bt_insure.setVisibility(View.GONE);
                insureImg.setVisibility(View.GONE);
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                progressDialog.dismiss();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==1){
            if ((resultCode == RESULT_OK)){
                 time = data.getStringExtra("time");
                uploadImg();
            }
        }
    }
}
