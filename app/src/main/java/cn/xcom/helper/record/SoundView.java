package cn.xcom.helper.record;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import cn.xcom.helper.R;

/**
 * Created by hzh on 17/1/17.
 * 需要加入麦克风权限 否则录不到声音 且没有数据
 */

public class SoundView extends RelativeLayout {
    public static final int COUNT_CODE =111;
    private boolean isShowDelete;
    private View view;
    private TextView countNumTv;
    private ImageView deleteBtn, animationImg;
    private String filePath;
    private AnimationDrawable animationDrawable;
    private boolean isPlaying;
    private MediaPlayer mediaPlayer;
    private int countNum;
    private Timer timer;
    private TimerTask timerTask;
    private Handler handler = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case COUNT_CODE:
                    countNumTv.setText(countNum+"");
                    break;
            }
        }
    };

    public SoundView(Context context) {
        super(context);
        initView(context);
    }

    public SoundView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SoundView);
        isShowDelete = a.getBoolean(R.styleable.SoundView_show_delete, false);
        initView(context);
    }

    private void initView(Context context) {
        view = LayoutInflater.from(context).inflate(R.layout.view_sound, this, true);
        deleteBtn = (ImageView) view.findViewById(R.id.delete_img);
        animationImg = (ImageView) view.findViewById(R.id.animation_img);
        countNumTv = (TextView) view.findViewById(R.id.num_text);
        if (isShowDelete) {
            deleteBtn.setVisibility(View.VISIBLE);
        } else {
            deleteBtn.setVisibility(View.GONE);
        }
        animationImg.setImageResource(R.drawable.animation_sound);
        animationDrawable = (AnimationDrawable) animationImg.getDrawable();
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isPlaying) {
                    isPlaying = true;
                    startPlay();
                } else {
                    isPlaying = false;
                    stopPlay();
                }
            }
        });

    }

    public void init(String filePath) {
        this.filePath = filePath;
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(filePath);
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    animationDrawable.stop();
//                    mediaPlayer.release();
                }
            });
            mediaPlayer.prepare();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    countNum = mediaPlayer.getDuration() / 100;
                    handler.sendEmptyMessage(COUNT_CODE);
                }
            });
            timer = new Timer();
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    if (countNum > 0) {
                        countNum--;
                        countNumTv.setText(countNum);
                    }
                }
            };
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startPlay() {
        animationDrawable.start();
        try {
            mediaPlayer.prepare();
            mediaPlayer.start();
            timer.schedule(timerTask, 0, 1000);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopPlay() {
        animationDrawable.stop();
        mediaPlayer.stop();
        if(timer != null){
            timer.cancel();
        }
        if(timerTask!=null){
            timerTask.cancel();
        }
    }
}
