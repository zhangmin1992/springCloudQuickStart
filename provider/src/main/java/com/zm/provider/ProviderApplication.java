package com.zm.provider;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.privider.config.MyRibbonConfiguration;

@MapperScan("com.zm.provider.dao")
@EnableDiscoveryClient
@SpringBootApplication
@EnableTransactionManagement
//@RibbonClient(name="hello-service", configuration=MyRibbonConfiguration.class)
/**
 *@MapperScan  mybatis扫描包地址
 *@EnableDiscoveryClient 服务注册和发现
 *@SpringBootApplication 启动类
 *@EnableTransactionManagement 开启事务
 * @author yp-tc-m-7129
 *
 */
public class ProviderApplication {
    
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
    	/*ConfigurableApplicationContext context = new SpringApplicationBuilder(ProviderApplication.class)
		.properties("spring.config.location=classpath:application.yml")
		.run(args);
    	System.out.println("------"+context.getEnvironment().getProperty("my.name"));*/
    }
	
       
}
