package cn.xcom.helper.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
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

public class SelectMapPoiActivity extends BaseActivity implements OnGetGeoCoderResultListener {

    @BindView(R.id.rl_help_me_back)
    RelativeLayout rlHelpMeBack;
    @BindView(R.id.mapview)
    MapView mapview;
    @BindView(R.id.lv_address)
    ListView lvAddress;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.iv_search_clear)
    ImageView ivSearchClear;
    @BindView(R.id.ll_address)
    LinearLayout llAddress;
    @BindView(R.id.listview_search_result)
    ListView listviewSearchResult;
    private Context context;

    private double lat;
    private double lon;
    GeoCoder mSearch = null; // 搜索模块，也可去掉地图模块独立使用
    private LatLng mLat;
    BaiduMap mBaiduMap = null;
    private List<PoiInformaiton> poiInformaitons;
    private CommonAdapter<PoiInformaiton> adapter;
    private LatLng currentPt;
    private Marker marker;
    private boolean isFisrtIn = true;

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
        initListener();
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
        mLat = new LatLng(lat, lon);
        // 反Geo搜索
        mSearch.reverseGeoCode(new ReverseGeoCodeOption()
                .location(mLat));
    }


    /**
     * 对地图事件的消息响应
     */
    private void initListener() {
        mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChange(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {
                mLat = mBaiduMap.getMapStatus().target;
                mSearch.reverseGeoCode(new ReverseGeoCodeOption()
                        .location(mLat));
                Log.e("中心点", mLat.latitude + "");
                if (marker != null) {
                    marker.remove();
                }
                mBaiduMap.clear();
            }
        });
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        lvAddress.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("siteName", poiInformaitons.get(position).getName());
                intent.putExtra("siteLat", poiInformaitons.get(position).getLat());
                intent.putExtra("siteLon", poiInformaitons.get(position).getLon());
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


    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
        if (result == null) {
            return;
        }
        mBaiduMap.clear();
        if (isFisrtIn) {
            mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(mLat));
            MapStatus.Builder builder = new MapStatus.Builder();
            builder.target(mLat).zoom(18.0f);
            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        }
        mBaiduMap.addOverlay(new MarkerOptions().position(mLat)
                .icon(BitmapDescriptorFactory
                        .fromResource(R.mipmap.ic_dingwei_shou)));
        createMarker(result.getLocation(), result.getPoiList().get(0).name);
        setAdapter(result);
    }

    /**
     * 构建坐标点
     *
     * @param mPt
     * @param name
     */
    public void createMarker(final LatLng mPt, String name) {
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.mipmap.ic_dingwei_shou);
        OverlayOptions options = new MarkerOptions()
                .position(mPt)  //设置marker的位置
                .icon(bitmap)  //设置marker图标
                .zIndex(9)  //设置marker所在层级
                .draggable(true);  //设置手势拖拽
        //将marker添加到地图上
        marker = (Marker) (mBaiduMap.addOverlay(options));
        //创建InfoWindow展示的view
        Button button = new Button(context);
        button.setText(name);
        //创建InfoWindow , 传入 view， 地理坐标， y 轴偏移量
        InfoWindow mInfoWindow = new InfoWindow(button, mPt, -47);
        //显示InfoWindow
        mBaiduMap.showInfoWindow(mInfoWindow);
    }

    /**
     * 为周边设置信息
     *
     * @param result
     */
    private void setAdapter(ReverseGeoCodeResult result) {
        poiInformaitons.clear();
        List<PoiInfo> poiInfos = result.getPoiList();
        for (int i = 0; i < poiInfos.size(); i++) {
            PoiInformaiton poiInformaiton = new PoiInformaiton();
            poiInformaiton.setName(poiInfos.get(i).name);
            poiInformaiton.setAddress(poiInfos.get(i).address);
            poiInformaiton.setLat(poiInfos.get(i).location.latitude);
            poiInformaiton.setLon(poiInfos.get(i).location.longitude);
            poiInformaitons.add(poiInformaiton);
        }
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        } else {
            adapter = new CommonAdapter<PoiInformaiton>(context, poiInformaitons, R.layout.item_poi_info) {
                @Override
                public void convert(ViewHolder holder, PoiInformaiton poiInformaiton) {
                    holder.setText(R.id.tv_name, poiInformaiton.getName());
                    holder.setText(R.id.tv_address, poiInformaiton.getAddress());
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

    @OnClick({R.id.rl_help_me_back, R.id.iv_search_clear})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_help_me_back:
                finish();
                break;
            case R.id.iv_search_clear:
                break;
        }
    }
}
