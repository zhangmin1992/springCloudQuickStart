package com.zm.spring.cloud.pptest_consumer.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.HystrixCollapser;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;

/**
 * 请求合并类
 * 继承的HystrixCollapser类的三个泛型是
 * BatchReturnType：createCommand()方法创建批量命令的返回值的类型。 
- ResponseType：单个请求返回的类型。 
- RequestArgumentType：getRequestArgument()方法请求参数的类型。
 * @author yp-tc-m-7129
 *
 */
public class StringCommandCollapser extends HystrixCollapser<List<String>, String, String> {

    private RestTemplate restTemplate;
	
	private final String name;
	
	public StringCommandCollapser(String key,RestTemplate restTemplate) {
        this.name = key;
        this.restTemplate = restTemplate;
    }

	/**
	 * 该函数用来定义获取请求参数的方法。
	 */
	@Override
	public String getRequestArgument() {
		return name;
	}

	/**
	 * 合并请求产生批量命令的具体实现方法
	 */
	@Override
	protected HystrixCommand<List<String>> createCommand(
			Collection<com.netflix.hystrix.HystrixCollapser.CollapsedRequest<String, String>> requests) {
		return new BatchCommand(requests);
	}

	/**
	 * 批量命令结果返回后的处理，这里需要实现将批量结果拆分并传递给合并前的各个原子请求命令的逻辑
	 */
	@Override
	protected void mapResponseToRequests(List<String> batchResponse,Collection<com.netflix.hystrix.HystrixCollapser.CollapsedRequest<String, String>> requests) {
		int count = 0;
        for (CollapsedRequest<String, String> request : requests) {
            request.setResponse(batchResponse.get(count++));
        }
	}
	
	private class BatchCommand extends HystrixCommand<List<String>> {
        
		private final Collection<CollapsedRequest<String, String>> requests;
        
		private BatchCommand(Collection<CollapsedRequest<String, String>> requests) {
			
                super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"))
                    .andCommandKey(HystrixCommandKey.Factory.asKey("GetValueForKey")));
            this.requests = requests;
        }
        
        //内部还是遍历入参进行调用
        @Override
        protected List<String> run() {
            ArrayList<String> response = new ArrayList<String>();
            for (CollapsedRequest<String, String> request : requests) {
        		String temp = restTemplate.getForEntity("http://HELLO-SERVICE/testZzul?param={1}", String.class,request.getArgument()).getBody();
        		response.add("en" + temp);
            }
            return response;
        }
    }


}
