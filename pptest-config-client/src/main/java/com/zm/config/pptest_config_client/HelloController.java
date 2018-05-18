package com.zm.config.pptest_config_client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 通过注入的Environment对象或者@Value来获取资源文件的值
 * @author yp-tc-m-7129
 *
 */
@RestController
@RefreshScope
public class HelloController {
	
    @Value("${my.name}")
    String myName;
    
    @Value("${name}")
    String name;
    
    @Autowired
    Environment env;

    @RequestMapping("/getMyName")
    public String getMyName() {
        return this.myName;
    }
    @RequestMapping("/getMyName2")
    public String getMyName2() {
        return env.getProperty("name", "未定义");
    }
}
