package cn.xcom.helper.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cn.xcom.helper.R;
import cn.xcom.helper.bean.ShopGoodInfo;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.utils.MyImageLoader;

/**
 * Created by Administrator on 2016/10/18 0018.
 * 我的订单页面适配器
 */

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.ViewHold> {
    private Context mContext;
    private List<ShopGoodInfo> goodInfos;
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener (OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
    public MyOrderAdapter(Context context, List<ShopGoodInfo> goodInfos) {
        mContext = context;
        this.goodInfos = goodInfos;
    }

    @Override
    public ViewHold onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_my_order, parent, false);
        return new ViewHold(view);

    }

    @Override
    public int getItemCount() {
        return goodInfos.size();
    }

    @Override
    public void onBindViewHolder(ViewHold holder, final int position) {
        ShopGoodInfo goodInfo = goodInfos.get(position);
        if (goodInfo.getPicture().size() > 0) {
            MyImageLoader.display(NetConstant.NET_DISPLAY_IMG +
                    goodInfo.getPicture().get(0).getFile(), holder.goodImage);
        } else {
            MyImageLoader.display(NetConstant.NET_DISPLAY_IMG, holder.goodImage);
        }
        holder.goodTitle.setText(goodInfo.getGoodsname());
        holder.goodPrice.setText(goodInfo.getMoney());
        switch (Integer.valueOf(goodInfo.getState())){
            case -1:
                holder.orderState.setText("已取消");
                break;
            case 1:
                holder.orderState.setText("待付款");
                holder.payBtn.setVisibility(View.VISIBLE);
                break;
            case 2:
                holder.orderState.setText("待发货");
                holder.cancelPaymentBtn.setVisibility(View.VISIBLE);
                break;
            case 3:
                holder.orderState.setText("待消费");
                break;
            case 4:
                holder.orderState.setText("待评价");
                holder.commentBtn.setVisibility(View.VISIBLE);
                break;
        }
        holder.payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "payBtn", Toast.LENGTH_SHORT).show();;
            }
        });

        holder.cancelPaymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "cancelPaymentBtn", Toast.LENGTH_SHORT).show();;
            }
        });

        holder.commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "commentBtn", Toast.LENGTH_SHORT).show();;
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onClick(position);
            }
        });


    }

    class ViewHold extends RecyclerView.ViewHolder {
        private ImageView goodImage;
        private TextView goodPrice,goodTitle,orderState;
        private Button payBtn,commentBtn,cancelPaymentBtn;

        public ViewHold(View itemView) {
            super(itemView);
            goodImage = (ImageView) itemView.findViewById(R.id.iv_order_image);
            goodTitle = (TextView) itemView.findViewById(R.id.tv_order_title);
            orderState = (TextView) itemView.findViewById(R.id.tv_order_state);
            goodPrice = (TextView) itemView.findViewById(R.id.tv_money);
            payBtn = (Button) itemView.findViewById(R.id.btn_to_pay);
            commentBtn = (Button) itemView.findViewById(R.id.btn_to_comment);
            cancelPaymentBtn = (Button) itemView.findViewById(R.id.btn_cancel_payment);

        }
    }

    public interface OnItemClickListener{
        void onClick(int position);
    }

}
