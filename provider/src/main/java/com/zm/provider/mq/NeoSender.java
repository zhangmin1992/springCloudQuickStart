package com.zm.provider.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NeoSender {

   private static final Logger logger = LoggerFactory.getLogger(NeoSender.class);
	
   @Autowired
   private AmqpTemplate rabbitTemplate;

   public void send(String message) {
	   try {
		      String context = message;
		      logger.info("准备发送消息 context=" + context);
		      this.rabbitTemplate.convertAndSend(MyRabbitMqConfig.queueName, message);
		      //this.rabbitTemplate.convertAndSend(MyRabbitMqConfig.queueName2,message);
		} catch (Exception e) {
			System.out.println("消息发送失败"+e);
		}
   }
}
