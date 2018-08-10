package com.zm.provider.redis.xianliu;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.apache.logging.log4j.core.appender.rolling.action.IfAccumulatedFileCount;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.zm.provider.entity.Pay;
import com.zm.provider.exception.MyException;
import com.zm.provider.util.aop.MyAspect;
import com.zm.provider.util.redis.RedisToolUtils;

@Component
@Aspect
public class RedisRateLimiter {

	@Autowired
	private Environment env;
	
	@Pointcut("execution(* com.zm.provider.service.impl.PayEntityServiceImpl.*(..))")
	public void pointcut() {
		
	}
	
	static boolean acquireTokenFromBucket(String key,int limitCount, int timeout) {
		Long count = RedisToolUtils.incr(key,timeout);
		if(count > limitCount) {
			return false;
		}else {
			return true;
		}
	}
	
	/**
	 * 获取缓存的key key 定义在注解上，支持SPEL表达式
	 * 
	 * @param pjp
	 * @return
	 */
	private String getSelDefKey(ProceedingJoinPoint pjp) {
		// 获取类名-全路径
		String classType = pjp.getTarget().getClass().getName();
		//获取方法名
		String methodName = pjp.getSignature().getName();
		//获取字符串数组的参数
		/*Object[]  paramterArray = (Object[]) pjp.getArgs();
		String paramterStr = Arrays.toString(paramterArray);*/
		//组装唯一key
		String key = classType + "." + methodName;
		return key;
	}
	
	@Around("pointcut()")
	public void redisLimit(ProceedingJoinPoint pjp) throws Throwable {
		Boolean cacheEnable = Boolean.valueOf(env.getProperty("my.limit.isOpen"));
		Boolean isDelete = Boolean.valueOf(env.getProperty("my.limit.isDelete"));
		int limitCount = Integer.valueOf(env.getProperty("my.limit.count"));
		int timeout = Integer.valueOf(env.getProperty("my.limit.time"));
		System.out.println("配置参数"+ cacheEnable+"--"+ limitCount + "--" + timeout);
		
		// 判断是否需要拦截
		if (!cacheEnable) {
			try {
				pjp.proceed();
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		String key = getSelDefKey(pjp);
		Method method = getMethod(pjp);
		RateLimiter rateLimiter = method.getAnnotation(RateLimiter.class);
		if(acquireTokenFromBucket(key,rateLimiter.limit(),rateLimiter.timeout())) {
			pjp.proceed();
		}else {
			if(isDelete) {
				System.out.println("超出次数限制，请求被拒绝");
				throw MyException.REQUEST_NOT_ALLOWED;
			}else {
				//不能被丢弃，放入队列中稍后处理
				System.out.println("超出次数限制，请求被拒绝，数据不能被丢弃");
				Object[] payArray = pjp.getArgs();
				Pay pay = (Pay) payArray[0];
				String value = JSONObject.toJSONString(pay);
				RedisToolUtils.lpush(CommonConstants.REDIS_BOLCK_QUEUE_FOR_PAY, value);
			}
		}
	}
	
	/**
	 * 获取被拦截方法对象
	 * MethodSignature.getMethod() 获取的是顶层接口或者父类的方法对象 而缓存的注解在实现类的方法上
	 * 所以应该使用反射获取当前对象的方法对象
	 */
	public Method getMethod(ProceedingJoinPoint pjp) {
		MethodSignature signature = (MethodSignature) pjp.getSignature();
		Method method = signature.getMethod();
		return method;
	}
}
