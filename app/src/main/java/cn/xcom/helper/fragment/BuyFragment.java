package cn.xcom.helper.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.navi.BaiduMapAppNotSupportNaviException;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviParaOption;
import com.baidu.mapapi.utils.DistanceUtil;
import com.baidu.mapapi.utils.OpenClientUtil;
import com.baidu.mapapi.utils.route.BaiduMapRoutePlan;
import com.baidu.mapapi.utils.route.RouteParaOption;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnDismissListener;
import com.bigkoo.alertview.OnItemClickListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.kyleduo.switchbutton.SwitchButton;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.xcom.helper.HelperApplication;
import cn.xcom.helper.R;
import cn.xcom.helper.activity.AuthorizedActivity;
import cn.xcom.helper.activity.ChangeSkillsActivity;
import cn.xcom.helper.activity.InsureActivity;
import cn.xcom.helper.activity.MyTaskActivity;
import cn.xcom.helper.activity.TaskDetailActivity;
import cn.xcom.helper.bean.TaskInfo;
import cn.xcom.helper.bean.UserInfo;
import cn.xcom.helper.constant.HelperConstant;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.net.HelperAsyncHttpClient;
import cn.xcom.helper.utils.CommonAdapter;
import cn.xcom.helper.utils.LogUtils;
import cn.xcom.helper.utils.SPUtils;
import cn.xcom.helper.utils.ToastUtil;
import cn.xcom.helper.utils.ViewHolder;
import cn.xcom.helper.view.MenuPopupWindow;
import cz.msebera.android.httpclient.Header;

/**
 * Created by zhuchongkun on 16/5/27.
 * 主页面——抢单
 */
public class BuyFragment extends Fragment implements View.OnClickListener{
    private String TAG="BuyFragment";
    private Context mContext;
    private Button bt_authorized;
    private UserInfo userInfo;
    private SwitchButton sb;
    private RelativeLayout ll_task;
    private SwipeRefreshLayout srl_task;
    private ListView lv_task;
    private SwitchButton sb_change;
    private boolean isChecked = true;
    private List<TaskInfo> taskInfos;
    private CommonAdapter<TaskInfo> adapter;
    private MenuPopupWindow menuPopupWindow;
    // 定位相关
    LocationClient mLocClient;
    public BDLocationListener myListener = new MyLocationListener();
    private String district;
    private boolean isFirstIn = true;
    private KProgressHUD hud;

    int flag = 0;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = null;
        Log.e("state",SPUtils.get(getActivity(), HelperConstant.IS_HAD_AUTHENTICATION,"").toString());
        if(SPUtils.get(getActivity(), HelperConstant.IS_HAD_AUTHENTICATION,"").equals("1")){
            view = inflater.inflate(R.layout.activity_grab_task,container,false);
        }else{
            view = inflater.inflate(R.layout.fragment_buy,container,false);
        }
        return view;
    }

    /**
     * 开始定位
     */
    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        mLocClient.setLocOption(option);
        mLocClient.start();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext=getActivity();
        taskInfos = new ArrayList<>();
        mLocClient = new LocationClient(mContext);     //声明LocationClient类
        mLocClient.registerLocationListener(myListener);    //注册监听函数
        initLocation();
        initView();
    }
    private void initView(){
        userInfo=new UserInfo(mContext);
        if(SPUtils.get(getActivity(), HelperConstant.IS_HAD_AUTHENTICATION,"").equals("1")){
            ll_task = (RelativeLayout) getView().findViewById(R.id.ll_task);
            ll_task.setOnClickListener(this);
            srl_task = (SwipeRefreshLayout) getView().findViewById(R.id.grab_task_srl);
            lv_task = (ListView) getView().findViewById(R.id.grab_task_list);
            sb_change = (SwitchButton) getView().findViewById(R.id.sb_fragment_buy);
            srl_task.setColorSchemeResources(R.color.background_white);
            srl_task.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.colorTheme));
            srl_task.setProgressViewOffset(true, 10, 100);
            srl_task.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    getData(HelperApplication.getInstance().mDistrict);
                }
            });
            lv_task.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(mContext, TaskDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("taskInfo",taskInfos.get(position));
                    intent.putExtras(bundle);
                    //抢单列表
                    intent.putExtra("type","1");
                    mContext.startActivity(intent);
                }
            });
        }else{
            bt_authorized= (Button) getView().findViewById(R.id.bt_fragment_buy_authorized);
            bt_authorized.setOnClickListener(this);
            sb= (SwitchButton) getView().findViewById(R.id.sb_fragment_buy);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        userInfo.readData(mContext);
        if(!isFirstIn){
            getData(HelperApplication.getInstance().mDistrict);
        }
        //getIdentity();
    }

    /**
     *显示选择菜单
     * */
    private void showPopupMenu() {
        menuPopupWindow = new MenuPopupWindow(mContext, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.add_qun:
                        startActivity(new Intent(mContext, MyTaskActivity.class));
                        menuPopupWindow.dismiss();
                        break;
                    case R.id.tong_bu:
                        startActivity(new Intent(mContext, ChangeSkillsActivity.class));
                        menuPopupWindow.dismiss();
                        break;
                }
            }
        });
        //设置弹出位置
        int[] location = new int[2];
        ll_task.getLocationOnScreen(location);
        menuPopupWindow.showAsDropDown(ll_task);

    }

    /**
     * 加载数据
     */
    private void getData(String district) {
        if(SPUtils.get(getActivity(), HelperConstant.IS_HAD_AUTHENTICATION,"").equals("1")){
            getWorkingState();
            RequestParams params=new RequestParams();
            params.put("city",district);
            Log.e("city",HelperApplication.getInstance().mDistrict);
            //params.put("city","芝罘区");
            params.put("userid",userInfo.getUserId());
            HelperAsyncHttpClient.get(NetConstant.GETTASKLIST, params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    if(srl_task!=null){
                        srl_task.setRefreshing(false);
                    }
                    if (response.optString("status").equals("success")) {
                        setAdapter(response);
                    }
                    LogUtils.e(TAG, response.toString());
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    LogUtils.e(TAG, responseString);
                }
            });
        }
    }

    /**
     * 获取当前用户开工收工状态
     */
    private void getWorkingState() {
        RequestParams params=new RequestParams();
        params.put("userid", userInfo.getUserId());
        HelperAsyncHttpClient.get(NetConstant.GET_WORKING_STATE, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                String state = response.optString("data");
                if(sb_change==null){
                    return;
                }
                if (state.equals("0")) {
                    sb_change.setChecked(false);
                } else if (state.equals("1")) {
                    sb_change.setChecked(true);
                }
                sb_change.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!SPUtils.get(mContext, HelperConstant.IS_INSURANCE, "").equals("1")) {
                            popDialog(mContext, "提示", "您未通过保险认证，不能开工，是否跳转到保险认证页面进行验证？");
                        } else {
                            if (sb_change.isChecked()) {
                                changeWorkingState("1");
                            } else {
                                changeWorkingState("0");
                            }
                        }
                    }
                });

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                LogUtils.e(TAG, responseString);
            }
        });
    }

    /**
     * 切换工作状态
     */
    private void changeWorkingState(String state) {
        hud = KProgressHUD.create(mContext)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(true);
        hud.show();
        RequestParams params=new RequestParams();
        params.put("userid",userInfo.getUserId());
        params.put("address",HelperApplication.getInstance().mLocAddress);
        params.put("longitude",HelperApplication.getInstance().mLocLon);
        params.put("latitude",HelperApplication.getInstance().mLocLat);
        params.put("isworking", state);
        HelperAsyncHttpClient.get(NetConstant.CHANGE_WORKING_STATE, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if(hud!=null){
                    hud.dismiss();
                }
                super.onSuccess(statusCode, headers, response);
                Log.e(TAG, String.valueOf(response));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                if(hud!=null){
                    hud.dismiss();
                }
                LogUtils.e(TAG, responseString);
            }
        });
    }

    /**
     * 根据请求数据
     * @param response
     */
    private void setAdapter(JSONObject response) {
        taskInfos.clear();
        taskInfos.addAll(getBeanFromJson(response));
        if(adapter!=null){
            adapter.notifyDataSetChanged();
        }else{
            adapter = new CommonAdapter<TaskInfo>(mContext,taskInfos,R.layout.item_task_info) {
                @Override
                public void convert(ViewHolder holder, TaskInfo taskInfo) {
                    setItem(holder,taskInfo);
                }
            };
            if(lv_task==null){
                return;
            }
            lv_task.setAdapter(adapter);
        }
    }

    /**
     * 为item填充内容
     * @param holder
     * @param taskInfo
     */
    private void setItem(ViewHolder holder, final TaskInfo taskInfo) {
        holder.setText(R.id.tv_name, taskInfo.getName())
                .setText(R.id.tv_task_name, taskInfo.getTitle())
                .setTimeText(R.id.tv_task_time, taskInfo.getTime())
                .setTimeTextWithStr(R.id.tv_expiry_time, taskInfo.getExpirydate(), "前有效")
                .setText(R.id.tv_type_name, taskInfo.getDescription())
                .setText(R.id.tv_address1,taskInfo.getAddress())
                .setText(R.id.tv_address2,taskInfo.getSaddress())
                .setText(R.id.tv_btn_grab, taskInfo.getState().equals("1") ? "抢单" : "已被抢")
                .setImageByUrl(R.id.iv_avatar, taskInfo.getPhoto())
                .setText(R.id.tv_price, taskInfo.getPrice());
        if(!taskInfo.getLongitude().equals("")&&!taskInfo.getLatitude().equals("")&&!taskInfo.getSlatitude().equals("")&&!taskInfo.getSlongitude().equals("")){
            holder.setText(R.id.tv_distance, (int) DistanceUtil.getDistance(
                    new LatLng(Double.parseDouble(taskInfo.getLatitude()),
                            Double.parseDouble(taskInfo.getLongitude())),
                    new LatLng(Double.parseDouble(taskInfo.getSlatitude()),
                            Double.parseDouble(taskInfo.getSlongitude()))) + "米");
        }
        TextView btn_grab = holder.getView(R.id.tv_btn_grab);
        if(taskInfo.getState().equals("1")){
            if(Long.parseLong(taskInfo.getExpirydate()) * 1000 < new Date().getTime()){
                btn_grab.setClickable(false);
                btn_grab.setText("过期");
                btn_grab.setTextColor(getResources().getColor(R.color.holo_red_light));
            }else{
                btn_grab.setClickable(true);
                btn_grab.setText("抢单");
                btn_grab.setTextColor(getResources().getColor(R.color.colorTheme));
                btn_grab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!SPUtils.get(mContext,HelperConstant.IS_INSURANCE,"").equals("1")){
                            popDialog(mContext,"提示","您未通过保险认证，不能抢单，是否跳转到保险认证页面进行验证？");
                        }else{
                            if(taskInfo.getUserid().equals(userInfo.getUserId())){
                                ToastUtil.showShort(mContext,"不能抢自己发布的任务");
                            }else{
                                updateState(taskInfo.getId());
                            }
                        }
                    }
                });
            }
        }else{
            btn_grab.setClickable(false);
            btn_grab.setText("已被抢");
            btn_grab.setTextColor(getResources().getColor(R.color.holo_red_light));
        }
        /*//启用百度地图app导航
        if(taskInfo.getLongitude().length()>0&&taskInfo.getLatitude().length()>0
                &&taskInfo.getSlongitude().length()>0&&taskInfo.getSlatitude().length()>0){
            holder.getView(R.id.ll_address).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startRoutePlanDriving(taskInfo.getLatitude(),taskInfo.getLongitude(),taskInfo.getAddress()
                                        ,taskInfo.getSlatitude(),taskInfo.getSlongitude(),taskInfo.getSaddress());
                }
            });
        }
        //启用百度地图app导航
        if(taskInfo.getLongitude().length()>0&&taskInfo.getLatitude().length()>0
                &&taskInfo.getSlongitude().length()>0&&taskInfo.getSlatitude().length()>0){
            holder.getView(R.id.ll_address).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startRoutePlanDriving(taskInfo.getLatitude(), taskInfo.getLongitude(), taskInfo.getAddress()
                            , taskInfo.getSlatitude(), taskInfo.getSlongitude(), taskInfo.getSaddress());
                }
            });
        }*/
        if(taskInfo.getLongitude().length()>0&&taskInfo.getLatitude().length()>0
                &&taskInfo.getSlongitude().length()>0&&taskInfo.getSlatitude().length()>0){
            holder.getView(R.id.ll_map).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startRoutePlanDriving(taskInfo.getLatitude(), taskInfo.getLongitude(), taskInfo.getAddress()
                            , taskInfo.getSlatitude(), taskInfo.getSlongitude(), taskInfo.getSaddress());
                }
            });
        }
    }

    /**
     * 弹框
     * @param context
     * @param title
     * @param message
     */
    private void popDialog(final Context context, String title, String message) {
        AlertView mAlertView = new AlertView(title, message, "取消", new String[]{"确定"}, null, context, AlertView.Style.Alert, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {
                if (position == 0) {
                    startActivity(new Intent(mContext, InsureActivity.class));
                }
            }
        }).setCancelable(true).setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(Object o) {

            }
        });
        mAlertView.show();
    }

    /**
     * 启动百度地图驾车路线规划
     */
    public void startRoutePlanDriving(String taskInfoLatitude, String taskInfoLongitude, String taskInfoAddress, String latitude, String longitude, String address) {
        // 起点坐标
        double mLat1 = Double.parseDouble(taskInfoLatitude);
        double mLon1 = Double.parseDouble(taskInfoLongitude);
        // 终点
        double mLat2 = Double.parseDouble(latitude);
        double mLon2 = Double.parseDouble(longitude);
        LatLng pt1 = new LatLng(mLat1, mLon1);
        LatLng pt2 = new LatLng(mLat2, mLon2);
        // 构建 route搜索参数
        RouteParaOption para = new RouteParaOption()
                .startPoint(pt1)
                .startName(taskInfoAddress)
                .endPoint(pt2)
                .endName(address);
        try {
            BaiduMapRoutePlan.setSupportWebRoute(true);
            BaiduMapRoutePlan.openBaiduMapDrivingRoute(para, mContext);
        } catch (Exception e) {
            e.printStackTrace();
            showDialog();
        }

    }

    /**
     * 提示未安装百度地图app或app版本过低
     */
    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("您尚未安装百度地图app或app版本过低，点击确认安装？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                OpenClientUtil.getLatestBaiduMapApp(mContext);
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();

    }

    /**
     * 启动百度地图驾车路线规划
     * @param taskInfoLatitude
     * @param taskInfoLongitude
     * @param taskInfoAddress
     * @param latitude
     * @param longitude
     * @param address
     */
    public void startRoutePlan(String taskInfoLatitude, String taskInfoLongitude, String taskInfoAddress, String latitude, String longitude, String address) {
        // 起点坐标
        double mLat1 = Double.parseDouble(taskInfoLatitude);
        double mLon1 = Double.parseDouble(taskInfoLongitude);
        // 终点
        double mLat2 = Double.parseDouble(latitude);
        double mLon2 = Double.parseDouble(longitude);
        LatLng pt1 = new LatLng(mLat1, mLon1);
        LatLng pt2 = new LatLng(mLat2, mLon2);
        BaiduMapNavigation.setSupportWebNavi(true);
        // 构建 导航参数
        NaviParaOption para = new NaviParaOption()
                .startPoint(pt1).endPoint(pt2)
                .startName(taskInfoAddress).endName(address);
        try {
        // 调起百度地图骑行导航
            BaiduMapNavigation.openBaiduMapBikeNavi(para, mContext);
        } catch (BaiduMapAppNotSupportNaviException e) {
            e.printStackTrace();
            //showDialog();
        }

    }

    /**
     * 抢单
     * @param id
     */
    private void updateState(String id) {
        RequestParams params=new RequestParams();
        params.put("userid", userInfo.getUserId());
        params.put("taskid",id);
        HelperAsyncHttpClient.get(NetConstant.GRAB_TASK, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response.optString("status").equals("success")) {
                    ToastUtil.showShort(mContext,"抢单");
                    getData(HelperApplication.getInstance().mDistrict);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    /**
     * 字符串转模型集合
     * @param response
     * @return
     */
    private List<TaskInfo> getBeanFromJson(JSONObject response) {
        String data = "";
        try {
            data = response.getString("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new Gson().fromJson(data,new TypeToken<List<TaskInfo>>(){}.getType());
    }

    private void getIdentity(){
        RequestParams params=new RequestParams();
        params.put("userid",userInfo.getUserId());
        HelperAsyncHttpClient.get(NetConstant.NET_GET_IDENTITY,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                LogUtils.e(TAG,"--statusCode->"+statusCode+"==>"+response.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                LogUtils.e(TAG,"--statusCode->"+statusCode+"==>"+responseString);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_fragment_buy_authorized:
                startActivity(new Intent(mContext, AuthorizedActivity.class));
                break;
            case R.id.ll_task:
                showPopupMenu();
        }
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if(location!=null){
                district = location.getDistrict();
                mLocClient.stop();
                if(isFirstIn){
                    isFirstIn = false;
                    getData(district);
                }
            }
        }
    }
}
