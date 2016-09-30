package cn.xcom.helper.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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
import cn.xcom.helper.adapter.ViewPageAdapter;
import cn.xcom.helper.bean.Collection;
import cn.xcom.helper.bean.Front;
import cn.xcom.helper.bean.ShopGoodInfo;
import cn.xcom.helper.bean.UserInfo;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.utils.MyImageLoader;
import cn.xcom.helper.utils.SingleVolleyRequest;
import cn.xcom.helper.utils.StringPostRequest;

public class ShopActivity extends BaseActivity implements View.OnClickListener {
    private ViewPager vp;
    private ImageView imageView,collect;
    private TextView tvContent,price,tvprice,adress;
    private RelativeLayout backImage;
    private List addViewList;//添加图片的list
    private ViewPageAdapter viewPageAdapter;
    private Front front;
    private ShopGoodInfo shopGoodInfo;
    private Context context;
    private UserInfo userInfo;
    private List<Collection>addList;
    private Collection collection;
    private int flag=2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        initView();
        Intent intent=getIntent();
        front= (Front) intent.getSerializableExtra("item1");
        tvContent.setText(front.getDescription());
        price.setText("￥"+front.getPrice());
        tvprice.setText("￥"+front.getPrice());

        if (front.getPicturelist().size()>0){
            for (int i=0;i<front.getPicturelist().size();i++){
                 imageView=new ImageView(this);
                 MyImageLoader.display(NetConstant.NET_DISPLAY_IMG + front.getPicturelist().get(i).getFile(),
                    imageView);
                  imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                  addViewList.add(imageView);
            }
        }else{
            imageView=new ImageView(this);
            MyImageLoader.display(NetConstant.NET_DISPLAY_IMG,imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            addViewList.add(imageView);
    }
    viewPageAdapter=new ViewPageAdapter(addViewList);
    vp.setAdapter(viewPageAdapter);
    vp.setCurrentItem(0);
        collectionList();
}
    //初始化控件
    public void initView(){
        context=this;
        userInfo=new UserInfo();
        userInfo.readData(context);
        addList=new ArrayList<>();
        addViewList=new ArrayList();
        vp= (ViewPager) findViewById(R.id.vp);
        tvContent= (TextView) findViewById(R.id.tvContent);
        price= (TextView) findViewById(R.id.price);
        tvprice= (TextView) findViewById(R.id.tvprice);
        collect= (ImageView) findViewById(R.id.collect);
        collect.setOnClickListener(this);
        backImage= (RelativeLayout) findViewById(R.id.back);
        backImage.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
            case R.id.collect:
                if (flag==1){
                    cancleCollection();
                }else if (flag==2){
                    collection();
                }
                break;
        }
    }
    //点击收藏商品
    public void collection(){
        String url=NetConstant.GOOD_COLLECTION;
        StringPostRequest request=new StringPostRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject=new JSONObject(s);

                    String status=jsonObject.getString("status");
                    Log.d("yijingshoucang", status);
                    if (status.equals("success")){
                        flag=1;
                        collect.setImageResource(R.mipmap.yijingshoucang);
                        Toast.makeText(context, "收藏成功", Toast.LENGTH_LONG).show();

                    }else{
                        flag=2;
                        Toast.makeText(context,"收藏失败",Toast.LENGTH_LONG).show();
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
        request.putValue("userid", userInfo.getUserId());
        Log.d("shoucangid", userInfo.getUserId());
        if (front.getPicturelist().size()==0){
            request.putValue("goodsid","");
        }else{
            request.putValue("goodsid",front.getPicturelist().get(0).getGoodsid());
        }
        //  request.putValue("goodsid",front.getPicturelist().get(0).getGoodsid());
        request.putValue("type",front.getType());
        request.putValue("title", front.getGoodsname());
        request.putValue("description", front.getDescription());
        SingleVolleyRequest.getInstance(context).addToRequestQueue(request);
    }
    //判断该商品是否被收藏
    public void judgeIsCollection(){
        for ( int j=0;j< addList.size();j++){
            if (front.getId().equals(addList.get(j).getObject_id())){
                String url = NetConstant.GOOD_IS_COLLECTION;
                StringPostRequest request = new StringPostRequest(url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            Log.d("==sou",s);
                            String status = jsonObject.getString("status");
                            if (status.equals("success")) {
                                flag=1;
                                collect.setImageResource(R.mipmap.yijingshoucang);

                            } else {
                                flag=2;
                                collect.setImageResource(R.mipmap.shoucang);
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
                request.putValue("userid", userInfo.getUserId());
                request.putValue("refid", collection.getObject_id());
                request.putValue("type", collection.getType());
                SingleVolleyRequest.getInstance(context).addToRequestQueue(request);
                break;
            }
        }



    }
    //获取收藏列表
    public void collectionList(){
        String url=NetConstant.HAS_COLLECTION;
        StringPostRequest request=new StringPostRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject=new JSONObject(s);
                    String data=jsonObject.getString("data");
                    Log.d("==用户data", data);
                    if (data==null){
                        collection=null;
                        Log.d("==用户是否收藏", "用户没有收藏");
                    }else {
                        Log.d("==用户是否收藏", "用户有收藏");
                        Gson gson=new Gson();
                        addList=gson.fromJson(data,
                                new TypeToken<ArrayList<Collection>>(){}.getType());
                        for (int i=0;i<addList.size();i++){
                            collection=addList.get(i);
                        }
                        judgeIsCollection();
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
        request.putValue("userid", userInfo.getUserId());
        SingleVolleyRequest.getInstance(context).addToRequestQueue(request);

    }
    //取消收藏
    public void cancleCollection(){
        String url = NetConstant.GOOD_CANCLE_COLLECTION;
        StringPostRequest request = new StringPostRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    Log.d("==sou",s);
                    String status = jsonObject.getString("status");
                    if (status.equals("success")) {
                        flag=2;
                        collect.setImageResource(R.mipmap.shoucang);
                        Toast.makeText(context,"取消收藏",Toast.LENGTH_LONG).show();
                        Log.d("===quxiao","取消收藏成功");
                    } else {
                        flag=1;
                        collect.setImageResource(R.mipmap.yijingshoucang);
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
        request.putValue("userid", userInfo.getUserId());
        if (front.getPicturelist().size()==0){
            request.putValue("goodsid","");
        }else {
            request.putValue("goodsid", front.getPicturelist().get(0).getGoodsid());
        }

        request.putValue("type", front.getType());
        SingleVolleyRequest.getInstance(context).addToRequestQueue(request);
    }

}
