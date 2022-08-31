package com.touchbiz.common.utils.validation;

import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author wangchunming
 * @version 1.0
 * @date 2019-05-15 10:38
 */
public class PhoneValidationUtils {

    private PhoneValidationUtils(){}
    /**
     * 只要号码是1开头并且11位即可
     */

    public final static String PHONE_REGEX = "^1\\d{10}$";

    public static boolean validate(String phone) {
        if (StringUtils.isEmpty(phone)) {
            return false;
        }
        Pattern p = Pattern.compile(PHONE_REGEX);
        Matcher m = p.matcher(phone);
        return m.matches();
    }

}
