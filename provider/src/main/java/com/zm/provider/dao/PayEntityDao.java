package com.zm.provider.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zm.provider.entity.Pay;


public interface PayEntityDao {

	void insertPay(Pay pay);
	
	Map<String, String> getPayInfo(@Param("name") String name);
	
	List<String> getNameList();
}
