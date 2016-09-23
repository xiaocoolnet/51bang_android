package cn.xcom.helper.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import cn.xcom.helper.R;

/**
 * Created by zhuchongkun on 16/6/12.
 * 提现页
 */
public class WithdrawCashActivity extends BaseActivity implements View.OnClickListener{
    private String TAG="WithdrawCashActivity";
    private Context mContext;
    private RelativeLayout rl_back;
    private EditText et_money,et_account;
    private Button bt_present;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_withdraw_cash);
        mContext=this;
        initView();
    }

    private void initView(){
        rl_back= (RelativeLayout) findViewById(R.id.rl_withdraw_cash_back);
        rl_back.setOnClickListener(this);
        et_money= (EditText) findViewById(R.id.et_withdraw_cash_money);
        et_account= (EditText) findViewById(R.id.et_withdraw_cash_account);
        bt_present= (Button) findViewById(R.id.bt_withdraw_cash_present);
        bt_present.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_withdraw_cash_back:
                finish();
                break;
            case R.id.bt_withdraw_cash_present:
                Toast.makeText(mContext,"未开发",Toast.LENGTH_SHORT).show();
                break;
        }

    }
}
