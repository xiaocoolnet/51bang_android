package cn.xcom.helper.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cn.xcom.helper.R;
import cn.xcom.helper.bean.UserInfo;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.net.HelperAsyncHttpClient;
import cz.msebera.android.httpclient.Header;


/**
 * Created by Administrator on 2016/10/11 0011.
 * 选择提现账户
 */
public class ChooseAccountActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {
    private TextView zfbAccountTv, bankAccountTv;
    private CheckBox zfbCheckBox, bankCheckBox;
    private UserInfo userInfo;
    private Button confirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_account);
        userInfo = new UserInfo(this);
        zfbAccountTv = (TextView) findViewById(R.id.tv_zhifubao_account);
        bankAccountTv = (TextView) findViewById(R.id.tv_bank_account);
        zfbCheckBox = (CheckBox) findViewById(R.id.cb_zhifubao);
        bankCheckBox = (CheckBox) findViewById(R.id.cb_bank);
        zfbCheckBox.setOnCheckedChangeListener(this);
        bankCheckBox.setOnCheckedChangeListener(this);
        getBankInfo();
        confirmButton = (Button) findViewById(R.id.bt_confirm);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(zfbCheckBox.isChecked()&&!bankCheckBox.isChecked()){

                }

            }
        });
    }

    private void getBankInfo() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("userid", userInfo.getUserId());
        HelperAsyncHttpClient.get(NetConstant.GET_USER_BANK_INFO, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null) {
                    try {
                        String state = response.getString("status");
                        if (state.equals("success")) {
                            JSONObject jsonObject = response.getJSONObject("data");
                            String zfbAccount = jsonObject.getString("alipay");
                            if (!"".equals(zfbAccount)) {
                                zfbAccountTv.setText(zfbAccount);
                            }
                            String bankAccount = jsonObject.getString("bank");
                            if (!"".equals(bankAccount)) {
                                bankAccountTv.setText(bankAccount);
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }
        });
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (!isChecked) {
            return;
        }
        switch (buttonView.getId()) {
            case R.id.cb_zhifubao:
                bankCheckBox.setChecked(false);
                break;
            case R.id.cb_bank:
                zfbCheckBox.setChecked(false);
                break;
        }
    }
}
