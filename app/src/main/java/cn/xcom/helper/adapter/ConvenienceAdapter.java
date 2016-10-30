package cn.xcom.helper.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnDismissListener;
import com.bigkoo.alertview.OnItemClickListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.xcom.helper.R;
import cn.xcom.helper.bean.Convenience;
import cn.xcom.helper.bean.UserInfo;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.utils.MyImageLoader;
import cn.xcom.helper.utils.NoScrollGridView;
import cn.xcom.helper.utils.RoundImageView;
import cn.xcom.helper.utils.SingleVolleyRequest;
import cn.xcom.helper.utils.StringPostRequest;
import cn.xcom.helper.utils.TimeUtils;
import cn.xcom.helper.utils.ToastUtil;

/**
 * Created by Administrator on 2016/9/25 0025.
 */
public class ConvenienceAdapter extends BaseAdapter {
    private List<Convenience> list;
    private List<String>addList;
    private Context context;
    private ImageView imageView;
    private ListGridview listGridview;
    private UserInfo userInfo;

    public ConvenienceAdapter(List<Convenience> list, Context context) {
        this.list = list;
        this.context = context;
        userInfo = new UserInfo(context);
        userInfo.readData(context);
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
            viewHolder.iv_shanchu = (ImageView) convertView.findViewById(R.id.iv_shanchu);
            viewHolder.iv_jubao = (ImageView) convertView.findViewById(R.id.iv_jubao);
          //  viewHolder.convenience_image = (ImageView) convertView.findViewById(R.id.convenience_image);
            viewHolder.convenience_phone= (ImageView) convertView.findViewById(R.id.convenience_phone);
            viewHolder.noScrollGridView= (NoScrollGridView) convertView.findViewById(R.id.gridview);
            convertView.setTag(viewHolder);
        }else {
           viewHolder= (ViewHolder) convertView.getTag();
        }
          final Convenience convenience=list.get(position);

          MyImageLoader.display(NetConstant.NET_DISPLAY_IMG + convenience.getPhoto(), viewHolder.convenience_photo);
          viewHolder.convenience_name.setText(convenience.getName());
          viewHolder.convenience_time.setText(TimeUtils.getDateToString(convenience.getCreate_time()*1000));
          viewHolder.convenience_content.setText(convenience.getContent());
          addList=new ArrayList<>();
          if (convenience.getPic().size()>0){
              for (int i=0;i<convenience.getPic().size();i++){
                  addList.add(NetConstant.NET_DISPLAY_IMG +convenience.getPic().get(i).getPictureurl());
              }
          }
          listGridview=new ListGridview(context,addList);
          viewHolder.noScrollGridView.setAdapter(listGridview);
        //监听打电话
        viewHolder.convenience_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + list.get(position).getPhone()));
                context.startActivity(intent);
            }
        });
        //显示删除按钮
        if(convenience.getUserid().equals(userInfo.getUserId())){
            viewHolder.iv_jubao.setVisibility(View.GONE);
            viewHolder.iv_shanchu.setVisibility(View.VISIBLE);
            viewHolder.iv_shanchu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertView mAlertView = new AlertView("提示", "你确定删除自己的便民圈？", "取消", new String[]{"确定"}, null, context, AlertView.Style.Alert, new OnItemClickListener() {
                        @Override
                        public void onItemClick(Object o, int position) {
                            if (position == 0) {
                                String url = NetConstant.DELETE_OWN_POST;
                                StringPostRequest request = new StringPostRequest(url, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String s) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(s);
                                            String status = jsonObject.getString("status");
                                            if (status.equals("success")) {
                                                ToastUtil.showShort(context, "删除成功");
                                                notifyDataSetChanged();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError volleyError) {
                                        ToastUtil.Toast(context, "网络错误，请检查");
                                    }
                                });
                                request.putValue("userid", userInfo.getUserId());
                                request.putValue("id", convenience.getMid());
                                SingleVolleyRequest.getInstance(context).addToRequestQueue(request);
                            }
                        }
                    }).setCancelable(true).setOnDismissListener(new OnDismissListener() {
                        @Override
                        public void onDismiss(Object o) {

                        }
                    });
                    mAlertView.show();
                }
            });
        }
        //显示举报按钮
        if(!convenience.getUserid().equals(userInfo.getUserId())){
            viewHolder.iv_jubao.setVisibility(View.VISIBLE);
            viewHolder.iv_shanchu.setVisibility(View.GONE);
            viewHolder.iv_jubao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertView mAlertView = new AlertView("提示", "你确定举报这条便民圈？", "取消", new String[]{"确定"}, null, context, AlertView.Style.Alert, new OnItemClickListener() {
                        @Override
                        public void onItemClick(Object o, int position) {
                            if (position == 0) {
                                String url = NetConstant.REPORT_OHTHER_POST;
                                StringPostRequest request = new StringPostRequest(url, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String s) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(s);
                                            String status = jsonObject.getString("status");
                                            if (status.equals("success")) {
                                                ToastUtil.showShort(context, "举报成功");
                                                notifyDataSetChanged();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError volleyError) {
                                        ToastUtil.Toast(context, "网络错误，请检查");
                                    }
                                });
                                request.putValue("userid", userInfo.getUserId());
                                request.putValue("refid", convenience.getMid());
                                request.putValue("type", "1");
                                SingleVolleyRequest.getInstance(context).addToRequestQueue(request);
                            }
                        }
                    }).setCancelable(true).setOnDismissListener(new OnDismissListener() {
                        @Override
                        public void onDismiss(Object o) {

                        }
                    });
                    mAlertView.show();
                }
            });
        }
        return convertView;
    }

    public class ViewHolder {
        public RoundImageView convenience_photo;
        public TextView convenience_name;
        public TextView convenience_time;
        public TextView convenience_content;
        public ImageView convenience_image;
        public ImageView convenience_phone;
        public NoScrollGridView noScrollGridView;
        public ImageView iv_shanchu,iv_jubao;



    }
}
