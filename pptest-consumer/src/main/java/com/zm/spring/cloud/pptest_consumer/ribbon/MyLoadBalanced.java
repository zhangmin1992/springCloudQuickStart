package com.zm.spring.cloud.pptest_consumer.ribbon;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.beans.factory.annotation.Qualifier;

/**
 * 自定义负载均衡的注解，要spring加载的时候扫描到因此放到和启动类同目录下
 * @Target元注解的作用是为了解释其他注解，描述注解的使用范围
 *    1.CONSTRUCTOR:用于描述构造器
　　　　2.FIELD:用于描述域
　　　　3.LOCAL_VARIABLE:用于描述局部变量
　　　　4.METHOD:用于描述方法
　　　　5.PACKAGE:用于描述包
　　　　6.PARAMETER:用于描述参数
　　　　7.TYPE:用于描述类、接口(包括注解类型) 或enum声明
 * @Qualifier 表示一个接口有多个实现类的时候使用
 * @Reteniton的作用是定义被它所注解的注解保留多久Reteniton的作用是定义被它所注解的注解保留多久,SOURCE（编译器忽略）CLASS（类文件）RUNTIME（运行时）
 * @author yp-tc-m-7129
 *
 */
@Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Qualifier
public @interface MyLoadBalanced {

}
