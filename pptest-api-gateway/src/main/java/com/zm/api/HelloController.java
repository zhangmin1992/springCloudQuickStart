package com.zm.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

	@RequestMapping(value="/hello")
	public String hello() {
		return "hello";
	}
	
	@RequestMapping(value="/local")
	public String local() {
		return "local";
	}
}
