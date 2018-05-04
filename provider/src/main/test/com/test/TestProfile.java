package com.test;
import java.time.LocalDate;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

import com.zm.provider.service.LegalHolidaysService;
import com.zm.provider.service.TestInsertService;

@ActiveProfiles("dev")
public class TestProfile extends SpringbootJunitTest {

	@Autowired
	private LegalHolidaysService legalHolidaysService;
	
	@Autowired
	private TestInsertService testInsertService;
	
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
		testInsertService.deleteInfo();
		for(int i = 0;i<20;i++) {
			testInsertService.insertTestInfo(i+"");
		}
		System.out.println("---------------耗时" + (System.currentTimeMillis()-beginLong));
	}
}
