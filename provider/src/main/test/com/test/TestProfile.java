package com.test;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;

import com.zm.provider.entity.Book;
import com.zm.provider.entity.Email;
import com.zm.provider.entity.TestInsertEntity;
import com.zm.provider.mq.RabbitMq;
import com.zm.provider.service.ExcelService;
import com.zm.provider.service.LegalHolidaysService;
import com.zm.provider.service.SendEmailService;
import com.zm.provider.service.TestInsertService;
import com.zm.provider.util.CheckUtils;

@ActiveProfiles("dev")
public class TestProfile extends SpringbootJunitTest {

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
		String[] receiveArray = {"2413@qq.com","min.zhang-2@jia007.com"};
		Email email = new Email(receiveArray, subject, content, null, null);
		sendEmailService.sendSimpleMail(email);
		
		//sendEmailService.sendHtmlSimpleMail();
		//sendEmailService.sendFreemarker();
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
}
