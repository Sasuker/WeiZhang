package com.example.administrator.take_two.adapter;


import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.take_two.AppConstanct;
import com.example.administrator.take_two.R;
import com.example.administrator.take_two.db.dao.MoneyDao;
import com.example.administrator.take_two.db.dao.MonthTotalDao;
import com.example.administrator.take_two.db.dao.OneDayTotalDao;
import com.example.administrator.take_two.model.MoneyModel;
import com.example.administrator.take_two.model.OneDayTotal;
import com.example.administrator.take_two.moneylist.MoneyDetailListActivity;
import com.example.administrator.take_two.moneylist.widget.OperateAlertDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/19.
 */
public class MobeyHistoryAdapter extends BaseMyAdapter<OneDayTotal>{
    private List<SectionInfo> sections = new ArrayList<SectionInfo>();
    int[] imgSXids = {
            R.drawable.icon_shengxiao_shu,
            R.drawable.icon_shengxiao_niu,
            R.drawable.icon_shengxiao_hu,
            R.drawable.icon_shengxiao_tu,
            R.drawable.icon_shengxiao_long,
            R.drawable.icon_shengxiao_she,
            R.drawable.icon_shengxiao_ma,
            R.drawable.icon_shengxiao_yang,
            R.drawable.icon_shengxiao_hou,
            R.drawable.icon_shengxiao_gou,
            R.drawable.icon_shengxiao_ji,
            R.drawable.icon_shengxiao_zhu};

    public void clearSections() {
        sections.clear();
    }

    public boolean needKeepSection(SectionInfo section_year_month) {
        for (SectionInfo se : sections) {
            //如果符合，则说明这个时间段已经出现过啦
            if (se.month == section_year_month.month && se.year == section_year_month.year) {
                //如果当前的位置，和之前存储第一次出现的位置一样，则说明需要保留
                if (se.appear_osition == section_year_month.appear_osition) {
                    return true;
                }
                return false;
            }
        }
        //都没有符合上述的，说明是第一次，所以需要添加
        sections.add(section_year_month);
        return true;
    }

    public MobeyHistoryAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.layout_money_list_item;
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
        holder.txt_year = (TextView) convertView.findViewById(R.id.txt_year);
        holder.txt_month = (TextView) convertView.findViewById(R.id.txt_month);
        holder.txt_total_money = (TextView) convertView.findViewById(R.id.txt_total_money);
        holder.txt_item_name = (TextView) convertView.findViewById(R.id.txt_item_name);
        holder.txt_item_money = (TextView) convertView.findViewById(R.id.txt_item_money);
        holder.txt_total_money_in = (TextView) convertView.findViewById(R.id.txt_total_money_in);
        holder.llay_year_month = (LinearLayout) convertView.findViewById(R.id.llay_year_month);
        holder.llay_money = (LinearLayout) convertView.findViewById(R.id.llay_money);
        holder.llay_in_out = (LinearLayout) convertView.findViewById(R.id.llay_in_out);
        holder.img_sx = (ImageView) convertView.findViewById(R.id.img_sx);
        holder.img_in_out = (ImageView) convertView.findViewById(R.id.img_in_out);
        return holder;
    }

    @Override
    public void fillView(final int position, final View convertView, Object mholder) {
        Holder holder = (Holder) mholder;
        OneDayTotal data = datas.get(position);
        holder.txt_day.setText(data.getDay() + "");
        holder.txt_year.setText(data.getYear() + "");
        holder.txt_month.setText(data.getMonth() + "");
        holder.txt_total_money.setText(data.getTotalMoney_out() + "");
        holder.txt_item_name.setText(data.getMaxOutName());
        holder.txt_item_money.setText(data.getMaxOutMoney() + "");
        holder.txt_total_money_in.setText(data.getTotalMoney_in()+"");
        if (needKeepSection(new SectionInfo(data.getYear(), data.getMonth(), data.getDay(), position))) {
            holder.llay_year_month.setVisibility(View.VISIBLE);
        } else {
            holder.llay_year_month.setVisibility(View.GONE);
        }
        holder.llay_money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OneDayTotal data = datas.get(position);

                Intent intent = new Intent(context, MoneyDetailListActivity.class);
                intent.putExtra(AppConstanct.DATEFMT, data.getDateFmt());
                context.startActivity(intent);
            }
        });
        holder.llay_money.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final OneDayTotal data = datas.get(position);
                OperateAlertDialog.show(context, "确定删除？", new OperateAlertDialog.OnButtonClickLinstener() {
                    @Override
                    public void confirm() {
                        delete(data);
                        sections.clear();
                        getData().remove(position);
                        notifyDataSetChanged();
                    }
                });

                return true;
            }
        });
        holder.img_sx.setBackgroundResource(getSXImgId(data.getYear()));
        if(data.getTotalMoney_in() > data.getTotalMoney_out()) {
            holder.llay_in_out.setBackgroundResource(R.drawable.shape_circle_green_a);
            holder.img_in_out.setBackgroundResource(R.drawable.icon_money_in);
        }else{
            holder.llay_in_out.setBackgroundResource(R.drawable.shape_circle_orange_a);
            holder.img_in_out.setBackgroundResource(R.drawable.icon_money_out);
        }
    }

    private int getSXImgId(int year) {
        return imgSXids[subtractYear(year) % 12];
    }


    /**
     * 获取当前年份与起始年之间的差值
     **/
    public static int subtractYear(int year) {
        int jiaziYear = startYear;
        if (year < jiaziYear) {//如果年份小于起始的甲子年(startYear = 1804),则起始甲子年往前偏移
            jiaziYear = jiaziYear - (60 + 60 * ((jiaziYear - year) / 60));//60年一个周期
        }
        return year - jiaziYear;
    }

    private final static int startYear = 1804;//定义起始年，1804年为甲子年属鼠

    public void delete(OneDayTotal data) {
        OneDayTotalDao.deleteById(context, data.getId());
        //删除啦某一天的，则说明，这天的所有子数据都要删除
        MoneyDao.delete(context, data.getUserId(), data.getDateFmt());//顺便删除相关的子类
        //且这个月的数据总值也要重新计算
        MoneyModel moneyModel = new MoneyModel();
        moneyModel.setMonth(data.getMonth());
        moneyModel.setYear(data.getYear());
        moneyModel.setUserId(data.getUserId());
        MonthTotalDao.updateMaxMoney_out(context, moneyModel);
        MonthTotalDao.updateMaxMoney_in(context,moneyModel);
    }

    private class Holder {
        LinearLayout llay_year_month, llay_money,llay_in_out;
        ImageView img_sx,img_in_out;
        TextView txt_day, txt_year, txt_month, txt_total_money, txt_item_name, txt_item_money,txt_total_money_in;
    }

    private class SectionInfo {
        int year, month, day, appear_osition;

        public SectionInfo(int year, int month, int day, int position) {
            this.year = year;
            this.month = month;
            this.day = day;
            this.appear_osition = position;
        }
    }
}
