package com.test;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.zm.provider.service.SendEmailService;
import com.zm.provider.util.redis.RedisToolUtils;

public class TestResource extends SpringbootJunitTest {

	private static final Logger logger = LoggerFactory.getLogger(TestResource.class);
	
	/**
	 * src/main/java：里面的java文件只能直接加载src/main/resources下的资源，不能直接加载src/test/resources下的资源；
	 *  src/test/java: 里面的java文件既能加载src/test/resources下的资源，
	 * 又能加载src/main/resources下的资源，当两个resources下都有要加载的同名资源时候，优先选择src/test/java下的资源
	 * 测试开始 test 和 main 下面redis路径地址不一样，发现test也有的时候会优先加载test下的资源文件
	 */
//	@Autowired
//    private RedisUtils redisUtils;
	
	@Test
	public void testMyResource() {
		/**
		 * 方式一：资源文件声明的spring自带的需要注入的redis工具类
		 */
		/*redisUtils.set("mm", "ddddd");
		logger.info("插入redis执行完毕");*/
		
		/**
		 * 方式二：工具类的方式，需要额外的指定名称的资源文件，需要yeepay的jar包
		 */
		RedisToolUtils.set("ssss", "ssss");
	}
	
	@Autowired
	private SendEmailService sendEmailService;
	
	/**
	 * 邮件发送
	 * @throws Exception
	 */
	@Test
	public void testEmail() throws Exception {
		sendEmailService.sendHtmlSimpleMail();
	}
}
