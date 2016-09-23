package cn.xcom.helper.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.xcom.helper.R;
import cn.xcom.helper.adapter.ViewPageAdapter;
import cn.xcom.helper.bean.AppInfo;
import cn.xcom.helper.bean.UserInfo;

/**
 * Created by zhuchongkun on 16/6/17.
 */
public class SplashActivity extends BaseActivity  implements View.OnClickListener,ViewPager.OnPageChangeListener{
    private String TAG="SplashActivity";
    private Context mContext;
    private ViewPager viewPager;
    private AppInfo appInfo;
    private ViewPageAdapter viewPageAdapter;
    private List<ImageView> views;
    private static final int[] pics = new int[]{R.mipmap.ic_splash_welcome_first, R.mipmap.ic_splash_welcome_second, R.mipmap.ic_splash_welcome_third};
    private LinearLayout ll_point;
    private TextView tv;
    private ImageView[] points;
    private int currentIndex;
    private UserInfo userInfo;
    SharedPreferences sp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mContext = this;
        View view = View.inflate(mContext, R.layout.activity_splash, null);
        setContentView(view);
        //判断是否是第一次进入此版本
        appInfo = new AppInfo();
        if (appInfo.isFristLogin(mContext)) {
            //是，则viewPager
            initViewPager();
        } else {
            //否，单页动画
            initLoding(view);
        }
    }
    private void initViewPager() {
        views = new ArrayList<ImageView>();
        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        for (int i = 0; i < pics.length; i++) {
            ImageView iv = new ImageView(mContext);
            iv.setLayoutParams(mParams);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            iv.setImageResource(pics[i]);
            views.add(i, iv);
        }
        viewPager = (ViewPager) findViewById(R.id.viewpager_splash);
        viewPager.setVisibility(View.VISIBLE);
        viewPageAdapter = new ViewPageAdapter(views);
        viewPager.setAdapter(viewPageAdapter);
        viewPager.setOnPageChangeListener(this);
        tv = (TextView)findViewById(R.id.tv_splash_into);
        tv.setVisibility(View.GONE);
        tv.setOnClickListener(this);
        ll_point= (LinearLayout) findViewById(R.id.ll_splash);
        ll_point.setVisibility(View.VISIBLE);
        points=new ImageView[pics.length];
        for (int i=0;i<pics.length;i++){
            points[i]= (ImageView) ll_point.getChildAt(i);
            points[i].setEnabled(true);
            points[i].setOnClickListener(this);
            points[i].setTag(i);
        }
        currentIndex=0;
        points[currentIndex].setEnabled(false);
    }

    private void initLoding(View view) {
        view.setBackgroundResource(R.mipmap.ic_splash_loading);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.3f, 1.0f);
        alphaAnimation.setDuration(1500);
        view.startAnimation(alphaAnimation);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                LoginIn();
            }
        }, 3000);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_splash_into:
                try {
                    PackageManager pm = mContext.getPackageManager();
                    PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(), 0);
                    String currentVersion = "" + pi.versionCode;
                    appInfo.setLastVersionCode(currentVersion);
                    appInfo.writeData(mContext);
                    userInfo = new UserInfo(mContext);
                  if (userInfo.isLogined()) {
                        LoginIn();
                    } else {
                        startActivity(new Intent(mContext, LoginActivity.class));
                        finish();
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }

                break;
            default:
                int position = (Integer) v.getTag();
                if (position >= 0 && position < pics.length) {
                    viewPager.setCurrentItem(position);
                    setCurrentDat(position);
                }
                break;
        }

    }
    private void LoginIn(){
            UserInfo userInfo=new UserInfo(mContext);
            userInfo.readData(mContext);
            if (!userInfo.getUserId().isEmpty()){
                startActivity(new Intent(mContext,HomeActivity.class));
                /*RequestParams params=new RequestParams();
                params.put("phone",userInfo.getUserPhone());
                params.put("password",userInfo.getUserPassword());
                HelperAsyncHttpClient.get(NetConstant.NET_LOGIN,params,new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        LogUtils.e(TAG,"--statusCode->"+statusCode+"==>"+response.toString());
                        if (response!=null){
                            try {
                                String state=response.getString("status");
                                if (state.equals("success")){
                                    JSONObject jsonObject=response.getJSONObject("data");
                                    UserInfo userInfo=new UserInfo(mContext);
                                    userInfo.setUserId(jsonObject.getString("id"));
                                    userInfo.setUserName(jsonObject.getString("name"));
                                    userInfo.setUserImg(jsonObject.getString("photo"));
                                    userInfo.setUserAddress(jsonObject.getString("address"));
                                    userInfo.setUserID(jsonObject.getString("idcard"));
                                    userInfo.setUserPhone(jsonObject.getString("phone"));
                                    userInfo.writeData(mContext);
                                    userInfo.setUserGender(jsonObject.getString("sex"));
                                    SharedPreferences.Editor editor = sp.edit();
                                    editor.putString("password",userInfo.getUserPassword());
                                    startActivity(new Intent(mContext,HomeActivity.class));
                                    finish();
                                }if(state.equals("error")){
                                    String data=response.getString("data");
                                    Toast.makeText(mContext,data,Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(mContext,LoginActivity.class));
                                    finish();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });*/
            }else{
                startActivity(new Intent(mContext,LoginActivity.class));
                finish();
            }

    }
    private void setCurrentDat(int i) {
        if (i<0||i>pics.length-1||currentIndex==i){
            return;
        }
        points[i].setEnabled(false);
        points[currentIndex].setEnabled(true);
        currentIndex = i;
        if (i == pics.length - 1) {
            ll_point.setVisibility(View.GONE);
            tv.setVisibility(View.VISIBLE);
        } else {
            ll_point.setVisibility(View.VISIBLE);
            tv.setVisibility(View.GONE);
        }
    }
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        setCurrentDat(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
