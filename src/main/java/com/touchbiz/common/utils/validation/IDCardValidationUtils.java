package com.touchbiz.common.utils.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static com.touchbiz.common.utils.date.DateTimeFormat.YYYYMMDD_REGEX;

/**
 * 身份证格式验证工具.
 * Created by zhaoyu on 15/5/20.
 */

@java.lang.SuppressWarnings({"squid:S1873", "squid:S2386"})
public class IDCardValidationUtils {

    private IDCardValidationUtils(){}
    /**
     * 第一代身份证长度.
     */
    public final static int ID_CARD_LEN_1 = 15;
    /**
     * 第二代身份证长度.
     */
    public final static int ID_CARD_LEN_2 = 18;
    /**
     * 每位加权因子.
     */
    public static final int[] POWER = {
            7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2
    };
    /**
     * 第18位校检码.
     */
    public static final String[] VERIFY_CODE = {
            "1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"
    };
    /**
     * 省、直辖市代码表.
     */
    public static final Map<String, String> CITY_CODE_MAP = new HashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(IDCardValidationUtils.class);

    static {
        CITY_CODE_MAP.put("11", "北京");
        CITY_CODE_MAP.put("12", "天津");
        CITY_CODE_MAP.put("13", "河北");
        CITY_CODE_MAP.put("14", "山西");
        CITY_CODE_MAP.put("15", "内蒙古");
        CITY_CODE_MAP.put("21", "辽宁");
        CITY_CODE_MAP.put("22", "吉林");
        CITY_CODE_MAP.put("23", "黑龙江");
        CITY_CODE_MAP.put("31", "上海");
        CITY_CODE_MAP.put("32", "江苏");
        CITY_CODE_MAP.put("33", "浙江");
        CITY_CODE_MAP.put("34", "安徽");
        CITY_CODE_MAP.put("35", "福建");
        CITY_CODE_MAP.put("36", "江西");
        CITY_CODE_MAP.put("37", "山东");
        CITY_CODE_MAP.put("41", "河南");
        CITY_CODE_MAP.put("42", "湖北");
        CITY_CODE_MAP.put("43", "湖南");
        CITY_CODE_MAP.put("44", "广东");
        CITY_CODE_MAP.put("45", "广西");
        CITY_CODE_MAP.put("46", "海南");
        CITY_CODE_MAP.put("50", "重庆");
        CITY_CODE_MAP.put("51", "四川");
        CITY_CODE_MAP.put("52", "贵州");
        CITY_CODE_MAP.put("53", "云南");
        CITY_CODE_MAP.put("54", "西藏");
        CITY_CODE_MAP.put("61", "陕西");
        CITY_CODE_MAP.put("62", "甘肃");
        CITY_CODE_MAP.put("63", "青海");
        CITY_CODE_MAP.put("64", "宁夏");
        CITY_CODE_MAP.put("65", "xinF新疆");
        CITY_CODE_MAP.put("71", "台湾");
        CITY_CODE_MAP.put("81", "香港");
        CITY_CODE_MAP.put("82", "澳门");
        CITY_CODE_MAP.put("91", "国外");
    }

    /**
     * 身份证验证.
     *
     * @param idCard 身份证
     * @return 验证结果
     */
    public static boolean validate(final String idCard) {

        if (null == idCard) {
            return false;
        }

        String date;
        if (ID_CARD_LEN_1 == idCard.length()) {
            // 全数字
            if (isNum(idCard)) {
                return false;
            }
            date = "19" + idCard.substring(6, 12);
        } else if (ID_CARD_LEN_2 == idCard.length()) {
            // 前17位全为数字
            String idCrad17 = idCard.substring(0, ID_CARD_LEN_2 - 1);
            if (isNum(idCrad17)) {
                return false;
            }
            date = idCard.substring(6, 14);
            // 校验位检验
            if (!getCheckCode(getPowerSum(converCharToInt(idCrad17.toCharArray()))).equals(
                    idCard.substring(ID_CARD_LEN_2 - 1))) {
                return false;
            }
        } else {
            return false;
        }

        return CITY_CODE_MAP.containsKey(idCard.substring(0, 2)) && date.matches(YYYYMMDD_REGEX);

    }

    /**
     * 数字验证.
     *
     * @param val 待验证的字符串
     * @return 是否为数字
     */
    public static boolean isNum(String val) {
        return val == null || "".equals(val) || !val.matches("^[0-9]*$");
    }

    /**
     * 将字符数组转换成数字数组.
     *
     * @param ca 字符数组
     * @return 数字数组
     */
    public static int[] converCharToInt(char[] ca) {
        int len = ca.length;
        int[] iArr = new int[len];
        try {
            for (int i = 0; i < len; i++) {
                iArr[i] = Integer.parseInt(String.valueOf(ca[i]));
            }
        } catch (NumberFormatException e) {
            logger.error("IDCardValidationUtils: converCharToInt failure-> {}, stackInfo-> {}", e, e.getStackTrace());
        }
        return iArr;
    }

    /**
     * 将身份证的每位和对应位的加权因子相乘之后，再得到和值.
     *
     * @param iArr
     * @return 身份证编码。
     */
    public static int getPowerSum(int[] iArr) {
        int sum = 0;
        if (POWER.length == iArr.length) {
            for (int i = 0; i < iArr.length; i++) {
                for (int j = 0; j < POWER.length; j++) {
                    if (i == j) {
                        sum = sum + iArr[i] * POWER[j];
                    }
                }
            }
        }
        return sum;
    }

    /**
     * 获取身份证校验位.
     *
     * @param sum 加权之和
     * @return 校验位
     */
    public static String getCheckCode(int sum) {
        return VERIFY_CODE[sum % 11];
    }

    /**
     * 根据身份证号码得到出生日期
     *
     * @param idCard
     * @return 19631001
     */
    public static String getBirthDay(String idCard) {
        if (idCard == null) {
            return null;
        }
        if (ID_CARD_LEN_1 == idCard.length()) {
            return "19" + idCard.substring(6, 12);
        } else if (ID_CARD_LEN_2 == idCard.length()) {
            return idCard.substring(6, 14);
        } else {
            return null;
        }
    }
}
