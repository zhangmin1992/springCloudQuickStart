package com.zm.provider.mq;

/**
 * 绑定关系的接口定义
 * @author yp-tc-m-7129
 *
 */
public interface MyRabbitMqConfig {

	/**
	 * 第一个绑定关系
	 */
	final static String queueName = "queueName";
	
	final static String exchangeName = "exchangeName";
	
	final static String keyName = "keyName";
	
	/**
	 * 第二个绑定关系
	 */
	final static String queueName2 = "queueName2";
	
	final static String exchangeName2 = "exchangeName2";
	
	final static String keyName2 = "keyName2";
}
