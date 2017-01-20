package cn.xcom.helper.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import cn.xcom.helper.activity.ChatActivity;
import cn.xcom.helper.bean.Convenience;
import cn.xcom.helper.bean.UserInfo;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.record.SoundView;
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
public class ConvenienceAdapter extends RecyclerView.Adapter<ConvenienceAdapter.ViewHolder> {
    private List<Convenience> list;
    private List<String> addList;
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
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.convenience_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Convenience convenience = list.get(position);

        MyImageLoader.display(NetConstant.NET_DISPLAY_IMG + convenience.getPhoto(), holder.convenience_photo);
        holder.convenience_name.setText(convenience.getName());
        holder.convenience_time.setText(TimeUtils.getDateToString(convenience.getCreate_time() * 1000));
        holder.convenience_content.setText(convenience.getContent());
        addList = new ArrayList<>();
        if (convenience.getPic().size() > 0) {
            for (int i = 0; i < convenience.getPic().size(); i++) {
                addList.add(NetConstant.NET_DISPLAY_IMG + convenience.getPic().get(i).getPictureurl());
            }
        }
        listGridview = new ListGridview(context, addList);
        holder.noScrollGridView.setAdapter(listGridview);
        //监听打电话
        holder.convenience_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + list.get(position).getPhone()));
                context.startActivity(intent);
            }
        });
        //聊天
        holder.convenience_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("id", convenience.getUserid());
                intent.putExtra("name", convenience.getName());
                context.startActivity(intent);
            }
        });
        //显示删除按钮
        if (convenience.getUserid().equals(userInfo.getUserId())) {
            holder.iv_jubao.setVisibility(View.GONE);
            holder.iv_shanchu.setVisibility(View.VISIBLE);
            holder.iv_shanchu.setOnClickListener(new View.OnClickListener() {
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
        if (!convenience.getUserid().equals(userInfo.getUserId())) {
            holder.iv_jubao.setVisibility(View.VISIBLE);
            holder.iv_shanchu.setVisibility(View.GONE);
            holder.iv_jubao.setOnClickListener(new View.OnClickListener() {
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

        int soundTime = 0;
        if (!TextUtils.isEmpty(convenience.getSoundtime())) {
            soundTime = Integer.valueOf(convenience.getSoundtime());
        }
        if (TextUtils.isEmpty(convenience.getSound())) {
            holder.soundView.setVisibility(View.GONE);
        } else {
            holder.soundView.setVisibility(View.VISIBLE);
            holder.soundView.init(NetConstant.NET_DISPLAY_IMG + convenience.getSound(), soundTime);
        }


    }

    @Override
    public int getItemCount() {
        return list.size() > 9 ? 9 : list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private RoundImageView convenience_photo;
        private TextView convenience_name;
        private TextView convenience_time;
        private TextView convenience_content;
        private ImageView convenience_image;
        private ImageView convenience_phone, convenience_message;
        private NoScrollGridView noScrollGridView;
        private ImageView iv_shanchu, iv_jubao;
        private SoundView soundView;

        public ViewHolder(View itemView) {
            super(itemView);
            convenience_photo = (RoundImageView) itemView.findViewById(R.id.convenience_photo);
            convenience_name = (TextView) itemView.findViewById(R.id.convenience_name);
            convenience_time = (TextView) itemView.findViewById(R.id.convenience_time);
            convenience_content = (TextView) itemView.findViewById(R.id.convenience_content);
            convenience_message = (ImageView) itemView.findViewById(R.id.convenience_message);
            iv_shanchu = (ImageView) itemView.findViewById(R.id.iv_shanchu);
            iv_jubao = (ImageView) itemView.findViewById(R.id.iv_jubao);
            //  viewHolder.convenience_image = (ImageView) convertView.findViewById(R.id.convenience_image);
            convenience_phone = (ImageView) itemView.findViewById(R.id.convenience_phone);
            noScrollGridView = (NoScrollGridView) itemView.findViewById(R.id.gridview);
            soundView = (SoundView) itemView.findViewById(R.id.sound_view);

        }

    }
}
