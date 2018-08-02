package com.zm.provider.service;

import java.util.List;

import com.zm.provider.entity.TestInsertEntity;

public interface TestInsertService {
	
	void insertTestInfo(String name);
	
	void deleteInfo();
	
	List<TestInsertEntity> getInsertInfo();
	
	String getCacheStr();
	
	void batchDelete(List<TestInsertEntity> list);
}
