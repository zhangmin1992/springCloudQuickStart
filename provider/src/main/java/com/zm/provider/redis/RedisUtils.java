package com.zm.provider.redis;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;


@Component
public class RedisUtils {

	@Autowired  
    private RedisTemplate  redisTemplate; 
	
	@Autowired
	private BaseCacheKeyGenerator baseCacheKeyGenerator;
	
	public boolean set(final String key, Object value) {
		boolean result = false;
		try {
			ValueOperations<Serializable,Object> operations = redisTemplate.opsForValue();
			operations.set(key, value);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public Object get(final String key) {
		Object result = null;
		ValueOperations<Serializable,Object> operations = redisTemplate.opsForValue();
		result = operations.get(key);
		return result;
	}
}
