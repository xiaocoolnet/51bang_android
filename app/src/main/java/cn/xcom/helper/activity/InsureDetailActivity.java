package cn.xcom.helper.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cn.xcom.helper.R;


/**
 * Created by hzh on 2016/10/24.
 */

public class InsureDetailActivity extends BaseActivity implements View.OnClickListener {
    private ImageView imageView;
    private TextView timeTv;
    private String url;
    String data_new;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insure_detail);
        url = getIntent().getStringExtra("url");
        imageView = (ImageView) findViewById(R.id.iv_insure);
        timeTv = (TextView) findViewById(R.id.tv_time);
        timeTv.setOnClickListener(this);
        Bitmap bm = BitmapFactory.decodeFile(url);
        imageView.setImageBitmap(bm);
        findViewById(R.id.confirm).setOnClickListener(this);
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
        DatePickerDialog dlg = new DatePickerDialog(InsureDetailActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                monthOfYear += 1;//monthOfYear 从0开始

                String data = year + "-" + monthOfYear + "-" + dayOfMonth;
                timeTv.setText(data);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date time = null;
                try {
                    time = simpleDateFormat.parse(data);
                    long ts = time.getTime();
                    String s = String.valueOf(ts);
                    data_new =s.substring(0, 10);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        }, year, month, day);
        dlg.show();
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_time:
                showTimePicker();
                break;
            case R.id.confirm:
                if(timeTv.getText().equals("")){
                    Toast.makeText(this, "请选择有限期", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent();
                intent.putExtra("time",data_new);
                setResult(RESULT_OK,intent);
                finish();
                break;
        }
    }

    public void onBack(View v){
        finish();
    }
}
