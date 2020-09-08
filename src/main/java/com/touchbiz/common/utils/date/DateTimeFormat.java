package com.touchbiz.common.utils.date;


/**
 * Date Time Utils Class.
 * Created by jiangyan on 15/3/12.
 */
public class DateTimeFormat {

    /**
     * 每天开始时间后缀.
     */
    public static final String START_TIME_SUF = "000000";

    /**
     * 每天开始时间后缀.
     */
    public static final String DEFAULT_FULL_DATE_FORMAT = "yyyy/MM/dd";

    /**
     * 每天结束时间后缀.
     */
    public static final String END_TIME_SUF = "235959";

    /**
     * 每天开始时间后缀 带冒号.
     */
    public static final String START_TIME_SUF_WITH_COLON = "00:00:00";

    /**
     * 每天结束时间后缀 带冒号.
     */
    public static final String END_TIME_SUF_WITH_COLON = "23:59:59";

    /**
     * 日期的默认格式.
     */
    public static final String DEFAULT_DATE_FORMAT = "yyyyMMdd";

    /**
     * yyyyMMddHHmmss日期格式.
     */
    public static final String DATE_FORMAT_YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    /**
     * yyyy-MM-dd HH:mm:ss日期格式.
     */
    public static final String DATE_FORMAT_FULL = "yyyy-MM-dd HH:mm:ss";

    /**
     * MM月dd日时间格式.
     */
    public static final String DATE_FORMAT_MMDD = "MM月dd日";

    /**
     * MM月dd日HH时 时间格式
     */
    public static final String DATE_FORMAT_MMDDHH = "MM月dd日HH时";

    /**
     * MM月dd日HH:MM时间格式.
     */
    public static final String DATE_FORMAT_MMDDHHMM = "MM月dd日HH:mm";

    /**
     * MM-dd HH:MM时间格式
     */
    public static final String DATE_FORMAT_MMDDHHMM_EN = "MM-dd HH:mm";

    /**
     * 一天的毫秒值.
     */
    public static final int MILLISECOND_OF_DAY = 1000 * 60 * 60 * 24;

    /**
     * 一小时的毫秒值.
     */
    public static final int MILLISECOND_OF_HOUR = 1000 * 60 * 60;

    /**
     * 一天的秒值.
     */
    public static final int SECOND_OF_DAY = 60 * 60 * 24;

    /**
     * 秒与毫秒的倍数
     */
    public static final int MILLI = 1000;

    /**
     * 包含横杠的日期字符串格式
     */
    public static final String DATE_FORMAT_WITH_BAR = "yyyy-MM-dd";

    /**
     * 带分钟的时间字符串格式.
     */
    public static final String DATE_FORMAT_WITH_MIN = "yyyy-MM-dd HH:mm";

    /**
     * yyyy-MM-dd格式日期正则表达式
     */
    public final static String YYYY_MM_DD_REGEX = "(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|" +
            "[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|" +
            "(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29)";

    /**
     * yyyyMMdd格式日期正则表达式
     */
    public final static String YYYYMMDD_REGEX = "(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|" +
            "[1-9][0-9]{3})(((0[13578]|1[02])(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)(0[1-9]|[12][0-9]|30))|" +
            "(02(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))0229)";

    /**
     * yyyyMMddHHmmss格式日期正则表达式
     */
    public final static String YYYYMMDDHHMMSS_REGEX = "((([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|" +
            "[1-9][0-9]{3})(((0[13578]|1[02])(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)(0[1-9]|[12][0-9]|30))|" +
            "(02(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))0229))(([01][0-9])|(2[0-3]))([0-5][0-9])([0-5][0-9])";


}
