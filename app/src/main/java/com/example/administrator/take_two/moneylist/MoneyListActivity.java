package com.example.administrator.take_two.moneylist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.take_two.AppConstanct;
import com.example.administrator.take_two.BaseActivity;
import com.example.administrator.take_two.MyApplication;
import com.example.administrator.take_two.R;
import com.example.administrator.take_two.Util.ToastUtil;
import com.example.administrator.take_two.adapter.MobeyHistoryAdapter;
import com.example.administrator.take_two.db.dao.MoneyDao;
import com.example.administrator.take_two.db.dao.OneDayTotalDao;
import com.example.administrator.take_two.model.MoneyModel;
import com.example.administrator.take_two.model.OneDayTotal;
import com.example.administrator.take_two.moneylist.widget.DateView.CalendarViewPager;
import com.example.administrator.take_two.moneylist.widget.DateView.CustomDate;
import com.example.administrator.take_two.moneylist.widget.FrameLayout;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import lib.xlistview.XListView;

/**
 * Created by Administrator on 2016/9/11.
 */
public class MoneyListActivity extends BaseActivity implements XListView.IXListViewListener {
    FrameLayout frameProgress;
    int pageIndex = 1;
    int pageSize = 15;
    ImageView img_bar_right, btn_left;
    RelativeLayout rly_rili,rly_liebiao;
    public String type = "liebiao";//当前显示类型，列表和日历显示
    CalendarViewPager vp_calendar;
    TextView txt_click_date_month,txt_click_date_day,txt_month_total_money, txt_month_total_money_in, txt_total_money_out, txt_total_oneday_in, txt_item_name, txt_item_money, txt_title_month, txt_title_year;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_list);
        registerEventBus();
        initView();
        initListView();
        initVpCalandar();
        loadDatas();
        getMonthTotal(currShowDate);
        getOneDay(currShowDate);
//        setOneDayTotalData("N/A", "N/A", "N/A", "N/A");
        setYearMonth(currShowDate.getYear() + "", (currShowDate.getMonth()) + "");
    }


    CustomDate currShowDate = new CustomDate();
    private void initVpCalandar() {
        //设置监听器
        vp_calendar = (CalendarViewPager) findViewById(R.id.vp_calendar);
        rly_rili = (RelativeLayout) findViewById(R.id.list_rili);
        rly_liebiao = (RelativeLayout) findViewById(R.id.list_leibiao);
        vp_calendar.setDateChoiceListener(new CalendarViewPager.DateChoiceListener() {
            @Override
            public void currShowDate(CustomDate date) {
                //滑动时候触发，代表某一个月
                getMonthTotal(date);
                currShowDate = date;
//                setOneDayTotalData("N/A", "N/A", "N/A", "N/A");
                setYearMonth(currShowDate.getYear() + "", (currShowDate.getMonth()) + "");
            }

            @Override
            public void currChoiceDate(CustomDate date) {
                getOneDay(date);//点击时候触发，代表某一天
            }
        });
    }
    public void getOneDay(CustomDate date) {
        txt_click_date_day.setText(date.getDateFmt()+"今日情况:");
        double totalOneDay_out = MoneyDao.getTotalOneDay(this, MyApplication.getInstance().useId, date.getDateFmt(), "0");
        double totalOneDay_in = MoneyDao.getTotalOneDay(this, MyApplication.getInstance().useId, date.getDateFmt(), "1");
        MoneyModel oneDayMax_out = MoneyDao.getMaxOneDay(this, MyApplication.getInstance().useId, date.getDateFmt(), "0");
        MoneyModel oneDayMax_in = MoneyDao.getMaxOneDay(this, MyApplication.getInstance().useId, date.getDateFmt(), "1");
        setOneDayTotalData(totalOneDay_in + "", totalOneDay_out + "", oneDayMax_out != null ? oneDayMax_out.getItemName() : "N/A", oneDayMax_out != null ? oneDayMax_out.getMoney() + "" : "N/A");
    }
    public void setOneDayTotalData(String oneDayTotal_in, String oneDayTotal_out, String maxOutName, String maxOutMoney) {
        txt_total_money_out.setText(oneDayTotal_out);
        txt_item_name.setText(maxOutName);
        txt_item_money.setText(maxOutMoney);
        txt_total_oneday_in.setText(oneDayTotal_in);
    }
    public void showYearMonth(boolean isShow) {
        if (isShow) {
            txt_title_year.setVisibility(View.VISIBLE);
            txt_title_month.setVisibility(View.VISIBLE);
        } else {
            txt_title_year.setVisibility(View.GONE);
            txt_title_month.setVisibility(View.GONE);
        }
    }
    public void setYearMonth(String year, String month) {
        txt_title_year.setText(year);
        txt_title_month.setText(month + "月");
        txt_click_date_month.setText(year+"-"+month+"当月情况:");
    }
    public void getMonthTotal(CustomDate data) {
        double totalMonth_out = MoneyDao.getTotalMonth(this, MyApplication.getInstance().useId, data.getYear(), data.getMonth(), "0");
        double totalMonth_in = MoneyDao.getTotalMonth(this, MyApplication.getInstance().useId, data.getYear(), data.getMonth(), "1");
        setMonthTotalData(totalMonth_out + "", totalMonth_in + "");
    }
    public void setMonthTotalData(String monthTotal, String monthTotal_in) {
        txt_month_total_money.setText(monthTotal);
        txt_month_total_money_in.setText(monthTotal_in);
    }
    MobeyHistoryAdapter adapter;
    XListView lvData;
    private void initListView() {
        adapter = new MobeyHistoryAdapter(this);
        lvData = (XListView) findViewById(R.id.lv_data);
        lvData.setAdapter(adapter);

        lvData.setXListViewListener(this);
        lvData.setPullLoadEnable(false);
        lvData.setPullRefreshEnable(true);
    }

    private void initView() {
        txt_month_total_money = (TextView) this.findViewById(R.id.txt_month_total_money);
        txt_month_total_money_in = (TextView) this.findViewById(R.id.txt_month_total_money_in);
        txt_total_money_out = (TextView) this.findViewById(R.id.txt_total_money);
        txt_total_oneday_in = (TextView) this.findViewById(R.id.txt_total_oneday_in);
        txt_item_name = (TextView) this.findViewById(R.id.txt_item_name);
        txt_item_money = (TextView) this.findViewById(R.id.txt_item_money);
        txt_title_month = (TextView) this.findViewById(R.id.txt_title_month);
        txt_title_year = (TextView) this.findViewById(R.id.txt_title_year);
        txt_click_date_month = (TextView) this.findViewById(R.id.txt_click_date_month);
        txt_click_date_day = (TextView) this.findViewById(R.id.txt_click_date_day);
        btn_left = (ImageView) this.findViewById(R.id.btn_left);
        btn_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MoneyListActivity.this, MoneyDetailEditActivity.class);
                startActivityForResult(intent, AppConstanct.REQ_CODE_ADD);
            }
        });
        frameProgress = (FrameLayout) this.findViewById(R.id.frame);
        frameProgress.initContainView();
        img_bar_right = (ImageView) this.findViewById(R.id.img_bar_right);
        img_bar_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ("rili".equals(type)) {
                    type = "liebiao";
                    //设置图标为日历
                    img_bar_right.setImageResource(R.drawable.selector_icon_rili);
                    rly_rili.setVisibility(View.GONE);
                    rly_liebiao.setVisibility(View.VISIBLE);
                    showYearMonth(false);
                    showBack(true);
                } else {
                    //设置图标为列表
                    type = "rili";
                    img_bar_right.setImageResource(R.drawable.selector_icon_liebiao);
                    rly_rili.setVisibility(View.VISIBLE);
                    rly_liebiao.setVisibility(View.GONE);
                    showYearMonth(true);
                    showBack(false);
                }
            }
        });
        findViewById(R.id.img_bar_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    public void showBack(boolean isShow){
        if(isShow){
            findViewById(R.id.img_bar_left).setVisibility(View.VISIBLE);
        }else{
            findViewById(R.id.img_bar_left).setVisibility(View.GONE);
        }
    }


    public void loadDatas() {
        List<OneDayTotal> datas = OneDayTotalDao.getScrollData(this, MyApplication.getInstance().useId, pageIndex, pageSize);
        loadDataSuccess(datas);
    }

    public void loadDataSuccess(Object object) {
        onLoad();
        if (object == null) {
            lvData.setPullLoadEnable(false);
            return;
        }
        List<OneDayTotal> datas = (List<OneDayTotal>) object;
        if (datas.size() < pageSize) {
            lvData.setPullLoadEnable(false);
        } else {
            lvData.setPullLoadEnable(true);
        }
        if (pageIndex == 1) {
            adapter.clearSections();
            adapter.setData(datas);
        } else {
            adapter.addData(datas);
        }
        pageIndex++;
    }

    public void loadDataFail(String errmsg) {
        onLoad();
        ToastUtil.show(errmsg);
    }
    private void onLoad() {
        frameProgress.destoryProgressView();

        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd  hh:mm:ss", Locale.CHINA);
        String date = sDateFormat.format(new Date());
        lvData.stopRefresh();
        lvData.stopLoadMore();
        lvData.setRefreshTime(date);
    }
    public void onRefresh() {
        pageIndex = 1;
        loadDatas();
    }

    public void onLoadMore() {
        loadDatas();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            pageIndex = 1;
            loadDatas();
//            setOneDayTotalData("N/A", "N/A", "N/A", "N/A");
            getMonthTotal(currShowDate);
        }
    }

    public void onEventMainThread(MoneyModel moneyModel) {
        pageIndex = 1;
        loadDatas();
//        setOneDayTotalData("N/A", "N/A", "N/A", "N/A");
        getMonthTotal(currShowDate);
    }

    @Override
    public void onBackPressed() {
        if ("rili".equals(type)) {
            type = "liebiao";
            //设置图标为日历
            img_bar_right.setImageResource(R.drawable.selector_icon_rili);
            rly_rili.setVisibility(View.GONE);
            rly_liebiao.setVisibility(View.VISIBLE);
            showYearMonth(false);
            return;
        }
        super.onBackPressed();
    }



}
