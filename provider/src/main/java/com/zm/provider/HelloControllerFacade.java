package com.zm.provider;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.zm.provider.entity.Book;

public interface HelloControllerFacade {

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
	
	@RequestMapping("/timeOut")
	String timeOut();
}
