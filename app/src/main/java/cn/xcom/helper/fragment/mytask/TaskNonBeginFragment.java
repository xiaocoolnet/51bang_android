package cn.xcom.helper.fragment.mytask;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

import cn.xcom.helper.R;
import cn.xcom.helper.bean.TaskItemInfo;
import cn.xcom.helper.bean.UserInfo;
import cn.xcom.helper.utils.CommonAdapter;

/**
 * Created by Administrator on 2016/9/27 0027.
 */
public class TaskNonBeginFragment extends Fragment {
    private Context context;
    private List<TaskItemInfo> taskItemInfos;
    private UserInfo userInfo;
    private CommonAdapter<TaskItemInfo> adapter;
    private SwipeRefreshLayout srl_task;
    private ListView lv_task;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.no_begin_task,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getActivity();
        initView();
    }

    /**
     * 初始化布局
     */
    private void initView() {

    }
}
