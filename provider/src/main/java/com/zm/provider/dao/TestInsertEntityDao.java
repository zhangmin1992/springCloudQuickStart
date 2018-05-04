package com.zm.provider.dao;

import com.zm.provider.entity.TestInsertEntity;

public interface TestInsertEntityDao {

	/**
	 * 插入数据
	 * @param entity
	 */
	void insetTestInfo(TestInsertEntity entity);
	
	void deleteInfo();
	
}
