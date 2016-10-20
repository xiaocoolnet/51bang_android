package cn.xcom.helper.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cn.xcom.helper.R;
import cn.xcom.helper.bean.OrderHelper;
import cn.xcom.helper.bean.ShopGoodInfo;
import cn.xcom.helper.bean.UserInfo;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.net.HelperAsyncHttpClient;
import cz.msebera.android.httpclient.Header;

/**
 * Created by Administrator on 2016/10/18 0018.
 * 我的订单 详情页
 */

public class MyOrderDetailActivity extends BaseActivity implements View.OnClickListener {
    private static final int ORDER_DETAIL_REQUEST_CODE = 1122;
    private static final int CANCEL_SUCCESS = 101;
    private static final int PAY_SUCCESS = 102;
    private static final int COMMENT_SUCCESS = 112;

    private ShopGoodInfo goodInfo;
    private TextView userNameTv, mobileTv, goodNameTv, priceTv, goodsCountTv, moneyTv, deliveryTv,
            remarksTv, addressTv, toPaytv, cancelPaymentTv, trackingTv, commentTv;
    private View backView;
    private UserInfo userInfo;
    private int orderType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order_detail);
        goodInfo = (ShopGoodInfo) getIntent().getBundleExtra("bundle").getSerializable("good");
        orderType = getIntent().getIntExtra("order_type", OrderHelper.SellerOrder);
        userInfo = new UserInfo(this);
        initView();
        setView();
    }

    private void initView() {
        backView = findViewById(R.id.rl_help_me_back);
        backView.setOnClickListener(this);
        userNameTv = (TextView) findViewById(R.id.tv_username);
        mobileTv = (TextView) findViewById(R.id.tv_mobile);
        goodNameTv = (TextView) findViewById(R.id.tv_good_name);
        priceTv = (TextView) findViewById(R.id.tv_price);
        goodsCountTv = (TextView) findViewById(R.id.tv_goods_count);
        moneyTv = (TextView) findViewById(R.id.tv_money);
        deliveryTv = (TextView) findViewById(R.id.tv_delivery);
        remarksTv = (TextView) findViewById(R.id.tv_remarks);
        addressTv = (TextView) findViewById(R.id.tv_address);
        toPaytv = (TextView) findViewById(R.id.tv_to_pay);
        toPaytv.setOnClickListener(this);
        cancelPaymentTv = (TextView) findViewById(R.id.tv_cancel_payment);
        cancelPaymentTv.setOnClickListener(this);
        trackingTv = (TextView) findViewById(R.id.tv_tracking);
        commentTv = (TextView) findViewById(R.id.tv_comment);
    }

    private void setView() {
        userNameTv.setText(goodInfo.getUsername());
        mobileTv.setText(goodInfo.getMobile());
        goodNameTv.setText(goodInfo.getGoodsname());
        priceTv.setText("￥" + goodInfo.getPrice());
        goodsCountTv.setText(goodInfo.getNumber());
        moneyTv.setText(goodInfo.getMoney());
        deliveryTv.setText(goodInfo.getDelivery());
        remarksTv.setText(goodInfo.getRemarks());
        addressTv.setText(goodInfo.getAddress());
        trackingTv.setText("卷码:" + goodInfo.getTracking());
        String state = goodInfo.getState();
        //状态：-1已取消，1:未付款，2未发货，3未确认，4买家未评价，5卖家未评价，10订单完成

        if (orderType == OrderHelper.BuyerOrder) {
            switch (Integer.valueOf(state)) {
                case OrderHelper.CANCELED:
                    trackingTv.setVisibility(View.GONE);//卷码未支付时隐藏
                    toPaytv.setVisibility(View.GONE);
                    cancelPaymentTv.setVisibility(View.GONE);
                    commentTv.setVisibility(View.GONE);
                    break;
                case OrderHelper.UNPAY:
                    trackingTv.setVisibility(View.GONE);
                    toPaytv.setVisibility(View.VISIBLE);
                    cancelPaymentTv.setVisibility(View.VISIBLE);
                    commentTv.setVisibility(View.GONE);
                    break;
                case OrderHelper.UN_SEND_OUT:
                    trackingTv.setVisibility(View.VISIBLE);
                    toPaytv.setVisibility(View.GONE);
                    cancelPaymentTv.setVisibility(View.VISIBLE);
                    commentTv.setVisibility(View.GONE);
                    break;
                case OrderHelper.UNCONFIRMED:
                    trackingTv.setVisibility(View.VISIBLE);
                    toPaytv.setVisibility(View.GONE);
                    cancelPaymentTv.setVisibility(View.GONE);
                    commentTv.setVisibility(View.GONE);
                    break;
                case OrderHelper.BUYER_UNCOMMENT:
                    trackingTv.setVisibility(View.VISIBLE);
                    toPaytv.setVisibility(View.GONE);
                    cancelPaymentTv.setVisibility(View.GONE);
                    commentTv.setVisibility(View.VISIBLE);
                    break;
                case OrderHelper.SELLER_UNCOMMENT:
                    trackingTv.setVisibility(View.VISIBLE);
                    toPaytv.setVisibility(View.GONE);
                    cancelPaymentTv.setVisibility(View.GONE);
                    commentTv.setVisibility(View.GONE);
                    break;
                case OrderHelper.COMPLETED:
                    trackingTv.setVisibility(View.GONE);
                    toPaytv.setVisibility(View.GONE);
                    cancelPaymentTv.setVisibility(View.GONE);
                    commentTv.setVisibility(View.GONE);

            }

        } else {
            trackingTv.setVisibility(View.VISIBLE);
            toPaytv.setVisibility(View.GONE);
            cancelPaymentTv.setVisibility(View.GONE);
            commentTv.setVisibility(View.GONE);
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_help_me_back:
                finish();
                break;
            case R.id.tv_to_pay:
                break;
            case R.id.tv_cancel_payment:
                new AlertDialog.Builder(this).setTitle("取消订单").setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cancelOrde();
                    }
                }).setNegativeButton("取消", null).show();
                break;
            case R.id.tv_comment:
                Intent intent = new Intent(this, MyOrderDetailActivity.class);
                intent.putExtra("order_id", goodInfo.getId());
                intent.putExtra("type", "2");//任务是1,商城是2
                startActivityForResult(intent, ORDER_DETAIL_REQUEST_CODE);

        }
    }

    private void cancelOrde() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("ordernum", goodInfo.getOrder_num());
        requestParams.put("userid", userInfo.getUserId());
        HelperAsyncHttpClient.get(NetConstant.CANCEL_ORDER, requestParams,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        if (response != null) {
                            try {
                                String state = response.getString("status");
                                if (state.equals("success")) {
                                    Toast.makeText(MyOrderDetailActivity.this, "取消订单成功", Toast.LENGTH_SHORT).show();
                                    setResult(CANCEL_SUCCESS);
                                    cancelPaymentTv.setVisibility(View.GONE);
                                    toPaytv.setVisibility(View.GONE);
                                } else {
                                    String data = response.getString("data");
                                    Toast.makeText(MyOrderDetailActivity.this, data, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ORDER_DETAIL_REQUEST_CODE) {
            if (resultCode == COMMENT_SUCCESS) {
                commentTv.setVisibility(View.GONE);
                setResult(COMMENT_SUCCESS);
            }

        }

    }
}
