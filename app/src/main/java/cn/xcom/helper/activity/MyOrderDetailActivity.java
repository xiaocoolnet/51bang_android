package cn.xcom.helper.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import cn.xcom.helper.R;
import cn.xcom.helper.bean.ShopGoodInfo;

/**
 * Created by Administrator on 2016/10/18 0018.
 * 我的订单 详情页
 */

public class MyOrderDetailActivity extends BaseActivity implements View.OnClickListener {
    private ShopGoodInfo goodInfo;
    private TextView userNameTv,mobileTv,goodNameTv,priceTv,goodsCountTv
            ,moneyTv,deliveryTv,remarksTv,addressTv,toPaytv,cancelPaymentTv,trackingTv;
    private View backView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order_detail);
        goodInfo = (ShopGoodInfo) getIntent().getBundleExtra("bundle").getSerializable("good");
        initView();
        setView();
    }

    private void initView(){
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
    }

    private void setView(){
        userNameTv.setText(goodInfo.getUsername());
        mobileTv.setText(goodInfo.getMobile());
        goodNameTv.setText(goodInfo.getGoodsname());
        priceTv.setText("￥"+goodInfo.getPrice());
        goodsCountTv.setText(goodInfo.getNumber());
        moneyTv.setText(goodInfo.getMoney());
        deliveryTv.setText(goodInfo.getDelivery());
        remarksTv.setText(goodInfo.getRemarks());
        addressTv.setText(goodInfo.getAddress());
        trackingTv.setText("卷码:"+goodInfo.getTracking());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_help_me_back:
                finish();
                break;
            case R.id.tv_to_pay:
                break;
            case R.id.tv_cancel_payment:
                break;
        }
    }
}
