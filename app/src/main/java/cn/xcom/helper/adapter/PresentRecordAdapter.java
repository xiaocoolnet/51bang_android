package cn.xcom.helper.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.xcom.helper.R;

/**
 * Created by zhuchongkun on 16/6/16.
 */
public class PresentRecordAdapter extends RecyclerView.Adapter<PresentRecordAdapter.ViewHolder> {

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_present_record,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_type,tv_time,tv_money,tv_account;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_type= (TextView) itemView.findViewById(R.id.tv_item_present_record_type);
            tv_time= (TextView) itemView.findViewById(R.id.tv_item_present_record_time);
            tv_money= (TextView) itemView.findViewById(R.id.tv_item_present_record_money);
            tv_account= (TextView) itemView.findViewById(R.id.tv_item_present_record_account);
        }
    }
}
