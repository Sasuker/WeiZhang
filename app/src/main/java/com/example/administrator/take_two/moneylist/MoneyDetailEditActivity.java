package com.example.administrator.take_two.moneylist;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.take_two.AppConstanct;
import com.example.administrator.take_two.BaseActivity;
import com.example.administrator.take_two.MyApplication;
import com.example.administrator.take_two.R;
import com.example.administrator.take_two.Util.DateUtil;
import com.example.administrator.take_two.Util.NumberUtil;
import com.example.administrator.take_two.Util.ToastUtil;
import com.example.administrator.take_two.db.dao.MoneyDao;
import com.example.administrator.take_two.db.dao.MonthTotalDao;
import com.example.administrator.take_two.db.dao.OneDayTotalDao;
import com.example.administrator.take_two.model.MoneyModel;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2016/9/19.
 */
public class MoneyDetailEditActivity extends BaseActivity{
    MoneyDetailEditActivity activity;
    TextView txt_date, txt_time, txt_title, txt_commit;
    EditText edit_item_name, edit_money, edit_remark;
    private String type = "";
    private String dateFmt = "", oldDateFmt = "";
    int year = 2016;
    int month = 2;
    int day = 17;
    int oldYear = 2016;
    int oldMonth = 2;
    long dateMills = 0;
    ImageView rdio_is_out,rdio_is_in;
    boolean isOut = true;
    MoneyModel oldMoneyModel = null;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == AppConstanct.HANDLER_CODE_BACK) {
                dismissLoadingDialog();
                activity.finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        setContentView(R.layout.activity_money_detail_edit);
        initView();
        initData();
    }

    MoneyModel moneyModel;
    private void initView() {
        rdio_is_out = (ImageView) findViewById(R.id.rdio_is_out);
        rdio_is_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isOut = true;
                rdio_is_in.setImageResource(R.drawable.icon_check_out);
                rdio_is_out.setBackgroundResource(R.drawable.icon_check_in);
            }
        });
        rdio_is_in = (ImageView) findViewById(R.id.rdio_is_in);
        rdio_is_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isOut = false;
                rdio_is_in.setImageResource(R.drawable.icon_check_in);
                rdio_is_out.setBackgroundResource(R.drawable.icon_check_out);
            }
        });
        txt_title = (TextView) findViewById(R.id.txt_title);
        txt_date = (TextView) findViewById(R.id.txt_date);
        txt_time = (TextView) findViewById(R.id.txt_time);
        txt_commit = (TextView) findViewById(R.id.txt_commit);
        txt_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insert();
            }
        });
        edit_item_name = (EditText) findViewById(R.id.edit_item_name);
        edit_money = (EditText) findViewById(R.id.edit_money);
        edit_remark = (EditText) findViewById(R.id.edit_remark);

        txt_date.setText(DateUtil.getToDayDate());
        txt_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeDate();
            }
        });
    }

    private void initData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        type = intent.getStringExtra(AppConstanct.TYPE);
        if (AppConstanct.TYPE_EDIT.equals(type)) {
            moneyModel = (MoneyModel) bundle.getSerializable(AppConstanct.DATE);
            if (moneyModel != null) {
                oldMoneyModel = new MoneyModel(moneyModel);
                oldDateFmt = dateFmt = moneyModel.getDateFmt();
                dateMills = moneyModel.getTimeSeconds();
                txt_date.setText(dateFmt);
                edit_item_name.setText(moneyModel.getItemName());
                edit_money.setText(moneyModel.getMoney() + "");
                edit_remark.setText(moneyModel.getRemark());
                day = moneyModel.getDay();
                oldYear = year = moneyModel.getYear();
                oldMonth = month = moneyModel.getMonth();
                if ("0".equals(moneyModel.getIsOut())) {
                    rdio_is_in.setImageResource(R.drawable.icon_check_out);
                    rdio_is_out.setBackgroundResource(R.drawable.icon_check_in);
                    isOut = true;
                } else {
                    isOut = false;
                    rdio_is_in.setImageResource(R.drawable.icon_check_in);
                    rdio_is_out.setBackgroundResource(R.drawable.icon_check_out);
                }
            }
            txt_title.setText("编辑");
        }else {
            txt_title.setText("微帐");
            dateMills = DateUtil.getToDayMills();
            year = DateUtil.getToDayYear();
            month = DateUtil.getToDayMonth();
            day = DateUtil.getToDayDay();
        }
    }
    public void goBack(View view) {
        finish();
    }

    public void changeDate() {
        DateUtil.datePicker(this, txt_date, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int yearC, int monthOfYear, int dayOfMonth) {
                if (DateUtil.smallThanCurrDate(yearC, monthOfYear, dayOfMonth)) {
                    year = yearC;
                    month = monthOfYear + 1;
                    day = dayOfMonth;
                    dateMills = DateUtil.getDateMills(yearC, monthOfYear, dayOfMonth);
                    txt_date.setText(DateUtil.getDateStr(yearC, monthOfYear, dayOfMonth));
                } else {
                    ToastUtil.show("选择日期不应该大于当前时间");
                }
            }
        });
    }
    public void insert() {
        String date = txt_date.getText().toString();
        String time = txt_time.getText().toString();
        String remark = edit_remark.getText().toString();
        String moneyStr = edit_money.getText().toString();
        String itemName = edit_item_name.getText().toString();
        if (TextUtils.isEmpty(date)) {
            Toast.makeText(this,"请选择日期" ,Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(itemName)) {
            Toast.makeText(this,"请填写名称" ,Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(moneyStr)) {
            Toast.makeText(this,"请填写金额" ,Toast.LENGTH_SHORT).show();
            return;
        }
        double moneyN = 0;
        if(!NumberUtil.isDouble(edit_money.getText().toString())){
            Toast.makeText(this,"请输入正确的金额" ,Toast.LENGTH_SHORT).show();
            return;
        }else{
            //转换金额
            moneyN = Double.parseDouble(edit_money.getText().toString());
            moneyN = NumberUtil.KeepAfew(moneyN,2);//保留两位
        }


        showLoadingDialog("请稍后");
        if (!AppConstanct.TYPE_EDIT.equals(type)) {
            moneyModel = new MoneyModel();
        }

        if (isOut) {
            //说明是支出
            moneyModel.setIsOut("0");
        } else {
            moneyModel.setIsOut("1");
        }
        moneyModel.setMoney(moneyN);
        moneyModel.setUserId(MyApplication.getInstance().useId);
        moneyModel.setMonth(month);
        moneyModel.setDay(day);
        moneyModel.setYear(year);
        moneyModel.setDateFmt(date);
        moneyModel.setItemName(edit_item_name.getText().toString());

        moneyModel.setRemark(edit_remark.getText().toString());
        moneyModel.setTimeSeconds(dateMills);
        if (!AppConstanct.TYPE_EDIT.equals(type)) {
            //插入新的一条
            MoneyDao.insert(activity, moneyModel);
            if (isOut) {
                //更新一天的最大值(支出）
                OneDayTotalDao.updateMaxMoney_out(activity, moneyModel);
                //更新一个月的最大值(支出）
                MonthTotalDao.updateMaxMoney_out(activity, moneyModel);
            } else {
                //更新一天的最大值(收入）
                OneDayTotalDao.updateMaxMoney_in(activity, moneyModel);
                //更新一个月的最大值(收入）
                MonthTotalDao.updateMaxMoney_in(activity, moneyModel);
            }
        } else {
            //删除旧的
            MoneyDao.deleteById(activity, oldMoneyModel.getId());
            OneDayTotalDao.deleteByDateFmt(activity, MyApplication.getInstance().useId, oldMoneyModel.getDateFmt());
//            MonthTotalDao.deleteByYearMonth(activity, MyApplication.getInstance().userId,  oldMoneyModel.getYear(), oldMoneyModel.getMonth());
            //更新旧的
            OneDayTotalDao.updateMaxMoney_out(activity, oldMoneyModel);
            MonthTotalDao.updateMaxMoney_out(activity, oldMoneyModel);
            OneDayTotalDao.updateMaxMoney_in(activity, oldMoneyModel);
            MonthTotalDao.updateMaxMoney_in(activity, oldMoneyModel);

            //插入新的
            MoneyDao.insert(activity, moneyModel);
            OneDayTotalDao.updateMaxMoney_out(activity, moneyModel);  //更新一天的最大值(支出）
            MonthTotalDao.updateMaxMoney_out(activity, moneyModel);  //更新一个月的最大值(支出）
            OneDayTotalDao.updateMaxMoney_in(activity, moneyModel);    //更新一天的最大值(收入）
            MonthTotalDao.updateMaxMoney_in(activity, moneyModel);    //更新一个月的最大值(收入）
//            if (oldMoneyModel != null && !txt_date.getText().toString().equals(oldMoneyModel.getDateFmt())) {
//               //TODO。。。之后判断是否是支出和收入的变化
//                MoneyDao.deleteById(activity,oldMoneyModel.getId());
//                //则删除之前旧的那个：
//                OneDayTotalDao.deleteByDateFmt(activity, oldMoneyModel.getUserId(), oldMoneyModel.getDateFmt());
//                //重新计算
//                //如果旧时间字符串和新字符串不一样，说明某天的时间变化啦，则那个时间下的月份和一天的收入支出都要重新算
//                //更新一天的最大值(支出）
//                OneDayTotalDao.updateMaxMoney_out(activity, oldMoneyModel);
//                //更新一天的最大值(收入）
//                OneDayTotalDao.updateMaxMoney_in(activity, oldMoneyModel);
//                //如果年月变化啦，说明一个月的统计就要重新计算
//                if (year != oldYear || month != oldMonth) {
//                    //则删除之前旧的那个月的统计
//                    MonthTotalDao.deleteByYearMonth(activity, oldMoneyModel.getYear(), oldMoneyModel.getMonth());
//                    //重新计算
//                    //更新一个月的最大值(支出）
//                    MonthTotalDao.updateMaxMoney_out(activity, oldMoneyModel);
//                    //更新一个月的最大值(收入）
//                    MonthTotalDao.updateMaxMoney_in(activity, oldMoneyModel);
//                }
//            }
        }
        setResult(Activity.RESULT_OK);
        EventBus.getDefault().post(new MoneyModel());
        handler.sendEmptyMessageDelayed(AppConstanct.HANDLER_CODE_BACK, 1000);
    }


}
