package com.touchbiz.common.utils.web;

import lombok.extern.slf4j.Slf4j;

import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * @author steven
 */
@Slf4j
public class UrlUtils {

    private UrlUtils(){}

    /**
     * 编码URL
     *
     * @param url
     * @param charset
     * @return
     */
    public static String encodeUrl(String url, String charset) {
        try {
            return URLEncoder.encode(url, charset);
        } catch (Exception e) {
            log.info("URLUtils: encodeUrl error->", e);
            return url;
        }
    }

    /**
     * 解码URL
     *
     * @param url
     * @param charset
     * @return
     */
    public static String decodeUrl(String url, String charset) {
        try {
            return URLDecoder.decode(url, charset);
        } catch (Exception e) {
            log.info("URLUtils: decodeUrl error->", e);
            return url;
        }
    }

    /**
     * 专为页面提供，当url包含http等前缀时，忽略prefix，否则返回prefix + url
     *
     * @param url
     * @param prefix
     * @return
     */
    public static String optionAppend(String url, String prefix) {
        if (url.startsWith("http:") || url.startsWith("https:")) {
            return url;
        } else {
            return prefix + url;
        }
    }
}
