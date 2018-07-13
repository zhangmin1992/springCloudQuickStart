package com.test;

import java.util.ArrayList;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.zm.provider.dao.BookEntityDao;
import com.zm.provider.mq.NeoSender;
import com.zm.provider.service.SendEmailService;
import com.zm.provider.util.redis.RedisToolUtils;
import com.zm.provider.vo.BookInfoVO;

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
	
	/**
	 * 测试mybatis返回list会过滤掉一样的?完全不会
	 */
	@Autowired
	private BookEntityDao bookEntityDao;
	
	@Test
	public void testRedisLock() {
		ArrayList<String> list = bookEntityDao.getBookInfo();
		System.out.println("-----"+JSONObject.toJSONString(list));
	}
	
	/**
	 * 测试批量更新不同数据对应的不同
	 */
	@Test
	public void testBatchupdate() {
		ArrayList<BookInfoVO> list = new ArrayList<>();
		BookInfoVO vo = new BookInfoVO();
		vo.setName("222");
		vo.setPrice(222);
		list.add(vo);
		BookInfoVO vo3 = new BookInfoVO();
		vo3.setName("333");
		vo3.setPrice(333);
		list.add(vo3);
		bookEntityDao.batchUpdateBookInfo(list);
		
//		ArrayList<String> list = new ArrayList<>();
//		list.add("9");
//		list.add("10");
//		bookEntityDao.batchDelete(list);
	}
	
	/**
     * 测试整合mq收发消息
     */
	@Autowired
	private NeoSender neoSender;
	
    @Test
    public void testMQ() {
    	logger.info("---开始准备发送mq消息");
    	neoSender.send("2018-03-01");
    }
}
