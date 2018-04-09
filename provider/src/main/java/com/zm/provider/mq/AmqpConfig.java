package com.zm.provider.mq;

import org.apache.log4j.Logger;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.zm.provider.ProviderApplication;

@Component
public class AmqpConfig {

	private final Logger logger = Logger.getLogger(AmqpConfig.class);
		 
	@Autowired
    private RabbitMq rabbitMq;
	
	@Autowired
	private RabbitMqExchangeConfig rabbitMqExchangeConfig;
	
//	public  void init() {
//		logger.info("准备初始化绑定关系");
//		String[] queueArray = rabbitMqExchangeConfig.getQueueList().split(",");
//		String[] exchangeArray = rabbitMqExchangeConfig.getExchangeList().split(",");
//		String[] bindArray = rabbitMqExchangeConfig.getBindingList().split(",");
//		for(int i=0;i<bindArray.length;i++) {
//			Queue queue = queue(queueArray[i]);
//			TopicExchange exchange = exchange(exchangeArray[i]);
//			binding(queue, exchange, bindArray[i]);
//		}
//		
//	}
	
	@Bean
    public ConnectionFactory connectionFactory(){
        CachingConnectionFactory connectionFactory=new CachingConnectionFactory();
        connectionFactory.setAddresses(rabbitMq.getHost());
        connectionFactory.setUsername(rabbitMq.getUsername());
        connectionFactory.setPassword(rabbitMq.getPassword());
        connectionFactory.setPublisherConfirms(true);
        connectionFactory.setPort(Integer.valueOf(rabbitMq.getPort()));
        connectionFactory.setVirtualHost("YQT");
        return connectionFactory;
    }
	
	@Bean
	Queue queue() {
		return new Queue(MyRabbitMqConfig.queueName, false);
	} 
	 
    @Bean
    TopicExchange exchange() {
        return new TopicExchange(MyRabbitMqConfig.exchangeName);
    }
    
    @Bean
    Binding binding() {
        return BindingBuilder.bind(queue()).to(exchange()).with(MyRabbitMqConfig.keyName);
    }
    
    @Bean
	Queue queue2() {
		return new Queue(MyRabbitMqConfig.queueName2, false);
	} 
	 
    @Bean
    TopicExchange exchange2() {
        return new TopicExchange(MyRabbitMqConfig.exchangeName2);
    }
    
    @Bean
    Binding binding2() {
        return BindingBuilder.bind(queue()).to(exchange()).with(MyRabbitMqConfig.keyName2);
    }
    
//    @Bean
//    Queue queue(String name) {
//        return new Queue(name, false);
//    }
//    
//    @Bean
//    TopicExchange exchange(String name) {
//        return new TopicExchange(name);
//    }
//    
//    @Bean
//    Binding binding(Queue queue,TopicExchange exchange,String key) {
//        return BindingBuilder.bind(queue).to(exchange).with(key);
//    }
//    
//    @Bean
//    Queue queue() {
//        return new Queue("cc", false);
//    }
//    
//    @Bean
//    TopicExchange exchange() {
//        return new TopicExchange("tt");
//    }
//    
//    @Bean
//    Binding binding() {
//        return BindingBuilder.bind(queue()).to(exchange()).with("keyname");
//    }
    
}
