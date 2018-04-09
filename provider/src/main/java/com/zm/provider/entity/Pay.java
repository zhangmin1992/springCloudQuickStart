package com.zm.provider.entity;

import java.io.Serializable;
import java.util.Date;

public class Pay  implements Serializable{

	private Integer id;
	
	private String name;
	
	private Integer price;
	
	private Date createTime;

	public Pay() {
		
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Pay(String name, Integer price, Date createTime) {
		super();
		this.name = name;
		this.price = price;
		this.createTime = createTime;
	}

	public Pay(Integer id, String name, Integer price, Date createTime) {
		super();
		this.id = id;
		this.name = name;
		this.price = price;
		this.createTime = createTime;
	}
	
	
	
}
