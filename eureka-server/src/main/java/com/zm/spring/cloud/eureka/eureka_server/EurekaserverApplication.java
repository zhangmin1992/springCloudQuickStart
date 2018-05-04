package com.zm.spring.cloud.eureka.eureka_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class EurekaserverApplication {

    public static void main(String[] args) {
      /**
       * 加载properties方式	
       */
      SpringApplication.run(EurekaserverApplication.class, args);
    	
    	/**
    	 * 指定资源文件加载路径
    	 */
/*    	ConfigurableApplicationContext context = new SpringApplicationBuilder(
    			EurekaserverApplication.class)
				//.properties("spring.config.location=classpath:application-peer2.properties")
				.properties("spring.config.location=classpath:application.yml")
				.properties("spring.profiles.active=dev").
				run(args);
    	System.out.println("------"+context.getEnvironment().getProperty("my.name"));*/
    }
}
