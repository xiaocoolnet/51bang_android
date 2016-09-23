package cn.xcom.helper.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;

import cn.xcom.helper.R;

/**
 * Created by zhuchongkun on 16/6/12.
 * 收藏页
 */
public class CollectionActivity extends BaseActivity implements View.OnClickListener{
    private String TAG="";
    private Context mContext;
    private RelativeLayout rl_back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_collection);
        mContext=this;
        initView();
    }

    private void initView(){
        rl_back= (RelativeLayout) findViewById(R.id.rl_collection_back);
        rl_back.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_collection_back:
                finish();
                break;
        }

    }
}
