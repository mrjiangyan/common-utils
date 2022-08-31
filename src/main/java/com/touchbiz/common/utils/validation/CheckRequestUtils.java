package com.touchbiz.common.utils.validation;


import org.springframework.util.StringUtils;

public class CheckRequestUtils {

    private CheckRequestUtils(){}

    public static String check(Object object1,Object object2){
        String err = ValidateUtils.validateData(object1);
        if(!StringUtils.isEmpty(err)) {
            return "非法参数："+err;
        }
        String err2 = ValidateUtils.validateData(object2);
        if(!StringUtils.isEmpty(err2)) {
            return "非法参数："+err2;
        }
        return null;
    }
}
