package cn.xcom.helper.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cn.xcom.helper.R;
import cn.xcom.helper.bean.TaskItemInfo;

/**
 * Created by Administrator on 2016/10/14 0014.
 */

public class OrderRecyclerViewAdapter extends RecyclerView.Adapter<OrderRecyclerViewAdapter.ViewHolder>{
    private Context mContext;
    private List<TaskItemInfo> taskItemInfos;
    public OrderRecyclerViewAdapter(Context context,List<TaskItemInfo> taskItemInfos){
        mContext = context;
        this.taskItemInfos = taskItemInfos;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_my_post_order,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TaskItemInfo taskItemInfo = taskItemInfos.get(position);
        holder.orderCompleteStateTv.setText(taskItemInfo.getTaskid());
    }

    @Override
    public int getItemCount() {
        return taskItemInfos.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView orderCompleteStateTv;
        public ViewHolder(View itemView) {
            super(itemView);
            orderCompleteStateTv = (TextView) itemView.findViewById(R.id.tv_order_complete_state);
        }
    }
}
