package com.example.administrator.take_two.moneylist.widget;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.take_two.R;

/**
 * Created by Administrator on 2016/9/20.
 */
public class OperateAlertDialog {
    private static Context mContext;
    private static Dialog mDialog;
    private static TextView txtvTitle;
    private static RelativeLayout confirmRlay;
    private static RelativeLayout cancelRlay;
    private static OnButtonClickLinstener mOnButtonClickLinstener;
    public static void show(Context context, String title,
                            OnButtonClickLinstener onButtonClickLinstener){
        mContext = context;
        mOnButtonClickLinstener = onButtonClickLinstener;
        initViews(true, true,title);
        initListener();
    }

    private static void initViews(boolean cancelable, boolean otoCancelable, String title) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View contentView = inflater.inflate(R.layout.dialog_operate_alert, null);
        txtvTitle = (TextView) contentView.findViewById(R.id.txtv_title);
        txtvTitle.setText(title);
        confirmRlay = (RelativeLayout) contentView.findViewById(R.id.lay_confirm);
        cancelRlay = (RelativeLayout) contentView.findViewById(R.id.lay_cancel);
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setView(contentView);
        mDialog = builder.create();
        mDialog.setCanceledOnTouchOutside(otoCancelable);
        mDialog.setCancelable(cancelable);
        mDialog.show();
    }

    public static void show(Context context, boolean cancelable, boolean otoCancelable,String title,
                            OnButtonClickLinstener onButtonClickLinstener){
        mContext = context;
        mOnButtonClickLinstener = onButtonClickLinstener;
        initViews(cancelable, otoCancelable,title);
        initListener();
    }
    private static void initListener(){
        confirmRlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                if(mOnButtonClickLinstener != null)
                    mOnButtonClickLinstener.confirm();
            }
        });
        cancelRlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
    }

    public interface OnButtonClickLinstener{
        void confirm();
    }
}
