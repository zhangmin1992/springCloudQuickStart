package com.zm.spring.cloud.pptest_consumer.controller;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.zm.provider.entity.Book;

/**
 * 使用feign避免使用restTemplate的统一模板方式
 * 使用了Spring Cloud的“翻译器”后，将不能再使用Feign的默认注解,两种翻译器两者只能二选一，使用fegin的翻译器需要注解@RequestLine
 * @author yp-tc-m-7129
 *
 */
//声明调用的服务名称
@FeignClient("HELLO-SERVICE")
public interface HelloFeignClient {

	@RequestMapping("/hello")
    String hello();
	
	@RequestMapping("/sayNameAndAge")
    String sayNameAndAge(@RequestParam(value ="name") String name,@RequestParam(value ="age") String age);
	
	@RequestMapping(value = "/mybatatisgetBook")
	Book mybatatisgetBook(@RequestParam(value ="id") Long id);
	
	@RequestMapping(value = "/getBook2")
	Book getBook2(@RequestBody Book book);
	
	//错误写法，不支持多个body对象，就像一个html页面只能有一个body对象一样
//	@RequestMapping(value = "/getBookForObject")
//	Book getBookForObject(@RequestBody Book book,@RequestBody Pay pay);
	
	//错误写法
	@RequestMapping(value = "/getBookForObject2")
	Book getBookForObject2(@RequestBody Book book,@RequestParam(value ="id") String id);
}
