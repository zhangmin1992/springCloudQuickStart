package com.zm.provider.mq;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NeoSender {

   @Autowired
   private AmqpTemplate rabbitTemplate;

   public void send(int i) {
	   try {
		      String context = "spirng boot neo queue"+" ****** "+i;
		      System.out.println("Sender2 : " + context);
		      this.rabbitTemplate.convertAndSend(MyRabbitMqConfig.queueName, context);
		      this.rabbitTemplate.convertAndSend(MyRabbitMqConfig.queueName2,"哈哈哈哈");
		} catch (Exception e) {
			System.out.println("消息发送失败"+e);
		}
   }
}
