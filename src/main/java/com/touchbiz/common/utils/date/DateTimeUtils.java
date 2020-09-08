package com.touchbiz.common.utils.date;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Pattern;

import static com.touchbiz.common.utils.date.DateTimeFormat.*;

/**
 * Date Time Utils Class.
 * Created by jiangyan on 15/3/12.
 */
public class DateTimeUtils {


    /**
     * 将字符串转换为时间格式,时间格式使用yyyyMMdd.
     *
     * @param dateStr 时间字符串
     * @return 转换后的时间
     * @throws ParseException 转换失败抛出异常
     */
    public static Date stringToTimestamp(String dateStr) throws ParseException {
        return stringToTimestamp(dateStr, DEFAULT_DATE_FORMAT);
    }

    /**
     * 将字符串转换为时间格式.
     *
     * @param dateStr    时间字符串
     * @param dateFormat 时间格式字符串
     * @return 转换后的时间
     * @throws ParseException 转换失败抛出异常
     */
    public static Date stringToTimestamp(String dateStr, String dateFormat) throws ParseException {
        return null == dateStr ? null : new Date(new SimpleDateFormat(dateFormat).parse(dateStr).getTime());
    }

    /**
     * 获取从start时间开始的hour小时以后的时间.
     *
     * @param start 开始时间
     * @param hour  增加小时数
     * @return 增加后的时间
     */
    public static Date addHours(Date start, int hour) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);
        calendar.add(Calendar.HOUR,hour);
        return calendar.getTime();
    }

    /**
     * 获取从start时间开始的day天以后的时间.
     *
     * @param start 开始时间
     * @param day   增加天数
     * @return 增加后的时间
     */
    public static Date addDays(Date start, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);
        calendar.add(Calendar.DAY_OF_WEEK,day);
        return calendar.getTime();
    }

    /**
     * 获取从start时间开始的year年以后的时间.
     *
     * @param start 开始时间
     * @param year  增加的年数
     * @return 增加后的时间
     */
    public static Date addYears(Date start, int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);
        calendar.add(Calendar.YEAR,year);
        return calendar.getTime();
    }

    /**
     * 获取当前日期字符串.
     *
     * @return 当前日期字符串
     */
    public static String getTodayStr() {
        return getCurrentTimeStr(DEFAULT_DATE_FORMAT);
    }

    /**
     * 获取当前时间字符串.
     *
     * @param dateFormat 时间格式
     * @return 当前时间字符串
     */
    public static String getCurrentTimeStr(String dateFormat) {
        return new SimpleDateFormat(dateFormat).format(new Date());
    }


    /**
     * 将Date类型的时间转换为字符串类型.
     *
     * @param time 待转换的时间
     * @return 转换后的时间字符串
     */
    public static String timestampToStr(Date time) {
        return timestampToStr(time, DEFAULT_DATE_FORMAT);
    }

    /**
     * 将Date类型的时间转换为字符串类型.
     *
     * @param time       待转换的时间
     * @param dateFormat 时间格式
     * @return 转换后的时间字符串
     */
    public static String timestampToStr(Date time, String dateFormat) {
        return null == time ? null : new SimpleDateFormat(dateFormat).format(time.getTime());
    }

    /**
     * 将Date类型的时间转换为字符串类型.
     *
     * @param date 待转换的时间
     * @return 转换后的时间字符串
     */
    public static String dateToStr(Date date) {
        return dateToStr(date, DEFAULT_DATE_FORMAT);
    }

    /**
     * 将Date类型的时间转换为字符串类型.
     *
     * @param date       待转换的时间
     * @param dateFormat 时间格式
     * @return 转换后的时间字符串
     */
    public static String dateToStr(Date date, String dateFormat) {
        return null == date ? null : new SimpleDateFormat(dateFormat).format(date);
    }

    /**
     * 获取两个时间的时间间隔(单位为为天).
     * 说明:跨天则按天计算.
     *
     * @param start 开始时间
     * @param end   结束时间
     * @return 时间间隔天数
     */
    public static int getIntervalOfDay(Date start, Date end) {

        Calendar c = Calendar.getInstance();
        c.setTime(start);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        return (int) ((end.getTime() - c.getTime().getTime()) / MILLISECOND_OF_DAY);

    }

    /**
     * 两个时间是否是在同一天
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isSameDay(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);

        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
                && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH)
                && cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取两个时间的时间间隔(单位为为天).
     * 说明:计算时如果不足一天,不足部分舍去.
     *
     * @param start      开始时间
     * @param end        结束时间
     * @param dateFormat 时间格式
     * @return 时间间隔天数
     * @throw ParseException    时间转换错误
     */
    public static int getIntervalOfDay(String start, String end, String dateFormat) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        startCalendar.setTime(sdf.parse(start));
        endCalendar.setTime(sdf.parse(end));

        startCalendar.set(Calendar.HOUR_OF_DAY, 0);
        startCalendar.set(Calendar.MINUTE, 0);
        startCalendar.set(Calendar.SECOND, 0);
        startCalendar.set(Calendar.MILLISECOND, 0);

        endCalendar.set(Calendar.HOUR_OF_DAY, 0);
        endCalendar.set(Calendar.MINUTE, 0);
        endCalendar.set(Calendar.SECOND, 0);
        endCalendar.set(Calendar.MILLISECOND, 0);

        return ((int) (endCalendar.getTime().getTime() / MILLI) -
                (int) (startCalendar.getTime().getTime() / MILLI)) /
                SECOND_OF_DAY;

    }

    /**
     * 获取两个时间的时间间隔(单位为为天).
     * 说明:计算时如果不足一天,不足部分舍去.
     *
     * @param start 开始时间
     * @param end   结束时间
     * @return 时间间隔天数
     * @throw ParseException    时间转换错误
     */
    public static int getIntervalOfDay(String start, String end) throws ParseException {
        return getIntervalOfDay(start, end, DEFAULT_DATE_FORMAT);
    }

    /**
     * 验证是否为日期格式.
     *
     * @param dateStr 日期字符串
     * @return 验证结果
     */
    public static boolean isDateTime(String dateStr) {
        return isDateTime(dateStr, YYYYMMDD_REGEX);
    }

    /**
     * 验证是否为日期格式.
     *
     * @param dateStr   日期字符串
     * @param dateRegex 日期正则
     * @return 验证结果
     */
    public static boolean isDateTime(String dateStr, String dateRegex) {
        if (null == dateStr) {
            return false;
        }
        return Pattern.compile(dateRegex).matcher(dateStr).matches();
    }

    /**
     * 判断时间time是否在start与end之间.
     *
     * @param time  待判断的时间
     * @param start 开始时间
     * @param end   结束时间
     * @return 判断结果
     */
    public static boolean between(Date time, Date start, Date end) {
        return (null != time) && start.getTime() <= time.getTime() && end.getTime() >= time.getTime();
    }


    /**
     * 获取当前日期时间
     *
     * @return Date
     */
    public static Date getCurrentDateTime() {
        Calendar cal = Calendar.getInstance();
        return cal.getTime();
    }

    /**
     * 获取当前日期时间
     *
     * @return Date
     */
    public static LocalDateTime getCurrentLocalDateTime() {
        return LocalDateTime.now();
    }

    public static Date parseExcelDate(String dateStr) throws ParseException {
        Date date;
        try {
            date =stringToTimestamp(dateStr, DEFAULT_FULL_DATE_FORMAT);
        }
        catch (Exception e) {
            try {
                date =stringToTimestamp(dateStr, DATE_FORMAT_WITH_BAR);
            } catch (Exception e1) {
                Calendar calendar = new GregorianCalendar(1900, Calendar.JANUARY, -1);
                date = addDays(calendar.getTime(), Integer.parseInt(dateStr));
            }
        }
        if(date == null){
            throw new ParseException("日期解析错误",0);
        }
        return date;
    }

    /***
     * 获取当前是这个月的第几天
     * @param date
     * @return
     */
    public static int getDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    /***
     * 获取这个月有多少天
     * @param date
     * @return
     */
    public static int getMaxDaysWithMonth(Date date) {
        Calendar cDay = Calendar.getInstance();
        cDay.setTime(date);
        cDay.set(Calendar.DAY_OF_MONTH, cDay.getActualMaximum(Calendar.DAY_OF_MONTH));
        return cDay.get(Calendar.DAY_OF_MONTH);
    }

}
