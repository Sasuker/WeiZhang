package com.example.administrator.take_two.model;

/**
 * Created by Administrator on 2016/9/19.
 */
public class OneDayTotal {
    private int id;
    private String userId;
    private int maxOneDayIn_id;//一天收入最大的id，，可为空
    private int maxOneDayOut_id;//一天支出最大的id，，可为空
    private String maxInName;//最大收入项名称
    private double maxInMoney;//最大收入金额
    private String maxOutName;//最大支出项名称
    private double maxOutMoney;//最大支出项金额
    private double totalMoney_in;//总收入金额
    private double totalMoney_out;//总收入金额
    private int year;
    private int month;
    private int day;
    private String dateFmt;
    private long timeSeconds;//具体时间秒数 支出和收入应该是一样的，代表某一天的时间秒数

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getMaxOneDayOut_id() {
        return maxOneDayOut_id;
    }

    public void setMaxOneDayOut_id(int maxOneDayOut_id) {
        this.maxOneDayOut_id = maxOneDayOut_id;
    }

    public int getMaxOneDayIn_id() {
        return maxOneDayIn_id;
    }

    public void setMaxOneDayIn_id(int maxOneDayIn_id) {
        this.maxOneDayIn_id = maxOneDayIn_id;
    }

    public String getMaxInName() {
        return maxInName;
    }

    public void setMaxInName(String maxInName) {
        this.maxInName = maxInName;
    }

    public double getMaxInMoney() {
        return maxInMoney;
    }

    public void setMaxInMoney(double maxInMoney) {
        this.maxInMoney = maxInMoney;
    }

    public String getMaxOutName() {
        return maxOutName;
    }

    public void setMaxOutName(String maxOutName) {
        this.maxOutName = maxOutName;
    }

    public double getTotalMoney_in() {
        return totalMoney_in;
    }

    public void setTotalMoney_in(double totalMoney_in) {
        this.totalMoney_in = totalMoney_in;
    }

    public double getMaxOutMoney() {
        return maxOutMoney;
    }

    public void setMaxOutMoney(double maxOutMoney) {
        this.maxOutMoney = maxOutMoney;
    }

    public double getTotalMoney_out() {
        return totalMoney_out;
    }

    public void setTotalMoney_out(double totalMoney_out) {
        this.totalMoney_out = totalMoney_out;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public long getTimeSeconds() {
        return timeSeconds;
    }

    public void setTimeSeconds(long timeSeconds) {
        this.timeSeconds = timeSeconds;
    }

    public String getDateFmt() {
        return dateFmt;
    }

    public void setDateFmt(String dateFmt) {
        this.dateFmt = dateFmt;
    }
}
