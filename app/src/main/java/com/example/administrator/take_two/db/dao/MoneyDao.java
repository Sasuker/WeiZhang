package com.example.administrator.take_two.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.administrator.take_two.db.DBOpenHelper;
import com.example.administrator.take_two.model.MoneyModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/20.
 */
public class MoneyDao {
    /**
     * 插入一条数据
     *
     * @return
     */
    public static void insert(Context context, MoneyModel moneyModel) {
        try {
            DBOpenHelper dbOpenHelper = DBOpenHelper.getInstance(context);
            ;
            SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("userId", moneyModel.getUserId());
            values.put("money", moneyModel.getMoney());
            values.put("remark", moneyModel.getRemark());
            values.put("timeSeconds", moneyModel.getTimeSeconds());
            values.put("itemName", moneyModel.getItemName());
            values.put("isOut", moneyModel.getIsOut());
            values.put("year", moneyModel.getYear());
            values.put("month", moneyModel.getMonth());
            values.put("day", moneyModel.getDay());
            values.put("dateFmt", moneyModel.getDateFmt());
            //插入
            db.insert(DBOpenHelper.tb_money, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取最大值
     */
    public static MoneyModel getMaxOneDay(SQLiteDatabase db, String userId, String dateFmt, String isOut) {

        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select * from " + DBOpenHelper.tb_money + " m where m.dateFmt =? and m.userId=? and m.isOut=? order by m.money desc limit 1", new String[]{dateFmt, userId, isOut});
            if (cursor.moveToFirst()) {
                MoneyModel data = new MoneyModel();
                data.setUserId(cursor.getString(cursor.getColumnIndex("userId")));
                data.setId(cursor.getInt(cursor.getColumnIndex("id")));
                data.setYear(cursor.getInt(cursor.getColumnIndex("year")));
                data.setMonth(cursor.getInt(cursor.getColumnIndex("month")));
                data.setDay(cursor.getInt(cursor.getColumnIndex("day")));
                data.setMoney(cursor.getDouble(cursor.getColumnIndex("money")));
                data.setRemark(cursor.getString(cursor.getColumnIndex("remark")));
                data.setTimeSeconds(cursor.getLong(cursor.getColumnIndex("timeSeconds")));
                data.setItemName(cursor.getString(cursor.getColumnIndex("itemName")));
                data.setIsOut(cursor.getString(cursor.getColumnIndex("isOut")));
                return data;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    public static MoneyModel getMaxOneDay(Context context, String userId, String dateFmt, String isOut) {


        Cursor cursor = null;
        try {
            DBOpenHelper dbOpenHelper = DBOpenHelper.getInstance(context);
            SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
            cursor = db.rawQuery("select * from " + DBOpenHelper.tb_money + " m where m.dateFmt =? and m.userId=? and m.isOut=? order by m.money desc limit 1", new String[]{dateFmt, userId, isOut});
            if (cursor.moveToFirst()) {
                MoneyModel data = new MoneyModel();
                data.setUserId(cursor.getString(cursor.getColumnIndex("userId")));
                data.setId(cursor.getInt(cursor.getColumnIndex("id")));
                data.setYear(cursor.getInt(cursor.getColumnIndex("year")));
                data.setMonth(cursor.getInt(cursor.getColumnIndex("month")));
                data.setDay(cursor.getInt(cursor.getColumnIndex("day")));
                data.setMoney(cursor.getDouble(cursor.getColumnIndex("money")));
                data.setRemark(cursor.getString(cursor.getColumnIndex("remark")));
                data.setTimeSeconds(cursor.getLong(cursor.getColumnIndex("timeSeconds")));
                data.setItemName(cursor.getString(cursor.getColumnIndex("itemName")));
                data.setIsOut(cursor.getString(cursor.getColumnIndex("isOut")));
                return data;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    /**
     * 获取一天的总钱数
     *
     * @param db
     * @param userId
     * @param dateFmt
     * @return
     */
    public static double getTotalOneDay(SQLiteDatabase db, String userId, String dateFmt, String isOut) {

        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select SUM(m.money) as sumMoney from " + DBOpenHelper.tb_money + " m where m.dateFmt=? and m.userId=? and m.isOut=?", new String[]{dateFmt, userId, isOut});
            if (cursor.moveToFirst()) {
                return cursor.getDouble(cursor.getColumnIndex("sumMoney"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return 0;
    }


    public static double getTotalOneDay(Context context, String userId, String dateFmt, String isOut) {
        Cursor cursor = null;
        try {
            DBOpenHelper dbOpenHelper = DBOpenHelper.getInstance(context);
            SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
            cursor = db.rawQuery("select SUM(m.money) as sumMoney from " + DBOpenHelper.tb_money + " m where m.dateFmt=? and m.userId=? and m.isOut=?", new String[]{dateFmt, userId, isOut});
            if (cursor.moveToFirst()) {
                return cursor.getDouble(cursor.getColumnIndex("sumMoney"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return 0;
    }

    /**
     * 获取一个月最大值
     */
    public static MoneyModel getMaxMonth(SQLiteDatabase db, String userId, int year, int month, String isOut) {
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select * from " + DBOpenHelper.tb_money + " m where m.year =? and m.month =? and m.userId=? and m.isOut=? order by m.money desc limit 1", new String[]{year + "", month + "", userId, isOut});
            if (cursor.moveToFirst()) {
                MoneyModel data = new MoneyModel();
                data.setUserId(cursor.getString(cursor.getColumnIndex("userId")));
                data.setId(cursor.getInt(cursor.getColumnIndex("id")));
                data.setYear(cursor.getInt(cursor.getColumnIndex("year")));
                data.setMonth(cursor.getInt(cursor.getColumnIndex("month")));
                data.setDay(cursor.getInt(cursor.getColumnIndex("day")));
                data.setMoney(cursor.getDouble(cursor.getColumnIndex("money")));
                data.setRemark(cursor.getString(cursor.getColumnIndex("remark")));
                data.setTimeSeconds(cursor.getLong(cursor.getColumnIndex("timeSeconds")));
                data.setItemName(cursor.getString(cursor.getColumnIndex("itemName")));
                data.setIsOut(cursor.getString(cursor.getColumnIndex("isOut")));
                data.setDateFmt(cursor.getString(cursor.getColumnIndex("dateFmt")));
                return data;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    public static double getTotalMonth(SQLiteDatabase db, String userId, int year, int month, String isOut) {
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select SUM(m.money) as sumMoney from " + DBOpenHelper.tb_money + " m where m.year =? and m.month =? and m.userId=? and m.isOut=? ", new String[]{year + "", month + "", userId, isOut});
            if (cursor.moveToFirst()) {
                return cursor.getDouble(cursor.getColumnIndex("sumMoney"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return 0;
    }

    public static double getTotalMonth(Context context, String userId, int year, int month, String isOut) {
        Cursor cursor = null;
        try {
            DBOpenHelper dbOpenHelper = DBOpenHelper.getInstance(context);
            SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
            cursor = db.rawQuery("select SUM(m.money) as sumMoney from " + DBOpenHelper.tb_money + " m where m.year =? and m.month =? and m.userId=? and m.isOut=? ", new String[]{year + "", month + "", userId, isOut});
            if (cursor.moveToFirst()) {
                return cursor.getDouble(cursor.getColumnIndex("sumMoney"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return 0;
    }

    /**
     * 获取分页数据
     *
     * @param offset    第几页
     * @param maxResult 一页几行
     * @param userId    第几页
     * @param dateFmt   一页几行
     * @return
     */
    public static List<MoneyModel> getScrollData(Context context, String userId, String dateFmt, int offset, int maxResult) {
        Cursor cursor = null;
        List<MoneyModel> datas = new ArrayList<MoneyModel>();
        try {
            DBOpenHelper dbOpenHelper = DBOpenHelper.getInstance(context);
            SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
            cursor = db.rawQuery("select * from " + DBOpenHelper.tb_money + " m where m.dateFmt=? and m.userId=? order by m.timeSeconds desc limit " + maxResult * (offset - 1) + "," + maxResult, new String[]{dateFmt, userId});
            while (cursor.moveToNext()) {
                MoneyModel data = new MoneyModel();
                data.setUserId(cursor.getString(cursor.getColumnIndex("userId")));
                data.setId(cursor.getInt(cursor.getColumnIndex("id")));
                data.setYear(cursor.getInt(cursor.getColumnIndex("year")));
                data.setMonth(cursor.getInt(cursor.getColumnIndex("month")));
                data.setDay(cursor.getInt(cursor.getColumnIndex("day")));
                data.setMoney(cursor.getDouble(cursor.getColumnIndex("money")));
                data.setRemark(cursor.getString(cursor.getColumnIndex("remark")));
                data.setTimeSeconds(cursor.getLong(cursor.getColumnIndex("timeSeconds")));
                data.setItemName(cursor.getString(cursor.getColumnIndex("itemName")));
                data.setIsOut(cursor.getString(cursor.getColumnIndex("isOut")));
                data.setDateFmt(cursor.getString(cursor.getColumnIndex("dateFmt")));
                datas.add(data);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return datas;
    }

    public static boolean delete(Context context, String userId, String dateFmt) {
        DBOpenHelper dbOpenHelper = DBOpenHelper.getInstance(context);
        try {
            SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
            long affectLines = db.delete(DBOpenHelper.tb_money, "userId=? and dateFmt = ?", new String[]{userId, dateFmt});
            if (affectLines <= 0) {
                return false;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public static boolean deleteById(Context context, Integer id) {
        DBOpenHelper dbOpenHelper = DBOpenHelper.getInstance(context);
        try {
            SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
            long affectLines = db.delete(DBOpenHelper.tb_money, "id=?", new String[]{id.toString()});
            if (affectLines <= 0) {
                return false;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean update(Context context, MoneyModel moneyModel) {
        DBOpenHelper dbOpenHelper = DBOpenHelper.getInstance(context);
        try {
            SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("userId", moneyModel.getUserId());
            values.put("money", moneyModel.getMoney());
            values.put("remark", moneyModel.getRemark());
            values.put("timeSeconds", moneyModel.getTimeSeconds());
            values.put("itemName", moneyModel.getItemName());
            values.put("isOut", moneyModel.getIsOut());
            values.put("year", moneyModel.getYear());
            values.put("month", moneyModel.getMonth());
            values.put("day", moneyModel.getDay());
            values.put("dateFmt", moneyModel.getDateFmt());
            long affectLines = db.update(DBOpenHelper.tb_money, values, "id=?", new String[]{moneyModel.getId() + ""});
            if (affectLines <= 0) {
                return false;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
