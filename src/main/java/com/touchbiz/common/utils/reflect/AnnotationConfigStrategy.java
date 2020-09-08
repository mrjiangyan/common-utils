package com.touchbiz.common.utils.reflect;

import lombok.Setter;
import org.springframework.context.ApplicationContext;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

/**
 * 
 *  基于策略模式的annotation配置实现封装类
 * @author rkzhang
 * @param <A> 配置的annotation
 * @param <I> 处理器接口
 * @param <R> 判断值对象
 */
public class AnnotationConfigStrategy<A, I, R> {
	
	private final static Map<String, Set<String>> beanMap = new ConcurrentHashMap<>();
	
	@Setter
	private Predicate<Object> isSub;
	
	@Setter
	private BiPredicate<A, R> compare;
	
	@Setter
	private Class annotationClass;
	
	@Setter
	private String key;
	
	public AnnotationConfigStrategy(Class annotationClass, String key){
		this.annotationClass = annotationClass;
		this.key = key;
	}
	
	public AnnotationConfigStrategy(Class annotationClass, String key, Predicate<Object> isSub, BiPredicate<A, R> compare,ApplicationContext context){
		this.annotationClass = annotationClass;
		this.key = key;
		this.isSub = isSub;
		this.compare = compare;
		init(isSub,context);
	}

	public void init(Predicate<Object> isSub, ApplicationContext context) {
		if(!CollectionUtils.isEmpty(beanMap.get(key))) {
			return;
		}
		beanMap.put(key, new HashSet<>());
		for(String beanName : context.getBeanDefinitionNames()){
			Object obj = context.getBean(beanName);
			if(isSub.test(obj)) {
				Set<String> list = beanMap.get(key);
				list.add(beanName);
			}
		}		
	}
	
	/**
	 * 查询指定处理器
	 * @param val
	 * @return
	 */
	public I lookupProcessor(R val, ApplicationContext context) throws Exception {
		Set<String> processorBeans = beanMap.get(key);
		if(CollectionUtils.isEmpty(processorBeans)) {
			init(isSub,context);
		}
		
		for(String beanName : processorBeans){
			Object obj = context.getBean(beanName);
			A eventConf = (A) ProxyUtils.getTarget(obj).getClass().getAnnotation(annotationClass);

			if(eventConf != null){	
				if(compare.test(eventConf, val)){					
					return (I) obj;
				}
			}
		}
		return null;
	}
	
	public <T> T getAnnotation(Object obj, Class cls) throws Exception {
		return (T) ProxyUtils.getTarget(obj).getClass().getAnnotation(cls);
	}
}
