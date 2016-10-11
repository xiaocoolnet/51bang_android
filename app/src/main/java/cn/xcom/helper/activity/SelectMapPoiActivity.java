package cn.xcom.helper.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.xcom.helper.R;
import cn.xcom.helper.bean.PoiInformaiton;
import cn.xcom.helper.utils.CommonAdapter;
import cn.xcom.helper.utils.ViewHolder;
import cn.xcom.helper.view.NoScrollListView;

public class SelectMapPoiActivity extends BaseActivity implements OnGetGeoCoderResultListener {

    @BindView(R.id.rl_help_me_back)
    RelativeLayout rlHelpMeBack;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.layout_default)
    LinearLayout layoutDefault;
    @BindView(R.id.mapview)
    MapView mapview;
    @BindView(R.id.lv_address)
    NoScrollListView lvAddress;
    private Context context;

    private double lat;
    private double lon;
    GeoCoder mSearch = null; // 搜索模块，也可去掉地图模块独立使用
    private LatLng mLat;
    BaiduMap mBaiduMap = null;
    private List<PoiInformaiton> poiInformaitons;
    private CommonAdapter<PoiInformaiton> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_select_map_poi);
        ButterKnife.bind(this);
        context = this;
        poiInformaitons = new ArrayList<>();
        mBaiduMap = mapview.getMap();
        initEvent();
        // 初始化搜索模块，注册事件监听
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(this);
        getData();
    }

    /**
     * 从上一页面接受经纬度
     */
    private void getData() {
        lat = getIntent().getDoubleExtra("lat", 0);
        lon = getIntent().getDoubleExtra("lon", 0);
        Log.e("hello", lat + lon + "");
        mLat = new LatLng(lat,lon);
        // 反Geo搜索
        mSearch.reverseGeoCode(new ReverseGeoCodeOption()
                .location(mLat));
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        lvAddress.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("siteName",poiInformaitons.get(position).getName());
                intent.putExtra("siteLat",poiInformaitons.get(position).getLat());
                intent.putExtra("siteLon",poiInformaitons.get(position).getLon());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        // editText 离开监听
        /*etSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // hasFocus 为false时表示点击了别的控件，离开当前editText控件
                if (!hasFocus) {
                    if ("".equals(etSearch.getText().toString())) {
                        layoutDefault.setVisibility(View.VISIBLE);
                    }
                }
                else {
                    layoutDefault.setVisibility(View.GONE);
                }
            }
        });*/
    }

    @OnClick(R.id.rl_help_me_back)
    public void onClick() {
        finish();
    }


    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
        mBaiduMap.clear();
        mBaiduMap.addOverlay(new MarkerOptions().position(mLat)
                .icon(BitmapDescriptorFactory
                        .fromResource(R.mipmap.ic_dingwei_shou)));
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(mLat));
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(mLat).zoom(18.0f);
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        setAdapter(result);
    }

    /**
     * 为周边设置信息
     * @param result
     */
    private void setAdapter(ReverseGeoCodeResult result) {
        poiInformaitons.clear();
        List<PoiInfo> poiInfos = result.getPoiList();
        for(int i=0;i<poiInfos.size();i++){
            PoiInformaiton poiInformaiton = new PoiInformaiton();
            poiInformaiton.setName(poiInfos.get(i).name);
            poiInformaiton.setAddress(poiInfos.get(i).address);
            poiInformaiton.setLat(poiInfos.get(i).location.latitude);
            poiInformaiton.setLon(poiInfos.get(i).location.longitude);
            poiInformaitons.add(poiInformaiton);
        }
        if(adapter!=null){
            adapter.notifyDataSetChanged();
        }else{
            adapter = new CommonAdapter<PoiInformaiton>(context, poiInformaitons, R.layout.item_poi_info) {
                @Override
                public void convert(ViewHolder holder, PoiInformaiton poiInformaiton) {
                    holder.setText(R.id.tv_name,poiInformaiton.getName());
                    holder.setText(R.id.tv_address,poiInformaiton.getAddress());
                }
            };
        }
        lvAddress.setAdapter(adapter);
    }

    @Override
    protected void onPause() {
        mapview.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mapview.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mapview.onDestroy();
        super.onDestroy();
    }
}
