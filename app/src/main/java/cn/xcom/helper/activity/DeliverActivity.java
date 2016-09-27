package cn.xcom.helper.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import cn.xcom.helper.R;
import cn.xcom.helper.constant.NetConstant;

public class DeliverActivity extends AppCompatActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deliver);
        getSupportActionBar().hide();
        webView= (WebView) findViewById(R.id.webView);
        webView.loadUrl(NetConstant.DELIEVER);
    }
}
