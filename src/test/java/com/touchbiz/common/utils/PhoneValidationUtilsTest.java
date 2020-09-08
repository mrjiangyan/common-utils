package com.touchbiz.common.utils;

import com.touchbiz.common.utils.date.LocalDateTimeUtils;
import com.touchbiz.common.utils.validation.PhoneValidationUtils;
import org.junit.Test;

import java.time.LocalDateTime;

public class PhoneValidationUtilsTest {

    @Test
    public void validate() {
        assert PhoneValidationUtils.validate("12345678901");
        assert !PhoneValidationUtils.validate("100000000");
        assert !PhoneValidationUtils.validate(null);
        assert !PhoneValidationUtils.validate("A1");
    }

    @Test
    public void datateimeConvert() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime convert = LocalDateTimeUtils.getDateTime(LocalDateTimeUtils.getPhpTimestampOfDateTime(now));
        assert now.equals(convert);
    }
}