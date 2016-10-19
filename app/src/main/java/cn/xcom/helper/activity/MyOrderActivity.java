package cn.xcom.helper.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RelativeLayout;

import com.lzy.widget.tab.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;

import cn.xcom.helper.R;
import cn.xcom.helper.adapter.MyOrderAdapter;
import cn.xcom.helper.fragment.order.MyOrderFragment;
import cn.xcom.helper.fragment.order.MyPostOrderFragment;

/**
 * Created by Administrator on 2016/10/18 0018.
 * 我的订单页面
 */

public class MyOrderActivity extends BaseActivity implements View.OnClickListener {
    private Context mContext;
    private RelativeLayout rl_back;
    private PagerSlidingTabStrip pagerTitles;
    private ViewPager viewPager;
    private List<Fragment> fragments;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        initView();
    }

    private void initView(){
        rl_back= (RelativeLayout) findViewById(R.id.rl_bill_back);
        rl_back.setOnClickListener(this);
        pagerTitles = (PagerSlidingTabStrip) findViewById(R.id.order_title);
        viewPager = (ViewPager) findViewById(R.id.vp_my_order);
        MyOrderFragment fragment1 = MyOrderFragment.newInstance(1);
        MyOrderFragment fragment2 = MyOrderFragment.newInstance(2);
        MyOrderFragment fragment3 = MyOrderFragment.newInstance(3);
        MyOrderFragment fragment4 = MyOrderFragment.newInstance(4);
        fragments = new ArrayList<>();
        fragments.add(fragment1);
        fragments.add(fragment2);
        fragments.add(fragment3);
        fragments.add(fragment4);
        MyOrderFragmentAdapter adapter = new MyOrderFragmentAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        pagerTitles.setViewPager(viewPager);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_bill_back:
                finish();
                break;
        }
    }

    public class MyOrderFragmentAdapter extends FragmentPagerAdapter{

        private String titles[] = {"全部","待付款","待消费","待评价"};

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        public MyOrderFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }

}