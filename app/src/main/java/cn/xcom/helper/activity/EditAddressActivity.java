package cn.xcom.helper.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import cn.xcom.helper.R;
import cn.xcom.helper.bean.City1;


/**
 * Created by zhuchongkun on 16/6/17.
 */
public class EditAddressActivity extends BaseActivity implements View.OnClickListener {
    private String TAG="EditAddressActivity";
    private Context mContext;
    private RelativeLayout rl_back,rl_save;
    private EditText et_consignee,et_phone,et_detail;
    private LinearLayout ll_city,ll_street;
    private TextView tv_city,tv_street;
    private String provinces;
    private City1 city ;
    private ArrayList<City1> toCitys;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_edit_address);
        mContext=this;

        initView();


    }

    private void  initView(){
        city = new City1();
        toCitys = new ArrayList<City1>();
        rl_back= (RelativeLayout) findViewById(R.id.rl_edit_address_back);
        rl_back.setOnClickListener(this);
        rl_save= (RelativeLayout) findViewById(R.id.rl_edit_address_save);
        rl_save.setOnClickListener(this);
        et_consignee= (EditText) findViewById(R.id.et_edit_address_consignee);
        et_phone= (EditText) findViewById(R.id.et_edit_address_phone);
        et_detail= (EditText) findViewById(R.id.et_edit_address_detail);
        tv_city= (TextView) findViewById(R.id.tv_edit_address_city);
        tv_street= (TextView) findViewById(R.id.tv_edit_address_street);
        ll_city= (LinearLayout) findViewById(R.id.ll_edit_address_city);
        ll_city.setOnClickListener(this);
        ll_street= (LinearLayout) findViewById(R.id.ll_edit_address_street);
        ll_street.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_edit_address_back:
                finish();
                break;
            case R.id.rl_edit_address_save:
                Toast.makeText(mContext,"未开发",Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_edit_address_city:
                Intent in=new Intent(EditAddressActivity.this,CitySelect1Activity.class);
                in.putExtra("city", city);
                startActivityForResult(in, 1);
                break;
            case R.id.ll_edit_address_street:
                Toast.makeText(mContext,"未开发",Toast.LENGTH_SHORT).show();
                break;
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 8){
            if(requestCode == 1){
                city = data.getParcelableExtra("city");
                tv_city.setText(city.getProvince()+city.getCity()+city.getDistrict());

            }else if(requestCode == 2){
                toCitys = data.getParcelableArrayListExtra("toCitys");
                StringBuffer ab = new StringBuffer();
                for (int i = 0; i < toCitys.size(); i++) {
                    if(i==toCitys.size()-1){//如果是最后一个城市就不需要逗号
                        ab.append(toCitys.get(i).getCity());
                    }else{
                        StringBuffer a = ab.append(toCitys.get(i).getCity()+"， ");//如果不是最后一个城市就需要逗号
                        tv_city.setText(a);
                    }
                }
            }
        }
    }
}
