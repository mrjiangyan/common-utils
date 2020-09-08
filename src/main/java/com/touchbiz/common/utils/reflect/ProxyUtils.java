package com.touchbiz.common.utils.reflect;

import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.framework.AopProxy;
import org.springframework.aop.support.AopUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;

/**
 * @author steven jiang
 * @version 1.0.0
 * @create 2018-06-19 下午3:34
 */
public class ProxyUtils {

    public static Object getTarget(Object proxy) throws Exception {
        //java原生代理对象
        if (Proxy.isProxyClass(proxy.getClass())) {

            if (!AopUtils.isCglibProxy(proxy)) {
                return getJdkDynamicProxyTargetObject(proxy);
            } else { //cglib
                return getCglibProxyTargetObject(proxy);
            }
        }
        //spring代理对象
        else if (AopUtils.isAopProxy(proxy)) {
            if (AopUtils.isJdkDynamicProxy(proxy)) {
                return getSpringJdkDynamicProxyTargetObject(proxy);
            } else { //cglib
                return getCglibProxyTargetObject(proxy);
            }
        } else {
            return proxy;
        }
    }


    private static Object getCglibProxyTargetObject(Object proxy) throws Exception {
        Field h = proxy.getClass().getDeclaredField("CGLIB$CALLBACK_0");
        h.setAccessible(true);
        Object dynamicAdvisedInterceptor = h.get(proxy);

        Field advised = dynamicAdvisedInterceptor.getClass().getDeclaredField("advised");
        advised.setAccessible(true);

        return ((AdvisedSupport) advised.get(dynamicAdvisedInterceptor)).getTargetSource().getTarget();
    }


    private static Object getSpringJdkDynamicProxyTargetObject(Object proxy) throws Exception {
        Field h = proxy.getClass().getSuperclass().getDeclaredField("h");
        h.setAccessible(true);
        AopProxy aopProxy = (AopProxy) h.get(proxy);

        Field advised = aopProxy.getClass().getDeclaredField("advised");
        advised.setAccessible(true);

        return ((AdvisedSupport) advised.get(aopProxy)).getTargetSource().getTarget();
    }

    private static Object getJdkDynamicProxyTargetObject(Object proxy) throws Exception {
        Field h = proxy.getClass().getSuperclass().getDeclaredField("h");
        h.setAccessible(true);
        return h.get(proxy);
    }
}
