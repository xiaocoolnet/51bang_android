package cn.xcom.helper.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;

import java.util.List;

import cn.xcom.helper.R;
import cn.xcom.helper.activity.AuthenticationListActivity;
import cn.xcom.helper.activity.CityPickerActivity;
import cn.xcom.helper.activity.HelpMeActivity;
import cn.xcom.helper.activity.IHelpActivity;

/**
 * Created by zhuchongkun on 16/5/27.
 * 主页面——地图
 */
public class MapFragment extends Fragment implements View.OnClickListener{
    private  String TAG="MapFragment";
    private Context mContext;
    private RelativeLayout rl_location,rl_authentication_list;
    private TextView tv_I_help,tv_help_me,tv_city_interaction;
    private MapView mMapView = null;
    private BaiduMap mBaiduMap;
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext=getActivity();
        initView();
    }
    private void initView(){
        rl_location= (RelativeLayout) getView().findViewById(R.id.rl_fragment_map_location);
        rl_location.setOnClickListener(this);
        rl_authentication_list= (RelativeLayout) getView().findViewById(R.id.rl_fragment_map_authentication_list);
        rl_authentication_list.setOnClickListener(this);
        tv_I_help= (TextView) getView().findViewById(R.id.tv_fragment_map_I_help);
        tv_I_help.setOnClickListener(this);
        tv_help_me= (TextView) getView().findViewById(R.id.tv_fragment_map_help_me);
        tv_help_me.setOnClickListener(this);
        tv_city_interaction= (TextView) getView().findViewById(R.id.tv_fragment_map_city_interaction);
        tv_city_interaction.setOnClickListener(this);
        mMapView = (MapView) getView().findViewById(R.id.mapView_fragment_map);
        mBaiduMap = mMapView.getMap();
        //普通地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
//        //卫星地图
//        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
//        //空白地图, 基础地图瓦片将不会被渲染。在地图类型中设置为NONE，将不会使用流量下载基础地图瓦片图层。使用场景：与瓦片图层一起使用，节省流量，提升自定义瓦片图下载速度。
//        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NONE);
        mBaiduMap.setIndoorEnable(true); // 打开室内图
        mLocationClient = new LocationClient(mContext.getApplicationContext());     //声明LocationClient类
        mLocationClient.registerLocationListener( myListener );    //注册监听函数
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
        initLocation();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        // 当不需要定位图层时关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_fragment_map_location:
                startActivity(new Intent(mContext, CityPickerActivity.class));
                break;
            case R.id.rl_fragment_map_authentication_list:
                startActivity(new Intent(mContext, AuthenticationListActivity.class));
                break;
            case R.id.tv_fragment_map_I_help:
                startActivity(new Intent(mContext, IHelpActivity.class));
                break;
            case R.id.tv_fragment_map_help_me:
                startActivity(new Intent(mContext, HelpMeActivity.class));
                break;
            case R.id.tv_fragment_map_city_interaction:
                Toast.makeText(mContext,"未开发",Toast.LENGTH_SHORT).show();
                break;
        }

    }
    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span=1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            display(location);
            //Receive Location
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            sb.append("\nradius : ");
            sb.append(location.getRadius());
            if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());// 单位：公里每小时
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
                sb.append("\nheight : ");
                sb.append(location.getAltitude());// 单位：米
                sb.append("\ndirection : ");
                sb.append(location.getDirection());// 单位度
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append("\ndescribe : ");
                sb.append("gps定位成功");

            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                //运营商信息
                sb.append("\noperationers : ");
                sb.append(location.getOperators());
                sb.append("\ndescribe : ");
                sb.append("网络定位成功");
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }
            sb.append("\nlocationdescribe : ");
            sb.append(location.getLocationDescribe());// 位置语义化信息
            List<Poi> list = location.getPoiList();// POI数据
            if (list != null) {
                sb.append("\npoilist size = : ");
                sb.append(list.size());
                for (Poi p : list) {
                    sb.append("\npoi= : ");
                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                }
            }
            Log.i("BaiduLocationApiDem", sb.toString());
        }

    }
    private void display(BDLocation location){
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
// 构造定位数据
        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(location.getRadius())
                // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(100).latitude(location.getLatitude())
                .longitude(location.getLongitude()).build();
// 设置定位数据
        mBaiduMap.setMyLocationData(locData);
// 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）
        MyLocationConfiguration.LocationMode mCurrentMode= MyLocationConfiguration.LocationMode.NORMAL;
        BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_002);
        MyLocationConfiguration config = new MyLocationConfiguration(mCurrentMode, true, mCurrentMarker);
        mBaiduMap.setMyLocationConfigeration(config);
    }
    }
