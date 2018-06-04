package com.zm.provider.mq;

import java.util.ArrayList;

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

	private ArrayList<String> queueList;

	private ArrayList<String> exchangeList;

	private ArrayList<String> bindingList;

	public ArrayList<String> getQueueList() {
		return queueList;
	}

	public void setQueueList(ArrayList<String> queueList) {
		this.queueList = queueList;
	}

	public ArrayList<String> getExchangeList() {
		return exchangeList;
	}

	public void setExchangeList(ArrayList<String> exchangeList) {
		this.exchangeList = exchangeList;
	}

	public ArrayList<String> getBindingList() {
		return bindingList;
	}

	public void setBindingList(ArrayList<String> bindingList) {
		this.bindingList = bindingList;
	}
}