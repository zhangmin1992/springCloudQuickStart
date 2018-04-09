package com.zm.provider.mq;

import org.springframework.boot.context.properties.ConfigurationProperties;  
import org.springframework.context.annotation.Configuration;  

@Configuration  
@ConfigurationProperties(prefix = "spring.rabbitmq")  
public class RabbitMq{  
  
    private String host; 
    
    private String username;
    
    private String password; 
    
    private Boolean publisherconfirms;
    
    private String port;
    
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Boolean getPublisherconfirms() {
		return publisherconfirms;
	}
	public void setPublisherconfirms(Boolean publisherconfirms) {
		this.publisherconfirms = publisherconfirms;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	} 
	
}  