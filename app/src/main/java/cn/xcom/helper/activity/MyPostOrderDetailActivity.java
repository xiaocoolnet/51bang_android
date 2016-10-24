package cn.xcom.helper.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.xcom.helper.R;
import cn.xcom.helper.adapter.CommentsListAdapter;
import cn.xcom.helper.bean.TaskItemInfo;
import cn.xcom.helper.bean.UserInfo;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.fragment.order.MyPostOrderFragment;
import cn.xcom.helper.net.HelperAsyncHttpClient;
import cz.msebera.android.httpclient.Header;

/**
 * 我的发单详情页面
 */

public class MyPostOrderDetailActivity extends BaseActivity {
    private int titleType;
    private TextView fistTitle, secondTitle, thirdTitle, fourthTitle, orderNumTv, descriptionTv,
            priceTv, saddressTv, addressTv, expiryTv, applyPhoneTv;
    private String orderId;
    private Button cancelBtn;
    private UserInfo userInfo;
    private Context mContext;
    private ListView commentsLv;
    private TaskItemInfo taskItemInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_post_order_detail);
        mContext = this;
        userInfo = new UserInfo(this);
//        titleType = getIntent().getIntExtra("type", 0);
//        orderId = getIntent().getStringExtra("taskid");
        Bundle bundle = getIntent().getBundleExtra("bundle");
        titleType = bundle.getInt("type");
        taskItemInfo = (TaskItemInfo) bundle.getSerializable("task_info");
        initView();
        setView(taskItemInfo);
//        getData();
    }

    private void initView() {
        fistTitle = (TextView) findViewById(R.id.tv_title_first);
        secondTitle = (TextView) findViewById(R.id.tv_title_second);
        thirdTitle = (TextView) findViewById(R.id.tv_title_third);
        fourthTitle = (TextView) findViewById(R.id.tv_title_fourth);
        orderNumTv = (TextView) findViewById(R.id.tv_order_num);
        descriptionTv = (TextView) findViewById(R.id.tv_description);
        priceTv = (TextView) findViewById(R.id.tv_price);
        saddressTv = (TextView) findViewById(R.id.tv_saddress);
        addressTv = (TextView) findViewById(R.id.tv_address);
        expiryTv = (TextView) findViewById(R.id.tv_expiry_time);
        applyPhoneTv = (TextView) findViewById(R.id.tv_apply_phone);
        cancelBtn = (Button) findViewById(R.id.btn_cancel);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(mContext).setTitle("取消订单").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cancelPayment();
                    }
                }).setNegativeButton("取消", null).show();
            }
        });

        commentsLv = (ListView) findViewById(R.id.lv_comments);
    }

    private void setView(TaskItemInfo taskItemInfo) {
        switch (titleType) {
            case 1:
                fistTitle.setBackgroundColor(getResources().getColor(R.color.colorTheme));
                cancelBtn.setVisibility(View.VISIBLE);
                break;
            case 2:
                secondTitle.setBackgroundColor(getResources().getColor(R.color.colorTheme));
                cancelBtn.setVisibility(View.VISIBLE);
                break;
            case 3:
                thirdTitle.setBackgroundColor(getResources().getColor(R.color.colorTheme));
                break;
            case 4:
                fourthTitle.setBackgroundColor(getResources().getColor(R.color.colorTheme));
        }
        orderNumTv.setText(taskItemInfo.getOrder_num());
        descriptionTv.setText(taskItemInfo.getDescription());
        priceTv.setText(taskItemInfo.getPrice());
        saddressTv.setText(taskItemInfo.getSaddress());
        addressTv.setText(taskItemInfo.getAddress());
        Date date = new Date();
        date.setTime(Long.parseLong(taskItemInfo.getExpirydate()) * 1000);
        expiryTv.setText(new SimpleDateFormat("MM-dd  HH:mm:ss").format(date));
        if (taskItemInfo.getApply() == null) {
            applyPhoneTv.setText("无人接单");
        } else {
            applyPhoneTv.setText(taskItemInfo.getApply().getPhone());
        }

        CommentsListAdapter adapter = new CommentsListAdapter(mContext,taskItemInfo.getEvaluate());
        commentsLv.setAdapter(adapter);

    }

    private void getData() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("taskid", orderId);
        HelperAsyncHttpClient.get(NetConstant.GET_TASK_INFO_BY_TASK_ID, requestParams,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        if (response != null) {
                            try {
                                String state = response.getString("status");
                                if (state.equals("success")) {
                                    String data = response.getString("data");
                                    TaskItemInfo taskItemInfo = new Gson().fromJson(data,
                                            new TypeToken<TaskItemInfo>() {
                                            }.getType());
                                    setView(taskItemInfo);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }

    private void cancelPayment() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("ownerid", userInfo.getUserId());
        requestParams.put("taskid", taskItemInfo.getId());
        HelperAsyncHttpClient.get(NetConstant.OWNER_CANCEL_TASK, requestParams,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        if (response != null) {
                            try {
                                String state = response.getString("status");
                                if (state.equals("success")) {
                                    Toast.makeText(MyPostOrderDetailActivity.this, "取消订单成功", Toast.LENGTH_SHORT).show();
                                    cancelBtn.setVisibility(View.GONE);
                                    setResult(MyPostOrderFragment.COMMENT_RESULT_CODE);
                                } else {
                                    Toast.makeText(MyPostOrderDetailActivity.this, "取消订单失败", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });


    }

    public void onBack(View v) {
        finish();
    }

}
