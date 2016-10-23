package cn.xcom.helper.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.xcom.helper.R;
import cn.xcom.helper.activity.SaleDetailActivity;
import cn.xcom.helper.bean.Front;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.utils.MyImageLoader;

/**
 * Created by zhuchongkun on 16/6/8.
 */
public class SaleAdapter extends BaseAdapter {
    private List<Front> addList;
    private Context context;

    public SaleAdapter(List<Front> addList, Context context) {
        this.addList = addList;
        this.context = context;
    }


    @Override
    public int getCount() {
        Log.e("=======shipei1qi", "" + addList.size());
        return addList.size();

    }

    @Override
    public Object getItem(int position) {
        return addList.get(position);

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Log.e("========shipeiqi", "ZOUBUZOU");
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.sale_fragment, null);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imgview);
            viewHolder.textView1 = (TextView) convertView.findViewById(R.id.title);
            viewHolder.textView2 = (TextView) convertView.findViewById(R.id.content);
            viewHolder.textView3 = (TextView) convertView.findViewById(R.id.price);
            viewHolder.textView4 = (TextView) convertView.findViewById(R.id.oldprice);
            viewHolder.textView5 = (TextView) convertView.findViewById(R.id.havesell);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final Front front = addList.get(position);
        Log.e("========shipeiqi", "" + addList.size());
        Log.e("========shipeiqi8989", "" + front.getPicturelist().size());
        if (front.getPicturelist().size() > 0) {
            MyImageLoader.display(NetConstant.NET_DISPLAY_IMG +
                    front.getPicturelist().get(0).getFile(), viewHolder.imageView);
        } else if (front.getPicturelist().size() == 0) {
            MyImageLoader.display(NetConstant.NET_DISPLAY_IMG, viewHolder.imageView);
        }
        viewHolder.textView1.setText(front.getGoodsname());
        viewHolder.textView2.setText(front.getDescription());
        viewHolder.textView3.setText("￥" + front.getPrice());
        viewHolder.textView4.setText(front.getOprice());
        viewHolder.textView4.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
        viewHolder.textView5.setText("已售" + front.getSellnumber());
        //对cinvertview进行监听
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SaleDetailActivity.class);
                intent.putExtra("id", front.getId());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    public class ViewHolder {
        private ImageView imageView;
        private TextView textView1, textView2, textView3, textView4, textView5;
    }
}
