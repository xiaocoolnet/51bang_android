package cn.xcom.helper.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.xcom.helper.R;

/**
 * Created by zhuchongkun on 16/6/17.
 */
public class EditAddressActivity extends BaseActivity implements View.OnClickListener {
    private String TAG="EditAddressActivity";
    private Context mContext;
    private RelativeLayout rl_back,rl_save;
    private EditText et_consignee,et_phone,et_detail;
    private LinearLayout ll_city,ll_street;
    private TextView tv_city,tv_street;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_edit_address);
        mContext=this;
        initView();
    }

    private void  initView(){
        rl_back= (RelativeLayout) findViewById(R.id.rl_edit_address_back);
        rl_back.setOnClickListener(this);
        rl_save= (RelativeLayout) findViewById(R.id.rl_edit_address_save);
        rl_save.setOnClickListener(this);
        et_consignee= (EditText) findViewById(R.id.et_edit_address_consignee);
        et_phone= (EditText) findViewById(R.id.et_edit_address_phone);
        et_detail= (EditText) findViewById(R.id.et_edit_address_detail);
        tv_city= (TextView) findViewById(R.id.tv_edit_address_city);
        tv_street= (TextView) findViewById(R.id.tv_edit_address_street);
        ll_city= (LinearLayout) findViewById(R.id.ll_edit_address_city);
        ll_city.setOnClickListener(this);
        ll_street= (LinearLayout) findViewById(R.id.ll_edit_address_street);
        ll_street.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_edit_address_back:
                finish();
                break;
            case R.id.rl_edit_address_save:
                Toast.makeText(mContext,"未开发",Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_edit_address_city:
                Toast.makeText(mContext,"未开发",Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_edit_address_street:
                Toast.makeText(mContext,"未开发",Toast.LENGTH_SHORT).show();
                break;
        }

    }
}
