package cn.xcom.helper.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import cn.xcom.helper.R;
import cn.xcom.helper.net.HelperAsyncHttpClient;
import cn.xcom.helper.utils.Md5;
import cn.xcom.helper.utils.TimeUtils;
import cz.msebera.android.httpclient.Header;

/**
 * Created by zhuchongkun on 16/6/4.
 * 支付页
 */
public class PaymentActivity extends BaseActivity implements View.OnClickListener{
    private String TAG="PaymentActivity";
    private Context mContext;
    private RelativeLayout rl_back;
    private CheckBox cb_zhifubao,cb_weixin,cb_yinhangka;
    private Button bt_submit;
    private IWXAPI iwxapi;
    private static final String APP_ID="";
    private PayReq req;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_payment);
        mContext=this;
        initView();

    }

    private void initView(){
        rl_back= (RelativeLayout) findViewById(R.id.rl_payment_back);
        rl_back.setOnClickListener(this);
        cb_zhifubao= (CheckBox) findViewById(R.id.cb_payment_zhifubao);
        cb_weixin= (CheckBox) findViewById(R.id.cb_payment_weixin);
        cb_yinhangka= (CheckBox) findViewById(R.id.cb_payment_yinhangka);
        bt_submit= (Button) findViewById(R.id.bt_payment_submit);
        bt_submit.setOnClickListener(this);
        iwxapi= WXAPIFactory.createWXAPI(mContext,APP_ID,false);
        iwxapi.registerApp(APP_ID);
        req=new PayReq();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_payment_back:
                finish();
                break;
            case R.id.bt_payment_submit:
                Toast.makeText(mContext,"未开发",Toast.LENGTH_SHORT).show();
                break;
        }
    }
    private void paymentByWeixin(){
        String url="";
        RequestParams params=new RequestParams();
        params.put("","");
        HelperAsyncHttpClient.post(url,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response!=null){
                    try {
                        req.appId=APP_ID;
                        req.partnerId=response.getString("");
                        req.prepayId=response.getString("");
                        req.nonceStr=response.getString("");
                        req.timeStamp=""+TimeUtils.getNowTime()/1000;
                        req.packageValue="Sign=WXPay";
                        String a="appid="+req.appId+"&noncestr="+req.nonceStr+"&package="+req.packageValue
                                +"&partnerid="+req.partnerId+"&prepayid="+req.prepayId+"&timestamp="+req.timeStamp;
                        String signTemp=a+"&key="+APP_ID;
                        String sign= Md5.getMessageDigest(signTemp.getBytes()).toUpperCase();
                        req.sign=sign;
                        Toast.makeText(mContext,"正常调起支付",Toast.LENGTH_SHORT).show();
                        iwxapi.sendReq(req);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


    }
}
