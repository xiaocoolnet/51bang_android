package cn.xcom.helper.activity;

import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.xcom.helper.R;

public class ConvenienceActivity extends BaseActivity implements View.OnClickListener{

    private RelativeLayout back;
    private TextView cnnvenience_release;
    private ListView listView_Convenience;
    private Button convenience_deliver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convenience);
        initView();
    }

    private void initView() {
        back = (RelativeLayout) findViewById(R.id.back);
        cnnvenience_release = (TextView) findViewById(R.id.cnnvenience_release);
        listView_Convenience = (ListView) findViewById(R.id.listView_Convenience);
        convenience_deliver= (Button) findViewById(R.id.convenience_deliver);

    }

    @Override
    public void onClick(View v) {

    }
}
