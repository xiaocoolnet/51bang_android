package cn.xcom.helper.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.xcom.helper.R;
import cn.xcom.helper.adapter.ViewPageAdapter;
import cn.xcom.helper.bean.Front;
import cn.xcom.helper.bean.ShopGoodInfo;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.utils.MyImageLoader;

public class ShopActivity extends BaseActivity implements View.OnClickListener {
    private ViewPager vp;
    private ImageView imageView,imageView2;
    private TextView tvContent,price,tvprice,adress;
    private RelativeLayout backImage;
    private List addViewList;//添加图片的list
    private ViewPageAdapter viewPageAdapter;
    private Front front;
    private ShopGoodInfo shopGoodInfo;
    private Context context;
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
}
    //初始化控件
    public void initView(){
        context=this;
        addViewList=new ArrayList();
        vp= (ViewPager) findViewById(R.id.vp);
        tvContent= (TextView) findViewById(R.id.tvContent);
        price= (TextView) findViewById(R.id.price);
        tvprice= (TextView) findViewById(R.id.tvprice);

        backImage= (RelativeLayout) findViewById(R.id.back);
        backImage.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
        }
    }
}
