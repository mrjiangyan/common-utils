package com.touchbiz.common.utils.reflect;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 类型转换工具
 *
 * @author wangliyue
 * @version 1.0
 * @created 16/12/19 下午4:22
 **/
@Slf4j
public class TransferUtils {

    private TransferUtils(){}
    /**
     * 从复杂结构列表中抽取出指定字段的列表
     *
     * @param list      源数据列表
     * @param fieldName 欲抽取字段名
     * @param clz       抽取字段类型
     * @return 去重结果
     * @throws NoSuchMethodException, InvocationTargetException, IllegalAccessException
     */
    public static <T> List<T> extractField(List<?> list, String fieldName, Class<T> clz) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if (CollectionUtils.isEmpty(list) || StringUtils.isEmpty(fieldName)) {
            return Collections.emptyList();
        }

        List<T> resultList = new ArrayList<>();
        String methodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        for (Object item : list) {
            Method method = item.getClass().getMethod(methodName);
            Object obj = method.invoke(item);
            if (obj != null && !resultList.contains(obj)) {
                resultList.add((T) obj);
            }
        }
        return resultList;
    }


}