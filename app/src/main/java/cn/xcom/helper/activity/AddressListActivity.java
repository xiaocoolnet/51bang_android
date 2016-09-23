package cn.xcom.helper.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;

import cn.xcom.helper.R;

/**
 * Created by zhuchongkun on 16/6/17.
 */
public class AddressListActivity extends BaseActivity implements View.OnClickListener {
    private String TAG="AddressListActivity";
    private Context mContext;
    private RelativeLayout rl_back;
    private Button bt_address;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_address_list);
        mContext=this;
        initView();
    }
    private void initView(){
        rl_back= (RelativeLayout) findViewById(R.id.rl_address_list_back);
        rl_back.setOnClickListener(this);
        bt_address= (Button) findViewById(R.id.bt_address_list_add);
        bt_address.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_address_list_back:
                finish();
                break;
            case R.id.bt_address_list_add:
                startActivity(new Intent(mContext,EditAddressActivity.class));
                break;
        }
    }
}
