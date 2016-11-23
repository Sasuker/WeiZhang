package com.example.administrator.take_two.moneylist.widget.DateView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2016/9/21.
 */
public class CalendarLinearLayout extends LinearLayout{
    public CalendarLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CalendarLinearLayout(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        ViewGroup.LayoutParams lps = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setLayoutParams(lps);
        setOrientation(VERTICAL);

        fillDate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void fillDate() {
    }
}
