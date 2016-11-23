package com.example.administrator.take_two.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016/9/20.
 */
public class DBOpenHelper extends SQLiteOpenHelper{
    private static int dbVersion = 1;
    private static String dbName = "mags.db";

    public static String tb_money = "tb_money";
    public static String tb_total_oneday = "tb_total_oneday";
    public static String tb_total_month = "tb_total_month";

    public static String tb_charge = "tb_charge";
    public static String tb_charge_child = "tb_charge_child";

    private static DBOpenHelper mInstance = null;

    public synchronized static DBOpenHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DBOpenHelper(context);
        }
        return mInstance;
    };
    private DBOpenHelper(Context context) {
        super(context, dbName, null, dbVersion);//之后会检查数据库是否存在，
        //如果存在看版本号是否相同，如果不同则调用onUpgrade方法
        //如果相同则不做任何修改。如果是第一次创建，则调用onCreate方法
        // "itcast.db"数据库名称，2为版本号
        //存放的位子为：
        //<包>/databases/
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //是在数据库第一次被创建的时候调用的
        //我们可以在初始化的时候创建一个表，也可以之后
        db.execSQL("DROP TABLE IF EXISTS " + tb_money+
                "");
        db.execSQL("CREATE TABLE " + tb_money+
                "(\n" +
                "  id integer primary key autoincrement,\n" +
                "  money double(9,2) NOT NULL,\n" +
                "  remark varchar(255) DEFAULT NULL,\n" +
                "  timeSeconds bigint(20) NOT NULL,\n" +
                "  itemName varchar(255) NOT NULL,\n" +
                "  isOut varchar(255) NOT NULL,\n" +
                "  year integer NOT NULL,\n" +
                "  dateFmt varchar(255) NOT NULL,\n" +
                "  month integer NOT NULL,\n" +
                "  day integer NOT NULL,\n" +
                "  userId varchar(11) NOT NULL\n" +
                ")");

        db.execSQL("DROP TABLE IF EXISTS "+ tb_total_oneday +
                "");
        db.execSQL("CREATE TABLE "+ tb_total_oneday +
                "(\n" +
                "  id integer primary key autoincrement,\n" +
                "  userId varchar(11) NOT NULL,\n" +
                "  maxOneDayIn_id int(11),\n" +
                "  maxOneDayOut_id int(11),\n" +
                "  maxInName varchar(255) DEFAULT NULL,\n" +
                "  maxInMoney double(9,2),\n" +
                "  maxOutName varchar(255) DEFAULT NULL,\n" +
                "  maxOutMoney double(9,2),\n" +
                "  totalMoney_in double(9,2),\n" +
                "  totalMoney_out double(9,2),\n" +

                "  dateFmt varchar(255) NOT NULL,\n" +
                "  year integer NOT NULL,\n" +
                "  month integer NOT NULL,\n" +
                "  day integer NOT NULL,\n" +
                "  timeSeconds bigint(20) NOT NULL\n" +
                ")");



        db.execSQL("DROP TABLE IF EXISTS "+ tb_total_month +
                "");
        db.execSQL("CREATE TABLE "+ tb_total_month +
                "(\n" +
                "  id integer primary key autoincrement,\n" +
                "  userId varchar(11) NOT NULL,\n" +
                "  maxOneDayIn_id int(11),\n" +
                "  maxOneDayOut_id int(11),\n" +
                "  totalMoney double(9,2),\n" +
                "  maxInName varchar(255) DEFAULT NULL,\n" +
                "  maxInMoney double(9,2),\n" +
                "  maxOutName varchar(255) DEFAULT NULL,\n" +
                "  maxOutMoney double(9,2),\n" +
                "  totalMoney_in double(9,2),\n" +
                "  totalMoney_out double(9,2),\n" +

                "  dateFmt varchar(255) NOT NULL,\n" +
                "  year integer NOT NULL,\n" +
                "  month integer NOT NULL,\n" +
                "  day integer NOT NULL,\n" +
                "  timeSeconds bigint(20) NOT NULL\n" +
                ")");


        db.execSQL("DROP TABLE IF EXISTS "+ tb_charge +
                "");
        db.execSQL("CREATE TABLE "+ tb_charge +
                "(\n" +
                "  id integer primary key autoincrement,\n" +
                "  userId varchar(11) NOT NULL,\n" +
                "  totalMoney double(9,2),\n" +
                "  balanceMoney double(9,2),\n" +
                "  name varchar(255) DEFAULT NULL,\n" +
                "  remark varchar(255) DEFAULT NULL" +
                ")");

        db.execSQL("DROP TABLE IF EXISTS "+ tb_charge_child +
                "");
        db.execSQL("CREATE TABLE "+ tb_charge_child +
                "(\n" +
                "  id integer primary key autoincrement,\n" +
                "  chargeParentId integer NOT NULL,\n" +
                "  userId varchar(11) NOT NULL,\n" +
                "  money double(9,2),\n" +
                "  name varchar(255) DEFAULT NULL,\n" +
                "  isOut varchar(255) DEFAULT NULL,\n" +
                "  remark varchar(255) DEFAULT NULL" +
                ")");
        //db.execSQL("CREATE TABLE person(personid integer primary key
        //autoincrement, name varchar(20), phone VARCHAR(12) NULL)");
    }

    //如果存在数据库并且版本号发生改变则调用。
    //这里面我们可以进行对表的结构或者是增加表等动作
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db即我们创建的数据库
    }
}
