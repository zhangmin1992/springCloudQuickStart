package com.zm.spring.cloud.pptest_consumer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.zm.provider.entity.Book;

public class CommandHelloWorld  extends HystrixCommand<Book> {

	private RestTemplate restTemplate;
	
	private String name;
	 
	public CommandHelloWorld(String name,RestTemplate restTemplate) {
        //父类构造方法,只需要传入一个GroupKey
        super(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"));
        this.restTemplate = restTemplate;
    }
    
    //真实的方法,在这调用服务等,并返回结果
    @Override
    protected Book run() throws Exception {
        return restTemplate.getForObject("http://HELLO-SERVICE/sayBook", Book.class);
    }
}