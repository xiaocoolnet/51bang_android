package cn.xcom.helper.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import cn.xcom.helper.R;
import cn.xcom.helper.adapter.ViewPageAdapter;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.entity.Front;
import cn.xcom.helper.utils.MyImageLoader;

public class SaleDetailActivity extends BaseActivity implements View.OnClickListener {
    private ViewPager vp;
    private ImageView imageView,imageView2;
    private TextView tvContent,price,tvprice,adress;
    private RelativeLayout backImage;
    private LinearLayout shopPublish;
    private List addViewList;//添加图片的list
    private ViewPageAdapter viewPageAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sale_detail);
         initView();

        //得到传过来的数据
        Intent intent=getIntent();
        Front  front= (Front) intent.getSerializableExtra("item");
        tvContent.setText(front.getDescription());
        price.setText("￥"+front.getPrice());
        tvprice.setText("￥"+front.getPrice());
        adress.setText(front.getAddress());
        if (front.getPicturelist().size()>0){
            for (int i=0;i<front.getPicturelist().size();i++){
                imageView=new ImageView(this);
                MyImageLoader.display(NetConstant.NET_DISPLAY_IMG +front.getPicturelist().get(i).getFile(),
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
  }
    //初始化控件
    public void initView(){
        addViewList=new ArrayList();
        vp= (ViewPager) findViewById(R.id.vp);
        tvContent= (TextView) findViewById(R.id.tvContent);
        price= (TextView) findViewById(R.id.price);
        tvprice= (TextView) findViewById(R.id.tvprice);
        adress= (TextView) findViewById(R.id.adress);
        backImage= (RelativeLayout) findViewById(R.id.back);
        backImage.setOnClickListener(this);
        shopPublish= (LinearLayout) findViewById(R.id.shopPublish);
        shopPublish.setOnClickListener(this);
    }
    //点击返回键
    public void backImage(){
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back :
               backImage();
                break;
            case R.id.shopPublish:
                Intent intent=new Intent(SaleDetailActivity.this,ShopReleaseActivity.class);
                startActivity(intent);
                break;
        }
    }
}
