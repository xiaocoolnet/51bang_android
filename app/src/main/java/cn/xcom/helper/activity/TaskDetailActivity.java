package cn.xcom.helper.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.xcom.helper.R;
import cn.xcom.helper.bean.TaskInfo;
import de.hdodenhof.circleimageview.CircleImageView;

public class TaskDetailActivity extends BaseActivity {

    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.iv_avatar)
    CircleImageView ivAvatar;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.rl_dianhua)
    RelativeLayout rlDianhua;
    @BindView(R.id.rl_xiaoxi)
    RelativeLayout rlXiaoxi;
    @BindView(R.id.tv_tradeNo)
    TextView tvTradeNo;
    @BindView(R.id.tv_tradeName)
    TextView tvTradeName;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_time)
    TextView tvTime;
    private Context context;
    private TaskInfo taskInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_task_detail);
        ButterKnife.bind(this);
        context = this;
        taskInfo = (TaskInfo) getIntent().getSerializableExtra("taskInfo");
        setData();
    }

    /**
     * 为view设置数据
     */
    private void setData() {
        tvName.setText(taskInfo.getName());
        tvTradeNo.setText(taskInfo.getOrder_num());
        tvTradeName.setText(taskInfo.getDescription());
        tvPrice.setText(taskInfo.getPrice());
        tvAddress.setText(taskInfo.getAddress());
        Date date = new Date();
        date.setTime(Long.parseLong(taskInfo.getExpirydate())*1000);
        tvTime.setText(new SimpleDateFormat("MM月dd日  HH:mm").format(date));
    }

    @OnClick({R.id.rl_back, R.id.rl_dianhua, R.id.rl_xiaoxi})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.rl_dianhua:
                break;
            case R.id.rl_xiaoxi:
                break;
        }
    }
}
