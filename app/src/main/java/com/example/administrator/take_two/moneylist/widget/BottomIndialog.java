package com.example.administrator.take_two.moneylist.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.administrator.take_two.R;

/**
 * Created by Administrator on 2016/9/21.
 */
public class BottomIndialog {
    private Dialog mDialog;
    public BottomIndialog(Context context, View contentView, boolean cancelable, boolean otoCancelable) {
        if (context == null)
            return;
        mDialog = new Dialog(context, R.style.custom_dialog_type);
        mDialog.setContentView(contentView);
        mDialog.setCancelable(cancelable);
        mDialog.setCanceledOnTouchOutside(otoCancelable);
        Window window = mDialog.getWindow();
        WindowManager m = window.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = window.getAttributes(); // 获取对话框当前的参数值
        p.width = (int) (d.getWidth());
//        p.height = (int)(d.getHeight()*0.5);
        window.setAttributes(p);
        window.setGravity(Gravity.BOTTOM);  //此处可以设置dialog显示的位置
        window.setWindowAnimations(R.style.dialog_in_out_anim_style);  //添加动画
    }

    public void dismiss() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    public void show(){
        if (mDialog != null && !mDialog.isShowing()){
            mDialog.show();
        }
    }
}
