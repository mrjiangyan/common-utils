package com.touchbiz.common.utils.web;


import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class HttpClientUtilsTest {

    @Test
    public void get() {
    }

    @Test
    public void testGet() {
    }

    @Test
    public void post() {
    }

    @Test
    public void testPost() {
    }

    @Test
    public void postFormData() {
        String url = "http://zfcloud.chinavem.com/operator/paydetail.html";
        Map<String,Object> headers= new HashMap<>();
        headers.put("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        headers.put("Content-Type","application/x-www-form-urlencoded");
        headers.put("Cookie","JSESSIONID=190608932D90991FB82014B712455E0B");

        Map<String,Object> params= new HashMap<>();
        params.put("startTime","2021-06-20 00:00");
        params.put("endTime","2021-06-20 23:00");
        params.put("payType","-1");
        params.put("status","-1");
        params.put("hasDelivery","-1");
        params.put("hasRefund","-1");

        String response = HttpClientUtils.postFormData(url,headers,params);
        System.out.println(response);
    }

    @Test
    public void testPost1() {
    }
}