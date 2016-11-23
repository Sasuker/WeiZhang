package com.example.administrator.take_two.moneylist.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.*;
import android.widget.TextView;

import com.example.administrator.take_two.R;

/**
 * Created by Administrator on 2016/9/12.
 */
public class LoadingDialog {
    private Dialog mDialog;
    public LoadingDialog(Context context, String message, boolean cancelable, boolean otoCancelable){
        View view = View.inflate(context, R.layout.dialog_loading,null);
        TextView title = (TextView) view.findViewById(R.id.dialog_loading_msg);
        title.setText(message);
        mDialog = new Dialog(context,R.style.loading_dialog);
        mDialog.setContentView(view);
        mDialog.setCancelable(cancelable);
        mDialog.setCanceledOnTouchOutside(otoCancelable);
    }
    public void show() {
        if (mDialog != null && !mDialog.isShowing()) {
            mDialog.show();
        }
    }

    public void dismiss() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }
}
