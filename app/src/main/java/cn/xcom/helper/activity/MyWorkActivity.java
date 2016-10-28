package cn.xcom.helper.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

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
import cn.xcom.helper.bean.UserInfo;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.utils.CommonAdapter;
import cn.xcom.helper.utils.SingleVolleyRequest;
import cn.xcom.helper.utils.StringPostRequest;
import cn.xcom.helper.utils.ToastUtil;
import cn.xcom.helper.utils.ViewHolder;

public class MyWorkActivity extends BaseActivity {

    @BindView(R.id.rl_wallet_back)
    RelativeLayout rlWalletBack;
    @BindView(R.id.lv_authentication)
    ListView lvAuthentication;
    @BindView(R.id.srl_authentication)
    SwipeRefreshLayout srlAuthentication;
    private Context context;
    private CommonAdapter<AuthenticationList> adapter;
    private List<AuthenticationList> authenticationLists;
    private UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_my_work);
        ButterKnife.bind(this);
        context = this;
        userInfo = new UserInfo(context);
        userInfo.readData(context);
        authenticationLists = new ArrayList<>();
        setRefresh();
        lvAuthentication.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MyWorkActivity.this,DetailAuthenticatinActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("authentication",authenticationLists.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    /**
     * 设置下拉刷新
     */
    private void setRefresh() {
        srlAuthentication.setColorSchemeResources(R.color.background_white);
        srlAuthentication.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.colorTheme));
        srlAuthentication.setProgressViewOffset(true, 10, 100);
        srlAuthentication.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    /**
     * 加载数据
     */
    private void getData() {
        String url = NetConstant.GET_MY_WORK;
        StringPostRequest request = new StringPostRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String status = jsonObject.getString("status");
                    if (status.equals("success")) {
                        srlAuthentication.setRefreshing(false);
                        setAdapter(jsonObject);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtil.Toast(context, "网络错误，请检查");
                srlAuthentication.setRefreshing(false);
            }
        });
        request.putValue("userid", userInfo.getUserId());
        SingleVolleyRequest.getInstance(context).addToRequestQueue(request);
    }

    /**
     * 设置适配器
     *
     * @param jsonObject
     */
    private void setAdapter(JSONObject jsonObject) {
        authenticationLists.clear();
        authenticationLists.addAll(getBeanFromJson(jsonObject));
        if(adapter!=null){
            adapter.notifyDataSetChanged();
        }else{
            adapter = new CommonAdapter<AuthenticationList>(context, authenticationLists, R.layout.item_authentication_info) {
                @Override
                public void convert(ViewHolder holder, AuthenticationList authenticationList) {
                    holder.setImageByUrl(R.id.iv_avatar, authenticationList.getPhoto())
                            .setText(R.id.tv_name, authenticationList.getName())
                            .setText(R.id.tv_address, authenticationList.getAddress())
                            .setText(R.id.tv_count, "服务" + authenticationList.getServiceCount() + "次");
                }
            };
            lvAuthentication.setAdapter(adapter);
        }
    }
    @OnClick(R.id.rl_wallet_back)
    public void onClick() {
        finish();
    }

    /**
     * 字符串转模型集合
     *
     * @param response
     * @return
     */
    private List<AuthenticationList> getBeanFromJson(JSONObject response) {
        String data = "";
        try {
            data = response.getString("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new Gson().fromJson(data, new TypeToken<List<AuthenticationList>>() {
        }.getType());
    }
}
