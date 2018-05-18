package com.zm.spring.cloud.pptest_consumer.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.HystrixCircuitBreaker;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheKey;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheRemove;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheResult;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import com.zm.provider.entity.Book;
import com.zm.provider.entity.Pay;
import com.zm.spring.cloud.pptest_consumer.service.CallService;
import com.zm.spring.cloud.pptest_consumer.service.HelloService;

@RestController
//@RibbonClient(name="HELLO-SERVICE", configuration=MyRibbonConfiguration.class)
/**
 * @RestController 不同于controller，只能返回数据不能返回页面
 * @RibbonClient
* @Description:hystrix缓存只有一个能用，剩下的缓存全部不管用，目前放弃使用hystrix缓存，反正你也是线程独享的
* @author zhangmin 
* @date 2018年4月9日 下午2:36:49
 */
public class ConsumerController {
	
	private static final Logger logger = LoggerFactory.getLogger(ConsumerController.class);
	
    @Autowired
    RestTemplate restTemplate;
    
    @Autowired
    private HelloFeignClient helloFeignClient;
    
    @Autowired
    private HelloService helloService;
    
    @Autowired
    private ApplicationContext ctx;
    
    @Autowired
    private CallService collService;

    
    /**
     * 测试所有的请求都会到这里，自定义拦截器
     * @return
     */
    @RequestMapping(value = "/testMyInterceptor",method = RequestMethod.GET)
    public String testMyInterceptor() {
        return "消费者所有请求到这里啦!";
    }
    
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
     *
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
     * 断路器一旦开启，就会直接调用回退方法，不再执行命令，而且也不会更新链路的健康状况，断路器的开启需要满足2个条件
     * 整个链路达到一定的阀值，默认情况下，10秒内产生超过20次请求，则符合第一个条件。
     * 满足第一个条件的情况下，如果请求的错误百分比大于阀值，则会打开断路器，默认为50%
     *
     * @return
     */
//  @HystrixCommand(fallbackMethod = "error",ignoreExceptions = ArithmeticException.class)
//  @HystrixCommand(fallbackMethod = "error")
    @HystrixCommand(
    		commandProperties = {
	            @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "1000"),
	            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "4"),
	            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50")
	        },fallbackMethod = "error")
    @RequestMapping("/testHystrix2")
    public String testHystrix2(String param) {
    	
    	/*for(int i=0;i<25;i++) {
    		try{
	    		logger.info("进入testHystrix2----方法");
	        	String result = helloService.testZzul(param);
	        	logger.info("result="+result);
    		}catch(Exception e) {
    			
    		}
    	}*/
    	
    	MyHystrixCommand myHystrixCommand1 = new MyHystrixCommand("2",restTemplate);
        myHystrixCommand1.execute();
        System.out.println("myHystrixCommand1 断路器状态" + myHystrixCommand1.isCircuitBreakerOpen());
        MyHystrixCommand myHystrixCommand2 = new MyHystrixCommand("2",restTemplate);
        myHystrixCommand2.execute();
        System.out.println("myHystrixCommand2 断路器状态" + myHystrixCommand2.isCircuitBreakerOpen());
        MyHystrixCommand myHystrixCommand3 = new MyHystrixCommand("2",restTemplate);
        myHystrixCommand3.execute();
        System.out.println("myHystrixCommand3 断路器状态" + myHystrixCommand3.isCircuitBreakerOpen());
        MyHystrixCommand myHystrixCommand4 = new MyHystrixCommand("2",restTemplate);
        myHystrixCommand4.execute();
        System.out.println("myHystrixCommand4 断路器状态" + myHystrixCommand4.isCircuitBreakerOpen());
        MyHystrixCommand myHystrixCommand5 = new MyHystrixCommand("2",restTemplate);
        myHystrixCommand5.execute();
        System.out.println("myHystrixCommand5 断路器状态" + myHystrixCommand5.isCircuitBreakerOpen());
        MyHystrixCommand myHystrixCommand6 = new MyHystrixCommand("2",restTemplate);
        myHystrixCommand6.execute();
        System.out.println("myHystrixCommand6 断路器状态" + myHystrixCommand6.isCircuitBreakerOpen());
        MyHystrixCommand myHystrixCommand7 = new MyHystrixCommand("2",restTemplate);
        myHystrixCommand7.execute();
        System.out.println("myHystrixCommand7 断路器状态" + myHystrixCommand7.isCircuitBreakerOpen());
        MyHystrixCommand myHystrixCommand8 = new MyHystrixCommand("2",restTemplate);
        myHystrixCommand8.execute();
        System.out.println("myHystrixCommand8 断路器状态" + myHystrixCommand8.isCircuitBreakerOpen());
        MyHystrixCommand myHystrixCommand9 = new MyHystrixCommand("2",restTemplate);
        myHystrixCommand9.execute();
        System.out.println("myHystrixCommand9 断路器状态" + myHystrixCommand9.isCircuitBreakerOpen());
        MyHystrixCommand myHystrixCommand10 = new MyHystrixCommand("2",restTemplate);
        myHystrixCommand10.execute();
        System.out.println("myHystrixCommand10 断路器状态" + myHystrixCommand10.isCircuitBreakerOpen());
        MyHystrixCommand myHystrixCommand11 = new MyHystrixCommand("2",restTemplate);
        myHystrixCommand11.execute();
        System.out.println("myHystrixCommand11 断路器状态" + myHystrixCommand11.isCircuitBreakerOpen());
        MyHystrixCommand myHystrixCommand12 = new MyHystrixCommand("2",restTemplate);
        myHystrixCommand12.execute();
        System.out.println("myHystrixCommand12 断路器状态" + myHystrixCommand12.isCircuitBreakerOpen());
        MyHystrixCommand myHystrixCommand13 = new MyHystrixCommand("2",restTemplate);
        myHystrixCommand13.execute();
        System.out.println("myHystrixCommand13 断路器状态" + myHystrixCommand13.isCircuitBreakerOpen());
        MyHystrixCommand myHystrixCommand14 = new MyHystrixCommand("2",restTemplate);
        myHystrixCommand14.execute();
        System.out.println("myHystrixCommand14 断路器状态" + myHystrixCommand14.isCircuitBreakerOpen());
        MyHystrixCommand myHystrixCommand15 = new MyHystrixCommand("2",restTemplate);
        myHystrixCommand15.execute();
        System.out.println("myHystrixCommand15 断路器状态" + myHystrixCommand15.isCircuitBreakerOpen());
        MyHystrixCommand myHystrixCommand16 = new MyHystrixCommand("2",restTemplate);
        myHystrixCommand16.execute();
        System.out.println("myHystrixCommand16 断路器状态" + myHystrixCommand16.isCircuitBreakerOpen());
    	
    	return "ok";
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
    public String testHystrixCommand() throws InterruptedException, ExecutionException, TimeoutException {
		logger.info("进入testHystrixCommand方法");
		//Future<Book> bookFuture = helloService.testHystrixCommand();
		// return bookFuture.get();
		// 1分钟没有返回则超时时长
		//return bookFuture.get(1, TimeUnit.MINUTES);
		
		Future<String> bookFuture = new CommandHelloWorld("World", restTemplate).queue();
		return bookFuture.get();
    }
    
    /**
     * 测试hystrix请求合并，输入三个名字，批量的给我打招呼-未实现
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/testCollapse", method = RequestMethod.GET)
    public String testCollapse() throws Exception {
    	HystrixRequestContext context = HystrixRequestContext.initializeContext();
    	StringCommandCollapser StringCommandCollapser = new StringCommandCollapser("zhangmin",restTemplate);
    	String result = StringCommandCollapser.queue().get();
    	logger.info("请求合并处理结果 result=" + result);
    	
    	StringCommandCollapser StringCommandCollapser2 = new StringCommandCollapser("xiaomin",restTemplate);
    	String result2 = StringCommandCollapser2.queue().get();
    	logger.info("请求合并处理结果 result2=" + result2);
    	
    	StringCommandCollapser StringCommandCollapser3 = new StringCommandCollapser("zhangmin",restTemplate);
    	String result3 = StringCommandCollapser3.queue().get();
    	logger.info("请求合并处理结果 result3=" + result3);
    	context.shutdown();
        return result;
    }
    
    /**
     * 测试hystrix缓存请求合并,未实现也是报错空指针
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/testCollapse2", method = RequestMethod.GET)
    public String testCollapse2() throws Exception {
    	Future<String> f1 = collService.sayHelloNames("zhangmin",restTemplate);
    	logger.info(f1.get());
    	Future<String> f2 = collService.sayHelloNames("xiaomin",restTemplate);
    	Future<String> f3 = collService.sayHelloNames("zhangmin",restTemplate);
    	Future<String> f4 = collService.sayHelloNames("haixing",restTemplate);
    	logger.info(f2.get());
    	logger.info(f3.get());
    	logger.info(f4.get());
    	return "访问成功";
    }
    
    /**
     * 测试纯Hystrix命令方式的缓存-非注解方式
     * 在Hystrix中带缓存的command是必须在request中调用的,在不同context中的缓存是不共享的,request只能限于当前线程
     * hystrix线程独享，因此一次调用相同命令可以缓存但是多次请求缓存不管用
     * @param id
     * @param aa
     * @return
     */
    @RequestMapping("/testHystrixCache")
    public String testHystrixCache(String name) {
    	HystrixRequestContext context = HystrixRequestContext.initializeContext();
        CommandHelloWorld commandHelloWorld  = new CommandHelloWorld(name, restTemplate);
        String result = commandHelloWorld.execute();
        logger.info("result="+result);
        
        CommandHelloWorld commandHelloWorld2  = new CommandHelloWorld(name, restTemplate);
        String result2 = commandHelloWorld2.execute();
        logger.info("result2="+result2);
        
        CommandHelloWorld commandHelloWorld3  = new CommandHelloWorld(name, restTemplate);
        String result3 = commandHelloWorld3.execute();
        logger.info("result3="+result3);
        
        context.shutdown();
        return result;
    }
    
    /**
     * 测试Hystrix缓存注解，无参数--缓存不管用
     * @param id
     * @param aa
     * @return
     */
    @RequestMapping("/testCacheResult")
    public String testCacheResult(String name) {
    	logger.info("进testCacheResult方法");
    	HystrixRequestContext.initializeContext();
        String resultString = helloService.hello(name);
        logger.info(resultString);
        String resultString2 = helloService.hello(name);
        logger.info(resultString2);
        return resultString;
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
    public String testCacheResult2(Integer age,String name) {
    	logger.info("进testCacheResult2--方法");
    	HystrixRequestContext.initializeContext();
    	ResponseEntity<String> responseEntity = restTemplate.getForEntity("http://HELLO-SERVICE/sayNameAndAge?name={name}&age={age}", String.class, name,age);
    	String resultString =  responseEntity.getBody();
    	logger.info(resultString);
    	ResponseEntity<String> responseEntity2 = restTemplate.getForEntity("http://HELLO-SERVICE/sayNameAndAge?name={name}&age={age}", String.class, name,age);
    	String resultString2 =  responseEntity2.getBody();
    	logger.info(resultString2);
    	return resultString;
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
    
    /**
     * 得到资源文件属性
     * @return
     */
    @RequestMapping("/getPro")
    public String getPro() {
    	return ctx.getEnvironment().getProperty("server.port");
    }
    
    /**
     * 请求fegin超时断路器打开测试
     * 并不是全部20次请求，断路器应该打开了只不过不是这个key报错空指针
     * @return
     */
    @HystrixCommand(commandKey="helloFeignClient#timeOut()")
    @RequestMapping(value = "/timeOut", method = RequestMethod.GET)
    public String timeOut() {
    	HystrixCircuitBreaker breaker = HystrixCircuitBreaker.Factory
                .getInstance(HystrixCommandKey.Factory
                        .asKey("helloFeignClient#timeOut()"));  
    	for(int i=0;i<20;i++) {
    		try {
    			helloFeignClient.timeOut();
			} catch (Exception e) {
				System.out.println("断路器中间状态：" + breaker.isOpen());
			}
    	}
        System.out.println("断路器状态：" + breaker.isOpen());
        return "ok";
    }
}
