package cn.xcom.helper.wxapi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import cn.xcom.helper.HelperApplication;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.utils.SingleVolleyRequest;
import cn.xcom.helper.utils.StringPostRequest;
import cn.xcom.helper.utils.ToastUtils;

/**
 * Created by zhuchongkun.
 */
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String APP_ID="";
    private Context mContext;
    private IWXAPI iwxapi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=this;
        iwxapi= WXAPIFactory.createWXAPI(this,APP_ID,false);
        iwxapi.handleIntent(getIntent(),this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        iwxapi.handleIntent(getIntent(), this);
    }

    @Override
    public void onReq(BaseReq baseReq) {
        finish();
    }

    @Override
    public void onResp(BaseResp resp) {
        Log.e("pay","===enter");
        Log.e("pay", HelperApplication.getInstance().tradeNo);
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if ("0".equals(String.valueOf(resp.errCode))){
                ToastUtils.showToast(this, "支付成功");
                Log.e("pay", "success");
                UpdateTradeState();
            }else if ("-1".equals(String.valueOf(resp.errCode))){
                ToastUtils.showToast(this, "支付失败");

            }else if ("-2".equals(String.valueOf(resp.errCode))){
                ToastUtils.showToast(this, "取消支付");

            }
        }
    }

    /**
     * 更新任务状态
     */
    private void UpdateTradeState() {
        String url = "";
        if(HelperApplication.getInstance().payType.equals("1")){
            url= NetConstant.UPDATETASKPAY;
        }else if(HelperApplication.getInstance().payType.equals("2")){
            url = NetConstant.UPDATESHOPPAY;
        }
        StringPostRequest request=new StringPostRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d("更新支付状态", s);
                HelperApplication.getInstance().payType = "";
                HelperApplication.getInstance().tradeNo = "";
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplication(), "网络错误，检查您的网络", Toast.LENGTH_SHORT).show();
            }
        });
        request.putValue("order_num", HelperApplication.getInstance().tradeNo);
        request.putValue("paytype", "weixin");
        SingleVolleyRequest.getInstance(getApplication()).addToRequestQueue(request);
    }
}
