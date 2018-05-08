package com.zm.spring.cloud.pptest_consumer.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCollapser;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

@Service
public class CallService {

    private RestTemplate restTemplate;
	
	@HystrixCollapser(batchMethod = "sayHelloName", collapserProperties = { @HystrixProperty(name = "timerDelayInMilliseconds", value = "10000") })
    public Future<String> sayHelloNames(String name,RestTemplate restTemplate) {
        // 无需实现，自动帮我们实现收集
        System.out.println("执行单个查询方法...");
        this.restTemplate = restTemplate;
        return null;
    }
	
	/**
	 *真正执行的方法
	 */
	@HystrixCommand
    public List<String> sayHelloName(List<String> names) {
		 List<String> result = new ArrayList<String>();
	     for (String name : names) {
	    	 String temp =  restTemplate.getForEntity("http://HELLO-SERVICE/testZzul?param={1}", String.class,name).getBody();
	    	 result.add(temp);
	     }
	     return result;
	}
}
