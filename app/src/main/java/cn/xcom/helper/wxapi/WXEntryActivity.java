package cn.xcom.helper.wxapi;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * Created by zhuchongkun
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private static final String APP_ID="";
    private IWXAPI iwxapi;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=this;
        iwxapi= WXAPIFactory.createWXAPI(this,APP_ID,false);
        iwxapi.registerApp(APP_ID);
        iwxapi.handleIntent(getIntent(),this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {

    }
}
