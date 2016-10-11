package cn.xcom.helper.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kyleduo.switchbutton.SwitchButton;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.xcom.helper.R;
import cn.xcom.helper.activity.AddressDetailActivity;
import cn.xcom.helper.activity.AuthorizedActivity;
import cn.xcom.helper.activity.MyTaskActivity;
import cn.xcom.helper.activity.TaskDetailActivity;
import cn.xcom.helper.bean.TaskInfo;
import cn.xcom.helper.bean.UserInfo;
import cn.xcom.helper.constant.HelperConstant;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.net.HelperAsyncHttpClient;
import cn.xcom.helper.utils.CommonAdapter;
import cn.xcom.helper.utils.LogUtils;
import cn.xcom.helper.utils.SPUtils;
import cn.xcom.helper.utils.ToastUtil;
import cn.xcom.helper.utils.ViewHolder;
import cz.msebera.android.httpclient.Header;

/**
 * Created by zhuchongkun on 16/5/27.
 * 主页面——抢单
 */
public class BuyFragment extends Fragment implements View.OnClickListener{
    private String TAG="BuyFragment";
    private Context mContext;
    private Button bt_authorized;
    private UserInfo userInfo;
    private SwitchButton sb;
    private LinearLayout ll_task;
    private SwipeRefreshLayout srl_task;
    private ListView lv_task;
    private SwitchButton sb_change;
    private boolean isChecked = true;
    private List<TaskInfo> taskInfos;
    private CommonAdapter<TaskInfo> adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = null;
        Log.e("state",SPUtils.get(getActivity(), HelperConstant.IS_HAD_AUTHENTICATION,"").toString());
        if(SPUtils.get(getActivity(), HelperConstant.IS_HAD_AUTHENTICATION,"").equals("1")){
            view = inflater.inflate(R.layout.activity_grab_task,container,false);
        }else{
            view = inflater.inflate(R.layout.fragment_buy,container,false);
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext=getActivity();
        taskInfos = new ArrayList<>();
        initView();
    }
    private void initView(){
        userInfo=new UserInfo(mContext);
        if(SPUtils.get(getActivity(), HelperConstant.IS_HAD_AUTHENTICATION,"").equals("1")){
            ll_task = (LinearLayout) getView().findViewById(R.id.ll_task);
            ll_task.setOnClickListener(this);
            srl_task = (SwipeRefreshLayout) getView().findViewById(R.id.grab_task_srl);
            lv_task = (ListView) getView().findViewById(R.id.grab_task_list);
            sb_change = (SwitchButton) getView().findViewById(R.id.sb_fragment_buy);
            srl_task.setColorSchemeResources(R.color.background_white);
            srl_task.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.colorTheme));
            srl_task.setProgressViewOffset(true, 10, 100);
            srl_task.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    getData();
                }
            });
            lv_task.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(mContext, TaskDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("taskInfo",taskInfos.get(position));
                    intent.putExtras(bundle);
                    //抢单列表
                    intent.putExtra("type","1");
                    mContext.startActivity(intent);
                }
            });
        }else{
            bt_authorized= (Button) getView().findViewById(R.id.bt_fragment_buy_authorized);
            bt_authorized.setOnClickListener(this);
            sb= (SwitchButton) getView().findViewById(R.id.sb_fragment_buy);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        userInfo.readData(mContext);
        getData();
        getIdentity();
    }

    /**
     * 加载数据
     */
    private void getData() {
        if(SPUtils.get(getActivity(), HelperConstant.IS_HAD_AUTHENTICATION,"").equals("1")){
            RequestParams params=new RequestParams();
            params.put("userid",userInfo.getUserId());
            HelperAsyncHttpClient.get(NetConstant.GETTASKLIST, params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    srl_task.setRefreshing(false);
                    if (response.optString("status").equals("success")) {
                        setAdapter(response);
                    }
                    LogUtils.e(TAG, response.toString());

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    LogUtils.e(TAG, responseString);
                }
            });
        }
    }

    /**
     * 根据请求数据
     * @param response
     */
    private void setAdapter(JSONObject response) {
        taskInfos.clear();
        taskInfos.addAll(getBeanFromJson(response));
        if(adapter!=null){
            adapter.notifyDataSetChanged();
        }else{
            adapter = new CommonAdapter<TaskInfo>(mContext,taskInfos,R.layout.item_task_info) {
                @Override
                public void convert(ViewHolder holder, TaskInfo taskInfo) {
                    setItem(holder,taskInfo);
                }
            };
            lv_task.setAdapter(adapter);
        }
    }

    /**
     * 为item填充内容
     * @param holder
     * @param taskInfo
     */
    private void setItem(ViewHolder holder, final TaskInfo taskInfo) {
        holder.setText(R.id.tv_name,taskInfo.getName())
                .setText(R.id.tv_task_name,taskInfo.getDescription())
                .setTimeText(R.id.tv_task_time,taskInfo.getTime())
                .setTimeTextWithStr(R.id.tv_expiry_time,taskInfo.getExpirydate(),"前有效")
                .setText(R.id.tv_type_name,taskInfo.getTypename())
                .setText(R.id.tv_address1,taskInfo.getAddress())
                .setText(R.id.tv_address2,taskInfo.getSaddress())
                .setText(R.id.tv_btn_grab,taskInfo.getState().equals("1")?"抢单":"已被抢")
                .setText(R.id.tv_price, taskInfo.getPrice());
        TextView btn_grab = holder.getView(R.id.tv_btn_grab);
        if(taskInfo.getState().equals("1")){
            btn_grab.setClickable(true);
            btn_grab.setText("抢单");
            btn_grab.setTextColor(getResources().getColor(R.color.colorTheme));
            btn_grab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateState(taskInfo.getId());
                }
            });
        }else{
            btn_grab.setClickable(false);
            btn_grab.setText("已被抢");
            btn_grab.setTextColor(getResources().getColor(R.color.holo_red_light));
        }
        //上门详细地址
        if(taskInfo.getLongitude().length()>0&&taskInfo.getLatitude().length()>0){
            holder.getView(R.id.ll_address).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, AddressDetailActivity.class);
                    intent.putExtra("lon",taskInfo.getLongitude());
                    intent.putExtra("lat",taskInfo.getLatitude());
                    startActivity(intent);
                }
            });
        }
        //服务详细地址
        if(taskInfo.getSlongitude().length()>0&&taskInfo.getSlatitude().length()>0){
            holder.getView(R.id.ll_saddress).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, AddressDetailActivity.class);
                    intent.putExtra("lon",taskInfo.getSlongitude());
                    intent.putExtra("lat",taskInfo.getSlatitude());
                    startActivity(intent);
                }
            });
        }
    }

    /**
     * 抢单
     * @param id
     */
    private void updateState(String id) {
        RequestParams params=new RequestParams();
        params.put("userid", userInfo.getUserId());
        params.put("taskid",id);
        HelperAsyncHttpClient.get(NetConstant.GRAB_TASK, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response.optString("status").equals("success")) {
                    ToastUtil.showShort(mContext,"抢单");
                    getData();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    /**
     * 字符串转模型集合
     * @param response
     * @return
     */
    private List<TaskInfo> getBeanFromJson(JSONObject response) {
        String data = "";
        try {
            data = response.getString("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new Gson().fromJson(data,new TypeToken<List<TaskInfo>>(){}.getType());
    }

    private void getIdentity(){
        RequestParams params=new RequestParams();
        params.put("userid",userInfo.getUserId());
        HelperAsyncHttpClient.get(NetConstant.NET_GET_IDENTITY,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                LogUtils.e(TAG,"--statusCode->"+statusCode+"==>"+response.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                LogUtils.e(TAG,"--statusCode->"+statusCode+"==>"+responseString);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_fragment_buy_authorized:
                startActivity(new Intent(mContext, AuthorizedActivity.class));
                break;
            case R.id.ll_task:
                startActivity(new Intent(mContext, MyTaskActivity.class));
        }

    }
}
