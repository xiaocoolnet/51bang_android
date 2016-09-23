package cn.xcom.helper.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
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
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

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

import cn.xcom.helper.HelperApplication;
import cn.xcom.helper.R;
import cn.xcom.helper.bean.SkillTagInfo;
import cn.xcom.helper.bean.TaskType;
import cn.xcom.helper.bean.UserInfo;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.net.HelperAsyncHttpClient;
import cn.xcom.helper.utils.LogUtils;
import cn.xcom.helper.utils.SingleVolleyRequest;
import cn.xcom.helper.utils.StringPostRequest;
import cn.xcom.helper.utils.ToastUtil;
import cn.xcom.helper.view.NoScrollGridView;
import cn.xcom.helper.view.WheelView;
import cz.msebera.android.httpclient.Header;

/**
 * Created by zhuchongkun on 16/6/4.
 * 帮我页
 */
public class HelpMeActivity extends BaseActivity implements View.OnClickListener {
    private String TAG="HelpMeActivity";
    private Context mContext;
    private RelativeLayout rl_back;
    private NoScrollGridView gv_skill;
    private ArrayList<SkillTagInfo> skillTagInfos;
    private HelpMeSkillAdapter mHelpMeSkillAdapter;
    private EditText et_content,et_phone,et_site_location,et_service_location,et_wages,et_validity_period;
    private TextView tv_time_unit,tv_service_charge;
    private ImageView iv_site_location,iv_service_location,iv_validity_period;
    private Button bt_submit;
    private SkillTagInfo selectTag;
    private UserInfo userInfo;
    private List<TaskType> selectList;
    private LinearLayout ll_time;
    private ScrollView bottom;
    private String begintime = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_help_me);
        mContext=this;
        userInfo = new UserInfo(mContext);
        userInfo.readData(mContext);
        initView();
    }

    private void initView(){
        bottom = (ScrollView) findViewById(R.id.bottom);
        ll_time = (LinearLayout) findViewById(R.id.ll_time);
        ll_time.setOnClickListener(this);
        rl_back= (RelativeLayout) findViewById(R.id.rl_help_me_back);
        rl_back.setOnClickListener(this);
        et_content= (EditText) findViewById(R.id.et_help_me_content);
        et_phone= (EditText) findViewById(R.id.et_help_me_phone);
        et_site_location= (EditText) findViewById(R.id.et_help_me_site_location);
        et_service_location= (EditText) findViewById(R.id.et_help_me_service_location);
        et_wages= (EditText) findViewById(R.id.et_help_me_wages);
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
        et_validity_period= (EditText) findViewById(R.id.et_help_me_validity_period);
        tv_time_unit= (TextView) findViewById(R.id.tv_help_me_time_unit);
        tv_time_unit.setOnClickListener(this);
        tv_service_charge= (TextView) findViewById(R.id.tv_help_me_service_charge);
        iv_site_location= (ImageView) findViewById(R.id.iv_help_me_site_location);
        iv_site_location.setOnClickListener(this);
        iv_service_location= (ImageView) findViewById(R.id.iv_help_me_service_location);
        iv_service_location.setOnClickListener(this);
        iv_validity_period= (ImageView) findViewById(R.id.iv_help_me_validity_period);
        iv_validity_period.setOnClickListener(this);
        bt_submit= (Button) findViewById(R.id.bt_help_me_submit);
        bt_submit.setOnClickListener(this);
        selectTag=new SkillTagInfo();
        skillTagInfos=new ArrayList<SkillTagInfo>();
        gv_skill= (NoScrollGridView) findViewById(R.id.gridView_help_me);
        gv_skill.setSelector(new ColorDrawable(Color.TRANSPARENT));
        mHelpMeSkillAdapter=new HelpMeSkillAdapter(mContext,skillTagInfos);
        gv_skill.setAdapter(mHelpMeSkillAdapter);
        gv_skill.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(HelpMeActivity.this,SelectTaskTypeActivity.class);
                intent.putExtra("id",skillTagInfos.get(position).getSkill_id());
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
        for(int i=0;i<skillTagInfos.size();i++){
            skillTagInfos.get(i).setChecked(check(skillTagInfos.get(i).getSkill_id()));
        }
        mHelpMeSkillAdapter.notifyDataSetChanged();
    }

    /**
     * 判断大分类是否选中
     * @param id
     * @return
     */
    private boolean check(String id){
        for(int i=0;i<selectList.size();i++){
            if(selectList.get(i).getParent().equals(id)){
                return true;
            }
        }

        return false;
    }

    private void getSkill(){
        RequestParams params=new RequestParams();
        params.put("id",0);
        HelperAsyncHttpClient.get(NetConstant.NET_GET_TASKLIST,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                LogUtils.e(TAG,"--statusCode->"+statusCode+"==>"+response.toString());
                if (response!=null){
                    try {
                        String state=response.getString("status");
                        if (state.equals("success")){
                            JSONArray data=response.getJSONArray("data");
                            skillTagInfos.clear();
                            for (int i=0;i<data.length();i++){
                                SkillTagInfo info=new SkillTagInfo();
                                JSONObject jsonObject=data.getJSONObject(i);
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
                LogUtils.e(TAG,"--statusCode->"+statusCode+"==>"+responseString);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_help_me_back:
                finish();
                break;
            case R.id.iv_help_me_site_location:
                break;
            case R.id.iv_help_me_service_location:
                break;
            case R.id.tv_help_me_time_unit:
                showTextPicker();
                break;
            //选择有效时间
            case R.id.iv_help_me_validity_period:
                showTimePicker();
                break;
            case R.id.bt_help_me_submit:
                submit();
                break;
        }

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
        if(selectList.size()==0){
            ToastUtil.showShort(mContext,"请选择分类");
            return;
        }
        if(et_content.getText().toString().length()==0){
            ToastUtil.showShort(mContext,"请输入任务描述");
            return;
        }
        if(et_phone.getText().toString().length()==0){
            ToastUtil.showShort(mContext,"请输入联系方式");
            return;
        }
        if(et_wages.getText().toString().length()==0){
            ToastUtil.showShort(mContext,"请输入工资");
            return;
        }
        if(begintime.length()==0){
            ToastUtil.showShort(mContext,"请选择有效时间");
            return;
        }
        String url=NetConstant.PUBLISHTASK;
        StringPostRequest request=new StringPostRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (s!=null){
                    try {
                        JSONObject object = new JSONObject(s);
                        String state=object.getString("status");
                        if (state.equals("success")){
                            String data=object.getString("data");
                            HelperApplication.getInstance().getTaskTypes().clear();
                            Log.d("发布任务", data);
                            Toast.makeText(getApplication(), "发布成功", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(mContext, UploadContractActivity.class);
                            intent.putExtra("price",et_wages.getText().toString());
                            intent.putExtra("tradeNo",data);
                            startActivity(intent);
                        }else{
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
                Toast.makeText(getApplication(),"网络错误，检查您的网络",Toast.LENGTH_SHORT).show();
            }
        });
        request.putValue("userid",userInfo.getUserId());
        request.putValue("description",et_content.getText().toString());
        request.putValue("expirydate",begintime);
        request.putValue("price",et_wages.getText().toString());
        request.putValue("type",getSelectString());
        SingleVolleyRequest.getInstance(getApplication()).addToRequestQueue(request);
    }

    /**
     * 根据选择分类得到分类拼接字符串
     * @return
     */
    private String getSelectString() {
        String str = "";
        for(int i=0;i<selectList.size()-1;i++){
            str += selectList.get(i).getId() + "," ;
        }
        str += selectList.get(selectList.size()-1).getId();
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
        String[] PLANETS = new String[]{"按小时计费","按天计费","按月计费","按趟计费","按件计费","按重量计费"};
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
     * @param time
     * @return
     */
    public String dataOne(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss",
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

    /**
     * 任务分类适配器
     */
    public class HelpMeSkillAdapter extends BaseAdapter {
        private Context mContext;
        private ArrayList<SkillTagInfo> mSkillTagInfos;
        public HelpMeSkillAdapter(Context context, ArrayList<SkillTagInfo> skillTagInfos) {
            this.mContext=context;
            if (skillTagInfos==null)
                skillTagInfos=new ArrayList<SkillTagInfo>();
            this.mSkillTagInfos=skillTagInfos;
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
            if (convertView==null){
                viewHolder=new ViewHolder();
                convertView= LayoutInflater.from(mContext).inflate(R.layout.item_help_me_skill_tag,null);
                viewHolder.tv_tag= (TextView) convertView.findViewById(R.id.tv_item_help_me_skill_tag);
                convertView.setTag(viewHolder);
            }else{
                viewHolder= (ViewHolder) convertView.getTag();
            }
            viewHolder.tv_tag.setText(mSkillTagInfos.get(position).getSkill_name());
            if (mSkillTagInfos.get(position).isChecked()){
                viewHolder.tv_tag.setTextColor(mContext.getResources().getColor(R.color.colorTextWhite));
                viewHolder.tv_tag.setBackgroundResource(R.drawable.tv_tag_select);
            }else{
                viewHolder.tv_tag.setTextColor(mContext.getResources().getColor(R.color.colorTheme));
                viewHolder.tv_tag.setBackgroundResource(R.drawable.tv_tag);
            }
            return convertView;
        }
        private  class ViewHolder{
            TextView tv_tag;
        }
    }

}
