package com.touchbiz.common.utils.text;

import org.slf4j.helpers.MessageFormatter;

/**
 * 字符串拼接工具类.
 * Created by zhaoyu on 15/5/22.
 */
public class StringSpliceUtils {

    private StringSpliceUtils() {
    }

    /**
     * 字符串拼接.
     *
     * @param pattern 拼接模板
     * @param arr     待拼接内容
     * @return 拼接结果
     */
    public static String splice(String pattern, Object... arr) {
        return MessageFormatter.arrayFormat(pattern, arr).getMessage();
    }

}
