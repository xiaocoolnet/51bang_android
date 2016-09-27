package cn.xcom.helper.activity;
/*
  * 点击便民圈礼的图片放大
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import cn.xcom.helper.R;
import cn.xcom.helper.adapter.ViewPageAdapter;
import cn.xcom.helper.bean.Convenience;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.utils.MyImageLoader;
import cn.xcom.helper.utils.SmoothImageView;

public class SpaceImageDetailActivity extends AppCompatActivity {
    private int mLocationX;
    private int mLocationY;
    private int mWidth;
    private int mHeight;
    private ImageView imageView1;
    private SmoothImageView imageView;
    private Convenience convenience;
    private Context context;
    private List addViewList;//添加图片的list
    private ViewPager viewPager;
    private ViewPageAdapter viewPageAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去掉Activity上面的状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_space_image_detail);
        getSupportActionBar().hide();
        context=this;
        addViewList=new ArrayList();
        Intent intent=getIntent();
        viewPager= (ViewPager) findViewById(R.id.view_pager);
        convenience= (Convenience) intent.getSerializableExtra("list");
        mLocationX = getIntent().getIntExtra("locationX", 0);
        mLocationY = getIntent().getIntExtra("locationY", 0);
        mWidth = getIntent().getIntExtra("width", 0);
        mHeight = getIntent().getIntExtra("height", 0);
        imageView = new SmoothImageView(this);
        imageView.setOriginalInfo(mWidth, mHeight, mLocationX, mLocationY);
        imageView.transformIn();
        imageView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        if (convenience.getPic().size()>0){
            for (int i=0;i<convenience.getPic().size();i++){

                imageView1=new ImageView(context);
                imageView.buildDrawingCache();
                Bitmap bmap = imageView.getDrawingCache();
                imageView1.setImageBitmap(bmap);
                ImageLoader.getInstance().displayImage(
                        NetConstant.NET_DISPLAY_IMG + convenience.getPic().get(i).getPictureurl(), imageView1);
                imageView1.setScaleType(ImageView.ScaleType.FIT_CENTER);
                addViewList.add(imageView1);
                viewPageAdapter=new ViewPageAdapter(addViewList);
                viewPager.setAdapter(viewPageAdapter);
            }
        }else {
            MyImageLoader.display(NetConstant.NET_DISPLAY_IMG, imageView);
            Toast.makeText(context, "暂无图片",Toast.LENGTH_LONG).show();
        }

        //MyImageLoader.display(NetConstant.NET_DISPLAY_IMG + convenience.getPic().get(0).getPictureurl(),imageView);

    }
}
