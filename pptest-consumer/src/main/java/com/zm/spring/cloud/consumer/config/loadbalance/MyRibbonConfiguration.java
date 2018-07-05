package com.zm.spring.cloud.consumer.config.loadbalance;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.netflix.loadbalancer.IPing;
import com.netflix.loadbalancer.IRule;

/**
 * 自定义负载均衡配合类,要在controller中声明使用
 * 返回自定义对象的时候要加上@Bean
 * @author yp-tc-m-7129
 *
 */
@Configuration
public class MyRibbonConfiguration {

	@Bean
	public IRule ribbonRule() {
		//return new RandomRule();
		return new MyRule();
	}
	
	@Bean
	public IPing getPing() {
		return new MyPing();
	}
}
