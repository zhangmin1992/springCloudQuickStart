package com.test;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;

import com.alibaba.fastjson.JSONObject;
import com.zm.provider.dao.BookEntityDao;
import com.zm.provider.dao.PayEntityDao;
import com.zm.provider.entity.Book;
import com.zm.provider.entity.Email;
import com.zm.provider.entity.TestInsertEntity;
import com.zm.provider.mq.RabbitMq;
import com.zm.provider.service.ExcelService;
import com.zm.provider.service.LegalHolidaysService;
import com.zm.provider.service.SendEmailService;
import com.zm.provider.service.TestInsertService;
import com.zm.provider.util.CheckUtils;
import com.zm.provider.util.redis.RedisToolUtils;
import com.zm.provider.util.sms.AliyunSmsCodeProperties;
import com.zm.provider.util.sms.SmsService;

@ActiveProfiles("dev")
public class TestProfile extends SpringbootJunitTest {

	private static final Logger logger = LoggerFactory.getLogger(TestProfile.class);
	
	@Autowired
	private BookEntityDao bookEntityDao;
	
	@Autowired
	private PayEntityDao payEntityDao;
	
	@Autowired
    private JavaMailSender mailSender;
	
	@Autowired
	private SendEmailService sendEmailService;
	
	@Autowired
	private LegalHolidaysService legalHolidaysService;
	
	@Autowired
	private TestInsertService testInsertService;
	
	@Autowired
	private ExcelService excelService;
	
	@Autowired
	private SmsService smsService;
	
	@Autowired
	private AliyunSmsCodeProperties properties;
	
	/**
	 * 测试多环境配置资源文件
	 */
	@Test
	public void testProfile() {
		System.out.println("-----我执行啥看端口号是哪个看不出来，数据库不一样看数据库吧");
		legalHolidaysService.insertLegalHolidaysInfo(LocalDate.now());
		
	}
	
	/**
	 * 测试唯一索引和删除再插入的耗时
	 *  5000条单条插入 耗时54055
		10000 条唯一索引耗时 137637
		10000 条删除再插入耗时107513
		
		20条插入耗时
		566
		468
		397
	 */
	@Test
	public void testInsert() {
		Long beginLong =  System.currentTimeMillis();
		//testInsertService.deleteInfo();
		for(int i = 0;i<20;i++) {
			testInsertService.insertTestInfo(i+"");
		}
		System.out.println("---------------耗时" + (System.currentTimeMillis()-beginLong));
	}
	
	/**
	 * 测试20条数据是直接删除快还是查询下按照id删除快
	 * 直接删除 耗时 81
	 * 查询 批量删除 耗时239
	 */
	@Test
	public void testDelete() {
		Long beginLong =  System.currentTimeMillis();
		//testInsertService.deleteInfo();
		List<TestInsertEntity> list = testInsertService.getInsertInfo();
		if(!CheckUtils.isEmpty(list)) {
			testInsertService.batchDelete(list);
		}
		System.out.println("---------------删除耗时" + (System.currentTimeMillis()-beginLong));
	}
	
	/**
	 * 测试发送简单邮件，多人邮箱中只要有一个发送异常整个就发送失败
	 * 测试发送HTML邮件，代码重复已弃用
	 * 测试发送模板文件，好看
	 * @throws Exception
	 */
	@Test
	public void testEmail() throws Exception {
		String subject="邮件主题";
		String content="邮件内容";
		String[] receiveArray = {"2413172711@qq.com"};
		Email email = new Email(receiveArray, subject, content, null, null);
		//sendEmailService.sendSimpleMail(email);
		
		sendEmailService.sendHtmlSimpleMail();
		//sendEmailService.sendFreemarker();
		
		//sendEmailService.sendFileMail(email);
	}
	
	/**
	 * 只需要保证资源文件在src/main/source文件夹下
	 * 测试加载本地资源文件的几种方式
	 */
	@Autowired
	private RabbitMq rabbitMq;
	
	@Value("{spring.rabbitmq.password}")
	private String password;
	
	@Autowired
	Environment env;
	
	@Test
	public void getProperties() {
		System.out.println("=========="+this.password);
		System.out.println("--------"+rabbitMq.getUsername());
		System.out.println("-------"+env.getProperty("spring.rabbitmq.password"));
	}
	
	/**
	 * 测试用txt模板生成txt文件
	 */
	@Test
	public void makeExcel() {
		List<Book> data = new ArrayList<>();
		data.add(new Book("zhanagmin", 15, new Date()));
		data.add(new Book("haixing", 25, new Date()));
		data.add(new Book("beibei", 18, new Date()));
		excelService.makeExcel(data);
	}
	
	/**
	 * 测试缓存和发邮件
	 */
	@Test
	public void testRedisLock() {
    	logger.info("---开始测试分布式锁");
    	for (int i = 0; i < 5; i++) {
			Boolean result = RedisToolUtils.set("mm", "value", "NX", "PX", 8000 * 60);
	    	if(result) {
	    		logger.info("准备发送邮件---");
	    		try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
    	}
    }
	
	/**
	 * 自定义缓存注解的测试，在方法上标记了这个注解，可以设置有效期，根据唯一key,第一次查出来存缓存第二次直接取缓存
	 */
	@Test
	public void testCache() {
		logger.info("---开始测试testCache");
		System.out.println(JSONObject.toJSONString(testInsertService.getCacheStr("1","2")));
		System.out.println(JSONObject.toJSONString(testInsertService.getCacheStr("1","2")));
		System.out.println(JSONObject.toJSONString(testInsertService.getCacheStr("1","3")));
	}
	
	/**
	 * 测试mybatis返回map,并且把返回的值设置成驼峰模式的
	 * 方式一：不用任何配置类，修改xml文件
	 * 在xml里面写这个
	 *  <resultMap id="myResultMap"  type="HashMap">  
		    <result property="name" column="name" />  
		    <result property="createTime" column="create_time" />  
  	    </resultMap>
  	    <select id="getPayInfo" parameterType="java.lang.String" resultMap="myResultMap">
	 	  select name,create_time from pay_test where name = #{name,jdbcType=VARCHAR}
	 	</select>
	 	方式二：增加资源文件属性，并且增加配置类，xml文件中这样写，适用于所有的返回map的东西
	 	<select id="getPayInfo" parameterType="java.lang.String" resultType="java.util.Map">
	 	  select name,create_time from pay_test where name = #{name,jdbcType=VARCHAR}
	 	</select>
	 	资源文件增加配置：将map转为驼峰模式配置true，
	 	mybatis.configuration.map-underscore-to-camel-case=true
	 	增加配置类：com.zm.provider.util.tuofeng下的文件
	 */
	@Test
	public void getPayInfo() {
		Map<String, String> resultMap = payEntityDao.getPayInfo("zm");
		System.out.println("-----"+JSONObject.toJSONString(resultMap));
	}
	
	@Test
	public void getNameList() {
		List<String> resultMap = payEntityDao.getNameList();
		System.out.println("-----"+JSONObject.toJSONString(resultMap));
	}
	
	/**
	 * 阿里云平台发送短信
	 */
	@Test
	public void testSms() {
		//System.out.println("======="+properties.getAccessKeyId());
		smsService.sendSmsCode("18701365103");
	}
}
