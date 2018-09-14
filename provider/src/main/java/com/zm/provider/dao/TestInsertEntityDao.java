package com.zm.provider.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

import com.zm.provider.entity.TestInsertEntity;

public interface TestInsertEntityDao {

	/**
	 * 插入数据
	 * @param entity
	 */
	void insetTestInfo(TestInsertEntity entity);
	
	void deleteInfo();
	
	/**
	 * 批量删除
	 * @param list
	 */
	void testBatchDelete(List<TestInsertEntity> list);
	
	List<TestInsertEntity> getInsertInfo();
	
	List<TestInsertEntity> getBatchInfo(@Param("pageNum") int pageNum,@Param("pagesize") int pagesize);
	
	void batchDelete(List<TestInsertEntity> list);
	
	public TestInsertEntity getCacheStr();
	
	long updateInfo(@Param("id") String id,@Param("oldName") String oldName,@Param("newName") String newName);
}
