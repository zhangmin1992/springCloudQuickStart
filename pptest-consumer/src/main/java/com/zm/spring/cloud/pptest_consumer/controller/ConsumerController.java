package com.zm.spring.cloud.pptest_consumer.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.Future;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheKey;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheRemove;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheResult;
import com.zm.provider.entity.Book;
import com.zm.provider.entity.Pay;
import com.zm.spring.cloud.pptest_consumer.service.HelloService;

@RestController
/**
* @Description:
* @author zhangmin 
* @date 2018年4月9日 下午2:36:49
 */
public class ConsumerController {
	
	private final Logger logger = Logger.getLogger(ConsumerController.class);
	
    @Autowired
    RestTemplate restTemplate;
    
    @Autowired
    private HelloFeignClient helloFeignClient;
    
    @Autowired
    private HelloService helloService;
    
    /**
     * 测试是否启动成功和多提供者负载均衡日志查看
     * @return
     */
    @RequestMapping(value = "/ribbon-consumer",method = RequestMethod.GET)
    public String helloController() {
        return restTemplate.getForEntity("http://HELLO-SERVICE/hello", String.class).getBody();
    }
    
    /**
     * restTemplate getForEntity-通过服务名调用测试
     * 响应消息体
     * 响应码、
     * contentType、contentLength、
     * @return
     */
    @RequestMapping("/gethello")
    public String getHello() {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity("http://HELLO-SERVICE/hello", String.class);
        String body = responseEntity.getBody();
        HttpStatus statusCode = responseEntity.getStatusCode();
        int statusCodeValue = responseEntity.getStatusCodeValue();
        HttpHeaders headers = responseEntity.getHeaders();
        StringBuffer result = new StringBuffer();
        result.append("responseEntity.getBody()：").append(body).append("<hr>")
                .append("responseEntity.getStatusCode()：").append(statusCode).append("<hr>")
                .append("responseEntity.getStatusCodeValue()：").append(statusCodeValue).append("<hr>")
                .append("responseEntity.getHeaders()：").append(headers).append("<hr>");
        return result.toString();
    }
    
    /**
     * getForEntity请求的时候传递一个参数
     * @return
     */
    @RequestMapping("/sayHello")
    public String sayHello() {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity("http://HELLO-SERVICE/sayHello?name={1}", String.class, "张三");
        return responseEntity.getBody();
    }
    
    /**
     * getForEntity请求的时候传递多个参数
     * @return
     */
    @RequestMapping("/sayNameAndAge")
    public String sayNameAndAge() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("name", "李四");
        map.put("age", "22");
        logger.info("----- ConsumerController sayNameAndAge 进入");
        ResponseEntity<String> responseEntity = restTemplate.getForEntity("http://HELLO-SERVICE/sayNameAndAge?name={name}&age={age}", String.class, map);
        return responseEntity.getBody();
    }
    
    /**
     * post
     * 请求的时候传递多个参数转为map
     * @return
     */
    @RequestMapping("/sayNameAndAge2")
    public String sayNameAndAge2() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("name", "小王");
        map.put("age", "33");
        logger.info("----- ConsumerController sayNameAndAge2 进入");
        ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://HELLO-SERVICE/sayNameAndAge2",map,String.class);
        return responseEntity.getBody();
    }
    
    /**
     * 返回提供者的一个实体.消费者的book实体需要依赖于提供者pom
     * @return
     */
    @RequestMapping("/sayBook")
    public Book sayBook() {
    	logger.info("----- ConsumerController getBook 进入");
    	Book responseEntity = restTemplate.getForObject("http://HELLO-SERVICE/sayBook", Book.class);
        return responseEntity;
    }
    
    /**
     * Hystrix断路器的短路机制
     * 方法上通过@HystrixCommand注解来指定请求失败时回调的方法,跳转方法不能再用@RequestMapping声明了
     * 请求失败(异常，拒绝，超时，短路)时执行fallback(降级)逻辑
     * 如果有异常我们需要直接捕获到抛给用户不是跳转到error方法可以加注解ignoreExceptions = ArithmeticException.class
     * @return
     */
    //@HystrixCommand(fallbackMethod = "error",ignoreExceptions = ArithmeticException.class)
    @HystrixCommand(fallbackMethod = "error")
    @RequestMapping("/testHystrix2")
    public String testHystrix2(String param) {
    	logger.info("进入testHystrix2----方法");
    	return helloService.testZzul(param);
    }
    
    /**
     * 有异常的回调函数,这个备用方法的参数返回值要和上述方法完全一致
     * @param throwable
     * @return
     */
    public String error(String param) {
    	logger.info("发现异常: 请求参数是 " + param);
        return "服务提供者挂机喽!请联系管理员";
    }
    
    /**
     * 测试Hystrix异步调用
     * @return
     * @throws InterruptedException
     * @throws ExecutionException
     * @throws TimeoutException 
     */
    @HystrixCommand
    @RequestMapping("/testHystrixCommand")
    public Book testHystrixCommand() throws InterruptedException, ExecutionException, TimeoutException {
    	logger.info("进入testHystrixCommand方法");
//    	Future<Book> bookFuture = helloService.testHystrixCommand();
//        //return bookFuture.get();
//        //1分钟没有返回则超时时长
//        return bookFuture.get(1, TimeUnit.MINUTES);
    	  Future<Book> bookFuture  = new CommandHelloWorld("World",restTemplate).queue();
    	  return bookFuture.get();
        
    }
    
    /**
     * 测试Hystrix缓存-key自定义
     * @param id
     * @param aa
     * @return
     */
    @CacheResult(cacheKeyMethod = "getCacheKey2")
    @HystrixCommand(commandProperties = {
            @HystrixProperty(name="requestCache.enabled",value = "true")
    })
    @RequestMapping("/testCacheResult")
    public String testCacheResult(Integer id,String aa) {
    	logger.info("进testCacheResult方法");
        return restTemplate.getForEntity("http://HELLO-SERVICE/hello", String.class).getBody();
    }
    
    /**
     * 测试Hystrix缓存参数加注解
     * @param id
     * @param aa
     * @return
     */
    @CacheResult
    @HystrixCommand
    @RequestMapping("/testCacheResult2")
    public String testCacheResult2(@CacheKey Integer id,String aa) {
    	logger.info("进testCacheResult2--方法");
    	ResponseEntity<String> responseEntity = restTemplate.getForEntity("http://HELLO-SERVICE/sayNameAndAge?name={name}&age={age}", String.class, id,aa);
        return responseEntity.getBody();
    }
    
    
    /**
     * 测试Hystrix删除缓存
     * @param id
     * @return
     */
    @CacheRemove(commandKey = "testCacheResult2")
    @HystrixCommand
    @RequestMapping("/testCacheResult3")
    public Book testCacheResult3(@CacheKey Integer id) {
    	logger.info("进testCacheResult3--即将删除testCacheResult2的缓存");
        return null;
    }
    
    /**
     * 缓存自定义key
     * @param id
     * @param aa
     * @return
     */
    public String getCacheKey2(Integer id,String aa) {
    	logger.info("进getCacheKey2方法");
    	return String.valueOf(id)+String.valueOf(aa);
    }
    
    /**
     * 测试Feign无参数调用提供者
     * @return
     */
    @RequestMapping("/testFeign")
    public String testFeign() {
    	logger.info("进入ConsumerController testFeign方法");
    	return helloFeignClient.hello();
    }
    
    /**
     * 测试Feign有参数调用提供者
     * @return
     */
    @RequestMapping("/testFeign2")
    public String testFeign2() {
    	logger.info("进入ConsumerController testFeign2方法");
    	return helloFeignClient.sayNameAndAge("mm", "11");
    }
    
    /**
     * 测试Feign有实体参数调用提供者--get方式
     * @return
     */
    @RequestMapping("/mybatatisgetBook")
    public Book mybatatisgetBook(Long id) {
    	logger.info("进入ConsumerController mybatatisgetBook方法 id={}" + id);
    	return helloFeignClient.mybatatisgetBook(id);
    }
    
    /**
     * 测试Feign有实体参数调用提供者--默认什么方式
     * @return
     */
    @RequestMapping("/testFeign4")
    public Book testFeign4() {
    	logger.info("进入ConsumerController testFeign3方法");
    	Book book = new Book("mm",11,new Date());
    	return helloFeignClient.getBook2(book);
    }
    
    @RequestMapping(value="testZzul")
    public String testZzul(String param) {
    	logger.info("进入ConsumerController testZzul方法 param= " + param);
    	try {
	    	int paramInt = Integer.parseInt(param);
	    	int num = 100 / paramInt;
    	} catch (Exception e) {
    		logger.info("testZzul provider has exception " + e);
    		throw e;
    	}
    	return "ok";
    }
    
    /**
     * 测试传递多个实体参数的问题--
     * 不支持多个@RequestBody实体参数,这个会报错
     * 但是支持1一个@RequestBody参数和多个普通类型参数不会报错
    * @Description: TODO(这里用一句话描述这个方法的作用) 
    * @param @return
    * @return Book    返回类型 
    * @throws
     */
    @RequestMapping("/getBookForObject")
    public Book getBookForObject() {
    	logger.info("进入ConsumerController testFeign3方法");
    	Book book = new Book("mm",11,new Date());
    	Pay pay = new Pay("mm", 11, new Date());
    	//return helloFeignClient.getBookForObject(book, pay);
    	return helloFeignClient.getBookForObject2(book, "1");
    }
}
