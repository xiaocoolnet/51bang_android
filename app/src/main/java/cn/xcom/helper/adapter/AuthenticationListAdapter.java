package cn.xcom.helper.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import cn.xcom.helper.R;

/**
 * Created by zhuchongkun on 16/8/31.
 */
public class AuthenticationListAdapter extends RecyclerView.Adapter<AuthenticationListAdapter.ViewHolder>{
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_authentication_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_name;
        private ImageView iv_head;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_head= (ImageView) itemView.findViewById(R.id.iv_item_authentication_list_head);
            tv_name= (TextView) itemView.findViewById(R.id.tv_item_authentication_list_name);
        }
    }
}
