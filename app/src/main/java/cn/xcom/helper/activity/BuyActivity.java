package cn.xcom.helper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.xcom.helper.R;
import cn.xcom.helper.bean.Front;
import cn.xcom.helper.bean.ShopGoodInfo;

public class BuyActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout back;
    private TextView buy_phone;
    private TextView buy_price;
    private Button btnDecrease;
    private EditText etAmount;
    private Button btnIncrease;
    private TextView buy_total;
    private TextView buy_deliver;
    private EditText buy_message;
    private TextView adress;
    private LinearLayout buy_address;
    private RelativeLayout buy_commit;
    private ShopGoodInfo shopGoodInfo;
    int amount=1;//初始化购买数量
    private TextWatcher textWatcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (etAmount.getText().toString().equals("")||etAmount.getText().equals("0")){
                etAmount.setText("1");
            }

        }

        @Override
        public void afterTextChanged(Editable s) {
            buy_total.setText((Double.parseDouble(etAmount.getText().toString()) *
                    Double.parseDouble(buy_price.getText().toString().substring(1))) + "");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);
        initView();
        Intent intent=getIntent();
        shopGoodInfo= (ShopGoodInfo) intent.getSerializableExtra("price");
        buy_price.setText("￥"+shopGoodInfo.getPrice());
        buy_total.setText(shopGoodInfo.getPrice());
        buy_deliver.setText(shopGoodInfo.getDelivery());
    }

    private void initView() {
        back = (RelativeLayout) findViewById(R.id.back);
        back.setOnClickListener(this);
        buy_phone = (TextView) findViewById(R.id.buy_phone);
        buy_price = (TextView) findViewById(R.id.buy_price);
        btnDecrease = (Button) findViewById(R.id.btnDecrease);
        etAmount = (EditText) findViewById(R.id.etAmount);
        btnIncrease = (Button) findViewById(R.id.btnIncrease);
        buy_total = (TextView) findViewById(R.id.buy_total);
        buy_deliver = (TextView) findViewById(R.id.buy_deliver);
        buy_message = (EditText) findViewById(R.id.buy_message);
        adress = (TextView) findViewById(R.id.adress);
        buy_address = (LinearLayout) findViewById(R.id.buy_address);
        buy_address.setOnClickListener(this);
        buy_commit = (RelativeLayout) findViewById(R.id.buy_commit);
        buy_commit.setOnClickListener(this);
        btnDecrease.setOnClickListener(this);
        btnIncrease.setOnClickListener(this);
        etAmount.addTextChangedListener(textWatcher);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnDecrease:
                if(amount>1){
                    amount= Integer.parseInt(String.valueOf(etAmount.getText()));
                    amount--;
                    etAmount.setText(amount + "");
             buy_total.setText( (Double.parseDouble(etAmount.getText().toString())*
                Double.parseDouble(buy_price.getText().toString().substring(1)))+"");
                }
                break;
            case R.id.btnIncrease:
                amount= Integer.parseInt(String.valueOf(etAmount.getText()));
                amount++;
                etAmount.setText(amount+"");
                break;
            case R.id.buy_commit:
                Intent intent=new Intent(BuyActivity.this,PaymentActivity.class);
                intent.putExtra("price", buy_total.getText().toString());
                startActivity(intent);
                break;
            case R.id.back:
                finish();
                break;
            case R.id.buy_address:
                Intent intent1=new Intent(BuyActivity.this,AddressListActivity.class);
                startActivity(intent1);
        }
    }

    private void submit() {
        // validate
        String etAmountString = etAmount.getText().toString().trim();
        if (TextUtils.isEmpty(etAmountString)) {
            Toast.makeText(this, "etAmountString不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        String message = buy_message.getText().toString().trim();
        if (TextUtils.isEmpty(message)) {
            Toast.makeText(this, "您可以在此留言", Toast.LENGTH_SHORT).show();
            return;
        }




    }
}
