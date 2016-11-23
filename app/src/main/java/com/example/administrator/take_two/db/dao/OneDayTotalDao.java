package com.example.administrator.take_two.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.administrator.take_two.db.DBOpenHelper;
import com.example.administrator.take_two.model.MoneyModel;
import com.example.administrator.take_two.model.OneDayTotal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/20.
 */
public class OneDayTotalDao {
    /**
     * 更新一天最大支出值
     *
     * @param moneyModel
     * @param context
     * @return
     */
    public static boolean updateMaxMoney_out(Context context, MoneyModel moneyModel) {
        try {
            DBOpenHelper dbOpenHelper = DBOpenHelper.getInstance(context);
            SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
            String dateFmt = moneyModel.getDateFmt();
            //支出最大值
            MoneyModel maxOutModel = MoneyDao.getMaxOneDay(db, moneyModel.getUserId(), dateFmt, "0");
            if (maxOutModel != null) {
                //获取数值
                double totalOut = MoneyDao.getTotalOneDay(db, maxOutModel.getUserId(), dateFmt, "0");
                boolean isInsertMaxSuccess = updateOrInsertTotal_out(db, maxOutModel.getUserId(), dateFmt, totalOut, maxOutModel);
                if (isInsertMaxSuccess) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean updateMaxMoney_in(Context context, MoneyModel moneyModel) {
        try {
            DBOpenHelper dbOpenHelper = DBOpenHelper.getInstance(context);
            SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
            String dateFmt = moneyModel.getDateFmt();
            //支出最大值
            MoneyModel maxInModel = MoneyDao.getMaxOneDay(db, moneyModel.getUserId(), dateFmt, "1");
            if (maxInModel != null) {
                //获取数值
                double totalIn = MoneyDao.getTotalOneDay(db, maxInModel.getUserId(), dateFmt, "1");
                boolean isInsertMaxSuccess = updateOrInsertTotal_in(db, maxInModel.getUserId(), dateFmt, totalIn, maxInModel);
                if (isInsertMaxSuccess) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 更新或者插入一天统计支出的最大值，和总钱数
     *
     * @return
     */
    public static boolean updateOrInsertTotal_out(SQLiteDatabase db, String userId, String dateFmt, double totalMoney, MoneyModel maxModel) {
        try {
            ContentValues values = new ContentValues();
            values.put("userId", maxModel.getUserId());
            values.put("maxOneDayOut_id", maxModel.getId());
            values.put("maxOutName", maxModel.getItemName());
            values.put("maxOutMoney", maxModel.getMoney());
            values.put("totalMoney_out", totalMoney);
            values.put("year", maxModel.getYear());
            values.put("month", maxModel.getMonth());
            values.put("day", maxModel.getDay());
            values.put("timeSeconds", maxModel.getTimeSeconds());
            values.put("dateFmt", dateFmt);
            long affectLines = db.update(DBOpenHelper.tb_total_oneday, values, "userId =? and dateFmt=?", new String[]{userId, dateFmt});
            if (affectLines <= 0) {
                //说明不存在，所以需要插入
                long insertAffectLines = db.insert(DBOpenHelper.tb_total_oneday, null, values);
                if (insertAffectLines <= 0) {
                    return false;
                } else {
                    return true;
                }
            } else {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 更新或者插入一天统计收入的最大值，和总钱数
     *
     * @return
     */
    public static boolean updateOrInsertTotal_in(SQLiteDatabase db, String userId, String dateFmt, double totalMoney, MoneyModel maxModel) {
        try {
            ContentValues values = new ContentValues();
            values.put("userId", maxModel.getUserId());
            values.put("maxOneDayIn_id", maxModel.getId());
            values.put("maxInName", maxModel.getItemName());
            values.put("maxInMoney", maxModel.getMoney());
            values.put("totalMoney_in", totalMoney);
            values.put("year", maxModel.getYear());
            values.put("month", maxModel.getMonth());
            values.put("day", maxModel.getDay());
            values.put("timeSeconds", maxModel.getTimeSeconds());
            values.put("dateFmt", dateFmt);
            long affectLines = db.update(DBOpenHelper.tb_total_oneday, values, "userId =? and dateFmt=?", new String[]{userId, dateFmt});
            if (affectLines <= 0) {
                //说明不存在，所以需要插入
                long insertAffectLines = db.insert(DBOpenHelper.tb_total_oneday, null, values);
                if (insertAffectLines <= 0) {
                    return false;
                } else {
                    return true;
                }
            } else {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 获取分页数据
     *
     * @param offset    第几页
     * @param maxResult 一页几行
     * @return
     */
    public static List<OneDayTotal> getScrollData(Context context, String userId, int offset, int maxResult) {
        Cursor cursor = null;
        List<OneDayTotal> datas = new ArrayList<OneDayTotal>();
        try {
            DBOpenHelper dbOpenHelper = DBOpenHelper.getInstance(context);
            SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
            cursor = db.rawQuery("select m.* from " + DBOpenHelper.tb_total_oneday + " m " + " where m.userId=" + userId + " order by m.timeSeconds desc limit " + maxResult * (offset - 1) + "," + maxResult, null);
            while (cursor.moveToNext()) {
                //循环去根据moneyDetailId去取moneyModel
                OneDayTotal data = new OneDayTotal();
                data.setMaxOneDayIn_id(cursor.getInt(cursor.getColumnIndex("maxOneDayIn_id")));
                data.setMaxOneDayOut_id(cursor.getInt(cursor.getColumnIndex("maxOneDayOut_id")));
                data.setUserId(cursor.getString(cursor.getColumnIndex("userId")));
                data.setId(cursor.getInt(cursor.getColumnIndex("id")));
                data.setYear(cursor.getInt(cursor.getColumnIndex("year")));
                data.setMonth(cursor.getInt(cursor.getColumnIndex("month")));
                data.setDay(cursor.getInt(cursor.getColumnIndex("day")));
                data.setTimeSeconds(cursor.getLong(cursor.getColumnIndex("timeSeconds")));
                data.setMaxInMoney(cursor.getDouble(cursor.getColumnIndex("maxInMoney")));
                data.setMaxInName(cursor.getString(cursor.getColumnIndex("maxInName")));

                data.setMaxOutMoney(cursor.getDouble(cursor.getColumnIndex("maxOutMoney")));
                data.setMaxOutName(cursor.getString(cursor.getColumnIndex("maxOutName")));
                data.setDateFmt(cursor.getString(cursor.getColumnIndex("dateFmt")));
                data.setTotalMoney_out(cursor.getDouble(cursor.getColumnIndex("totalMoney_out")));
                data.setTotalMoney_in(cursor.getDouble(cursor.getColumnIndex("totalMoney_in")));

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


    public static boolean deleteById(Context context, Integer id) {
        try {
            DBOpenHelper dbOpenHelper = DBOpenHelper.getInstance(context);
            SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
            long affectLines = db.delete(DBOpenHelper.tb_total_oneday, "id=?", new String[]{id.toString()});
            if (affectLines <= 0) {
                return false;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteByDateFmt(Context context, String userId, String dateFmt) {
        try {
            DBOpenHelper dbOpenHelper = DBOpenHelper.getInstance(context);
            SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
            long affectLines = db.delete(DBOpenHelper.tb_total_oneday, "userId =? and dateFmt=?", new String[]{userId, dateFmt});
            if (affectLines <= 0) {
                return false;
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

}
