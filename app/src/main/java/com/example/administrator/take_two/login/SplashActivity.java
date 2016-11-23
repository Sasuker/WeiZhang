package com.example.administrator.take_two.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.example.administrator.take_two.BaseActivity;
import com.example.administrator.take_two.MainActivity;
import com.example.administrator.take_two.MyApplication;
import com.example.administrator.take_two.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2016/9/26.
 */
public class SplashActivity extends BaseActivity{
    int finish = 1;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == finish){
                SplashActivity.this.finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        //启动定时器，三秒后进入
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                timer.cancel();
                if (!TextUtils.isEmpty(MyApplication.getInstance().useId)) {
                    //说明已经有账号在啦，就直接跳入下一个界面
                    SplashActivity.this.startActivity(new Intent(SplashActivity.this, MainActivity.class));
                } else {
                    SplashActivity.this.startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                }
                handler.sendEmptyMessageDelayed(1, 300);
            }
        }, 1000);
    }

    @Override
    public void onBackPressed() {
    }
}
