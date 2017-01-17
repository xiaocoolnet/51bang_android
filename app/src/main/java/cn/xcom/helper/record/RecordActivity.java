package cn.xcom.helper.record;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import cn.xcom.helper.R;
import cn.xcom.helper.activity.BaseActivity;

/**
 * Created by hzh on 17/1/17.
 */

public class RecordActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_sound);
        RecordButton recordButton = (RecordButton) findViewById(R.id.record_button);
        recordButton.setAudioRecord(new AudioRecorder());
        recordButton.setRecordListener(new RecordButton.RecordListener() {
            @Override
            public void recordEnd(String filePath) {
                Log.d("record",filePath);
                Intent intent = new Intent();
                intent.putExtra("path",filePath);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }

}
