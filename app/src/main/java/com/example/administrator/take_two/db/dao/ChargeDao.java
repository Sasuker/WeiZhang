package com.example.administrator.take_two.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.administrator.take_two.db.DBOpenHelper;
import com.example.administrator.take_two.model.ChargeModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/26.
 */
public class ChargeDao {
    public static void insert(Context context, ChargeModel chargeModel) {
        try {
            DBOpenHelper dbOpenHelper = DBOpenHelper.getInstance(context);;
            SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("userId", chargeModel.getUserId());
            values.put("totalMoney", chargeModel.getTotalMoney());
            values.put("balanceMoney", chargeModel.getBalanceMoney());
            values.put("remark", chargeModel.getRemark());
            values.put("name", chargeModel.getName());
            //插入
            db.insert(DBOpenHelper.tb_charge, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取最后插入的id
     * @param context
     * @return
     */
    public int getLastInsertId(Context context){
        DBOpenHelper dbOpenHelper = DBOpenHelper.getInstance(context);;
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select last_insert_rowid()",null);
        if(cursor.moveToFirst())
            return cursor.getInt(0);
        return -1;
    }
    /**
     * 获取分页数据
     *
     * @param offset    第几页
     * @param maxResult 一页几行
     * @param userId    第几页
     * @return
     */
    public static List<ChargeModel> getScrollData(Context context, String userId, int offset, int maxResult) {
        Cursor cursor = null;
        List<ChargeModel> datas = new ArrayList<ChargeModel>();
        try{
            DBOpenHelper dbOpenHelper = DBOpenHelper.getInstance(context);;
            SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
            cursor = db.rawQuery("select * from " + DBOpenHelper.tb_charge + " m where m.userId=? order by m.id desc limit "+ maxResult*(offset-1) + "," + maxResult, new String[]{userId});
            while (cursor.moveToNext()) {
                ChargeModel data = new ChargeModel();
                data.setUserId(cursor.getString(cursor.getColumnIndex("userId")));
                data.setId(cursor.getInt(cursor.getColumnIndex("id")));
                data.setTotalMoney(cursor.getDouble(cursor.getColumnIndex("totalMoney")));
                data.setBalanceMoney(cursor.getDouble(cursor.getColumnIndex("balanceMoney")));
                data.setRemark(cursor.getString(cursor.getColumnIndex("remark")));
                data.setName(cursor.getString(cursor.getColumnIndex("name")));
                datas.add(data);
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(cursor != null){
                cursor.close();
            }
        }
        return datas;
    }

    public static boolean deleteById(Context context,Integer id) {
        DBOpenHelper dbOpenHelper = DBOpenHelper.getInstance(context);;
        try {
            SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
            long affectLines = db.delete(DBOpenHelper.tb_charge, "id = ?", new String[]{id.toString()});
            if (affectLines <= 0) {
                return false;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean update(Context context, ChargeModel chargeModel) {
        DBOpenHelper dbOpenHelper = DBOpenHelper.getInstance(context);;
        try {
            SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("userId", chargeModel.getUserId());
            values.put("totalMoney", chargeModel.getTotalMoney());
            values.put("balanceMoney", chargeModel.getBalanceMoney());
            values.put("remark", chargeModel.getRemark());
            values.put("name", chargeModel.getName());
            long affectLines = db.update(DBOpenHelper.tb_charge, values, "id=?", new String[]{chargeModel.getId() + ""});
            if (affectLines <= 0) {
                return false;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static double getTotalMoney(Context context, String userId,Integer id) {

        Cursor cursor = null;
        try{

            DBOpenHelper dbOpenHelper = DBOpenHelper.getInstance(context);;
            SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
            cursor = db.rawQuery("select m.totalMoney as sumMoney from " + DBOpenHelper.tb_charge + " m where m.id =? and m.userId=?", new String[]{id.toString(), userId});
            if (cursor.moveToFirst()) {
                return cursor.getDouble(cursor.getColumnIndex("sumMoney"));
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(cursor != null){
                cursor.close();
            }
        }
        return 0;
    }
}
