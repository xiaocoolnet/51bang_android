package cn.xcom.helper.activity;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.xcom.helper.R;
import cn.xcom.helper.bean.AuthenticationList;
import cn.xcom.helper.bean.SkillTagInfo;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.net.HelperAsyncHttpClient;
import cn.xcom.helper.utils.CommonAdapter;
import cn.xcom.helper.utils.SingleVolleyRequest;
import cn.xcom.helper.utils.StringPostRequest;
import cn.xcom.helper.utils.ToastUtil;
import cn.xcom.helper.utils.ViewHolder;
import cz.msebera.android.httpclient.Header;

public class AuthenticationActivity extends BaseActivity {

    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.tv_type1)
    TextView tvType1;
    @BindView(R.id.rl_type1)
    RelativeLayout rlType1;
    @BindView(R.id.tv_type2)
    TextView tvType2;
    @BindView(R.id.rl_type2)
    RelativeLayout rlType2;
    @BindView(R.id.tv_type3)
    TextView tvType3;
    @BindView(R.id.rl_type3)
    RelativeLayout rlType3;
    @BindView(R.id.lv_authentication)
    ListView lvAuthentication;
    @BindView(R.id.srl_authentication)
    SwipeRefreshLayout srlAuthentication;
    private Context context;
    private String type,sort,onlineType;
    private CommonAdapter<AuthenticationList> adapter;
    private List<AuthenticationList> authenticationLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_authentication);
        ButterKnife.bind(this);
        context = this;
        authenticationLists = new ArrayList<>();
        type = "0";
        sort = "1";
        setRefresh();
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
        String url= NetConstant.GET_AUTHENTICATION_LIST;
        StringPostRequest request=new StringPostRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject=new JSONObject(s);
                    String status=jsonObject.getString("status");
                    if (status.equals("success")){
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
        request.putValue("cityname","芝罘区");
        request.putValue("latitude","");
        request.putValue("longitude","");
        request.putValue("sort",sort);
        request.putValue("type", type);
        SingleVolleyRequest.getInstance(context).addToRequestQueue(request);
    }


    /**
     * 字符串转模型集合
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

    /**
     * 设置适配器
     * @param jsonObject
     */
    private void setAdapter(JSONObject jsonObject) {
        authenticationLists.clear();
        authenticationLists.addAll(getBeanFromJson(jsonObject));
        if(adapter!=null){
            adapter.notifyDataSetChanged();
        }else{
            adapter = new CommonAdapter<AuthenticationList>(context,authenticationLists,R.layout.item_authentication_info) {
                @Override
                public void convert(ViewHolder holder, AuthenticationList authenticationList) {
                    holder.setImageByUrl(R.id.iv_avatar,authenticationList.getPhoto())
                            .setText(R.id.tv_name,authenticationList.getName())
                            .setText(R.id.tv_address,authenticationList.getAddress())
                            .setText(R.id.tv_count,"服务"+authenticationList.getServiceCount()+"次");
                }
            };
            lvAuthentication.setAdapter(adapter);
        }

    }

    @OnClick({R.id.rl_back, R.id.rl_type1, R.id.rl_type2, R.id.rl_type3})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.rl_type1:
                showPopupMenu();
                break;
            case R.id.rl_type2:
                showPopupWindow1();
                break;
            case R.id.rl_type3:
                showPopupWindow2();
                break;
        }
    }

    /**
     * 服务类型弹出框
     */
    private void showPopupWindow1() {
        //自定义布局
        View layout = LayoutInflater.from(context).inflate(R.layout.select_type_mode1, null);
        //初始化popwindow
        final PopupWindow popupWindow = new PopupWindow(layout, FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        //设置弹出位置
        int[] location = new int[2];
        rlType1.getLocationOnScreen(location);
        popupWindow.showAsDropDown(rlType1);
        final TextView tv1 = (TextView) layout.findViewById(R.id.tv1);
        final TextView tv2 = (TextView) layout.findViewById(R.id.tv2);
        final TextView tv3 = (TextView) layout.findViewById(R.id.tv3);
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvType2.setText(tv1.getText());
                sort="1";
                popupWindow.dismiss();
                getData();
            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvType2.setText(tv2.getText());
                sort="2";
                popupWindow.dismiss();
                getData();
            }
        });
        tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvType2.setText(tv3.getText());
                sort="3";
                popupWindow.dismiss();
                getData();
            }
        });
        // 设置背景颜色变暗
        final WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.7f;
        getWindow().setAttributes(lp);
        //监听popwindow消失事件，取消遮盖层
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                lp.alpha = 1.0f;
                getWindow().setAttributes(lp);
            }
        });
    }

    /**
     * 服务类型弹出框
     */
    private void showPopupWindow2() {
        //自定义布局
        View layout = LayoutInflater.from(context).inflate(R.layout.select_type_mode2, null);
        //初始化popwindow
        final PopupWindow popupWindow = new PopupWindow(layout, FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        //设置弹出位置
        int[] location = new int[2];
        rlType1.getLocationOnScreen(location);
        popupWindow.showAsDropDown(rlType1);
        final TextView tv1 = (TextView) layout.findViewById(R.id.tv1);
        final TextView tv2 = (TextView) layout.findViewById(R.id.tv2);
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvType3.setText(tv1.getText());
                popupWindow.dismiss();
            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvType3.setText(tv2.getText());
                popupWindow.dismiss();
            }
        });
        // 设置背景颜色变暗
        final WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.7f;
        getWindow().setAttributes(lp);
        //监听popwindow消失事件，取消遮盖层
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                lp.alpha = 1.0f;
                getWindow().setAttributes(lp);
            }
        });
    }

    /**
     *技能分类弹出框
     * */
    private void showPopupMenu() {
        final ArrayList<SkillTagInfo> skillTagInfos = new ArrayList<>();
        RequestParams params=new RequestParams();
        params.put("id",0);
        HelperAsyncHttpClient.get(NetConstant.NET_GET_TASKLIST, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null) {
                    try {
                        String state = response.getString("status");
                        if (state.equals("success")) {
                            JSONArray data = response.getJSONArray("data");
                            skillTagInfos.clear();
                            SkillTagInfo tagInfo = new SkillTagInfo();
                            tagInfo.setSkill_name("全部");
                            tagInfo.setSkill_id("0");
                            skillTagInfos.add(tagInfo);
                            for (int i = 0; i < data.length(); i++) {
                                SkillTagInfo info = new SkillTagInfo();
                                JSONObject jsonObject = data.getJSONObject(i);
                                info.setSkill_id(jsonObject.getString("id"));
                                info.setSkill_name(jsonObject.getString("name"));
                                skillTagInfos.add(info);
                            }
                            //自定义布局
                            View layout = LayoutInflater.from(context).inflate(R.layout.select_type_list, null);
                            //初始化popwindow
                            final PopupWindow popupWindow = new PopupWindow(layout, FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.WRAP_CONTENT);
                            popupWindow.setFocusable(true);
                            popupWindow.setOutsideTouchable(true);
                            popupWindow.setBackgroundDrawable(new BitmapDrawable());
                            //设置弹出位置
                            int[] location = new int[2];
                            rlType1.getLocationOnScreen(location);
                            popupWindow.showAsDropDown(rlType1);
                            ListView listView = (ListView) layout.findViewById(R.id.type_list);
                            listView.setAdapter(new CommonAdapter<SkillTagInfo>(context,skillTagInfos,R.layout.item_skill) {
                                @Override
                                public void convert(ViewHolder holder, SkillTagInfo skillTagInfo) {
                                    holder.setText(R.id.tv_type_name,skillTagInfo.getSkill_name());
                                }
                            });
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    tvType1.setText(skillTagInfos.get(position).getSkill_name());
                                    type = skillTagInfos.get(position).getSkill_id();
                                    getData();
                                    popupWindow.dismiss();
                                }
                            });
                            // 设置背景颜色变暗
                            final WindowManager.LayoutParams lp = getWindow().getAttributes();
                            lp.alpha = 0.7f;
                            getWindow().setAttributes(lp);
                            //监听popwindow消失事件，取消遮盖层
                            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                                @Override
                                public void onDismiss() {
                                    lp.alpha = 1.0f;
                                    getWindow().setAttributes(lp);
                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });

    }
}
