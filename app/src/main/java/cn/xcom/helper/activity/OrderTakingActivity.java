package cn.xcom.helper.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.xcom.helper.R;
import cn.xcom.helper.bean.MyApplyTask;
import cn.xcom.helper.bean.UserInfo;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.net.HelperAsyncHttpClient;
import cn.xcom.helper.utils.CommonAdapter;
import cn.xcom.helper.utils.LogUtils;
import cn.xcom.helper.utils.ViewHolder;
import cz.msebera.android.httpclient.Header;

/**
 * Created by zhuchongkun on 16/6/12.
 * 接单页
 */
public class OrderTakingActivity extends BaseActivity {
    @BindView(R.id.rl_order_taking_back)
    RelativeLayout rlOrderTakingBack;
    @BindView(R.id.task_list)
    ListView taskList;
    @BindView(R.id.task_srl)
    SwipeRefreshLayout taskSrl;
    private TextView tvCount,tv_today_count;
    private String TAG = "OrderTakingActivity";
    private Context mContext;
    private UserInfo userInfo;
    private List<MyApplyTask> myApplyTasks;
    private CommonAdapter<MyApplyTask> adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_order_taking);
        ButterKnife.bind(this);
        mContext = this;
        setRefresh();
        userInfo = new UserInfo(mContext);
        userInfo.readData(mContext);
        myApplyTasks = new ArrayList<>();
        tvCount = (TextView) findViewById(R.id.tv_count);
        tv_today_count = (TextView) findViewById(R.id.tv_today_count);
        taskList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(myApplyTasks.get(position).getState().equals("2")){
                    Intent intent = new Intent(mContext, NoBeginTaskActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("mytask",myApplyTasks.get(position));
                    intent.putExtras(bundle);
                    intent.putExtra("type","2");
                    mContext.startActivity(intent);
                }else if(myApplyTasks.get(position).getState().equals("3")){
                    Intent intent = new Intent(mContext, InProgressTaskActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("mytask",myApplyTasks.get(position));
                    intent.putExtra("type","2");
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                }
            }
        });
    }

    /**
     * 设置下拉加载
     */
    private void setRefresh() {
        taskSrl.setColorSchemeResources(R.color.background_white);
        taskSrl.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.colorTheme));
        taskSrl.setProgressViewOffset(true, 10, 100);
        taskSrl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
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
        getCount();
    }

    /**
     * 获取接单统计数据
     */
    private void getCount() {
        RequestParams params = new RequestParams();
        params.put("userid", userInfo.getUserId());
        HelperAsyncHttpClient.get(NetConstant.MY_APPLY_COUNT, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response.optString("status").equals("success")) {
                   JSONObject object = response.optJSONObject("data");
                    tvCount.setText(object.optString("monthcount"));
                    tv_today_count.setText(object.optString("daycount"));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    /**
     * 获取数据
     */
    private void getData() {
        RequestParams params = new RequestParams();
        params.put("userid", userInfo.getUserId());
        //params.put("state", "2");
        HelperAsyncHttpClient.get(NetConstant.GET_MY_TASK, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                taskSrl.setRefreshing(false);
                if (response.optString("status").equals("success")) {
                    setAdapter(response);
                }
                LogUtils.e(TAG, response.toString());

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                taskSrl.setRefreshing(false);
                LogUtils.e(TAG, responseString);
            }
        });

    }

    /**
     * 根据请求数据
     *
     * @param response
     */
    private void setAdapter(JSONObject response) {
        myApplyTasks.clear();
        myApplyTasks.addAll(getBeanFromJson(response));
        tvCount.setText(myApplyTasks.size()+"");
        adapter = new CommonAdapter<MyApplyTask>(mContext, myApplyTasks, R.layout.item_my_task) {
            @Override
            public void convert(ViewHolder holder, MyApplyTask myApplyTask) {
                setItem(holder, myApplyTask);
            }
        };
        taskList.setAdapter(adapter);
    }

    /**
     * 为item填充内容
     *
     * @param holder
     * @param myApplyTask
     */
    private void setItem(ViewHolder holder, MyApplyTask myApplyTask) {
        holder.setText(R.id.tv_tradeNo, myApplyTask.getOrder_num())
                .setTimeTextWithStr(R.id.tv_time, myApplyTask.getTime(), "")
                .setText(R.id.tv_trade_name, myApplyTask.getDescription())
                .setText(R.id.tv_service_address, myApplyTask.getSaddress())
                .setText(R.id.tv_price, myApplyTask.getPrice());
        TextView tv_state = holder.getView(R.id.tv_state);
        if(myApplyTask.getState().equals("2")){
            tv_state.setText("已抢单");
        }else if(myApplyTask.getState().equals("3")){
            tv_state.setText("已上门");
        }else if(myApplyTask.getState().equals("4")){
            tv_state.setText("申请付款");
        }else if(myApplyTask.getState().equals("5")){
            tv_state.setText("已付款");
        }else if (myApplyTask.getState().equals("-1")){
            tv_state.setText("已取消");
        }
    }

    /**
     * 字符串转模型集合
     *
     * @param response
     * @return
     */
    private List<MyApplyTask> getBeanFromJson(JSONObject response) {
        String data = "";
        try {
            data = response.getString("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new Gson().fromJson(data, new TypeToken<List<MyApplyTask>>() {
        }.getType());
    }

    @OnClick(R.id.rl_order_taking_back)
    public void onClick() {
        finish();
    }
}
