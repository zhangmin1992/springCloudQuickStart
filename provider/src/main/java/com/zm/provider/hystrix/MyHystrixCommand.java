package com.zm.provider.hystrix;

import com.alibaba.fastjson.JSONObject;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.zm.provider.util.HttpClientUtils;

public class MyHystrixCommand  extends HystrixCommand<String>{
	
	private final String name;
	
    public MyHystrixCommand(String name) {
    	//设置要使用的线程池资源名称,否则报错Implicit super constructor HystrixCommand<String>() is undefined. Must explicitly invoke another constructor
        super(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"));
        this.name = name;
    }
    
	@Override
	protected String run() throws Exception {
		String url = "http://127.0.0.1:8081/getProductInfo?productId=" + name;
		String response = HttpClientUtils.sendGetRequest(url);
		return JSONObject.parseObject(response, String.class);  
	}
}
