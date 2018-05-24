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
	
	/**
	 * @Value获取指定资源文件的值
	 */
    @Value("${my.name}")
    String myName;
    
    @RequestMapping("/getMyName")
    public String getMyName() {
        return this.myName;
    }
    
    /**
     * Environment方式获取指定资源文件的值
     */
    @Autowired
    Environment env;
    
    @RequestMapping("/getMyName2")
    public String getMyName2() {
        return env.getProperty("name", "未定义");
    }
    
    @RequestMapping("/getMeName")
    public String getMeName() {
        return env.getProperty("me.name", "义");
    }
    
    /**
     * 写一个common组件获取所有dev资源文件的配置
     */
    @Autowired
	ClientConfig clientConfig;
    
    @RequestMapping("/getMyName3")
    public String getMyName3() {
        return clientConfig.getReceiveMailList();
    }
}
