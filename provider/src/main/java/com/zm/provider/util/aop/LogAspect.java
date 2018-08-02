package com.zm.provider.util.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

import com.alibaba.fastjson.JSONObject;
import com.zm.provider.util.redis.RedisToolUtils;

@Aspect
public class LogAspect {

	@Pointcut("execution(String com.zm.provider.service.impl.TestInsertServiceImpl.getCacheStr())")
	public void pointcut() {
		
	}
	
	@Before("pointcut()")
	public void logStart(JoinPoint joinPoint) {
		String key = joinPoint.getSignature().toString();
		Object result = RedisToolUtils.get(key);
		if(result == null) {
			System.out.println("-----");
		}
		
		String[] nameArray = joinPoint.getSignature().toString().split(" ");
		String name = nameArray[1];
		int num = name.lastIndexOf(".");
		name = name.substring(0, num);
		System.out.println("--------logStart------类地址--"+name +"--方法名-"+joinPoint.getSignature().getName()+"---参数是"+JSONObject.toJSONString(joinPoint.getArgs()));
	}
	
	@After("pointcut()")
	public void logAfter() {
		System.out.println("--------logAfter---------");
	}
	
	@AfterReturning(value="pointcut()",returning="result")
	public void logReturn(JoinPoint joinPoint,Object result) {
		System.out.println("--------logReturn--------方法返回值是:"+result);
		String key = joinPoint.getSignature().toString();
		RedisToolUtils.set(key,(String)result, "NX", "PX", 8000 * 60);
	}
	
	@AfterThrowing(throwing="exception",value="pointcut()")
	public void logException(Exception exception) {
		System.out.println("--------logException----异常是-----"+exception.getMessage());
	}
}
