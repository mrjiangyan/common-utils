package com.touchbiz.common.utils.validation;

import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import java.util.Set;

public class ValidateUtils {

	private static final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

	private ValidateUtils(){}

    public static String validateData(Object obj) {
    	Assert.notNull(obj,"对象不能为空");
     
        Set<ConstraintViolation<Object>> validResult = validatorFactory.getValidator().validate(obj);
        StringBuilder sb = new StringBuilder();
        if (!CollectionUtils.isEmpty(validResult)) {
            for (ConstraintViolation<Object> constraintViolation : validResult) {
                if (!StringUtils.isEmpty(constraintViolation.getMessage())) {
                    sb.append("[").append(constraintViolation.getMessage()).append("]");
                } else {
                    sb.append("[").append(constraintViolation.getPropertyPath().toString()).append("不合法").append("]");
                }
            }
        }
        return sb.toString();
    }
}
