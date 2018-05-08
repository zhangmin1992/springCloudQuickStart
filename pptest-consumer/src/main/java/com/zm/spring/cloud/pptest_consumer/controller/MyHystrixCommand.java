package com.zm.spring.cloud.pptest_consumer.controller;

import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;

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
		
		//添加了超时时间的命令
        super(
                Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                    .withExecutionTimeoutInMilliseconds(500))
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
