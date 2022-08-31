package com.touchbiz.common.utils.exception;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

/**
 * @author wangliyue
 * @version 1.0
 * @created 16/8/11 下午10:45
 **/
@Slf4j
public class StackTraceUtils {

    /**
     * debug时,调用该方法,将打印出完整调用栈,方便排查问题
     */
    public static void printStackTrace() {
        StackTraceElement[] traceElements = Thread.currentThread().getStackTrace();
        for (StackTraceElement traceElement : traceElements) {
            log.info("{}", traceElement);
        }
    }



    /**
     * 獲取完整堆棧信息
     */
    public static String getStackTrace(Throwable e) {
        StackTraceElement[] traceElements = e.getStackTrace();
        StringBuilder stringBuilder = new StringBuilder(512);
        for (StackTraceElement traceElement : traceElements) {
            stringBuilder.append(traceElement.toString()).append("\n");
        }
        return stringBuilder.toString();
    }


    /**
     * 输出完整的异常堆栈信息
     */
    public static String logErrorStackMsg(Logger log, Throwable e) {
        StackTraceElement[] aStackTraceElements = e.getStackTrace();
        StringBuilder errorInfo = new StringBuilder();
        errorInfo.append(e.getMessage()).append("\n");
        for (StackTraceElement tStackTraceElement : aStackTraceElements) {
            errorInfo.append(tStackTraceElement.toString()).append("\n");
        }
        log.error(errorInfo.toString());
        return errorInfo.toString();
    }

}