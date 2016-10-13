package cn.xcom.helper.fragment.Authorized;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.xcom.helper.R;

/**
 * 绑定账号认证
 * */
public class BindAccountAuthorizedFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bind_account_authorized,container,false);
        return view;
    }



}
