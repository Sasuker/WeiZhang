package com.example.administrator.take_two.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/9/19.
 */
public class MoneyModel implements Serializable{
    private int id;
    private String userId;//使用者ID
    private double money;//花费钱数
    private String remark;//备注
    private String itemName;//记录项目名称
    private String isOut;//0代表支出，1代表收入
    private long timeSeconds;//具体时间秒数
    private int year;
    private int day;
    private int month;
    private String dateFmt;
    public MoneyModel(){

    }

    public MoneyModel(MoneyModel moneyModel){
        this.id = moneyModel.getId();
        this.userId = moneyModel.getUserId();
        this.money = moneyModel.getMoney();
        this.remark = moneyModel.getRemark();
        this.itemName = moneyModel.getItemName();
        this.isOut = moneyModel.getIsOut();
        this.timeSeconds = moneyModel.getTimeSeconds();
        this.year = moneyModel.getYear();
        this.day = moneyModel.getDay();
        this.month = moneyModel.getMonth();
        this.dateFmt = moneyModel.getDateFmt();
    }
    public String getDateFmt() {
        return dateFmt;
    }

    public void setDateFmt(String dateFmt) {
        this.dateFmt = dateFmt;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIsOut() {
        return isOut;
    }

    public void setIsOut(String isOut) {
        this.isOut = isOut;
    }

    public long getTimeSeconds() {
        return timeSeconds;
    }

    public void setTimeSeconds(long timeSeconds) {
        this.timeSeconds = timeSeconds;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}
