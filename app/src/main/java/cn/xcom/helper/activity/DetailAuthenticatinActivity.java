package cn.xcom.helper.activity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.xcom.helper.R;
import cn.xcom.helper.bean.AuthenticationList;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.utils.CommonAdapter;
import cn.xcom.helper.utils.MyImageLoader;
import cn.xcom.helper.utils.SingleVolleyRequest;
import cn.xcom.helper.utils.StringPostRequest;
import cn.xcom.helper.utils.ViewHolder;
import cn.xcom.helper.view.NoScrollListView;

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
    @BindView(R.id.sale_detail_comment)
    NoScrollListView saleDetailComment;
    @BindView(R.id.tv_distance)
    TextView tvDistance;
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
        getSkills();
        setData();
    }

    /**
     * 为布局设置值
     */
    private void setData() {
        MyImageLoader.display(NetConstant.NET_DISPLAY_IMG + authenticationList.getPhoto(), ivAvatar);
        tvName.setText(authenticationList.getName());
        tvServiceState.setText(authenticationList.getIsworking().equals("1") ? "服务中" : "未服务");
        tvServiceCount.setText(authenticationList.getServiceCount());
        tvPaiming.setText(authenticationList.getRanking() + "");
        tvDistance.setText(authenticationList.getAddress());

        saleDetailComment.setAdapter(new CommonAdapter<AuthenticationList.EvaluatelistBean>(context, authenticationList.getEvaluatelist(), R.layout.item_comment_info) {
            @Override
            public void convert(ViewHolder holder, AuthenticationList.EvaluatelistBean evaluatelistBean) {
                holder.setImageByUrl(R.id.iv_avatar, evaluatelistBean.getPhoto())
                        .setText(R.id.tv_name, evaluatelistBean.getName())
                        .setTimeText(R.id.tv_time, evaluatelistBean.getAdd_time())
                        .setText(R.id.tv_content, evaluatelistBean.getContent());
                RatingBar ratingBar = holder.getView(R.id.rating_bar);
                ratingBar.setNumStars(Integer.valueOf(evaluatelistBean.getScore()));
            }
        });
    }


    /**
     * 接受intent传入的值
     */
    private void getData() {
        authenticationList = (AuthenticationList) getIntent().getSerializableExtra("authentication");
    }


    private void getSkills() {
        Log.d("testid",authenticationList.getId());
        String url = NetConstant.GET_SKILLS_BY_USERID;
        StringPostRequest request = new StringPostRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(s);
                    String status = jsonObject.getString("status");
                    if(status.equals("success")){
                        String data = jsonObject.getString("data");
                        JSONObject jo = new JSONObject(data);
                        String skillStr = jo.getString("skilllist");
                        Gson gson = new Gson();
                        List <AuthenticationList.SkilllistBean> skills = gson.fromJson(skillStr,
                                new TypeToken<ArrayList<AuthenticationList.SkilllistBean>>(){}.getType());
                        gridViewSkill.setSelector(new ColorDrawable(Color.TRANSPARENT));
                        gridViewSkill.setAdapter(new CommonAdapter<AuthenticationList.SkilllistBean>(context, skills, R.layout.item_skill_tag) {
                            @Override
                            public void convert(ViewHolder holder, AuthenticationList.SkilllistBean skilllistBean) {
                                holder.setText(R.id.tv_item_help_me_skill_tag, skilllistBean.getTypename());
                            }
                        });
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        request.putValue("userid", authenticationList.getId());
        SingleVolleyRequest.getInstance(context).addToRequestQueue(request);

    }


    @OnClick(R.id.rl_back)
    public void onClick() {
        finish();
    }
}
