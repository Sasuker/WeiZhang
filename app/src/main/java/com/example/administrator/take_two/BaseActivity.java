package com.example.administrator.take_two;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.example.administrator.take_two.Other.SystemBarTintManager;
import com.example.administrator.take_two.Util.ToastUtil;
import com.example.administrator.take_two.moneylist.widget.LoadingDialog;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2016/9/12.
 */
public class BaseActivity extends FragmentActivity{
    protected Boolean isLoading;
    protected LoadingDialog mLoadingDialog;
    private SystemBarTintManager tintManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            setTranslucentStatus(true);

            tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setNavigationBarTintEnabled(true);
            //此处可以重新指定状态栏颜色
            tintManager.setTintColor(0xff20b2aa);
        }
    }
    @TargetApi(19)
    private void setTranslucentStatus(boolean b) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (b) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
    @Override
    public void finish() {
        hideSoftInput();
        super.finish();
    }

    private void hideSoftInput() {
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    protected void  onDestroy() {
        super.onDestroy();
        if(isRegisterEventBus)
            EventBus.getDefault().unregister(this);
    }
    private boolean isRegisterEventBus = false;//判断是否进行EventBus的注册，用于取消注册时的判断
    /**
     * 注册EventBus ，如果调用此方法，必须要声明接收函数，否则报错
     * onEvent(AnyTypeEvent event)/onEventMainThread(AnyTypeEvent event)..
     * 具体参见EventBus使用介绍http://blog.csdn.net/harvic880925/article/details/40787203
     */
    protected void registerEventBus(){
        EventBus.getDefault().register(this);
        isRegisterEventBus = true;
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    private void init() {
        ToastUtil.setContext(this);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
    }
    public void showLoadingDialog(String message){
        this.showLoadingDialog(message,true,true);
    }
    public void showLoadingDialog(String message, boolean cancelable, boolean otoCancelable) {
        mLoadingDialog = new LoadingDialog(this, message, cancelable, otoCancelable);
        mLoadingDialog.show();
    }
    public void dismissLoadingDialog() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
        isLoading = false;
    }


}
