package com.touchbiz.common.utils.date;


import java.text.ParseException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

/**
 * Date Time Utils Class.
 * Created by jiangyan on 15/3/12.
 */
public class LocalDateTimeUtils {


    /**
     * 将字符串转换为时间格式,时间格式使用yyyyMMdd.
     *
     * @param dateStr 时间字符串
     * @return 转换后的时间
     * @throws ParseException 转换失败抛出异常
     */
    public static LocalDateTime stringToTimestamp(String dateStr) throws ParseException {
        return stringToTimestamp(dateStr, DateTimeFormat.DEFAULT_DATE_FORMAT);
    }

    /**
     * 将字符串转换为时间格式.
     *
     * @param dateStr    时间字符串
     * @param dateFormat 时间格式字符串
     * @return 转换后的时间
     */
    public static LocalDateTime stringToTimestamp(String dateStr, String dateFormat) {
        return LocalDateTime.parse(dateStr,DateTimeFormatter.ofPattern(dateFormat));
    }


    /**
     * 获取当前日期字符串.
     *
     * @return 当前日期字符串
     */
    public static String getTodayStr() {
        return getCurrentTimeStr(DateTimeFormat.DEFAULT_DATE_FORMAT);
    }

    /**
     * 获取当前时间字符串.
     *
     * @param dateFormat 时间格式
     * @return 当前时间字符串
     */
    public static String getCurrentTimeStr(String dateFormat) {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(dateFormat));
    }


    /**
     * 将Date类型的时间转换为字符串类型.
     *
     * @param time       待转换的时间
     * @param dateFormat 时间格式
     * @return 转换后的时间字符串
     */
    public static String dateToStr(LocalDateTime time, String dateFormat) {
        return null == time ? null :  time.format(DateTimeFormatter.ofPattern(dateFormat));
    }

    /**
     * 将Date类型的时间转换为字符串类型.
     *
     * @param date 待转换的时间
     * @return 转换后的时间字符串
     */
    public static String dateToStr(LocalDateTime date) {
        return dateToStr(date, DateTimeFormat.DEFAULT_DATE_FORMAT);
    }


    /**
     * 获取两个时间的时间间隔(单位为为天).
     * 说明:跨天则按天计算.
     *
     * @param start 开始时间
     * @param end   结束时间
     * @return 时间间隔天数
     */
    public static long getIntervalOfDay(LocalDateTime start, LocalDateTime end) {
        Duration duration = java.time.Duration.between(start, end);
        return duration.toDays();
    }

    /**
     * 两个时间是否是在同一天
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isSameDay(LocalDateTime date1, LocalDateTime date2) {
        return date1.toLocalDate().isEqual(date2.toLocalDate());
    }



    /**
     * 验证是否为日期格式.
     *
     * @param dateStr 日期字符串
     * @return 验证结果
     */
    public static boolean isDateTime(String dateStr) {
        return isDateTime(dateStr, DateTimeFormat.YYYYMMDD_REGEX);
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
    public static boolean between(LocalDateTime time, LocalDateTime start, LocalDateTime end) {
        return (start.isBefore(time) || start.equals(time)) && (end.isAfter(time) && end.equals(time));
    }

    public static LocalDateTime parseExcelDate(String dateStr) throws ParseException {
        LocalDateTime date;
        try {
            date =stringToTimestamp(dateStr, DateTimeFormat.DEFAULT_FULL_DATE_FORMAT);
        }
        catch (Exception e) {
            try {
                date =stringToTimestamp(dateStr, DateTimeFormat.DATE_FORMAT_WITH_BAR);
            } catch (Exception e1) {
                date = LocalDateTime.of(1900, 1, 1,0,0);
                date.plusDays(Integer.parseInt(dateStr));
            }
        }
        if(date == null){
            throw new ParseException("日期解析错误",0);
        }
        return date;
    }


    /**
     * 将日期时间转换成PHP时间戳的形式（Php为10位只精确到秒）
     *
     * @param phpTime php时间
     * @return 1970年1月1日开始的时间戳
     */
    public static LocalDateTime getDateTime(Integer phpTime) {
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = Instant.ofEpochMilli((long)phpTime*1000);
        return instant.atZone(zone).toLocalDateTime();
    }

}
