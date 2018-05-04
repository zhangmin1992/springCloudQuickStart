package com.privider.config;

import org.springframework.context.annotation.Configuration;

import com.netflix.loadbalancer.IRule;

@Configuration
public class MyRibbonConfiguration {

	public IRule ribbonRule() {
		//return new RandomRule();
		return new MyRule();
	}
}
