package com.zm.spring.cloud.pptest_consumer.controller;

import org.apache.log4j.Logger;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.zm.provider.entity.Book;

public class CommandHelloWorld  extends HystrixCommand<String> {

	private final Logger logger = Logger.getLogger(CommandHelloWorld.class);
	
	private RestTemplate restTemplate;
	
	private String name;
	 
	public CommandHelloWorld(String name,RestTemplate restTemplate) {
        //父类构造方法,只需要传入一个GroupKey
        super(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"));
        this.restTemplate = restTemplate;
        this.name = name;
    }
    
    //真实的方法,在这调用服务等,并返回结果
    @Override
    protected String run() throws Exception {
    	logger.info("准备执行run方法调用服务提供者");
        return restTemplate.getForObject("http://HELLO-SERVICE/sayHello?name={1}", String.class,name);
    }
    
    @Override
    protected String getFallback() {
        Throwable executionException = getExecutionException();
        System.out.println(executionException.getMessage());
        return "发生异常!";
    }
    
    /**
     * 在run方法之前执行参数一致直接返回缓存的结果
     */
    @Override
    protected String getCacheKey() {
    	logger.info("检查参数name="+name);
        return String.valueOf(name);
    }
}