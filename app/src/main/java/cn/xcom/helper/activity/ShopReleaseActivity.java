package cn.xcom.helper.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.RelativeLayout;

import cn.xcom.helper.R;

public class ShopReleaseActivity extends BaseActivity {
    private ListView listView;
    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_release);
        relativeLayout= (RelativeLayout) findViewById(R.id.back);
        listView= (ListView) findViewById(R.id.shop_list);

    }
}
