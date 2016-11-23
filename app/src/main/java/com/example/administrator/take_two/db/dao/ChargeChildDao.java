package com.example.administrator.take_two.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.administrator.take_two.db.DBOpenHelper;
import com.example.administrator.take_two.model.ChargeChildModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/26.
 */
public class ChargeChildDao {
    public static void insert(Context context, ChargeChildModel chargeChildModel) {
        try {
            DBOpenHelper dbOpenHelper = DBOpenHelper.getInstance(context);
            SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("userId", chargeChildModel.getUserId());
            values.put("chargeParentId", chargeChildModel.getChargeParentId());
            values.put("money", chargeChildModel.getMoney());
            values.put("remark", chargeChildModel.getRemark());
            values.put("name", chargeChildModel.getName());
            values.put("isOut", chargeChildModel.getIsOut());
            //插入
            db.insert(DBOpenHelper.tb_charge_child, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取分页数据
     *
     * @param offset    第几页
     * @param maxResult 一页几行
     * @param userId    第几页
     * @return
     */
    public static List<ChargeChildModel> getScrollData(Context context, String userId, Integer chargeParentId, int offset, int maxResult) {
        Cursor cursor = null;
        List<ChargeChildModel> datas = new ArrayList<ChargeChildModel>();
        try {
            DBOpenHelper dbOpenHelper = DBOpenHelper.getInstance(context);
            SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
            cursor = db.rawQuery("select * from " + DBOpenHelper.tb_charge_child + " m where m.userId=? and m.chargeParentId=? order by m.id desc limit " + maxResult * (offset - 1) + "," + maxResult, new String[]{userId, chargeParentId.toString()});
            while (cursor.moveToNext()) {
                ChargeChildModel data = new ChargeChildModel();
                data.setUserId(cursor.getString(cursor.getColumnIndex("userId")));
                data.setId(cursor.getInt(cursor.getColumnIndex("id")));
                data.setChargeParentId(cursor.getInt(cursor.getColumnIndex("chargeParentId")));
                data.setMoney(cursor.getDouble(cursor.getColumnIndex("money")));
                data.setRemark(cursor.getString(cursor.getColumnIndex("remark")));
                data.setName(cursor.getString(cursor.getColumnIndex("name")));
                data.setIsOut(cursor.getString(cursor.getColumnIndex("isOut")));
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
            long affectLines = db.delete(DBOpenHelper.tb_charge_child, "id = ?", new String[]{id.toString()});
            if (affectLines <= 0) {
                return false;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteByParentId(Context context, Integer parentId) {
        try {
            DBOpenHelper dbOpenHelper = DBOpenHelper.getInstance(context);
            SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
            long affectLines = db.delete(DBOpenHelper.tb_charge_child, "chargeParentId = ?", new String[]{parentId.toString()});
            if (affectLines <= 0) {
                return false;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean update(Context context, ChargeChildModel chargeChildModel) {
        try {
            DBOpenHelper dbOpenHelper = DBOpenHelper.getInstance(context);
            SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("userId", chargeChildModel.getUserId());
            values.put("chargeParentId", chargeChildModel.getChargeParentId());
            values.put("money", chargeChildModel.getMoney());
            values.put("remark", chargeChildModel.getRemark());
            values.put("name", chargeChildModel.getName());
            values.put("isOut", chargeChildModel.getIsOut());
            long affectLines = db.update(DBOpenHelper.tb_charge_child, values, "id=?", new String[]{chargeChildModel.getId() + ""});
            if (affectLines <= 0) {
                return false;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取总钱数，
     *
     * @param context
     * @param userId
     * @param isOut   输出/收入
     * @return
     */
    public static double getTotal(Context context, String userId, Integer parentId, String isOut) {
        Cursor cursor = null;
        try {
            DBOpenHelper dbOpenHelper = DBOpenHelper.getInstance(context);
            SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
            cursor = db.rawQuery("select SUM(m.money) as sumMoney from " + DBOpenHelper.tb_charge_child + " m where m.chargeParentId =? and m.userId=? and m.isOut=? ", new String[]{parentId.toString(), userId, isOut});
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
}
