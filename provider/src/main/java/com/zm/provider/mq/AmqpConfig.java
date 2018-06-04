package com.zm.provider.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * mq的绑定关系
 * @author yp-tc-m-7129
 *
 */
@Component
public class AmqpConfig {

	private static final Logger Logger = LoggerFactory.getLogger(AmqpConfig.class);
		 
	@Autowired
    private RabbitMq rabbitMq;
	
	@Autowired
	private RabbitMqExchangeConfig rabbitMqExchangeConfig;
	
	/**
	 * 尝试使用资源文件的方式一次注入多个同类型的queue,exchange,绑定关系
	 * @return
	 */
	/*public  void init() {
		logger.info("准备初始化绑定关系");
		String[] queueArray = rabbitMqExchangeConfig.getQueueList().split(",");
		String[] exchangeArray = rabbitMqExchangeConfig.getExchangeList().split(",");
		String[] bindArray = rabbitMqExchangeConfig.getBindingList().split(",");
		for(int i=0;i<bindArray.length;i++) {
			Queue queue = queue(queueArray[i]);
			TopicExchange exchange = exchange(exchangeArray[i]);
			binding(queue, exchange, bindArray[i]);
		}
		
	}*/
	
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
	
	/**
	 * 定义多个对应的队列交换机和绑定关系-队列1
	 * @return
	 */
//	@Bean
//	Queue queue() {
//		return new Queue(MyRabbitMqConfig.queueName, false);
//	} 
//	 
//    @Bean
//    TopicExchange exchange() {
//        return new TopicExchange(MyRabbitMqConfig.exchangeName);
//    }
//    
//    @Bean
//    Binding binding() {
//        return BindingBuilder.bind(queue()).to(exchange()).with(MyRabbitMqConfig.keyName);
//    }
    
    /**
     * 队列2
     * @return
     */
//    @Bean
//	Queue queue2() {
//		return new Queue(MyRabbitMqConfig.queueName2, false);
//	} 
//	 
//    @Bean
//    TopicExchange exchange2() {
//        return new TopicExchange(MyRabbitMqConfig.exchangeName2);
//    }
//    
//    @Bean
//    Binding binding2() {
//        return BindingBuilder.bind(queue()).to(exchange()).with(MyRabbitMqConfig.keyName2);
//    }
    
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
