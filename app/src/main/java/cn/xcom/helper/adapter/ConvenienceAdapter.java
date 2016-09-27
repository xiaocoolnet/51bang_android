package cn.xcom.helper.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cn.xcom.helper.R;
import cn.xcom.helper.activity.SpaceImageDetailActivity;
import cn.xcom.helper.bean.Convenience;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.utils.MyImageLoader;
import cn.xcom.helper.utils.RoundImageView;
import cn.xcom.helper.utils.TimeUtils;

/**
 * Created by Administrator on 2016/9/25 0025.
 */
public class ConvenienceAdapter extends BaseAdapter {
    private List<Convenience> list;
    private Context context;

    public ConvenienceAdapter(List<Convenience> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.convenience_layout, null);
            viewHolder.convenience_photo = (RoundImageView) convertView.findViewById(R.id.convenience_photo);
            viewHolder.convenience_name = (TextView) convertView.findViewById(R.id.convenience_name);
            viewHolder.convenience_time = (TextView) convertView.findViewById(R.id.convenience_time);
            viewHolder.convenience_content = (TextView) convertView.findViewById(R.id.convenience_content);
            viewHolder.convenience_image = (ImageView) convertView.findViewById(R.id.convenience_image);
            viewHolder.convenience_phone= (ImageView) convertView.findViewById(R.id.convenience_phone);
            convertView.setTag(viewHolder);
        }else {
           viewHolder= (ViewHolder) convertView.getTag();
        }
          final Convenience convenience=list.get(position);
          MyImageLoader.display(NetConstant.NET_DISPLAY_IMG + convenience.getPhoto(), viewHolder.convenience_photo);
          viewHolder.convenience_name.setText(convenience.getName());
          viewHolder.convenience_time.setText(TimeUtils.getDateToString(convenience.getCreate_time()));
          viewHolder.convenience_content.setText(convenience.getContent());
          if (convenience.getPic().size()>0){
              MyImageLoader.display(NetConstant.NET_DISPLAY_IMG +convenience.getPic().get(0).getPictureurl(),viewHolder.convenience_image );
          }else{
              MyImageLoader.display(NetConstant.NET_DISPLAY_IMG , viewHolder.convenience_image);
          }
        final ViewHolder finalViewHolder = viewHolder;
        //监听图片
        viewHolder.convenience_image.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  if (convenience.getPic().size()==0){
                      Toast.makeText(context, "暂无图片", Toast.LENGTH_SHORT).show();
                  }else{
                      Intent intent = new Intent(context, SpaceImageDetailActivity.class);
                      Bundle bundle=new Bundle();
                      bundle.putSerializable("list",list.get(position));
                      intent.putExtras(bundle);
                      int[] location = new int[2];
                      intent.putExtra("locationX", location[1]);//必须
                      intent.putExtra("locationY", location[0]);//必须
                      intent.putExtra("width", finalViewHolder.convenience_image.getWidth());//必须
                      intent.putExtra("height", finalViewHolder.convenience_image.getHeight());//必须
                      context.startActivity(intent);
                  }


              }
          });
        //监听打电话
        viewHolder.convenience_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setAction(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + list.get(position).getPhone()));
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    public class ViewHolder {
        public RoundImageView convenience_photo;
        public TextView convenience_name;
        public TextView convenience_time;
        public TextView convenience_content;
        public ImageView convenience_image;
        public ImageView convenience_phone;



    }
}
