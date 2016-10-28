package cn.xcom.helper.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.xcom.helper.R;
import cn.xcom.helper.WXpay.Constants;
import cn.xcom.helper.bean.UserInfo;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.utils.ToastUtils;
import cn.xcom.helper.view.SharePopupWindow;

public class ShareQRCodeActivity extends BaseActivity {
    @BindView(R.id.back)
    RelativeLayout back;
    @BindView(R.id.rl_share)
    RelativeLayout rlShare;
    @BindView(R.id.main_view)
    View mainView;
    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.tv_referral)
    TextView tvReferral;
    @BindView(R.id.ll_jizhi)
    LinearLayout llJizhi;
    @BindView(R.id.tv_jizhi)
    TextView tvJizhi;
    private Context context;
    SharePopupWindow takePhotoPopWin;
    IWXAPI msgApi;
    private UserInfo userInfo;
    private int wxflag = 0;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_share_qrcode);
        ButterKnife.bind(this);
        context = this;
        msgApi = WXAPIFactory.createWXAPI(context, Constants.APP_ID, false);
        msgApi.registerApp(Constants.APP_ID);
        userInfo = new UserInfo(context);
        userInfo.readData(context);
        setData();
    }

    /**
     * 显示数据
     */
    private void setData() {
        tvJizhi.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG ); //下划线
        tvJizhi.getPaint().setAntiAlias(true);//抗锯齿
        url = NetConstant.MY_QR_CODE + "&uid=" + userInfo.getUserId();
        tvReferral.setText(userInfo.getMyReferral());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.loadUrl(url);
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

    }

    public void showPopFormBottom(View view) {
        takePhotoPopWin = new SharePopupWindow(this, onClickListener);
        //SharePopupWindow takePhotoPopWin = new SharePopupWindow(this, onClickListener);
        takePhotoPopWin.showAtLocation(findViewById(R.id.rl_bottom), Gravity.BOTTOM, 0, 0);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.haoyou:
                    ToastUtils.showToast(ShareQRCodeActivity.this, "微信好友");
                    setting();
                    break;
                case R.id.dongtai:
                    ToastUtils.showToast(ShareQRCodeActivity.this, "微信朋友圈");
                    history();
                    break;
            }
        }
    };

    /**
     * 微信分享网页
     */
    private void shareWX() {
        //创建一个WXWebPageObject对象，用于封装要发送的Url
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = NetConstant.SHARE_QRCODE_H5 + userInfo.getUserId();
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = "我注册了51bang，发布了商品，来加入吧";
        msg.description = "基于同城个人，商户服务 。商品购买。给个人，商户提供交流与服务平台";
        Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.logo_wx);
        msg.setThumbImage(thumb);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = "weiyi";
        req.message = msg;
        req.scene = wxflag == 0 ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
        msgApi.sendReq(req);
    }

    /**
     * 分享到微信好友
     */
    private void setting() {
        //ToastUtils.ToastShort(this, "分享到微信好友");
        wxflag = 0;
        shareWX();
        takePhotoPopWin.dismiss();

    }

    /**
     * 分享到微信朋友圈
     */
    private void history() {
        // ToastUtils.ToastShort(this, "分享到微信朋友圈");
        wxflag = 1;
        shareWX();
        takePhotoPopWin.dismiss();
    }

    @OnClick({R.id.back, R.id.rl_share})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.rl_share:
                showPopFormBottom(view);
                break;
        }
    }

    @OnClick(R.id.ll_jizhi)
    public void onClick() {
        startActivity(new Intent(context,VIPArticleActivity.class));
    }
}
