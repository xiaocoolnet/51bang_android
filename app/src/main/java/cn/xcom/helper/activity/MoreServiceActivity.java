package cn.xcom.helper.activity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.xcom.helper.R;
import cn.xcom.helper.bean.UserInfo;

/**
 * Created by zhuchongkun on 16/6/8.
 * 更多服务页
 */
public class MoreServiceActivity extends BaseActivity implements View.OnClickListener {
    private String TAG="MoreServiceActivity";
    private Context mContext;
    private RelativeLayout rl_back;
    private LinearLayout ll_training_course,ll_common_problem,ll_legal_agreement,ll_score,ll_clear_cache;
    private TextView tv_exit;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_more_service);
        mContext=this;
        initView();
    }

    private void initView(){
        rl_back= (RelativeLayout) findViewById(R.id.rl_more_service_back);
        rl_back.setOnClickListener(this);
        ll_training_course= (LinearLayout) findViewById(R.id.ll_more_service_training_course);
        ll_training_course.setOnClickListener(this);
        ll_common_problem= (LinearLayout) findViewById(R.id.ll_more_service_common_problem);
        ll_common_problem.setOnClickListener(this);
        ll_legal_agreement= (LinearLayout) findViewById(R.id.ll_more_service_legal_agreement);
        ll_legal_agreement.setOnClickListener(this);
        ll_score= (LinearLayout) findViewById(R.id.ll_more_service_score);
        ll_score.setOnClickListener(this);
        ll_clear_cache= (LinearLayout) findViewById(R.id.ll_more_service_clear_cache);
        ll_clear_cache.setOnClickListener(this);
        tv_exit= (TextView) findViewById(R.id.tv_more_service_exit);
        tv_exit.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_more_service_back:
                finish();
                break;
            case R.id.ll_more_service_training_course:
                startActivity(new Intent(mContext,TrainingCourseActivity.class));
                break;
            case R.id.ll_more_service_common_problem:
                startActivity(new Intent(mContext,CommonProblemActivity.class));
                break;
            case R.id.ll_more_service_legal_agreement:
                startActivity(new Intent(mContext,LegalAgreementActivity.class));
                break;
            case R.id.ll_more_service_score:
                toScore();
                break;
            case R.id.ll_more_service_clear_cache:
                Toast.makeText(mContext,"未开发",Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_more_service_exit:
                toExit();
                break;
        }
    }
    private void toScore(){
        try{
            Uri uri=Uri.parse("market://details?id="+getPackageName());
            Intent intent =new Intent(Intent.ACTION_VIEW,uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }catch (ActivityNotFoundException e){
            Toast.makeText(mContext,"未发现应用市场",Toast.LENGTH_SHORT).show();
        }
    }
    private void toExit(){
        final AlertDialog.Builder builder=new AlertDialog.Builder(mContext);
        builder.setMessage("确认退出吗？");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                UserInfo userInfo=new UserInfo();
                userInfo.clearDataExceptPhone(mContext);
                startActivity(new Intent(mContext,LoginActivity.class));
                finish();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();

    }
}
