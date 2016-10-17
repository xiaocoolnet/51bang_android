package cn.xcom.helper.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.xcom.helper.R;
import cn.xcom.helper.bean.TaskItemInfo;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.net.HelperAsyncHttpClient;
import cz.msebera.android.httpclient.Header;

/**
 * 我的发单详情页面
 */

public class MyPostOrderDetailActivity extends BaseActivity {
    private int titleType;
    private TextView fistTitle, secondTitle, thirdTitle, fourthTitle,orderNumTv,descriptionTv
            ,priceTv,saddressTv,addressTv,expiryTv,applyPhoneTv;
    private String orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_post_order_detail);
        titleType = getIntent().getIntExtra("type", 0);
        orderId = getIntent().getStringExtra("taskid");
        initView();
        getData();
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
    }

    private void setView(TaskItemInfo taskItemInfo) {
        switch (titleType) {
            case 1:
                fistTitle.setBackgroundColor(getResources().getColor(R.color.colorTheme));
                break;
            case 2:
                secondTitle.setBackgroundColor(getResources().getColor(R.color.colorTheme));
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
        if(taskItemInfo.getApply() == null){
            applyPhoneTv.setText("无人接单");
        }else{
            applyPhoneTv.setText(taskItemInfo.getApply().getPhone());
        }


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
                                            new TypeToken<TaskItemInfo>(){}.getType());
                                    setView(taskItemInfo);
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
