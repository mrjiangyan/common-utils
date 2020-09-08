package com.touchbiz.common.utils.web;

import lombok.extern.slf4j.Slf4j;

/**
 * @author ice吴斌 2010-3-24
 */
@Slf4j
public class WebTool {

    /**
     * 判断callback参数是否合法
     * 不合法设定403错误，返回false,  合法返回true
     * jsoncallback为空或null返回true，因为没有callback参数，不违法
     *
     * @param jsoncallback
     * @param res
     * @return
     */
//    public static boolean checkCallbackName(String jsoncallback, HttpServletResponse res) {
//        if (StringUtils.isEmpty(jsoncallback) || StringUtilsEx.isCallbackName(jsoncallback)) {
//            return true;
//        }
//        senderror403(res);
//        return false;
//    }
//
//    private static void senderror403(HttpServletResponse res) {
//        try {
//            res.sendError(HttpServletResponse.SC_FORBIDDEN);
//        } catch (IOException ex) {
//            log.info(StringUtilsEx.ex2Str(ex));
//        }
//    }
}