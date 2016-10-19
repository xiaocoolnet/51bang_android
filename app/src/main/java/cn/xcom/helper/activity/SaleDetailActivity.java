package cn.xcom.helper.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.xcom.helper.R;
import cn.xcom.helper.WXpay.Constants;
import cn.xcom.helper.adapter.ViewPageAdapter;
import cn.xcom.helper.bean.Collection;
import cn.xcom.helper.bean.Front;
import cn.xcom.helper.bean.ShopGoodInfo;
import cn.xcom.helper.bean.UserInfo;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.utils.MyImageLoader;
import cn.xcom.helper.utils.SingleVolleyRequest;
import cn.xcom.helper.utils.StringPostRequest;
import cn.xcom.helper.utils.ToastUtil;
import cn.xcom.helper.utils.ToastUtils;
import cn.xcom.helper.view.SharePopupWindow;

public class SaleDetailActivity extends BaseActivity implements View.OnClickListener {
    private ViewPager vp;
    private ImageView imageView,collect;
    private TextView tvContent,price,tvprice,adress,buy;
    private RelativeLayout backImage;
    private RelativeLayout shopPublish;
    private List addViewList;//添加图片的list
    private ViewPageAdapter viewPageAdapter;
    private Front front;
    private ShopGoodInfo shopGoodInfo=new ShopGoodInfo();
    private Context context;
    private UserInfo userInfo;
    private List<Collection>addList;
    private Collection collection;
    private int flag=2,wxflag=1;
    SharePopupWindow takePhotoPopWin;
    private RelativeLayout rl_share;
    IWXAPI msgApi;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sale_detail);
        context=this;
        msgApi = WXAPIFactory.createWXAPI(context, Constants.APP_ID, false);
        msgApi.registerApp(Constants.APP_ID);
         initView();
        //得到传过来的数据
        Intent intent=getIntent();
        front= (Front) intent.getSerializableExtra("item10");
        tvContent.setText(front.getDescription());
        price.setText("￥" + front.getPrice());
        tvprice.setText("￥" + front.getPrice());
        adress.setText(front.getAddress());
        Log.e("========shipeiqiwocao", "" + front.getPicturelist().size());
        if (front.getPicturelist().size()>0){
            for (int i=0;i<front.getPicturelist().size();i++){
                imageView=new ImageView(this);
                MyImageLoader.display(NetConstant.NET_DISPLAY_IMG +front.getPicturelist().get(i).getFile(),
                        imageView);
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                addViewList.add(imageView);
            }
        }else{
            imageView=new ImageView(this);
            MyImageLoader.display(NetConstant.NET_DISPLAY_IMG,imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            addViewList.add(imageView);
        }
        viewPageAdapter=new ViewPageAdapter(addViewList);
        vp.setAdapter(viewPageAdapter);
        vp.setCurrentItem(0);
        collectionList();
        addGood();
  }

    //初始化控件
    public void initView(){
        userInfo=new UserInfo();
        userInfo.readData(context);
        addViewList=new ArrayList();
        addList=new ArrayList<>();
        vp= (ViewPager) findViewById(R.id.vp);
        tvContent= (TextView) findViewById(R.id.tvContent);
        collect= (ImageView) findViewById(R.id.collect);
        collect.setOnClickListener(this);
        price= (TextView) findViewById(R.id.price);
        tvprice= (TextView) findViewById(R.id.tvprice);
        adress= (TextView) findViewById(R.id.adress);
        backImage= (RelativeLayout) findViewById(R.id.back);
        backImage.setOnClickListener(this);
        shopPublish= (RelativeLayout) findViewById(R.id.shopPublish);
        shopPublish.setOnClickListener(this);
        buy= (TextView) findViewById(R.id.buy);
        buy.setOnClickListener(this);
        rl_share = (RelativeLayout) findViewById(R.id.rl_share);
        rl_share.setOnClickListener(this);
    }
    //根据商品的id得到商家的id
    public void addGood(){

        String url= NetConstant.SHOP_INFO;
        StringPostRequest request=new StringPostRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject=new JSONObject(s);
                    String status=jsonObject.getString("status");
                    if (status.equals("success")){
                        String data=jsonObject.getString("data");
                        Log.d("data",data);
                        Gson gson=new Gson();
                        shopGoodInfo=gson.fromJson(data,
                                new TypeToken<ShopGoodInfo>() {
                                }.getType());

                        Log.d("data1111",shopGoodInfo.getUserid());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showToast(context, "网络错误，请检查网络");
            }
        });
        request.putValue("id",front.getId());
        SingleVolleyRequest.getInstance(context).addToRequestQueue(request);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back :
               finish();
                break;
            case R.id.shopPublish:
                if (shopGoodInfo.getUserid()==""){
                    Intent intent=new Intent(SaleDetailActivity.this,ShopReleaseActivity.class);
                    startActivity(intent);
                    ToastUtil.Toast(context,"网络错误，请检查网络");
                }else{
                    Intent intent=new Intent(SaleDetailActivity.this,ShopReleaseActivity.class);
                    intent.putExtra("userid",shopGoodInfo.getUserid());
                    Log.d("===id",front.getId());
                    startActivity(intent);
                }

                break;
            case R.id.collect:
                if (flag==1){
                    cancleCollection();
                }else if (flag==2){
                    collection();
                }
                break;
            case R.id.buy:
                Intent intent=new Intent(context,BuyActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("price", front);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.rl_share:
                showPopFormBottom(v);
                break;
        }
    }

    public void showPopFormBottom(View view) {
        takePhotoPopWin = new SharePopupWindow(this, onClickListener);
        //SharePopupWindow takePhotoPopWin = new SharePopupWindow(this, onClickListener);
        takePhotoPopWin.showAtLocation(findViewById(R.id.rl_bottom), Gravity.BOTTOM, 0, 0);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.haoyou:
                    ToastUtils.showToast(SaleDetailActivity.this, "微信好友");
                    setting();
                    break;
                case R.id.dongtai:
                    ToastUtils.showToast(SaleDetailActivity.this, "微信朋友圈");
                    history();
                    break;
            }
        }
    };

    /**
     * 微信分享网页
     * */
    private void shareWX() {
        //创建一个WXWebPageObject对象，用于封装要发送的Url
        WXWebpageObject webpage =new WXWebpageObject();
        webpage.webpageUrl=NetConstant.SHARE_SHOP_H5+userInfo.getUserId();
        WXMediaMessage msg =new WXMediaMessage(webpage);
        msg.title="我注册了51bang，发布了商品，来加入吧";
        msg.description="基于同城个人，商户服务 。商品购买。给个人，商户提供交流与服务平台";
        Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.logo_wx);
        msg.setThumbImage(thumb);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = "weiyi";
        req.message = msg;
        req.scene = wxflag==0? SendMessageToWX.Req.WXSceneSession: SendMessageToWX.Req.WXSceneTimeline;
        msgApi.sendReq(req);
    }

    /**
     * 分享到微信好友
     */
    private void setting() {
        //ToastUtils.ToastShort(this, "分享到微信好友");
        wxflag = 0;
        shareWX();
        takePhotoPopWin.dismiss();

    }

    /**
     * 分享到微信朋友圈
     */
    private void history() {
        // ToastUtils.ToastShort(this, "分享到微信朋友圈");
        wxflag = 1;
        shareWX();
        takePhotoPopWin.dismiss();
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
                    Log.d("yijingshoucang",status);
                    if (status.equals("success")){
                        flag=1;
                        collect.setImageResource(R.mipmap.yijingshoucang);
                        Toast.makeText(context,"收藏成功",Toast.LENGTH_LONG).show();

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
