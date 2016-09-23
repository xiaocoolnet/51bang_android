package cn.xcom.helper.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 尉鑫鑫 on 2016/9/19.
 */
public class DateUtil {
    public static SimpleDateFormat sf = null;

    public static String getCurrentDate() {
        Date d = new Date();
        sf = new SimpleDateFormat("yyyy年MM月dd日");
        return sf.format(d);
    }

    /*时间戳转换成字符窜*/
    public static String getDateToString(long time) {
        Date d = new Date(time);
        sf = new SimpleDateFormat("yyyy年MM月dd日hh时mm分ss秒");
        return sf.format(d);
    }

    /*将字符串转为时间戳*/
    public static long getStringToDate(String time) {
        sf = new SimpleDateFormat("yyyy年MM月dd日");
        Date date = new Date();
        try{
            date = sf.parse(time);
        } catch(ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date.getTime();
    }
}
