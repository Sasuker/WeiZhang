package com.example.administrator.take_two.Util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2016/9/8.
 */
public class CacheUtil {
    public static String PRE_NAME_USER = "pre_name_user";//用户相关
    public static String KEY_USER_ID = "key_user_id";//用户id

    public static void cacheStrValue(Context context, String preName, String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preName, Context.MODE_PRIVATE); //私有数据
        sharedPreferences.edit().putString(key, value).commit();
    }

    public static String getCacheStrValue(Context context, String preName, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preName, Context.MODE_PRIVATE); //私有数据
        return sharedPreferences.getString(key, "");
    }
}
