package com.example.administrator.take_two.Util;

import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/9/8.
 */
public final class RegexUtil {
    public static boolean checkMobile(String mobile) {
        String regex = "(\\+\\d+)?1[34578]\\d{9}$";
        return Pattern.matches(regex, mobile);
    }
}
