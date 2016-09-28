package cn.xcom.helper.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.xcom.helper.R;
import cn.xcom.helper.bean.Front;
import cn.xcom.helper.bean.UserInfo;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.utils.MyImageLoader;
import cn.xcom.helper.utils.SingleVolleyRequest;
import cn.xcom.helper.utils.StringPostRequest;

/**
 * Created by zhuchongkun on 16/6/12.
 * 我的订单页
 */
public class OrderActivity extends BaseActivity implements View.OnClickListener{
    private String TAG="OrderActivity";
    private Context mContext;
    private RelativeLayout rl_back;
    private ListView mine_listView;
    private UserInfo userInfo;
    private String userid="";
    private List<Front>list;
    private int position1;
    private MineReleaseAdapter mineReleaseAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what==position1){
                mineReleaseAdapter.notifyDataSetChanged();
            }
        }
    };

    private Runnable runnable=new Runnable() {
        @Override
        public void run() {
            swipeRefreshLayout.setRefreshing(false);
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_order);
        mContext=this;
        initView();
        addData();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                handler.removeCallbacks(runnable);
                handler.postDelayed(runnable, 1000);
                addData();
                Toast.makeText(mContext, "刷新成功", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initView(){
        mContext=this;
        userInfo=new UserInfo();
        userInfo.readData(mContext);
        userid=userInfo.getUserId();

        rl_back= (RelativeLayout) findViewById(R.id.rl_order_back);
        rl_back.setOnClickListener(this);
        mine_listView= (ListView) findViewById(R.id.mine_listView);
        swipeRefreshLayout= (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.colorTheme);
        swipeRefreshLayout.setSize(SwipeRefreshLayout.LARGE);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorTextWhite));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_order_back:
                finish();
                break;
        }

    }
    //获取数据我发布的任务
    public void addData(){
        String url= NetConstant.SHOP_GOOD;
        StringPostRequest request=new StringPostRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject=new JSONObject(s);
                    String status=jsonObject.getString("status");
                    if (status.equals("success")){
                        String data=jsonObject.getString("data");
                        Gson gson=new Gson();
                        list=gson.fromJson(data,new TypeToken<ArrayList<Front>>(){}.getType());
                        mineReleaseAdapter=new MineReleaseAdapter(mContext,list);
                        mine_listView.setAdapter(mineReleaseAdapter);
                        mineReleaseAdapter.notifyDataSetChanged();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        request.putValue("userid", userid);
        SingleVolleyRequest.getInstance(mContext).addToRequestQueue(request);
    }
     class MineReleaseAdapter extends BaseAdapter {
        private Context context;
        private List<Front> addlist;

        public MineReleaseAdapter(Context context, List<Front> addlist) {
            this.context = context;
            this.addlist = addlist;
        }

        @Override
        public int getCount() {
            return addlist.size();
        }

        @Override
        public Object getItem(int position) {
            return addlist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder=null;
            if (convertView==null){
                viewHolder=new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.mine_layout, null);
                viewHolder.mine_image = (ImageView) convertView.findViewById(R.id.mine_image);
                viewHolder.mine_title = (TextView) convertView.findViewById(R.id.mine_title);
                viewHolder.mine_content = (TextView) convertView.findViewById(R.id.mine_content);
                viewHolder.mine_price = (TextView) convertView.findViewById(R.id.mine_price);
                viewHolder.linlayout = (LinearLayout) convertView.findViewById(R.id.linlayout);
                viewHolder.mine_editor = (TextView) convertView.findViewById(R.id.mine_editor);
                viewHolder.mine_delete = (TextView) convertView.findViewById(R.id.mine_delete);
                viewHolder.mine_saled = (TextView) convertView.findViewById(R.id.mine_saled);
                convertView.setTag(viewHolder);
            }else {
                viewHolder= (ViewHolder) convertView.getTag();
            }
            final Front front=addlist.get(position);
            if (front.getPicturelist().size()>0){
                MyImageLoader.display(NetConstant.NET_DISPLAY_IMG +
                        front.getPicturelist().get(0).getFile(), viewHolder.mine_image);
            }else {
                MyImageLoader.display(NetConstant.NET_DISPLAY_IMG , viewHolder.mine_image);
            }
            viewHolder.mine_title.setText(front.getGoodsname());
            viewHolder.mine_content.setText(front.getDescription());
            viewHolder.mine_price.setText("￥"+front.getPrice());
            viewHolder.mine_saled.setText("已售"+front.getSellnumber());
            //删除
            viewHolder.mine_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    position1=position;
                    final Message message=Message.obtain();
                    String url=NetConstant.DELETE_GOOD;
                    StringPostRequest request=new StringPostRequest(url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            try {
                                JSONObject jsonObject=new JSONObject(s);
                                String status=jsonObject.getString("status");
                                if (status.equals("success")){
                                    Toast.makeText(context,"删除成功",Toast.LENGTH_LONG).show();
                                }else {
                                    Toast.makeText(context,"删除失败",Toast.LENGTH_LONG).show();
                                }
                                addlist.remove(position);
                                message.what=position;
                                handler.sendMessage(message);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {

                        }
                    });
                    request.putValue("id",front.getId());
                    Log.d("delete", front.getId());
                    SingleVolleyRequest.getInstance(context).addToRequestQueue(request);
                }
            });
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context,ShopActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("item1", addlist.get(position));
                    intent.putExtras(bundle);
                    // intent.putExtra("item",bundle);
                    Log.i("---", position + "");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
            return convertView;
        }

        public class ViewHolder {

            public ImageView mine_image;
            public TextView mine_title;
            public TextView mine_content;
            public TextView mine_price;
            public LinearLayout linlayout;
            public TextView mine_editor;
            public TextView mine_delete;
            public TextView mine_saled;






        }
    }
}
