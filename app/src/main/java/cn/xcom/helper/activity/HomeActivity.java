package cn.xcom.helper.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;
import cn.xcom.helper.HelperApplication;
import cn.xcom.helper.R;
import cn.xcom.helper.bean.UserInfo;
import cn.xcom.helper.constant.HelperConstant;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.fragment.BuyFragment;
import cn.xcom.helper.fragment.MapFragment;
import cn.xcom.helper.fragment.MeFragment;
import cn.xcom.helper.fragment.SaleFragment;
import cn.xcom.helper.net.HelperAsyncHttpClient;
import cn.xcom.helper.utils.SPUtils;
import cz.msebera.android.httpclient.Header;

/**
 * Created by zhuchongkun on 16/5/27.
 * 主页面
 */
public class HomeActivity extends BaseActivity{
    private Context mContext;
    private Button[] mTabs;
    private TextView unReadMap, unReadBuy, unReadSale,unReadMe;
    private MapFragment mapFragment;
    private BuyFragment buyFragment;
    private SaleFragment saleFragment;
    private MeFragment meFragment;
    private Fragment[] fragments;
    private UserInfo userInfo;

    private int a=0;
    private int index;
    private int currentTanIndex;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home);
        mContext=this;
        userInfo = new UserInfo(mContext);
        userInfo.readData(mContext);
        initView();
        initFragment();
    }
    private void initView(){
        unReadMap = (TextView) findViewById(R.id.tv_home_map_red);
        unReadBuy = (TextView) findViewById(R.id.tv_home_buy_red);
        unReadSale = (TextView) findViewById(R.id.tv_home_sale_red);
        unReadMe= (TextView) findViewById(R.id.tv_home_me_red);
        mTabs=new Button[4];
        mTabs[0]= (Button) findViewById(R.id.bt_home_map);
        mTabs[1]= (Button) findViewById(R.id.bt_home_buy);
        mTabs[2]= (Button) findViewById(R.id.bt_home_sale);
        mTabs[3]= (Button) findViewById(R.id.bt_home_me);
        mTabs[a].setSelected(true);
    }
    private void initFragment(){
        mapFragment=new MapFragment();
        buyFragment=new BuyFragment();
        saleFragment=new SaleFragment();
        meFragment=new MeFragment();
        fragments=new Fragment[]{mapFragment,buyFragment,saleFragment,meFragment};
        switch (a){
            case 0:
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.rl_home_fragment_container,mapFragment)
                        .add(R.id.rl_home_fragment_container,buyFragment)
                        .hide(buyFragment)
                        .show(mapFragment).commit();
                break;
            case 1:
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.rl_home_fragment_container,mapFragment)
                        .add(R.id.rl_home_fragment_container,buyFragment)
                        .add(R.id.rl_home_fragment_container,saleFragment)
                        .hide(mapFragment)
                        .show(buyFragment)
                        .hide(saleFragment).commit();
                break;
            case 2:
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.rl_home_fragment_container,buyFragment)
                        .add(R.id.rl_home_fragment_container,saleFragment)
                        .add(R.id.rl_home_fragment_container,meFragment)
                        .hide(buyFragment)
                        .show(saleFragment)
                        .hide(meFragment).commit();
                break;
            case 3:
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.rl_home_fragment_container,saleFragment)
                        .add(R.id.rl_home_fragment_container,meFragment)
                        .hide(saleFragment)
                        .show(meFragment).commit();
                break;

        }

    }
    public void onTabClicked(View view){
        switch (view.getId()){
            case R.id.bt_home_map:
                index=0;
                break;
            case R.id.bt_home_buy:
                index=1;
                break;
            case R.id.bt_home_sale:
                index=2;
                break;
            case R.id.bt_home_me:
                index=3;
                break;
        }
        if (currentTanIndex!=index){
            FragmentTransaction trx =getSupportFragmentManager().beginTransaction();
            trx.hide(fragments[currentTanIndex]);
            if(!fragments[index].isAdded()){
                trx.add(R.id.rl_home_fragment_container,fragments[index]);
            }
            trx.show(fragments[index]).commit();
        }
        mTabs[currentTanIndex].setSelected(false);
        mTabs[index].setSelected(true);
        currentTanIndex=index;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getInsurance();
        if(HelperApplication.getInstance().flag.equals("true")){
            popDialog();
        }
    }

    /**
     * 弹出退出提示框
     */
    private void popDialog() {
        final AlertView mAlertView = new AlertView("提示", "您的账号在另一个设备登录，请重新登陆", null, new String[]{"确定"}, null, this, AlertView.Style.Alert, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {
                UserInfo userInfo=new UserInfo();
                userInfo.clearDataExceptPhone(mContext);
                SPUtils.clear(mContext);
                JPushInterface.stopPush(mContext);
                startActivity(new Intent(mContext, LoginActivity.class));
                HelperApplication.getInstance().onTerminate();
            }
        });
        mAlertView.setCancelable(false);
        mAlertView.show();
    }

    /**
     * 获取保险认证
     */
    private void getInsurance() {
        RequestParams params=new RequestParams();
        params.put("userid",userInfo.getUserId());
        HelperAsyncHttpClient.get(NetConstant.Check_Insurance, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.e("认证",response.toString());
                if(response.optString("status").equals("success")){
                    SPUtils.put(mContext,HelperConstant.IS_INSURANCE,response.optString("data"));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.e("认证", responseString);
            }
        });
    }
}
