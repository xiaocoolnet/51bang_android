package cn.xcom.helper.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baoyz.actionsheet.ActionSheet;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.feezu.liuli.timeselector.TimeSelector;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import cn.xcom.helper.HelperApplication;
import cn.xcom.helper.R;
import cn.xcom.helper.adapter.LocalImgGridAdapter;
import cn.xcom.helper.bean.SkillTagInfo;
import cn.xcom.helper.bean.TaskType;
import cn.xcom.helper.bean.UserInfo;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.net.HelperAsyncHttpClient;
import cn.xcom.helper.utils.GalleryFinalUtil;
import cn.xcom.helper.utils.LogUtils;
import cn.xcom.helper.utils.PushImage;
import cn.xcom.helper.utils.PushImageUtil;
import cn.xcom.helper.utils.SingleVolleyRequest;
import cn.xcom.helper.utils.StringJoint;
import cn.xcom.helper.utils.StringPostRequest;
import cn.xcom.helper.utils.ToastUtil;
import cn.xcom.helper.view.NoScrollGridView;
import cn.xcom.helper.view.WheelView;
import cz.msebera.android.httpclient.Header;

/**
 * Created by zhuchongkun on 16/6/4.
 * 帮我页
 */
public class HelpMeActivity extends BaseActivity implements View.OnClickListener, OnGetGeoCoderResultListener {
    private String TAG = "HelpMeActivity";
    private Context mContext;
    private RelativeLayout rl_back;
    private NoScrollGridView gv_skill;
    private ArrayList<SkillTagInfo> skillTagInfos;
    private HelpMeSkillAdapter mHelpMeSkillAdapter;
    private EditText et_content, et_phone, et_site_location, et_service_location, et_wages, et_validity_period;
    private TextView tv_time_unit, tv_service_charge;
    private ImageView iv_site_location, iv_service_location, iv_validity_period;
    private Button bt_submit;
    private SkillTagInfo selectTag;
    private UserInfo userInfo;
    private List<TaskType> selectList;
    private LinearLayout ll_time;
    private ScrollView bottom;
    private String begintime = "";

    private double lat;
    private double lon;
    GeoCoder mSearch = null; // 搜索模块，也可去掉地图模块独立使用
    private LatLng mLat;

    private RelativeLayout rl_photo, rl_voice, rl_grid_photo;
    private NoScrollGridView addsnPicGrid;
    private GalleryFinalUtil galleryFinalUtil;
    private final int REQUEST_CODE_CAMERA = 1000;
    private final int REQUEST_CODE_GALLERY = 1001;
    private ArrayList<PhotoInfo> mPhotoList;
    private LocalImgGridAdapter localImgGridAdapter;
    private List<String> nameList;//添加相册选取完返回的的list

    private double mSiteLat, mSiteLon, mServiceLat, mServiewLon;
    private String mSiteName, mServiceName;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_help_me);
        mContext = this;
        userInfo = new UserInfo(mContext);
        userInfo.readData(mContext);
        mPhotoList = new ArrayList<>();
        galleryFinalUtil = new GalleryFinalUtil(9);
        nameList = new ArrayList<>();
        // 初始化搜索模块，注册事件监听
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(this);
        initView();
        getData();
    }

    /**
     * 从上一页面接受经纬度
     */
    private void getData() {
        lat = getIntent().getDoubleExtra("lat", 0);
        lon = getIntent().getDoubleExtra("lon", 0);
        mServiceLat = lat;
        mSiteLat = lat;
        mServiewLon = lon;
        mSiteLon = lon;
        Log.e("hello", lat + lon + "");
        mLat = new LatLng(lat, lon);
        // 反Geo搜索
        mSearch.reverseGeoCode(new ReverseGeoCodeOption()
                .location(mLat));
    }

    private void initView() {
        addsnPicGrid = (NoScrollGridView) findViewById(R.id.addsn_pic_grid);
        rl_grid_photo = (RelativeLayout) findViewById(R.id.rl_grid_photo);
        rl_photo = (RelativeLayout) findViewById(R.id.rl_photo);
        rl_photo.setOnClickListener(this);
        rl_voice = (RelativeLayout) findViewById(R.id.rl_voice);
        rl_voice.setOnClickListener(this);
        bottom = (ScrollView) findViewById(R.id.bottom);
        ll_time = (LinearLayout) findViewById(R.id.ll_time);
        ll_time.setOnClickListener(this);
        rl_back = (RelativeLayout) findViewById(R.id.rl_help_me_back);
        rl_back.setOnClickListener(this);
        et_content = (EditText) findViewById(R.id.et_help_me_content);
        et_phone = (EditText) findViewById(R.id.et_help_me_phone);
        et_site_location = (EditText) findViewById(R.id.et_help_me_site_location);
        et_service_location = (EditText) findViewById(R.id.et_help_me_service_location);
        et_wages = (EditText) findViewById(R.id.et_help_me_wages);
        //监听单价的变化
        et_wages.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tv_service_charge.setText("¥" + et_wages.getText());
            }
        });
        et_validity_period = (EditText) findViewById(R.id.et_help_me_validity_period);
        tv_time_unit = (TextView) findViewById(R.id.tv_help_me_time_unit);
        tv_time_unit.setOnClickListener(this);
        tv_service_charge = (TextView) findViewById(R.id.tv_help_me_service_charge);
        iv_site_location = (ImageView) findViewById(R.id.iv_help_me_site_location);
        iv_site_location.setOnClickListener(this);
        iv_service_location = (ImageView) findViewById(R.id.iv_help_me_service_location);
        iv_service_location.setOnClickListener(this);
        iv_validity_period = (ImageView) findViewById(R.id.iv_help_me_validity_period);
        iv_validity_period.setOnClickListener(this);
        bt_submit = (Button) findViewById(R.id.bt_help_me_submit);
        bt_submit.setOnClickListener(this);
        selectTag = new SkillTagInfo();
        skillTagInfos = new ArrayList<SkillTagInfo>();
        gv_skill = (NoScrollGridView) findViewById(R.id.gridView_help_me);
        gv_skill.setSelector(new ColorDrawable(Color.TRANSPARENT));
        mHelpMeSkillAdapter = new HelpMeSkillAdapter(mContext, skillTagInfos);
        gv_skill.setAdapter(mHelpMeSkillAdapter);
        gv_skill.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(HelpMeActivity.this, SelectTaskTypeActivity.class);
                intent.putExtra("id", skillTagInfos.get(position).getSkill_id());
                startActivity(intent);
            }
        });

        getSkill();
    }

    @Override
    protected void onResume() {
        super.onResume();
        selectList = HelperApplication.getInstance().getTaskTypes();
        Log.e("count", String.valueOf(selectList.size()));
        for (int i = 0; i < skillTagInfos.size(); i++) {
            skillTagInfos.get(i).setChecked(check(skillTagInfos.get(i).getSkill_id()));
        }
        mHelpMeSkillAdapter.notifyDataSetChanged();
    }

    /**
     * 判断大分类是否选中
     *
     * @param id
     * @return
     */
    private boolean check(String id) {
        for (int i = 0; i < selectList.size(); i++) {
            if (selectList.get(i).getParent().equals(id)) {
                return true;
            }
        }

        return false;
    }

    private void getSkill() {
        RequestParams params = new RequestParams();
        params.put("id", 0);
        HelperAsyncHttpClient.get(NetConstant.NET_GET_TASKLIST, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                LogUtils.e(TAG, "--statusCode->" + statusCode + "==>" + response.toString());
                if (response != null) {
                    try {
                        String state = response.getString("status");
                        if (state.equals("success")) {
                            JSONArray data = response.getJSONArray("data");
                            skillTagInfos.clear();
                            for (int i = 0; i < data.length(); i++) {
                                SkillTagInfo info = new SkillTagInfo();
                                JSONObject jsonObject = data.getJSONObject(i);
                                info.setSkill_id(jsonObject.getString("id"));
                                info.setSkill_name(jsonObject.getString("name"));
                                skillTagInfos.add(info);
                            }
                            mHelpMeSkillAdapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                LogUtils.e(TAG, "--statusCode->" + statusCode + "==>" + responseString);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_help_me_back:
                finish();
                break;
            //选择上门地址
            case R.id.iv_help_me_site_location:
                Intent intent1 = new Intent(mContext, SelectMapPoiActivity.class);
                intent1.putExtra("lat", lat);
                intent1.putExtra("lon", lon);
                startActivityForResult(intent1, 1);
                break;
            //选择服务地址
            case R.id.iv_help_me_service_location:
                Intent intent2 = new Intent(mContext, SelectMapPoiActivity.class);
                intent2.putExtra("lat", lat);
                intent2.putExtra("lon", lon);
                startActivityForResult(intent2, 2);
                break;
            case R.id.tv_help_me_time_unit:
                showTextPicker();
                break;
            //选择有效时间
            case R.id.iv_help_me_validity_period:
                //showTimePicker();
                Date date = new Date();
                String startStr = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date);
                TimeSelector timeSelector = new TimeSelector(this, new TimeSelector.ResultHandler() {
                    @Override
                    public void handle(String time) {
                        et_validity_period.setText(time);
                        begintime = dataOne(time + ":00");
                    }
                },startStr, "2020-12-31 23:59");
                timeSelector.show();
                break;
            case R.id.bt_help_me_submit:
                submit();
                break;
            //选择图片
            case R.id.rl_photo:
                showActionSheet();
                break;
            //语音
            case R.id.rl_voice:
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 1:
                    if (data != null) {
                        mSiteName = data.getStringExtra("siteName");
                        mSiteLat = data.getDoubleExtra("siteLat", 0);
                        mSiteLon = data.getDoubleExtra("siteLon", 0);
                        et_site_location.setText(mSiteName);
                    }
                    break;
                case 2:
                    if (data != null) {
                        mServiceName = data.getStringExtra("siteName");
                        mServiceLat = data.getDoubleExtra("siteLat", 0);
                        mServiewLon = data.getDoubleExtra("siteLon", 0);
                        et_service_location.setText(mServiceName);
                    }
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 选择图片后 返回的图片数据
     */

    private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {
        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
            if (resultList != null) {
                mPhotoList.clear();
                mPhotoList.addAll(resultList);
                rl_grid_photo.setVisibility(View.VISIBLE);
                localImgGridAdapter = new LocalImgGridAdapter(mPhotoList, HelpMeActivity.this);
                addsnPicGrid.setAdapter(localImgGridAdapter);
            }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            Toast.makeText(HelpMeActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
            rl_grid_photo.setVisibility(View.GONE);
        }
    };

    /**
     * 弹出选择框
     */
    private void showActionSheet() {
        ActionSheet.createBuilder(HelpMeActivity.this, getSupportFragmentManager())
                .setCancelButtonTitle("取消")
                .setOtherButtonTitles("打开相册", "拍照")
                .setCancelableOnTouchOutside(true)
                .setListener(new ActionSheet.ActionSheetListener() {
                    @Override
                    public void onDismiss(ActionSheet actionSheet, boolean isCancel) {

                    }

                    @Override
                    public void onOtherButtonClick(ActionSheet actionSheet, int index) {

                        switch (index) {
                            case 0:
                                galleryFinalUtil.openAblum(HelpMeActivity.this, mPhotoList, REQUEST_CODE_GALLERY, mOnHanlderResultCallback);
                                break;
                            case 1:
                                //获取拍照权限
                                if (galleryFinalUtil.openCamera(HelpMeActivity.this, mPhotoList, REQUEST_CODE_CAMERA, mOnHanlderResultCallback)) {
                                    return;
                                } else {
                                    String[] perms = {"android.permission.CAMERA"};
                                    int permsRequestCode = 200;
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermissions(perms, permsRequestCode);
                                    }
                                }
                                break;

                            default:
                                break;
                        }
                    }
                })
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        HelperApplication.getInstance().getTaskTypes().clear();
    }

    /**
     * 上传任务
     */
    private void submit() {
        if (selectList.size() == 0) {
            ToastUtil.showShort(mContext, "请选择分类");
            return;
        }
        if (et_content.getText().toString().length() == 0) {
            ToastUtil.showShort(mContext, "请输入任务描述");
            return;
        }
        if (et_phone.getText().toString().length() == 0) {
            ToastUtil.showShort(mContext, "请输入联系方式");
            return;
        }
        if (et_wages.getText().toString().length() == 0) {
            ToastUtil.showShort(mContext, "请输入工资");
            return;
        }
        if (begintime.length() == 0) {
            ToastUtil.showShort(mContext, "请选择有效时间");
            return;
        }
        //先上传图片再发布
        new PushImageUtil().setPushIamge(getApplication(), mPhotoList, nameList, new PushImage() {
            @Override
            public void success(boolean state) {

                Toast.makeText(getApplication(), "图片上传成功", Toast.LENGTH_SHORT).show();

                String url = NetConstant.PUBLISHTASK;
                StringPostRequest request = new StringPostRequest(url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (s != null) {
                            try {
                                JSONObject object = new JSONObject(s);
                                String state = object.getString("status");
                                if (state.equals("success")) {
                                    String data = object.getString("data");
                                    HelperApplication.getInstance().getTaskTypes().clear();
                                    Log.d("发布任务", data);
                                    Toast.makeText(getApplication(), "发布成功", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(mContext, UploadContractActivity.class);
                                    intent.putExtra("price", et_wages.getText().toString());
                                    intent.putExtra("tradeNo", data);
                                    startActivity(intent);
                                } else {
                                    HelperApplication.getInstance().getTaskTypes().clear();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        HelperApplication.getInstance().getTaskTypes().clear();
                        Toast.makeText(getApplication(), "网络错误，检查您的网络", Toast.LENGTH_SHORT).show();
                    }
                });
                String s = StringJoint.arrayJointchar(nameList, ",");
                request.putValue("picurl", s);
                request.putValue("userid", userInfo.getUserId());
                request.putValue("description", et_content.getText().toString());
                request.putValue("expirydate", begintime);
                request.putValue("price", et_wages.getText().toString());
                request.putValue("type", getSelectString());
                request.putValue("address", et_site_location.getText().toString());
                request.putValue("longitude", mSiteLon + "");
                request.putValue("latitude", mSiteLat + "");
                request.putValue("saddress", et_service_location.getText().toString());
                request.putValue("slongitude", mServiewLon + "");
                request.putValue("slatitude", mServiceLat + "");
                SingleVolleyRequest.getInstance(getApplication()).addToRequestQueue(request);


            }

            @Override
            public void error() {
                Toast.makeText(getApplication(), "上传失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 根据选择分类得到分类拼接字符串
     *
     * @return
     */
    private String getSelectString() {
        String str = "";
        for (int i = 0; i < selectList.size() - 1; i++) {
            str += selectList.get(i).getId() + ",";
        }
        str += selectList.get(selectList.size() - 1).getId();
        return str;
    }

    /**
     * 弹出计费方式选择框
     */
    private void showTextPicker() {
        View layout = LayoutInflater.from(this).inflate(R.layout.popwindow_select_unit, null);
        final PopupWindow popupWindow = new PopupWindow(layout, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.showAtLocation(bottom, Gravity.BOTTOM, 0, 0);

        // 设置背景颜色变暗
        final WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.7f;
        getWindow().setAttributes(lp);
        //监听popwindow消失事件，取消遮盖层
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                lp.alpha = 1.0f;
                getWindow().setAttributes(lp);
            }
        });
        final WheelView wv = (WheelView) layout.findViewById(R.id.wheelView);
        String[] PLANETS = new String[]{"按小时计费", "按天计费", "按月计费", "按趟计费", "按件计费", "按重量计费"};
        wv.setOffset(2);
        wv.setItems(Arrays.asList(PLANETS));
        wv.setSeletion(3);
        TextView tv_quxiao = (TextView) layout.findViewById(R.id.tv_quxiao);
        TextView tv_queding = (TextView) layout.findViewById(R.id.tv_queding);
        tv_queding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_time_unit.setText(wv.getSeletedItem());
                popupWindow.dismiss();
            }
        });
        tv_quxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }

    /**
     * 弹出时间选择对话框
     */
    private void showTimePicker() {
        Calendar cal = Calendar.getInstance();
        Date myData = new Date();
        cal.setTime(myData);

        //获取系统的时间
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        final int hour = cal.get(Calendar.HOUR_OF_DAY);
        final int minute = cal.get(Calendar.MINUTE);
        final int second = cal.get(Calendar.SECOND);
        DatePickerDialog dlg = new DatePickerDialog(HelpMeActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Log.e("MONTH", "monthOfYear" + monthOfYear);
                monthOfYear += 1;//monthOfYear 从0开始

                String data = year + "-" + monthOfYear + "-" + dayOfMonth;
                et_validity_period.setText(data);
                //时分秒用0代替
                String data_new = dataOne(data + "-" + hour + "-" + minute + "-" + second);
                Log.e("--444444---", data_new);
                begintime = data_new;

            }
        }, year, month, day);
        dlg.show();
    }

    /**
     * 时间转时间戳
     *
     * @param time
     * @return
     */
    public String dataOne(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                Locale.CHINA);
        Date date;
        String times = null;
        try {
            date = sdr.parse(time);
            long l = date.getTime();
            String stf = String.valueOf(l);
            times = stf.substring(0, 10);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return times;
    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
        Log.e("hello", result.getAddress());
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(HelpMeActivity.this, "抱歉，未能找到结果", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        String address = result.getAddressDetail().city + result.getAddressDetail().district + result.getPoiList().get(0).name;
        et_service_location.setText(address);
        et_site_location.setText(address);
        /*mBaiduMap.addOverlay(new MarkerOptions().position(result.getLocation())
                .icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.icon_marka)));
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result
                .getLocation()));*/

    }

    /**
     * 任务分类适配器
     */
    public class HelpMeSkillAdapter extends BaseAdapter {
        private Context mContext;
        private ArrayList<SkillTagInfo> mSkillTagInfos;

        public HelpMeSkillAdapter(Context context, ArrayList<SkillTagInfo> skillTagInfos) {
            this.mContext = context;
            if (skillTagInfos == null)
                skillTagInfos = new ArrayList<SkillTagInfo>();
            this.mSkillTagInfos = skillTagInfos;
        }

        @Override
        public int getCount() {
            return mSkillTagInfos.size();
        }

        @Override
        public SkillTagInfo getItem(int position) {
            return mSkillTagInfos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_help_me_skill_tag, null);
                viewHolder.tv_tag = (TextView) convertView.findViewById(R.id.tv_item_help_me_skill_tag);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.tv_tag.setText(mSkillTagInfos.get(position).getSkill_name());
            if (mSkillTagInfos.get(position).isChecked()) {
                viewHolder.tv_tag.setTextColor(mContext.getResources().getColor(R.color.colorTextWhite));
                viewHolder.tv_tag.setBackgroundResource(R.drawable.tv_tag_select);
            } else {
                viewHolder.tv_tag.setTextColor(mContext.getResources().getColor(R.color.colorTheme));
                viewHolder.tv_tag.setBackgroundResource(R.drawable.tv_tag);
            }
            return convertView;
        }

        private class ViewHolder {
            TextView tv_tag;
        }
    }

    /**
     * 授权权限
     *
     * @param permsRequestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults) {

        switch (permsRequestCode) {

            case 200:

                boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if (cameraAccepted) {
                    //授权成功之后，调用系统相机进行拍照操作等
                    galleryFinalUtil.openCamera(HelpMeActivity.this, mPhotoList, REQUEST_CODE_CAMERA, mOnHanlderResultCallback);
                } else {
                    //用户授权拒绝之后，友情提示一下就可以了
                    ToastUtil.showShort(this, "已拒绝进入相机，如想开启请到设置中开启！");
                }

                break;

        }

    }

}
