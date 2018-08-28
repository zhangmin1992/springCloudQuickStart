package com.zm.spring.cloud.pptest_consumer.controller;

import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolProperties;

/**
* @Description:
* @author zhangmin 
* @date 2018年4月9日 下午2:36:49
 */
public class MyHystrixCommand  extends HystrixCommand<String> {
	
	private RestTemplate restTemplate;
	
	private String param;
	 
	public MyHystrixCommand(String param,RestTemplate restTemplate) {
		
        //父类构造方法,只需要传入一个GroupKey
        //super(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"));
		
        super(
                Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"))
                //设置超时时间
                //.andCommandPropertiesDefaults(HystrixCommandProperties.Setter().withExecutionTimeoutInMilliseconds(1000))
                //设置等待队列长度和核心线程数
                .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter().withCoreSize(3).withMaxQueueSize(2))
             );
        this.restTemplate = restTemplate;
        this.param = param;
    }
    
    //真实的方法,在这调用服务等,并返回结果
    @Override
    protected String run() throws Exception {
    	 return restTemplate.getForEntity("http://HELLO-SERVICE/testZzul?param={1}", String.class,param).getBody();
    }
	
}
