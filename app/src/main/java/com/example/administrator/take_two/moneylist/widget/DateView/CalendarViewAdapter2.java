package com.example.administrator.take_two.moneylist.widget.DateView;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2016/9/18.
 */
public class CalendarViewAdapter2 extends PagerAdapter{
    public static final String TAG = "CalendarViewAdapter";
    private CalendarGridView[] views;

    public CalendarViewAdapter2(CalendarGridView[] views) {
        super();
        this.views = views;
    }
    public Object instantiateItem(ViewGroup container, int position) {

        if (((ViewPager) container).getChildCount() == views.length) {
            ((ViewPager) container).removeView(views[position % views.length]);
        }

        ((ViewPager) container).addView(views[position % views.length], 0);
        return views[position % views.length];
    }
    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((View) object);
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) container);
    }

    public void setDate(CalendarGridView[] views) {
        this.views = views;
        notifyDataSetChanged();
    }

    public CalendarGridView[] getAllItems() {
        return views;
    }
}
