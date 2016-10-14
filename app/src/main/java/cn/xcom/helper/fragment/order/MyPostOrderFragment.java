package cn.xcom.helper.fragment.order;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jcodecraeer.xrecyclerview.XRecyclerView;

import cn.xcom.helper.R;


/**
 * Created by Administrator on 2016/10/14 0014.
 * 我的发单fragment
 */

public class MyPostOrderFragment extends Fragment {
    private XRecyclerView mRecyclerView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order,container,false);
        return view;
    }

    private void initView(View v){
        mRecyclerView = (XRecyclerView) v.findViewById(R.id.rv_order);
    }

}
