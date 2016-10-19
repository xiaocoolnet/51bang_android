package cn.xcom.helper.fragment.order;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cn.xcom.helper.R;
import cn.xcom.helper.activity.MyOrderDetailActivity;
import cn.xcom.helper.activity.MyPostOrderDetailActivity;
import cn.xcom.helper.adapter.MyOrderAdapter;
import cn.xcom.helper.adapter.OrderRecyclerViewAdapter;
import cn.xcom.helper.bean.ShopGoodInfo;
import cn.xcom.helper.bean.TaskItemInfo;
import cn.xcom.helper.bean.UserInfo;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.net.HelperAsyncHttpClient;
import cn.xcom.helper.view.DividerItemDecoration;
import cz.msebera.android.httpclient.Header;

/**
 * Created by Administrator on 2016/10/18 0018.
 * 我的订单fragment
 */

public class MyOrderFragment extends Fragment{
    private int orderListType;//1全部 2待付款 3待消费 4待评价
    private Context mContext;
    private XRecyclerView mRecyclerView;
    private UserInfo userInfo;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        orderListType = getArguments().getInt("type");
        mContext = getContext();
        userInfo = new UserInfo(mContext);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_order,container,false);
        initView(view);
        getOrder();
        return view;
    }


    private void initView(View v) {
        mRecyclerView = (XRecyclerView) v.findViewById(R.id.rv_order);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                getOrder();
            }

            @Override
            public void onLoadMore() {
                mRecyclerView.loadMoreComplete();
            }
        });


    }

    private void getOrder(){
        RequestParams requestParams = new RequestParams();
        requestParams.put("userid", userInfo.getUserId());
        switch (orderListType) {
            case 1:
                requestParams.put("state", "");
                break;
            case 2:
                requestParams.put("state", "1");
                break;
            case 3:
                requestParams.put("state", "2,3");
                break;
            case 4:
                requestParams.put("state", "4");
                break;
        }

        HelperAsyncHttpClient.get(NetConstant.BUYER_GET_SHOP_ORDER_LIST, requestParams,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        if (response != null) {
                            try {
                                String state = response.getString("status");
                                if (state.equals("success")) {
                                    String data = response.getString("data");
                                    final List<ShopGoodInfo> shopGoodInfos = new Gson().fromJson(data,
                                            new TypeToken<List<ShopGoodInfo>>() {
                                            }.getType());
                                    MyOrderAdapter myOrderAdapter = new MyOrderAdapter(mContext,shopGoodInfos);
                                    myOrderAdapter.setOnItemClickListener(new MyOrderAdapter.OnItemClickListener() {

                                        @Override
                                        public void onClick(int position) {
                                            ShopGoodInfo shopGoodInfo = shopGoodInfos.get(position);
                                            Intent intent = new Intent(mContext, MyOrderDetailActivity.class);
                                            Bundle bundle = new Bundle();
                                            bundle.putSerializable("good",shopGoodInfo);
                                            intent.putExtra("bundle",bundle);
                                            startActivity(intent);
                                        }
                                    });

                                    mRecyclerView.setAdapter(myOrderAdapter);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        mRecyclerView.refreshComplete();

                    }
                });


    }




    public static final MyOrderFragment newInstance(int orderListType){
        MyOrderFragment myOrderFragment = new MyOrderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type",orderListType);
        myOrderFragment.setArguments(bundle);
        return  myOrderFragment;
    }


}
