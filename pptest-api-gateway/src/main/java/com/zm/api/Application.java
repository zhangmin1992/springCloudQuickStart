package com.zm.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

/**
 * @EnableZuulProxy注解表示开启Zuul的API网关服务
 * SpringBootApplication表示自己是启动类
 * @author yp-tc-m-7129
 *
 */
@EnableZuulProxy
@SpringBootApplication
public class Application {

	 public static void main(String[] args) {
		 SpringApplication.run(Application.class, args);
	 }
	 
	 /**
	  * 注册pre过滤器
	  * @return
	  */
	 @Bean
	 PermisFilter permisFilter() {
	     return new PermisFilter();
	 }
	 
	 /**
	  * 注册统一异常过滤器
	  * @return
	  */
	 @Bean
	 ErrorFilter errorFilter() {
	     return new ErrorFilter();
	 }
}
