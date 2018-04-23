package com.zm.provider.redis;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;

@Component
public class BaseCacheKeyGenerator implements KeyGenerator {

	private final Logger logger = Logger.getLogger(RedisCon.class);
	
	@Override
	public Object generate(Object target, Method method, Object... params) {
		 logger.info("开始自定义生成规则");
		 StringBuilder sb = new StringBuilder();
         sb.append(target.getClass().getName());
         sb.append(method.getName());
         for (Object obj : params) {
             sb.append(obj.toString());
         }
         return sb.toString();
	}  

}
