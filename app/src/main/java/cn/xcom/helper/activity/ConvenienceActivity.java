package cn.xcom.helper.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.xcom.helper.R;
import cn.xcom.helper.adapter.ConvenienceAdapter;
import cn.xcom.helper.bean.Convenience;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.utils.SingleVolleyRequest;
import cn.xcom.helper.utils.StringPostRequest;
import cn.xcom.helper.utils.ToastUtil;
import cn.xcom.helper.utils.ToastUtils;

public class ConvenienceActivity extends BaseActivity implements View.OnClickListener{

    private RelativeLayout back;
    private TextView cnnvenience_release;
    private ListView listView_Convenience;
    private Button convenience_deliver;
    private List<Convenience> addlist;
    private ConvenienceAdapter convenienceAdapter;
    private Context context;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Handler handler=new Handler();
    private Runnable runnable=new Runnable() {
        @Override
        public void run() {
            swipeRefreshLayout.setRefreshing(false);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convenience);
        initView();
        addData();
        swipeRefresh();
    }

    private void initView() {
        context=this;
        addlist=new ArrayList<>();
        back = (RelativeLayout) findViewById(R.id.back);
        back.setOnClickListener(this);
        cnnvenience_release = (TextView) findViewById(R.id.cnnvenience_release);
        cnnvenience_release.setOnClickListener(this);
        listView_Convenience = (ListView) findViewById(R.id.listView_Convenience);
        convenience_deliver= (Button) findViewById(R.id.convenience_deliver);
        convenience_deliver.setOnClickListener(this);
        swipeRefreshLayout= (SwipeRefreshLayout) findViewById(R.id.convenience_refelsh);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.cnnvenience_release:
                startActivity(new Intent(ConvenienceActivity.this,ReleaseConvenienceActivity.class));
                break;

            case R.id. convenience_deliver:
                startActivity(new Intent(ConvenienceActivity.this,DeliverActivity.class));
                break;
        }
    }
    public void addData(){
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
                        addlist=gson.fromJson(data,new TypeToken<ArrayList<Convenience>>(){}.getType());
                        convenienceAdapter=new ConvenienceAdapter(addlist,context);
                        listView_Convenience.setAdapter(convenienceAdapter);
                        convenienceAdapter.notifyDataSetChanged();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtil.Toast(context,"网络错误，请检查");
            }
        });
        request.putValue("beginid","0");
        request.putValue("type","1");
        SingleVolleyRequest.getInstance(context).addToRequestQueue(request);
    }
    public void swipeRefresh(){
        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.colorTheme);
        swipeRefreshLayout.setSize(SwipeRefreshLayout.LARGE);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorTextWhite));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                handler.removeCallbacks(runnable);
                handler.postDelayed(runnable, 1000);
                addData();
                ToastUtils.showToast(context, "刷新成功");
            }
        });
    }
}
