package com.zm.provider;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@MapperScan("com.zm.provider.dao")
@EnableDiscoveryClient
@SpringBootApplication
@EnableTransactionManagement
/**
 * mybatis扫描包地址
 * 服务注册和发现
 * 启动类
 * 开启事务
 * @author yp-tc-m-7129
 *
 */
public class ProviderApplication {
    
    public static void main(String[] args) {
        //SpringApplication.run(ProviderApplication.class, args);
        new SpringApplicationBuilder(ProviderApplication.class)
        .listeners(new LoadAdditionalProperties())  
        .run(args);  
    }
	
       
}
