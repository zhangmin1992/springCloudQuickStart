package com.zm.spring.cloud.pptest_consumer.ribbon;
import java.net.URI;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;

/**
 * 自定义的请求类，重写了getHeaders() getMethod() getURI()方法，实际上只重写了返回url方法
 * @author 杨恩雄
 *
 */
public class MyHttpRequest implements HttpRequest {

	/**
	 * 自定义的本请求类的对象
	 */
	private HttpRequest sourceRequest;
	
	public MyHttpRequest(HttpRequest sourceRequest) {
		this.sourceRequest = sourceRequest;
	}

	@Override
	public HttpHeaders getHeaders() {
		return sourceRequest.getHeaders();
	}

	@Override
	public HttpMethod getMethod() {
		return sourceRequest.getMethod();
	}

	/**
	 * 默认返回provider的这个url,如果有异常就返回没重写之前的url
	 */
	@Override
	public URI getURI() {
		try {
			URI newUri = new URI("http://localhost:8019/testMyInterceptor");
			return newUri;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sourceRequest.getURI();
	}
	

}
