package cn.xcom.helper.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import cn.xcom.helper.R;

/**
 * Created by zhuchongkun on 16/6/12.
 * 绑定帐号页
 */
public class BindAccountActivity extends BaseActivity implements View.OnClickListener{
    private String TAG="BindAccountActivity";
    private Context mContext;
    private RelativeLayout rl_back;
    private EditText et_account,et_password,et_name;
    private Button bt_bind;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_bind_account);
        mContext=this;
        initView();
    }

    private void initView(){
        rl_back= (RelativeLayout) findViewById(R.id.rl_bind_account_back);
        rl_back.setOnClickListener(this);
        et_account= (EditText) findViewById(R.id.et_bind_account_account);
        et_password= (EditText) findViewById(R.id.et_bind_account_password);
        et_name= (EditText) findViewById(R.id.et_bind_account_name);
        bt_bind= (Button) findViewById(R.id.bt_bind_account_bind);
        bt_bind.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_bind_account_back:
                finish();
                break;
            case R.id.bt_bind_account_bind:
                startActivity(new Intent(mContext,WithdrawCashActivity.class));
                break;
        }
    }
}
