package com.example.ddschedule.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 常见的日期格式：
 *
 * 1，日期格式：String dateString = "2017-06-20 10:30:30" 对应的格式：String pattern = "yyyy-MM-dd HH:mm:ss";
 *
 * 2，日期格式：String dateString = "2017-06-20" 对应的格式：String pattern = "yyyy-MM-dd";
 *
 * 3，日期格式：String dateString = "2017年06月20日 10时30分30秒 对应的格式：String pattern = "yyyy年MM月dd日 HH时mm分ss秒";
 *
 * 4，日期格式：String dateString = "2017年06月20日" 对应的格式：String pattern = "yyyy年MM月dd日";
 */
public class DateUtil {

    public static final long OFFSET_TIMESTAMP_BEFORE = 3600000L;
    public static final long OFFSET_TIMESTAMP_AFTER = 172800000L;

    /**
     * 获取系统时间戳
     * @return
     */
    public long getCurTimeLong(){
        long time=System.currentTimeMillis();
        return time;
    }
    /**
     * 获取当前时间
     * @param pattern
     * @return
     */
    public static String getCurDate(String pattern){
        SimpleDateFormat sDateFormat = new SimpleDateFormat(pattern);
        return sDateFormat.format(new java.util.Date());
    }

    /**
     * 时间戳转换成字符窜
     * @param milSecond
     * @param pattern
     * @return
     */
    public static String getDateToString(long milSecond, String pattern) {
        Date date = new Date(milSecond);
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }

    /**
     * 将字符串转为时间戳
     * @param dateString
     * @param pattern
     * @return
     */
    public static long getStringToDate(String dateString, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        Date date = new Date();
        try{
            date = dateFormat.parse(dateString);
        } catch(ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date.getTime();
    }

    public long getOffsetTimestampBefore() {
        long timeNow = System.currentTimeMillis();
        return timeNow - OFFSET_TIMESTAMP_BEFORE;
    }

    public long getOffsetTimestampAfter() {
        long timeNow = System.currentTimeMillis();
        return timeNow + OFFSET_TIMESTAMP_AFTER;
    }
}
