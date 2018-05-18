package com.zm.provider;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.arjuna.ats.internal.arjuna.objectstore.jdbc.drivers.ibm_driver;

@MapperScan("com.zm.provider.dao")
@EnableDiscoveryClient
@SpringBootApplication
@EnableTransactionManagement
/**
 *@MapperScan  mybatis扫描包地址
 *@EnableDiscoveryClient 服务注册和发现
 *@SpringBootApplication 启动类
 *@EnableTransactionManagement 开启事务
 * @author yp-tc-m-7129
 *
 */
public class ProviderApplication {
    
	private static final Logger LOGGER = LoggerFactory.getLogger(ProviderApplication.class);
	
	/**
	 * 健康检查器，用于监控mq redis等第三方组件的可用性，发现并没有多大卵用，并不会阻止消费者调用到提供者上去
	 * @return
	 */
	/*@Bean
	public MyHealthIndicator myHealthIndicator(){
		return new MyHealthIndicator();
	}*/
	
    public static void main(String[] args) {
    	
    	String name = "zhangmin";
    	int age = 11;
    	LOGGER.info("即将启动日志 name={} 你的年龄是{}",name,age);
    	
    	try {
//			int i=9 /0;
			throw MyException.REMIT_UNKOWN_EXCEPTION.newInstance("我抛出异常 name={0} age={1}", "zhangmin",11);
		} catch (Exception e) {
			LOGGER.error("发生异常1", e);
			LOGGER.info("发生异常2 e={}",e);
			LOGGER.info("发生异常3 e={0}",e);
		}
    	
    	/**
    	 * 使用默认的启动方式 properties文件会优先于yml文件
    	 */
        //SpringApplication.run(ProviderApplication.class, args);
        
    	/**
    	 * 使用另外加载mq资源文件的方式
    	 */
//         new SpringApplicationBuilder(ProviderApplication.class)
//        .listeners(new LoadAdditionalProperties())  
//        .run(args);
    	
    	/**
    	 * 使用指定yml或者资源文件的方式
    	 */
    	/*ConfigurableApplicationContext context = new SpringApplicationBuilder(ProviderApplication.class)
		.properties("spring.config.location=classpath:application.yml")
		.run(args);
    	System.out.println("------"+context.getEnvironment().getProperty("my.name"));*/
    }
	
       
}
