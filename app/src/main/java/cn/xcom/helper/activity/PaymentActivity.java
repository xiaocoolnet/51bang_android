package cn.xcom.helper.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;

import java.util.Date;
import java.util.Map;

import cn.xcom.helper.PayNew.OrderInfoUtil2_0;
import cn.xcom.helper.PayNew.PayResult;
import cn.xcom.helper.R;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.utils.SingleVolleyRequest;
import cn.xcom.helper.utils.StringPostRequest;

/**
 * Created by zhuchongkun on 16/6/4.
 * 支付页
 */
public class PaymentActivity extends BaseActivity implements View.OnClickListener{
    private String TAG="PaymentActivity";
    private Context mContext;
    private RelativeLayout rl_back;
    private CheckBox cb_zhifubao,cb_weixin,cb_yinhangka;
    private TextView tv_price;
    private Button bt_submit;
    private IWXAPI iwxapi;
    private static final int SDK_PAY_FLAG = 1;
    /** 支付宝支付业务：入参app_id */
    public static final String APPID = "2016083001821606";
    // 商户私钥，pkcs8格式
    public static final String RSA_PRIVATE = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAM2arVc/DULOrVn5VVdkALDETOa1sznxviFaDD/+bZJI+3C7ls1HVZzKNCj7uwKKk02fRnLU70twRMifdhbIwEpqIFQLZW2HXbbif9+74BhetsQiQ/kyzRAhWEqeppNv/KTCtPM5d99S74diQuHHIH0cz4g7Xy9i/9RH8oS315bDAgMBAAECgYEAqpiO/3dXn3kxqRgS0aIuWH1oeX2GKqwE4FOBGpAXhmt8BfwAkm9//8pfISpN7zvgIWXo5Fr9+pA64mQ9bYZA1YDMLxcebn6uRqXZGoa0iZmx0n8/JpTw9L9A0Jt2HBJltrW5vsHqwOkjNL3sPxeeLOnNT9kVRSpp2gRFQuqZPoECQQDvYBpPUOeQ9KC1nouv3ngXOZg0Pw/vJbxaQWwqCAjN/l2m6sBjU6lP2dVB6QSVbD4V6rNABX63PW69uo8V5e1jAkEA2+ImfhcdSM1zS3QnKeyNd0HCKNvXXWXhjnAZ22pHI7tApIexsa/IlbQYNGbL14ZyRD6jq64P2FPwxt4hHUcfIQJAbSBdviz+9GlhXorh2ZJNIyFhjuf05qxIWskae2rgQLCmlzLL9DwuorWG8B4/tbL79tfhUd1vcC/0bVBAbNY+SwJACG2SrCKWrMOzN6EsHx9CDOAoYQiMKLhO/PavBwn70BLNV4Eb/oOOXK6afuexyIEOwC7mdx4k3VXaVMUO3+BqAQJAeMQtg/QEDZJb+frQLOlElYpsUS/J+bASiHHb6j0UTgUYfEtC34oJDd5lX1ZkiaQV5lnGUT8T0da+bzomkZ5xSA==";
    private PayReq req;
    private String price,tradeNumber,tradeNo;
    private boolean tag;
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Toast.makeText(PaymentActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        UpdateTradeState();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(PaymentActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                default:
                    break;
            }
        };
    };

    /**
     * 更新任务状态
     */
    private void UpdateTradeState() {
        String url= NetConstant.UPDATETASKPAY;
        StringPostRequest request=new StringPostRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d("更新支付状态", s);
                Toast.makeText(getApplication(), "订单生成！", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplication(),"网络错误，检查您的网络",Toast.LENGTH_SHORT).show();
            }
        });
        request.putValue("order_num",tradeNo);
        request.putValue("paytype","alipay");
        SingleVolleyRequest.getInstance(getApplication()).addToRequestQueue(request);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_payment);
        mContext=this;
        tag = false;
        getInfo();
        initView();
        checkPayType();
    }

    /**
     * 切换支付类型
     */
    private void checkPayType() {
        //1----支付宝
        cb_zhifubao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cb_zhifubao.isChecked()) {
                    cb_weixin.setChecked(false);
                    cb_yinhangka.setChecked(false);
                }
            }
        });
        //2----微信
        cb_weixin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cb_weixin.isChecked()) {
                    cb_zhifubao.setChecked(false);
                    cb_yinhangka.setChecked(false);
                }
            }
        });
        //3----银行卡
        cb_yinhangka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cb_yinhangka.isChecked()) {
                    cb_zhifubao.setChecked(false);
                    cb_weixin.setChecked(false);
                }
            }
        });
    }

    /**
     * 获取上页传来的任务价格和订单号
     */
    private void getInfo() {
        price = getIntent().getStringExtra("price");
        tradeNumber = getIntent().getStringExtra("tradeNo");
    }

    private void initView(){
        tv_price = (TextView) findViewById(R.id.tv_price);
        tv_price.setText("¥"+price);
        rl_back= (RelativeLayout) findViewById(R.id.rl_payment_back);
        rl_back.setOnClickListener(this);
        cb_zhifubao= (CheckBox) findViewById(R.id.cb_payment_zhifubao);
        cb_weixin= (CheckBox) findViewById(R.id.cb_payment_weixin);
        cb_yinhangka= (CheckBox) findViewById(R.id.cb_payment_yinhangka);
        bt_submit= (Button) findViewById(R.id.bt_payment_submit);
        bt_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_payment_back:
                finish();
                break;
            case R.id.bt_payment_submit:
                if(cb_zhifubao.isChecked()){
                    payV2();
                }
                break;
        }
    }

    /**
     * 支付宝支付业务
     */
    public void payV2() {
        if (TextUtils.isEmpty(APPID) || TextUtils.isEmpty(RSA_PRIVATE)) {
            new AlertDialog.Builder(this).setTitle("警告").setMessage("需要配置APPID | RSA_PRIVATE")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                            //
                            finish();
                        }
                    }).show();
            return;
        }
        tradeNo = getOutTradeNo(tradeNumber);
        Log.d("更新支付状态",tradeNo);
        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID,tradeNo,price);
        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);
        String sign = OrderInfoUtil2_0.getSign(params, RSA_PRIVATE);
        final String orderInfo = orderParam + "&" + sign;

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(PaymentActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Log.i("msp", result.toString());

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * 生成支付宝订单号
     * @return
     */
    private String getOutTradeNo(String str) {
        String key = "";
        if(tag){
            return tradeNo;
        }else {
            Date date = new Date();
            key = String.valueOf(date.getTime()/1000)+"_"+str;
            tag = true;
        }
        return key;
    }

}
