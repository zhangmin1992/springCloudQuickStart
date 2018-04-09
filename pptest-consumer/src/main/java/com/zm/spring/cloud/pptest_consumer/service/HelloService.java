package com.zm.spring.cloud.pptest_consumer.service;

import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.command.AsyncResult;
import com.zm.provider.entity.Book;

@Service
public class HelloService {
	
    @Autowired
    private RestTemplate restTemplate;
    
    public String testZzul(String param) {
        return restTemplate.getForEntity("http://HELLO-SERVICE/testZzul?param={1}", String.class,param).getBody();
    }
    
    @HystrixCommand
    public Future<Book> testHystrixCommand() {
        return new AsyncResult<Book>() {
            @Override
            public Book invoke() {
                return restTemplate.getForObject("http://HELLO-SERVICE/sayBook", Book.class);
            }
        };
    }
}
