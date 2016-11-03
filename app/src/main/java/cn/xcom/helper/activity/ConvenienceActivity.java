package cn.xcom.helper.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.xcom.helper.HelperApplication;
import cn.xcom.helper.R;
import cn.xcom.helper.adapter.ConvenienceAdapter;
import cn.xcom.helper.bean.Convenience;
import cn.xcom.helper.constant.HelperConstant;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.utils.SPUtils;
import cn.xcom.helper.utils.SingleVolleyRequest;
import cn.xcom.helper.utils.StringPostRequest;
import cn.xcom.helper.utils.ToastUtil;
import cn.xcom.helper.utils.ToastUtils;

public class ConvenienceActivity extends BaseActivity implements View.OnClickListener{

    private RelativeLayout back;
    private TextView cnnvenience_release;
    private Button convenience_deliver;
    private List<Convenience> addlist;
    private ConvenienceAdapter convenienceAdapter;
    private Context context;
    private XRecyclerView xRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convenience);
        initView();
        getNewDatas();
    }

    private void initView() {
        context=this;
        addlist=new ArrayList<>();
        back = (RelativeLayout) findViewById(R.id.back);
        back.setOnClickListener(this);
        cnnvenience_release = (TextView) findViewById(R.id.cnnvenience_release);
        cnnvenience_release.setOnClickListener(this);
        convenience_deliver= (Button) findViewById(R.id.convenience_deliver);
        convenience_deliver.setOnClickListener(this);
        xRecyclerView = (XRecyclerView) findViewById(R.id.recycler_view);
        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener(){

            @Override
            public void onRefresh() {
                getNewDatas();
            }

            @Override
            public void onLoadMore() {
                getMoreDatas();
            }
        });
        convenienceAdapter = new ConvenienceAdapter(addlist,context);
        xRecyclerView.setAdapter(convenienceAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            //发布便民消息
            case R.id.cnnvenience_release:
                if(SPUtils.get(context, HelperConstant.IS_HAD_AUTHENTICATION,"").equals("1")){
                    goPublish();
                }else {
                    goAuthorized();
                }
                break;
            case R.id. convenience_deliver:
                startActivity(new Intent(ConvenienceActivity.this,DeliverActivity.class));
                break;
        }
    }

    private void goPublish() {
        startActivity(new Intent(ConvenienceActivity.this,ReleaseConvenienceActivity.class));
    }

    private void goAuthorized() {
        Intent intent = new Intent(ConvenienceActivity.this, BindAccountAuthorizedActivity.class);
        startActivity(intent);
    }

    public void getNewDatas(){
        String url= NetConstant.CONVENIENCE;
        StringPostRequest request=new StringPostRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject=new JSONObject(s);
                    String status=jsonObject.getString("status");
                    if (status.equals("success")){
                        String data=jsonObject.getString("data");
                        Gson gson=new Gson();
                        List<Convenience> lists=gson.fromJson(data,new TypeToken<ArrayList<Convenience>>(){}.getType());
                        addlist.clear();
                        addlist.addAll(lists);
                        convenienceAdapter.notifyDataSetChanged();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                xRecyclerView.refreshComplete();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtil.Toast(context,"网络错误，请检查");
                xRecyclerView.refreshComplete();

            }
        });
        request.putValue("beginid","0");
        request.putValue("type","1");
        request.putValue("city", HelperApplication.getInstance().mDistrict);
        Log.e("获取便民圈",HelperApplication.getInstance().mDistrict);
        SingleVolleyRequest.getInstance(context).addToRequestQueue(request);
    }

    private void getMoreDatas(){
        String url= NetConstant.CONVENIENCE;
        StringPostRequest request=new StringPostRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject=new JSONObject(s);
                    String status=jsonObject.getString("status");
                    if (status.equals("success")){
                        String data=jsonObject.getString("data");
                        Gson gson=new Gson();
                        List<Convenience> lists=gson.fromJson(data,new TypeToken<ArrayList<Convenience>>(){}.getType());
                        addlist.addAll(lists);
                        convenienceAdapter.notifyDataSetChanged();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                xRecyclerView.refreshComplete();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtil.Toast(context,"网络错误，请检查");
                xRecyclerView.refreshComplete();
            }

        });
        Convenience lastConV =addlist.get(addlist.size()-1);
        request.putValue("beginid",lastConV.getMid());
        request.putValue("type","1");
        request.putValue("city", HelperApplication.getInstance().mDistrict);
        Log.e("获取便民圈",HelperApplication.getInstance().mDistrict);
        SingleVolleyRequest.getInstance(context).addToRequestQueue(request);

    }
}
