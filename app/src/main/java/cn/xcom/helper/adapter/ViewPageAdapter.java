package cn.xcom.helper.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by zhuchongkun on 16/6/16.
 */
public class ViewPageAdapter extends PagerAdapter {
	private List<ImageView> addList;//存放数据

	public ViewPageAdapter(List<ImageView> addList) {
		this.addList = addList;
	}

	@Override
	public int getCount() {
		return addList.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view==object;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		container.addView( addList.get(position));
		return addList.get(position);
	}
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(addList.get(position));
	}
}
