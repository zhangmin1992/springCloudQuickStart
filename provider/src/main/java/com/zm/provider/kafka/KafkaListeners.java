package com.zm.provider.kafka;

import java.util.Optional;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaListeners {

	@KafkaListener(topics = {"test3"})
	public void listen(ConsumerRecord<?,?> record){
		System.out.println("进入方法");
		Optional<?> kafkaMessage = Optional.ofNullable(record.value());
		if (kafkaMessage.isPresent()){
			Object content = kafkaMessage.get();
	        System.out.println("我收到来自主题test2的消息："+ content);
		}
    }
}
