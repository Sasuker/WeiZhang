package com.example.administrator.take_two.moneylist.widget;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.take_two.R;

/**
 * Created by Administrator on 2016/9/21.
 */
public class Alertdialog {
    private static Dialog sDialog;
    private static void dismiss(){
        if (sDialog != null && sDialog.isShowing()) {
            sDialog.dismiss();
        }
    }
    private static void show(Context context , CharSequence message){
        show(context, message, true, true);
    }
    public static boolean isShow() {
        boolean isShow = false;
        if (sDialog != null)
            isShow = sDialog.isShowing();
        return isShow;
    }

    public static void show(Context context, CharSequence title, boolean cancelable, boolean otoCancelable) {
        dismiss();
        if (context == null)
            return;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View contentView = inflater.inflate(R.layout.dialog_text_alert, null);
        TextView txtvTitle = (TextView) contentView.findViewById(R.id.txt_title);
        txtvTitle.setText(title);
        builder.setView(contentView);
        sDialog = builder.create();
        sDialog.setCancelable(cancelable);
        sDialog.setCanceledOnTouchOutside(otoCancelable);
        sDialog.show();
    }
}
