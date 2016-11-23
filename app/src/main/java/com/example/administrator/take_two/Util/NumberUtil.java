package com.example.administrator.take_two.Util;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2016/9/20.
 */
public class NumberUtil {
    public static boolean isDouble(String parseString){
        try{
            Double.parseDouble(parseString);
        }catch (Exception e){
            return false;
        }
        return true;
    }

    /**
     *     保留几位
     */
    public static double KeepAfew(double value,int few){
        return new BigDecimal(value).setScale(few, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
