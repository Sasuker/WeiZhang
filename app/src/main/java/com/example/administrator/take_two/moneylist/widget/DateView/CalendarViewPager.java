package com.example.administrator.take_two.moneylist.widget.DateView;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2016/9/18.
 */
public class CalendarViewPager extends ViewPager{
    public CustomDate currShowDate = new CustomDate();
    public CalendarViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CalendarViewPager(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        ViewGroup.LayoutParams lps = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setLayoutParams(lps);

        mShowViews = new CalendarGridView[3];
        for (int i = 0; i < 3; i++) {
            mShowViews[i] = new CalendarGridView(context, new CalendarGridView.OnCellListener() {

                @Override
                public void toPreMonth(CustomDate date) {
                    setCurrentItem(mCurrentIndex - 1);
                }

                @Override
                public void toNextMonth(CustomDate date) {
                    setCurrentItem(mCurrentIndex + 1);
                }

                @Override
                public void choiceDate(CustomDate date) {
                    if(dateChoiceListener != null){
                        dateChoiceListener.currChoiceDate(date);
                    }
                }

                @Override
                public void showDate(CustomDate date) {
                }

            });
        }
        adapter = new com.example.administrator.take_two.moneylist.widget.DateView.CalendarViewAdapter2(mShowViews);
        setViewPager();
    }
    private void setViewPager() {
        setAdapter(adapter);
        setCurrentItem(mCurrentIndex);
        setDatePreNext(mCurrentIndex);
        setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                setDatePreNext(position);
                mCurrentIndex = position;
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }


        });
    }

    private void setDatePreNext(final int position) {
        //提前更新号，便于直接显示
        CalendarGridView view = mShowViews[(position) % mShowViews.length];
        view.update();
        currShowDate = view.getmShowDate();
        if(dateChoiceListener != null){
            dateChoiceListener.currShowDate(currShowDate);
        }
        //为了防止由于对象引用导致反复设置
        CustomDate showDate1 = new CustomDate(currShowDate.getYear(), currShowDate.getMonth(), currShowDate.getDay());
        CustomDate showDate2 = new CustomDate(currShowDate.getYear(), currShowDate.getMonth(), currShowDate.getDay());
        mShowViews[(position + 1) % mShowViews.length].nextShowDate(showDate1);//右侧的项目应该要，加一个月
        mShowViews[(position - 1) % mShowViews.length].preShowDate(showDate2);//左侧的项目应该要，减一个月

    }

    /**
     * 计算方向
     *
     * @param arg0
     */
    private void measureDirection(int arg0) {

        if (arg0 > mCurrentIndex) {
            mDirection = SildeDirection.RIGHT;

        } else if (arg0 < mCurrentIndex) {
            mDirection = SildeDirection.LEFT;
        }
        mCurrentIndex = arg0;
    }

    // 更新日历视图
    private void updateBoth(int arg0) {
        mShowViews = adapter.getAllItems();
        mShowViews[(arg0 + 1) % mShowViews.length].rightSlide();//右侧的项目应该要，加一个月
        mShowViews[(arg0 - 1) % mShowViews.length].leftSlide();//左侧的项目应该要，减一个月
        mDirection = SildeDirection.NO_SILDE;
        setCurrentItem(mCurrentIndex);
    }

    // 更新日历视图
    private void updateCalendarView(int arg0) {
        mShowViews = adapter.getAllItems();
        if (mDirection == SildeDirection.RIGHT) {
            mShowViews[arg0 % mShowViews.length].rightSlide();
        } else if (mDirection == SildeDirection.LEFT) {
            mShowViews[arg0 % mShowViews.length].leftSlide();
        }
        mDirection = SildeDirection.NO_SILDE;
        setCurrentItem(mCurrentIndex);
    }

    private int mCurrentIndex = 498;
    private CalendarGridView[] mShowViews;
    private com.example.administrator.take_two.moneylist.widget.DateView.CalendarViewAdapter2 adapter;
    private SildeDirection mDirection = SildeDirection.NO_SILDE;

    enum SildeDirection {
        RIGHT, LEFT, NO_SILDE;
    }

    public void setDateChoiceListener(DateChoiceListener dateChoiceListener) {
        this.dateChoiceListener = dateChoiceListener;
    }

    DateChoiceListener dateChoiceListener;
    public interface DateChoiceListener{
        public void currShowDate(CustomDate date);
        public void currChoiceDate(CustomDate date);
    }
}
