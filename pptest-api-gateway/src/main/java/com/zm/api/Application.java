package com.zm.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

/**
 * @EnableZuulProxy表示自己是网关
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
	 
	 @Bean
	 PermisFilter permisFilter() {
	     return new PermisFilter();
	 }
}
