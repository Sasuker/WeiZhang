package com.example.administrator.take_two;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;

/**
 * Created by Administrator on 2016/9/8.
 */
public class MyCrashHandler implements Thread.UncaughtExceptionHandler {
    private static MyCrashHandler myCrashHandler;
    private Context mContext;
    public static boolean IM_TESTOR = false;
    private MyCrashHandler(){

    }
    public static synchronized MyCrashHandler getInstance(){
        if(myCrashHandler == null){
            myCrashHandler = new MyCrashHandler();
        }
        return myCrashHandler;
    }
    public void init(Context context){
        this.mContext = context;
        //检测是否为测试人员
        checkIsTestor();
        //获取系统默认的UncaughtException处理器
        //mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        //设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    private void checkIsTestor() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("IM_TESTOR", Context.MODE_PRIVATE); //私有数据
        //如果没有取到数据，就就代表是第一次启动
        boolean imTestor = sharedPreferences.getBoolean("IM_TESTOR", false);
        if(imTestor){
            IM_TESTOR = true;
        }
    }

    @Override
    public void uncaughtException(Thread thread, Throwable arg1) {
        // 1.获取当前程序的版本号. 版本的id
        String versioninfo = getVersionInfo();
        // 2.获取手机的硬件信息.
        String mobileInfo = getMobileInfo();
        // 3.把错误的堆栈信息 获取出来
        String errorinfo = getErrorInfo(arg1);
        Log.e("error",errorinfo);
        if(!BuildConfig.DEBUG) {
            // 干掉当前的程序
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }

    private String getMobileInfo() {
        StringBuffer sb = new StringBuffer();
        try{
            Field[] fields = Build.class.getDeclaredFields();
            for(Field field : fields){
                field.setAccessible(true);
                String name  = field.getName();
                String value = field.get(null).toString();
                sb.append(name+ "=" + value);
                sb.append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    private String getVersionInfo() {
        try {
            PackageManager pm = this.mContext.getPackageManager();
            PackageInfo info = pm.getPackageInfo(this.mContext.getPackageName(), 0);
            return info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return "版本号未知";
        }
    }
    private String getErrorInfo(Throwable arg1){
        Writer writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);
        arg1.printStackTrace(pw);
        pw.close();
        String error = writer.toString();
        return error;
    }
}
