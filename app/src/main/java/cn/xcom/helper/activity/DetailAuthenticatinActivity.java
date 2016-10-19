package cn.xcom.helper.activity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.xcom.helper.R;
import cn.xcom.helper.bean.AuthenticationList;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.utils.CommonAdapter;
import cn.xcom.helper.utils.MyImageLoader;
import cn.xcom.helper.utils.ViewHolder;

public class DetailAuthenticatinActivity extends BaseActivity {

    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_service_state)
    TextView tvServiceState;
    @BindView(R.id.tv_service_count)
    TextView tvServiceCount;
    @BindView(R.id.tv_paiming)
    TextView tvPaiming;
    @BindView(R.id.gridView_skill)
    GridView gridViewSkill;
    private Context context;
    private AuthenticationList authenticationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_detail_authenticatin);
        ButterKnife.bind(this);
        context = this;
        getData();
        setData();
    }

    /**
     * 为布局设置值
     */
    private void setData() {
        MyImageLoader.display(NetConstant.NET_DISPLAY_IMG+authenticationList.getPhoto(),ivAvatar);
        tvName.setText(authenticationList.getName());
        tvServiceState.setText(authenticationList.getIsworking().equals("1") ? "服务中" : "未服务");
        tvServiceCount.setText(authenticationList.getServiceCount());
        gridViewSkill.setSelector(new ColorDrawable(Color.TRANSPARENT));
        gridViewSkill.setAdapter(new CommonAdapter<AuthenticationList.SkilllistBean>(context,authenticationList.getSkilllist(),R.layout.item_skill_tag) {
            @Override
            public void convert(ViewHolder holder, AuthenticationList.SkilllistBean skilllistBean) {
                holder.setText(R.id.tv_item_help_me_skill_tag,skilllistBean.getTypename());
            }
        });
    }

    /**
     * 接受intent传入的值
     */
    private void getData() {
        authenticationList = (AuthenticationList) getIntent().getSerializableExtra("authentication");
    }

    @OnClick(R.id.rl_back)
    public void onClick() {
        finish();
    }
}
