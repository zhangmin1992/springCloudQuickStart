package com.zm.provider.util.aop;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.zm.provider.util.redis.RedisToolUtils;
/**
 * 注意除了标记切面外，还要标记切面被spring容器加载
 * 注意pointcut小写
 * @author yp-tc-m-7129
 *
 */
@Aspect
@Component
public class LogAspect {

	@Pointcut("execution(* com.zm.provider.service.impl.TestInsertServiceImpl.*(..))")
	public void pointcut() {
		
	}
	
	@Around("pointcut()")
	public Object cache(ProceedingJoinPoint pjp) throws Throwable {
		Object result = null;
		Boolean cacheEnable = true;
		// 判断是否开启缓存,否直接执行目标方法
		if (!cacheEnable) {
			try {
				result = pjp.proceed();
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		/**
		 * 把方法所在全路径+方法名+参数作为一个关键key，不会重复
		 */
		String key = getSelDefKey(pjp);
		
		//获取注解参数
		Method method = getMethod(pjp);
		MyAspect myAspect = method.getAnnotation(MyAspect.class);
		
		// 获取方法的返回类型,让缓存可以返回正确的类型
		Class returnType = ((MethodSignature) pjp.getSignature()).getReturnType();
		result = RedisToolUtils.get(key);
		if (result == null) {
			result = pjp.proceed();
			if(result !=null){
				//只有数据库返回对象才会存储缓存，避免无用的null字符串对象遭受攻击造成redis服务器的内存移除
				String temp = com.alibaba.fastjson.JSONObject.toJSONString(result);
				RedisToolUtils.setex(key, myAspect.expireTime(), temp);
			}
		} else {
			//找到直接返回缓存的结果，转为可用的类型
			result = JSONObject.parseObject((String) result, returnType);
		}
		return result;
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
		Object[]  paramterArray = (Object[]) pjp.getArgs();
		String paramterStr = Arrays.toString(paramterArray);
		//组装唯一key
		String key = classType + "." + methodName + "." + paramterStr;
		return key;
	}
	
	/**
	 * 获取被拦截方法对象
	 * 
	 * MethodSignature.getMethod() 获取的是顶层接口或者父类的方法对象 而缓存的注解在实现类的方法上
	 * 所以应该使用反射获取当前对象的方法对象
	 */
	public Method getMethod(ProceedingJoinPoint pjp) {
		// 获取参数的类型
		/*Object[] args = pjp.getArgs();
		Class[] argTypes = new Class[pjp.getArgs().length];
		for (int i = 0; i < args.length; i++) {
			argTypes[i] = args[i].getClass();
		}*/
		MethodSignature signature = (MethodSignature) pjp.getSignature();
		Method method = signature.getMethod();
		return method;

	}

}
