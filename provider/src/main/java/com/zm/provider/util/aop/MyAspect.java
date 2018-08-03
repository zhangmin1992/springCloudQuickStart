package com.zm.provider.util.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义魂村注解，在方法上面加上这话的话可以自动缓存的插入和获取
 * @author yp-tc-m-7129
 *
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MyAspect {

    /**
     * 失效时间
     * @return
     */
    int expireTime() default 3600;
}
