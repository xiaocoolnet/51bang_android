package cn.xcom.helper.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import cn.finalteam.galleryfinal.model.PhotoInfo;
import cn.xcom.helper.R;
import cn.xcom.helper.utils.MyImageLoader;

/**
 * Created by 尉鑫鑫 on 2016/9/20.
 */
public class GridViewAdapter extends BaseAdapter {
    private int i;
    private ArrayList<PhotoInfo> list;
    private Context context;

    public GridViewAdapter(Context context,   ArrayList<PhotoInfo> list) {
        this.context = context;
        this.list = list;
        i=list.size();
    }

    @Override
    public int getCount() {
       // return list.size();
        return i > 8 ? 9 : i+1;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView==null){
            viewHolder=new ViewHolder();
            convertView= LayoutInflater.from(context).inflate(R.layout.layout_gridview,null);
            viewHolder.imageView1= (ImageView) convertView.findViewById(R.id.add_image);
           // viewHolder.imageView2= (ImageView) convertView.findViewById(R.id.delete_image);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }

        if (position+1<=i){
            Log.d("======hahaaaa",list.size() + "");
            MyImageLoader.display("file:/" + list.get(position).getPhotoPath(), viewHolder.imageView1);
       }
        return convertView;
    }
    public class ViewHolder{
        ImageView imageView1,imageView2;
    }
}
