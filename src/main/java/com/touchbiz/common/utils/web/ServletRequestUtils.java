//package com.mrjiangyan@hotmail.com.common.utils.web;
//
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//import javax.servlet.ServletRequest;
//import javax.servlet.http.HttpServletRequest;
//import java.util.Objects;
//
///**
// * 提供ServletRequest操作的方法
// */
//public class ServletRequestUtils {
//
//    public static HttpServletRequest getRequest() {
//        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
//    }
//
//    /**
//     * 从Request中获取参数，且可以指定默认值
//     *
//     * @param request
//     * @param name
//     * @param defaultVal
//     * @return
//     */
//    public static String[] getStringParameter(ServletRequest request,
//                                              String name, String defaultVal) {
//        String[] results = request.getParameterValues(name);
//        if (results != null) {
//            for (int i = 0; i < results.length; i++) {
//                results[i] = results[i] == null ? "" : results[i].trim();
//                if ("".equals(results[i])) {
//                    results[i] = defaultVal;
//                }
//            }
//        }
//        return results;
//    }
//
//    /**
//     * 从request中获取单个参数，可以指定默认值
//     */
//    public static String getSingleStringParameter(ServletRequest request,
//                                                  String name, String defaultVal) {
//        String[] results = getStringParameter(request, name, defaultVal);
//        if (results != null) {
//            return results[0];
//        } else {
//            return null;
//        }
//    }
//}
