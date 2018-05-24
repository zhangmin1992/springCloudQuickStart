package com.zm.config.pptest_config_client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Configuration
public class ClientConfig {

	@Autowired
    Environment env;
	 
	//name
	@Value("${name}")
	private String name;
	
	public String getName() {
		return this.name;
	}
	
	//my.receive.mail.list
	@Value("${my.receive.mail.list}")
	private String mailName;
	
	//name获取的方式二
	public String getReceiveMailList() {
		System.out.println(this.mailName);
	    return env.getProperty("my.receive.mail.list", "未定义");
	}
}
