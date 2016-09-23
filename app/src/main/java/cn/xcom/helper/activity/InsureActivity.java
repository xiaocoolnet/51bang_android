package cn.xcom.helper.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.xcom.helper.R;

/**
 * Created by zhuchongkun on 16/6/12.
 * 投保页
 */
public class InsureActivity extends BaseActivity implements View.OnClickListener{
    private String TAG="InsureActivity";
    private Context mContext;
    private RelativeLayout rl_back,rl_state;
    private TextView tv_state;
    private Button bt_insure;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_insure);
        mContext=this;
        initView();
    }

    private void initView(){
        rl_back= (RelativeLayout) findViewById(R.id.rl_insure_back);
        rl_back.setOnClickListener(this);
        rl_state= (RelativeLayout) findViewById(R.id.rl_insure_state);
        tv_state= (TextView) findViewById(R.id.tv_insure_state);
        bt_insure= (Button) findViewById(R.id.bt_insure);
        bt_insure.setOnClickListener(this);
        rl_state.setBackgroundResource(R.color.colorTextGray);
        tv_state.setText(getString(R.string.tv_insure_state_no));

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_insure_back:
                finish();
                break;
            case R.id.bt_insure:
                Toast.makeText(mContext,"未开发",Toast.LENGTH_SHORT).show();
                rl_state.setBackgroundResource(R.color.colorTheme);
                tv_state.setText(getString(R.string.tv_insure_state_yes));
                bt_insure.setSelected(true);
                bt_insure.setClickable(false);
                break;
        }

    }
}
