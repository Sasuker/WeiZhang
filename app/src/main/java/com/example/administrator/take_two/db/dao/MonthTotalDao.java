package com.example.administrator.take_two.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.administrator.take_two.db.DBOpenHelper;
import com.example.administrator.take_two.model.MoneyModel;

/**
 * Created by Administrator on 2016/9/20.
 */
public class MonthTotalDao {
    public static boolean updateMaxMoney_out(Context context, MoneyModel moneyModel) {
        try {
            DBOpenHelper dbOpenHelper = DBOpenHelper.getInstance(context);
            SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
            //支出最大值
            MoneyModel maxOutModel = MoneyDao.getMaxMonth(db, moneyModel.getUserId(), moneyModel.getYear(), moneyModel.getMonth(), "0");
            if (maxOutModel != null) {
                //获取数值
                double totalOut = MoneyDao.getTotalMonth(db, maxOutModel.getUserId(), maxOutModel.getYear(), maxOutModel.getMonth(), "0");
                boolean isInsertMaxSuccess = updateOrInsertTotal_out(db, maxOutModel.getUserId(), moneyModel.getYear(), maxOutModel.getMonth(), totalOut, maxOutModel);
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
            //支出最大值
            MoneyModel maxInModel = MoneyDao.getMaxMonth(db, moneyModel.getUserId(), moneyModel.getYear(), moneyModel.getMonth(), "1");
            if (maxInModel != null) {
                //获取数值
                double totalIn = MoneyDao.getTotalMonth(db, maxInModel.getUserId(), maxInModel.getYear(), maxInModel.getMonth(), "1");
                boolean isInsertMaxSuccess = updateOrInsertTotal_in(db, maxInModel.getUserId(), maxInModel.getYear(), maxInModel.getMonth(), totalIn, maxInModel);
                if (isInsertMaxSuccess) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean updateOrInsertTotal_out(SQLiteDatabase db, String userId, int year, int month, double totalMoney, MoneyModel maxModel) {
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
            values.put("dateFmt", maxModel.getDateFmt());

            long affectLines = db.update(DBOpenHelper.tb_total_month, values, "year =? and month =? and userId=?", new String[]{year + "", month + "", userId});
            if (affectLines <= 0) {
                //说明不存在，所以需要插入
                long insertAffectLines = db.insert(DBOpenHelper.tb_total_month, null, values);
                if (insertAffectLines <= 0) {
                    return false;
                } else {
                    return true;
                }
            } else {
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public static boolean updateOrInsertTotal_in(SQLiteDatabase db, String userId, int year, int month, double totalMoney, MoneyModel maxModel) {
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
            values.put("dateFmt", maxModel.getDateFmt());

            long affectLines = db.update(DBOpenHelper.tb_total_month, values, "year =? and month =? and userId=?", new String[]{year + "", month + "", userId});
            if (affectLines <= 0) {
                //说明不存在，所以需要插入
                long insertAffectLines = db.insert(DBOpenHelper.tb_total_month, null, values);
                if (insertAffectLines <= 0) {
                    return false;
                } else {
                    return true;
                }
            } else {
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public static boolean deleteById(Context context, Integer id) {
        try {
            DBOpenHelper dbOpenHelper = DBOpenHelper.getInstance(context);
            SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
            long affectLines = db.delete(DBOpenHelper.tb_total_month, "id=?", new String[]{id.toString()});
            if (affectLines <= 0) {
                return false;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteByYearMonth(Context context, String userId, int year, int month) {
        try {
            DBOpenHelper dbOpenHelper = DBOpenHelper.getInstance(context);
            SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
            long affectLines = db.delete(DBOpenHelper.tb_total_month, "year=? and month=? and userId=?", new String[]{year + "", month + "", userId});
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
