package com.zm.provider.mq;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 尝试加载额外的资源文件
 * @author yp-tc-m-7129
 *
 */
@Configuration
@ConfigurationProperties(prefix = "my.rabitmq")
public class RabbitMqExchangeConfig {

	private String queueList;

	private String exchangeList;

	private String bindingList;

	public String getQueueList() {
		return queueList;
	}

	public void setQueueList(String queueList) {
		this.queueList = queueList;
	}

	public String getExchangeList() {
		return exchangeList;
	}

	public void setExchangeList(String exchangeList) {
		this.exchangeList = exchangeList;
	}

	public String getBindingList() {
		return bindingList;
	}

	public void setBindingList(String bindingList) {
		this.bindingList = bindingList;
	}

}