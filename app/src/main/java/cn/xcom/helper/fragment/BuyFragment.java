package cn.xcom.helper.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.kyleduo.switchbutton.SwitchButton;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cn.xcom.helper.R;
import cn.xcom.helper.activity.AuthorizedActivity;
import cn.xcom.helper.bean.UserInfo;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.net.HelperAsyncHttpClient;
import cn.xcom.helper.utils.LogUtils;
import cz.msebera.android.httpclient.Header;

/**
 * Created by zhuchongkun on 16/5/27.
 * 主页面——抢单
 */
public class BuyFragment extends Fragment implements View.OnClickListener{
    private String TAG="BuyFragment";
    private Context mContext;
    private Button bt_authorized;
    private UserInfo userInfo;
    private SwitchButton sb;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_buy,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext=getActivity();
        initView();
    }
    private void initView(){
        bt_authorized= (Button) getView().findViewById(R.id.bt_fragment_buy_authorized);
        bt_authorized.setOnClickListener(this);
        sb= (SwitchButton) getView().findViewById(R.id.sb_fragment_buy);
        userInfo=new UserInfo(mContext);
    }

    @Override
    public void onResume() {
        super.onResume();
        userInfo.readData(mContext);
        getIdentity();

    }

    private void getIdentity(){
//        http://bang.xiaocool.net/index.php?g=apps&m=index&a=getAuthRecord&userid=1
        RequestParams params=new RequestParams();
        params.put("userid",userInfo.getUserId());
        HelperAsyncHttpClient.get(NetConstant.NET_GET_IDENTITY,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                LogUtils.e(TAG,"--statusCode->"+statusCode+"==>"+response.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                LogUtils.e(TAG,"--statusCode->"+statusCode+"==>"+responseString);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_fragment_buy_authorized:
                startActivity(new Intent(mContext, AuthorizedActivity.class));
                break;
        }

    }
}
