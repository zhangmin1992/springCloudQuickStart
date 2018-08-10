package com.zm.provider.redis.xianliu;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimiter {
	/**
	 * 次数限制
	 * @return
	 */
    int limit() default 5;
    /**
     * 时间限制毫秒级
     * @return
     */
    int timeout() default 1000;
    /**
     * 超过的请求是否丢弃
     * @return
     */
    boolean isDelete() default false;
}
