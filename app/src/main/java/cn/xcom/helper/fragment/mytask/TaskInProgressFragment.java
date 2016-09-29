package cn.xcom.helper.fragment.mytask;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.xcom.helper.R;
import cn.xcom.helper.activity.InProgressTaskActivity;
import cn.xcom.helper.bean.TaskItemInfo;
import cn.xcom.helper.bean.UserInfo;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.net.HelperAsyncHttpClient;
import cn.xcom.helper.utils.CommonAdapter;
import cn.xcom.helper.utils.LogUtils;
import cn.xcom.helper.utils.ViewHolder;
import cz.msebera.android.httpclient.Header;

/**
 * Created by Administrator on 2016/9/27 0027.
 */
public class TaskInProgressFragment extends Fragment {
    private static final String TAG = "TaskInProgressFragment";
    private Context context;
    private List<TaskItemInfo> taskItemInfos;
    private UserInfo userInfo;
    private CommonAdapter<TaskItemInfo> adapter;
    private SwipeRefreshLayout srl_task;
    private ListView lv_task;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.in_progress_task,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getActivity();
        userInfo=new UserInfo(context);
        userInfo.readData(context);
        taskItemInfos = new ArrayList<>();
        initView();
    }

    /**
     * 初始化布局
     */
    private void initView() {
        srl_task = (SwipeRefreshLayout) getView().findViewById(R.id.task_srl);
        lv_task = (ListView) getView().findViewById(R.id.task_list);
        lv_task.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, InProgressTaskActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("task",taskItemInfos.get(position));
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
        srl_task.setColorSchemeResources(R.color.background_white);
        srl_task.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.colorTheme));
        srl_task.setProgressViewOffset(true, 10, 100);
        srl_task.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
    }

    /**
     * 获取数据
     */
    private void getData() {
        RequestParams params=new RequestParams();
        params.put("userid", userInfo.getUserId());
        params.put("state", "3");
        HelperAsyncHttpClient.get(NetConstant.GET_MEY_TASK, params, new JsonHttpResponseHandler() {
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
                srl_task.setRefreshing(false);
                LogUtils.e(TAG, responseString);
            }
        });
    }

    /**
     * 根据请求数据
     * @param response
     */
    private void setAdapter(JSONObject response) {
        taskItemInfos.clear();
        taskItemInfos.addAll(getBeanFromJson(response));
        if(adapter!=null){
            adapter.notifyDataSetChanged();
        }else{
            adapter = new CommonAdapter<TaskItemInfo>(context,taskItemInfos,R.layout.item_nobegin_task) {
                @Override
                public void convert(ViewHolder holder, TaskItemInfo taskItemInfo) {
                    setItem(holder,taskItemInfo);
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
    private void setItem(ViewHolder holder, TaskItemInfo taskInfo) {
        holder.setText(R.id.tv_tradeNo,taskInfo.getOrder_num())
                .setTimeTextWithStr(R.id.tv_time,taskInfo.getTime(),"")
                .setText(R.id.tv_tradeName,taskInfo.getDescription())
                .setText(R.id.tv_price,taskInfo.getPrice())
                .setText(R.id.tv_address1,taskInfo.getAddress())
                .setText(R.id.tv_address2,taskInfo.getSaddress());
    }

    /**
     * 字符串转模型集合
     * @param response
     * @return
     */
    private List<TaskItemInfo> getBeanFromJson(JSONObject response) {
        String data = "";
        try {
            data = response.getString("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new Gson().fromJson(data, new TypeToken<List<TaskItemInfo>>() {
        }.getType());
    }
    @Override
    public void onResume() {
        super.onResume();
        getData();
    }
}
