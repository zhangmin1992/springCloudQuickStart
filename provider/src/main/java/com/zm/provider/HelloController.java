package com.zm.provider;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.zm.provider.dao.BookEntityDao;
import com.zm.provider.dao.PayEntityDao;
import com.zm.provider.entity.Book;
import com.zm.provider.entity.Pay;
import com.zm.provider.mq.NeoSender;
import com.zm.provider.redis.RedisUtils;
import com.zm.provider.util.redis.RedisDistributeLock;
import com.zm.provider.util.redis.RedisToolUtils;

@RestController
public class HelloController {
    
	private static final Logger logger = LoggerFactory.getLogger(HelloController.class);
    
	@Autowired
	private BookEntityDao bookEntityDao;
	
	@Autowired
	private PayEntityDao payEntityDao;
	
    @Autowired
    private DiscoveryClient client;
    
    @Autowired
    private NeoSender neoSender;
    
    @Autowired
    private RedisUtils redisUtils;

    /**
     * 测试资源文件启动正常
     * @return
     */
    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String hello() {
        List<ServiceInstance> instances = client.getInstances("hello-service");
        for (int i = 0; i < instances.size(); i++) {
            logger.info("/hello,host:" + instances.get(i).getHost() + ",service_id:" + instances.get(i).getServiceId());
        }
        return "Hello World";
    }
   
    /**
     * 测试启动正常-单普通参数
     * @param name
     * @return
     */
    @RequestMapping(value = "/sayHello", method = RequestMethod.GET)
    public String sayHello(String name) {
    	logger.info("我进入了服务提供者 name=" + name);
    	return "您好"+name;
    }
    
    /**
     * 多普通参数
     * @param name
     * @param age
     * @return
     */
    @RequestMapping(value = "/sayNameAndAge", method = RequestMethod.GET)
    public String sayNameAndAge(String name,String age) {
    	logger.info("----- HelloController sayNameAndAge 进入 name= "+name + "age="+ age);
    	return "您好"+name + "您今年"+age +"岁了!";
    }
    
    /**
     * 返回实体
    * @Description: TODO(这里用一句话描述这个方法的作用) 
    * @param @return
    * @return Book    返回类型 
    * @throws
     */
    @RequestMapping(value = "/sayBook", method = RequestMethod.GET)
    public Book sayBook() {
    	logger.info("----- HelloController sayBook");
        return new Book("三国演义", 90, new Date());
    }
    
    /**
     * 单实体参数
     * @param map
     * @return
     */
    @RequestMapping(value = "/sayNameAndAge2", method = RequestMethod.POST)
    public String sayNameAndAge2(@RequestBody Map<String, String> map) {
    	logger.info("----- HelloController sayNameAndAge2 进入");
    	return "您好"+map.get("name") + "您今年"+map.get("age") +"岁了!";
    }
   
    /**
     * 单实体参数返回实体get方式
     * @param book
     * @return
     */
    @RequestMapping(value = "/getBook", method = RequestMethod.GET)
    public Book getBook(@RequestBody Book book) {
    	logger.info("----- HelloController getBook, book=" + JSONObject.toJSONString(book));
    	book.setName("哈哈你过来了");
        return book;
    }
    
    /**
     * 单实体参数返回实体
     * @param book
     * @return
     */
    @RequestMapping(value = "/getBook2")
    public Book getBook2(@RequestBody Book book) {
    	logger.info("----- HelloController getBook2, book=" + JSONObject.toJSONString(book));
    	book.setName("说谎");
        return book;
    }
    
    /**
     * 返回实体多实体参数-------错误写法会报错
     * @param book
     * @param pay
     * @return
     */
    @RequestMapping(value = "/getBookForObject")
    public Book getBookForObject(@RequestBody Book book,@RequestBody Pay pay) {
    	logger.info("----- HelloController getBookForObject, book=" + JSONObject.toJSONString(book));
    	logger.info("----- HelloController getBookForObject, pay=" + JSONObject.toJSONString(pay));
    	book.setName("说谎");
    	pay.setName("gg");
        return book;
    }
    
    /**
     * 这种支持
     * @param book
     * @param id
     * @return
     */
    @RequestMapping(value = "/getBookForObject2")
    public Book getBookForObject2(@RequestBody Book book,@RequestParam(value ="id") String id) {
    	logger.info("----- HelloController getBookForObject,id= "+ id +" book=" + JSONObject.toJSONString(book));
    	book.setName("说谎");
        return book;
    }
    
    /**
     * 测试提供者的mybatis整合
     * @param id
     * @return
     */
    @RequestMapping(value="mybatatisgetBook")
    public Book mybatatisgetBook(Long id) {
    	logger.info("----- HelloController mybatatisgetBook,id= "+id);
    	Book book = bookEntityDao.getBook(id);
    	logger.info("----- HelloController mybatatisgetBook,book= "+ JSONObject.toJSONString(book));
        return book;
    }
    
    /**
     * 测试事务
     * @param name
     * @return
     */
    @Transactional
    @RequestMapping(value="testTransaction")
    public String testTransaction() {
    	logger.info("----- HelloController testTransaction");
    	Pay pay = new Pay(1, "zm", 2, new Date());
    	Book book = new Book(1, "zm", 2, new Date());
    	try {
    		payEntityDao.insertPay(pay);
        	bookEntityDao.insertBook(book);
		} catch (Exception e) {
			logger.info("----- HelloController catch exception " + e);
			return "no";
		}
    	return "yes";
    }
   
    @RequestMapping(value="timeOut")
    public String timeOut() {
    	logger.info("timeOut我进入了服务提供者。。。。");
    	try {
			Thread.sleep(1100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return "ok";
    }
    
    /**
     * 测试网关和断路器是否每次都会请求到这里
     * @param param
     * @return
     */
    @RequestMapping(value="testZzul")
    public String testZzul(String param) {
    	try {
    		logger.info("我进入了服务提供者。。。。");
	    	int paramInt = Integer.parseInt(param);
	    	int num = 100 / paramInt;
    	} catch (Exception e) {
    		logger.info("testZzul provider has exception " + e);
    		throw e;
    	}
    	return "ok";
    }
    
    /**
     * 测试整合mq收发消息
     */
    @RequestMapping(value="testMQ")
    public void testMQ() {
    	logger.info("---开始准备发送mq消息");
    	neoSender.send("2018-01-01");
    }
    
    /**
     * 测试redis整合，spring自带的redis处理方式
     * 缺点需要注入没法公有化
     */
    /*@RequestMapping(value="testRedisLock")
    public void testRedisLock() {
    	logger.info("---开始测试分布式锁");
    	redisUtils.set("mykey", "myvalue");
    	Object result = (String) redisUtils.get("mykey");
    	logger.info("------"+ result);
    }*/
    
    /**
     * 测试redis整合，工具类方式
     */
    @RequestMapping(value="testRedisLock")
    public void testRedisLock() {
    	logger.info("---开始测试分布式锁");
    	
    	new Thread(new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < 5; i++) {
					Boolean result = RedisToolUtils.set("mm", "value", "NX", "PX", 8000 * 60);
			    	if(result) {
			    		logger.info("准备发送邮件---");
			    	}
				}
			}
    	}).start();
    }
    
    /**
     * 测试分布式锁-暂时没发现问题
     */
	/*@RequestMapping(value="testRedisLock")
	public void testRedisLock() {
		logger.info("---开始测试分布式锁");
		new Thread(new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < 10; i++) {
					String key = RedisDistributeLock.getLockKey(
							HelloController.class, "key", String.valueOf(i), "");
					if (RedisDistributeLock.tryLock(key)) {
						logger.info("线程一获取到锁 key" + key);
						try {
							Thread.sleep(1000);
						} catch (Exception e) {
							// TODO: handle exception
						} finally {
							logger.info("线程一准备删除 key" + key);
							RedisDistributeLock.unlock(key);
						}
					}
				}

			}
		}).start();

		for (int i = 0; i < 10; i++) {
			String key = RedisDistributeLock.getLockKey(HelloController.class,
					"key", String.valueOf(i), "");
			if (RedisDistributeLock.tryLock(key)) {
				logger.info("线程二获取到锁 key" + key);
				try {
					Thread.sleep(1000);
				} catch (Exception e) {
					// TODO: handle exception
				} finally {
					logger.info("线程二准备删除 key" + key);
					RedisDistributeLock.unlock(key);
				}
			}
		}
	}*/
	 
	 //测试获取统一配置的值
	@RequestMapping(value="getMyName3")
    public String getMyName3() {
		return "";
	}
    
}