package com.example.administrator.take_two.moneylist.widget.DateView;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.administrator.take_two.R;
import com.example.administrator.take_two.adapter.BaseMyAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/18.
 */
public class CalendarGridView extends GridView implements AdapterView.OnItemClickListener {
    private static final int TOTAL_COL = 7; // 7列
    private static final int TOTAL_ROW = 6; // 6行

    public CustomDate getmShowDate() {
        return mShowDate;
    }

    private CustomDate mShowDate; // 自定义的日期，包括year,month,day  当前展示的月份
    private OnCellListener mCellClickListener; // 单元格点击回调事件

    private static Cell_new mClickCell;

    List<Cell_new> cellList = new ArrayList<Cell_new>();
    CalendarGridAdapter calendarGridAdapter = null;
    private static int firstDayWeek = 0;
    private static int currentMonthDays = 0;
    public CalendarGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public CalendarGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CalendarGridView(Context context) {
        super(context);
        init(context);
    }

    public CalendarGridView(Context context, OnCellListener listener) {
        super(context);
        this.mCellClickListener = listener;
        init(context);
    }
    private void init(Context context) {
        setNumColumns(TOTAL_COL);
        ViewGroup.LayoutParams lps = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setLayoutParams(lps);
        setBackgroundColor(0xffffffff);
        calendarGridAdapter = new CalendarGridAdapter(context);
        this.setAdapter((ListAdapter) calendarGridAdapter);
        setOnItemClickListener(this);
        mShowDate = new CustomDate();
        setSelector(new ColorDrawable(Color.TRANSPARENT));//设置选择的背景为无色
        fillDate();//
    }
    private void fillDate() {
        int monthDay = DateUtil.getCurrentMonthDay(); // 今天
        int lastMonthDays = DateUtil.getMonthDays(mShowDate.year,
                mShowDate.month - 1); // 上个月的天数
        currentMonthDays = DateUtil.getMonthDays(mShowDate.year,
                mShowDate.month); // 当前月的天数
        firstDayWeek = DateUtil.getWeekDayFromDate(mShowDate.year,
                mShowDate.month);
        boolean isCurrentMonth = false;
        if (DateUtil.isCurrentMonth(mShowDate)) {
            isCurrentMonth = true;
        }
        int day = 0;
        for (int j = 0; j < TOTAL_ROW; j++) {
            for (int i = 0; i < TOTAL_COL; i++) {
                Cell_new cellAdding = null;
                int position = i + j * TOTAL_COL; // 单元格位置
                // 这个月的
                if (position >= firstDayWeek
                        && position < firstDayWeek + currentMonthDays) {
                    day++;
                    cellAdding = new Cell_new(CustomDate.modifiDayForObject(
                            mShowDate, day), State.CURRENT_MONTH_DAY, false);
                    // 今天
                    if (isCurrentMonth && day == monthDay) {
//                        CustomDate date = CustomDate.modifiDayForObject(mShowDate, day);
//                        cellAdding = new Cell_new(date, State.TODAY, false);
                        cellAdding.state = State.TODAY;
                    }

                    // 过去一个月
                } else if (position < firstDayWeek) {
                    cellAdding = new Cell_new(new CustomDate(mShowDate.year,
                            mShowDate.month - 1, lastMonthDays
                            - (firstDayWeek - position - 1)),
                            State.PAST_MONTH_DAY, false);
                    // 下个月
                } else if (position >= firstDayWeek + currentMonthDays) {
                    cellAdding = new Cell_new((new CustomDate(mShowDate.year,
                            mShowDate.month + 1, position - firstDayWeek
                            - currentMonthDays + 1)),
                            State.NEXT_MONTH_DAY, false);
                }

                //设置是否选中
                cellAdding.isChoice = cellAdding.isEquals(mClickCell);
                cellList.add(cellAdding);
            }
        }
        calendarGridAdapter.setData(cellList);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Cell_new cellClick = (Cell_new) parent.getItemAtPosition(position);
        if (cellClick == null) {
            return;
        }
        mClickCell = cellClick;
        if (mCellClickListener != null) {
            mCellClickListener.choiceDate(mClickCell.date);
        }
        // 上个月
        if (mClickCell.date.getYear() < mShowDate.getYear() || mClickCell.date.getMonth() < mShowDate.getMonth()) {
            if (mCellClickListener != null) {
                mCellClickListener.toPreMonth(mClickCell.date);
            }
            // 下个月
        } else if (mClickCell.date.getYear() > mShowDate.getYear() || mClickCell.date.getMonth() > mShowDate.getMonth()) {
            if (mCellClickListener != null) {
                mCellClickListener.toNextMonth(mClickCell.date);
            }
        } else {
            //其他情况就是直接刷新
            update();
        }
    }

    public void update() {
        cellList.clear();
        fillDate();
        invalidate();
    }

    /**
     * 单元格点击的回调接口
     *
     * @author wuwenjie
     */
    public interface OnCellListener {
        void toPreMonth(CustomDate date);// 告知去上一个月

        void toNextMonth(CustomDate date);// 告知去下一个月

        void choiceDate(CustomDate date);

        void showDate(CustomDate date);//展示时间
    }
    class Cell_new {
        public CustomDate date;
        public State state;
        public boolean isChoice = false;

        public Cell_new(CustomDate date, State state, boolean isChoice) {
            super();
            this.isChoice = isChoice;
            this.date = date;
            this.state = state;
        }

        boolean isEquals(Cell_new clickCell) {
            if (clickCell == null || date == null) {
                return false;
            }
            CustomDate clickDate = clickCell.date;
            if (clickDate == null) {
                return false;
            }
            if (date.getDay() == clickDate.getDay() && date.getMonth() == clickDate.getMonth() && date.getYear() == clickDate.getYear()) {
                return true;
            }
            return false;
        }
    }

    /**
     * @author wuwenjie 单元格的状态 当前月日期，过去的月的日期，下个月的日期
     */
    enum State {
        TODAY, CURRENT_MONTH_DAY, PAST_MONTH_DAY, NEXT_MONTH_DAY, UNREACH_DAY;
    }
    // 从左往右划，上一个月
    public void leftSlide() {
        if (mShowDate.month == 1) {
            mShowDate.month = 12;
            mShowDate.year -= 1;
        } else {
            mShowDate.month -= 1;
        }
        update();
    }

    // 从右往左划，下一个月
    public void rightSlide() {
        if (mShowDate.month == 12) {
            mShowDate.month = 1;
            mShowDate.year += 1;
        } else {
            mShowDate.month += 1;
        }
        update();
    }

    public void nextShowDate(CustomDate currShowDate) {
        if (currShowDate.month == 12) {
            currShowDate.month = 1;
            currShowDate.year += 1;
        } else {
            currShowDate.month += 1;
        }
        this.mShowDate = currShowDate;
        update();
    }

    public void preShowDate(CustomDate currShowDate) {
        if (currShowDate.month == 1) {
            currShowDate.month = 12;
            currShowDate.year -= 1;
        } else {
            currShowDate.month -= 1;
        }
        this.mShowDate = currShowDate;
        update();
    }
    class CalendarGridAdapter extends BaseMyAdapter<Cell_new> {

        public CalendarGridAdapter(Context context) {
            super(context);
        }

        @Override
        public int getLayoutResourceId() {
            return R.layout.layout_calendar_grid_item;
        }

        @Override
        public int getCount() {
            if (datas != null) {
                return datas.size();
            }
            return 0;
        }

        @Override

        public Object initView(int position, View convertView) {
            Holder holder = new Holder();
            holder.txt_day = (TextView) convertView.findViewById(R.id.txt_day);
            return holder;
        }

        @Override
        public void fillView(final int position, final View convertView, Object mholder) {
            Holder holder = (Holder) mholder;
            Cell_new data = cellList.get(position);
            if (data == null || data.date == null) {
                return;
            }
            switch (data.state) {
                case TODAY: // 今天
                    holder.txt_day.setBackgroundResource(R.drawable.shape_circle_green_a);
                    holder.txt_day.setTextColor(0xffffffff);
                    break;
                case CURRENT_MONTH_DAY: // 当前月日期
                    holder.txt_day.setBackgroundColor(0xffffffff);
                    holder.txt_day.setTextColor(0xff404040);
                    break;
                case PAST_MONTH_DAY: // 过去一个月
                    holder.txt_day.setBackgroundColor(0xffffffff);
                    holder.txt_day.setTextColor(0xffb3b3b3);
                case NEXT_MONTH_DAY: // 下一个月
                    holder.txt_day.setBackgroundColor(0xffffffff);
                    holder.txt_day.setTextColor(0xffb3b3b3);
                    break;
                default:
                    break;
            }
            if (data.isChoice) {
                holder.txt_day.setBackgroundResource(R.drawable.shape_circle_orange_a);
                holder.txt_day.setTextColor(0xffffffff);
            }
            holder.txt_day.setText(data.date.day + "");
        }

        private class Holder {
            TextView txt_day;
        }
    }
}
