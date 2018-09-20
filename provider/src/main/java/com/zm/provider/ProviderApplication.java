package com.zm.provider;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.transaction.annotation.EnableTransactionManagement;
@MapperScan("com.zm.provider.dao")
@EnableDiscoveryClient
@SpringBootApplication
@EnableTransactionManagement
@EnableAspectJAutoProxy
@EnableCaching
@EnableKafka
/**
 *@MapperScan  mybatis扫描包地址
 *@EnableDiscoveryClient 服务注册和发现
 *@SpringBootApplication 启动类
 *@EnableTransactionManagement 开启事务
 *@EnableAspectJAutoProxy 开启aop动态代理
 * @author yp-tc-m-7129
 *
 */
public class ProviderApplication {
    
	private static final Logger LOGGER = LoggerFactory.getLogger(ProviderApplication.class);
	
	/**
	 * 项目启动监听redis
	 * @param bean
	 * @return
	 */
	/*@Bean(initMethod="init")
	public RedisQueueListener RedisQueueListener() {
		return new RedisQueueListener();
	}*/
	
	/*@Bean(initMethod="init")
	public MyRedisQueueListener MyRedisQueueListener() {
		return new MyRedisQueueListener();
	}*/
	
	/**
	 * 健康检查器，用于监控mq redis等第三方组件的可用性
	 * @return
	 */
	/*@Bean
	public MyHealthIndicator myHealthIndicator(){
		return new MyHealthIndicator();
	}*/
	
	
    public static void main(String[] args) {
    	
    	/**
    	 * 使用默认的启动方式 properties文件会优先于yml文件
    	 */
        SpringApplication.run(ProviderApplication.class, args);
        
    	/**
    	 * 使用另外加载mq资源文件的方式
    	 */
//         new SpringApplicationBuilder(ProviderApplication.class)
//        .listeners(new LoadAdditionalProperties())  
//        .run(args);
    	
    	/**
    	 * 使用指定yml或者资源文件的方式
    	 */
//    	ConfigurableApplicationContext context = new SpringApplicationBuilder(ProviderApplication.class)
//		.properties("spring.config.location=classpath:bootstrap.properties")
//		.run(args);
    	
    }
	
       
}
