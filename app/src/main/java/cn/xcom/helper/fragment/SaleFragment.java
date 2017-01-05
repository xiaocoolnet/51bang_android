package cn.xcom.helper.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.xcom.helper.HelperApplication;
import cn.xcom.helper.R;
import cn.xcom.helper.activity.BindAccountAuthorizedActivity;
import cn.xcom.helper.activity.ReleaseActivity;
import cn.xcom.helper.adapter.GroupAdapter;
import cn.xcom.helper.adapter.SaleAdapter;
import cn.xcom.helper.bean.DictionaryList;
import cn.xcom.helper.bean.Front;
import cn.xcom.helper.bean.UserInfo;
import cn.xcom.helper.constant.HelperConstant;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.net.HelperAsyncHttpClient;
import cn.xcom.helper.utils.SPUtils;
import cn.xcom.helper.utils.SingleVolleyRequest;
import cn.xcom.helper.utils.StringPostRequest;
import cn.xcom.helper.utils.ToastUtils;
import cn.xcom.helper.view.DividerItemDecoration;
import cn.xcom.helper.view.SaleTypePopupWindow;
import cz.msebera.android.httpclient.Header;

/**
 * Created by zhuchongkun on 16/5/27.
 * 主页面——特卖
 */
public class SaleFragment extends Fragment implements View.OnClickListener {
    private String TAG = "SaleFragment";
    private Context mContext;
    private PopupWindow popupWindow;
    private RelativeLayout rl_classification, rl_release;
    private View view;
    private List<Front> addlist = new ArrayList<>();
    private SaleAdapter saleAdapter;
    private SaleTypePopupWindow saleTypePopupWindow;
    private TextView tv_typeName;
    private XRecyclerView xRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sale, container, false);
        xRecyclerView = (XRecyclerView) view.findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        xRecyclerView.setLayoutManager(linearLayoutManager);
        xRecyclerView.addItemDecoration(new DividerItemDecoration(getContext()
                , DividerItemDecoration.VERTICAL_LIST));
        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                getNewData();
            }

            @Override
            public void onLoadMore() {
                getMore();
            }
        });
        saleAdapter = new SaleAdapter(addlist, getContext());
        xRecyclerView.setAdapter(saleAdapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!HelperApplication.getInstance().saleBack) {
            getNewData();
        }
        HelperApplication.getInstance().saleBack = false;
    }


    private void getNewData() {
        String url = NetConstant.GOODSLIST;
        StringPostRequest request = new StringPostRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    Log.d("=====显示111", "" + s);
                    JSONObject jsonObject = new JSONObject(s);
                    String state = jsonObject.getString("status");
                    if (state.equals("success")) {
                        tv_typeName.setText("全部分类");
                        String jsonObject1 = jsonObject.getString("data");
                        Gson gson = new Gson();
                        addlist.clear();
                        List<Front> fronts = gson.fromJson(jsonObject1,
                                new TypeToken<ArrayList<Front>>() {
                                }.getType());
                        Log.e("========fragment", "" + addlist.size());
                        addlist.addAll(fronts);
                        saleAdapter = new SaleAdapter(addlist,mContext);
                        xRecyclerView.setAdapter(saleAdapter);
//                        ToastUtils.showToast(mContext, "刷新成功");
                    } else {
                        addlist.clear();
                        saleAdapter.notifyDataSetChanged();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                xRecyclerView.refreshComplete();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showToast(mContext, "网络连接错误，请检查您的网络");
                xRecyclerView.refreshComplete();
            }
        });
        request.putValue("city", HelperApplication.getInstance().mDistrict);
        request.putValue("beginid", "0");
        SingleVolleyRequest.getInstance(getContext()).addToRequestQueue(request);
    }


    private void getMore() {
        String url = NetConstant.GOODSLIST;
        StringPostRequest request = new StringPostRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String state = jsonObject.getString("status");
                    if (state.equals("success")) {
                        String jsonObject1 = jsonObject.getString("data");
                        Gson gson = new Gson();
                        List<Front> fronts = gson.fromJson(jsonObject1,
                                new TypeToken<ArrayList<Front>>() {
                                }.getType());
                        addlist.addAll(fronts);
                        saleAdapter.notifyDataSetChanged();
                    } else {

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                xRecyclerView.loadMoreComplete();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showToast(mContext, "网络连接错误，请检查您的网络");
                xRecyclerView.loadMoreComplete();
            }
        });
        request.putValue("city", HelperApplication.getInstance().mDistrict);
        Front front = addlist.get(addlist.size() - 1);
        request.putValue("beginid", front.getId());
        SingleVolleyRequest.getInstance(getContext()).addToRequestQueue(request);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getActivity();
        initView();
    }

    private void initView() {
        rl_classification = (RelativeLayout) getView().findViewById(R.id.rl_fragment_sale_classification);
        rl_classification.setOnClickListener(this);
        rl_release = (RelativeLayout) getView().findViewById(R.id.rl_fragment_sale_release);
        rl_release.setOnClickListener(this);
        tv_typeName = (TextView) getView().findViewById(R.id.tv_sale_type);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_fragment_sale_classification:
                saleTypePopupWindow = new SaleTypePopupWindow(mContext, new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        final List aList = new ArrayList();
                        DictionaryList dictionaryList = saleTypePopupWindow.addAllList.get(position);
                        if (dictionaryList.getName().equals("全部分类")) {
                            aList.addAll(addlist);
                        } else {
                            for (int i = 0; i < addlist.size(); i++) {
                                Front front = addlist.get(i);
                                if (dictionaryList.getId().equals(front.getType())) {
                                    Log.d("=== 数据", front.getId());
                                    aList.add(front);
                                } else if (dictionaryList.getName().equals("其他") && front.getType().length() <= 0) {
                                    Log.d("=== 其他", front.getType().toString());
                                    aList.add(front);
                                }
                            }
                        }
                        tv_typeName.setText(dictionaryList.getName());
                        saleAdapter = new SaleAdapter(aList, mContext);
                        xRecyclerView.setAdapter(saleAdapter);
                        saleAdapter.notifyDataSetChanged();
                        if (saleTypePopupWindow != null) {
                            saleTypePopupWindow.dismiss();
                        }
                    }
                });
                //设置弹出位置
                int[] location = new int[2];
                rl_classification.getLocationOnScreen(location);
                saleTypePopupWindow.showAsDropDown(rl_classification);
                break;
            case R.id.rl_fragment_sale_release:
                getNameAuthentication(new UserInfo(mContext).getUserId());
                break;
        }

    }

    /**
     * 获取实名认证
     */
    private void getNameAuthentication(final String userid) {
        RequestParams params = new RequestParams();
        params.put("userid", userid);
        HelperAsyncHttpClient.get(NetConstant.Check_Had_Authentication, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.e("认证", response.toString());
                if (response.optString("status").equals("success")) {
                    SPUtils.put(mContext, HelperConstant.IS_HAD_AUTHENTICATION, "1");
                    goPublish();
                } else {
                    SPUtils.put(mContext, HelperConstant.IS_HAD_AUTHENTICATION, "0");
                    goAuthorized();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                goAuthorized();

            }
        });
    }


    private void goPublish() {
        Intent intent = new Intent(getActivity(), ReleaseActivity.class);
        intent.putExtra("judge", "我是fragemnt");
        startActivity(intent);
    }

    private void goAuthorized() {
        Intent intent = new Intent(getActivity(), BindAccountAuthorizedActivity.class);
        startActivity(intent);
    }

}