package com.zm.provider.mq;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rabbitmq.client.Channel;
import com.zm.provider.HelloController;
import com.zm.provider.service.LegalHolidaysService;
import com.zm.provider.util.LocalDateUtils;

@Service
@RabbitListener(queues= MyRabbitMqConfig.queueName)
public class NeoProcessor {
	 
	 @Autowired
	 private LegalHolidaysService legalHolidaysService;
	 
	 private static final Logger LOGGER = LoggerFactory.getLogger(HelloController.class);
	
	 public void onMessage(Message message, Channel channel) throws Exception {
		LOGGER.info("进入onMessage");
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
     }
	
	 @RabbitHandler
	 public void process(String object) {
		LOGGER.info("收到消息"+object);
		LocalDate localDate = LocalDateUtils.parseDate(object);
    	LOGGER.info("localDate"+ localDate);
    	legalHolidaysService.insertLegalHolidaysInfo(localDate);
	 }

}
