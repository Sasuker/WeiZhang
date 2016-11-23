package com.example.administrator.take_two;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.multidex.MultiDexApplication;

import com.example.administrator.take_two.Util.CacheUtil;

/**
 * Created by Administrator on 2016/9/8.
 */
public class MyApplication extends MultiDexApplication{
    private static MyApplication myApplication;
    public String useId = "";

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
        MyCrashHandler handler = MyCrashHandler.getInstance();
        handler.init(this);
        useId = CacheUtil.getCacheStrValue(this, CacheUtil.PRE_NAME_USER, CacheUtil.KEY_USER_ID);
    }
    public static MyApplication getInstance() {
        return myApplication;
    }

    public boolean isFirstStart = false;

    public void checkFirstStart() {
        SharedPreferences sharedPreferences = getSharedPreferences("startStatus", Context.MODE_PRIVATE); //私有数据
        //如果没有取到数据，就就代表是第一次启动
        boolean flag = sharedPreferences.getBoolean("firstStart", true);
        if (flag) {
            isFirstStart = true;
            sharedPreferences.edit().putBoolean("firstStart", false).commit();
        }
    }
}
