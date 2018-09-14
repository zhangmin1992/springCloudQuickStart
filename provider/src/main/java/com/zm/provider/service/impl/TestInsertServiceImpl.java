package com.zm.provider.service.impl;

import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.zm.provider.dao.TestInsertEntityDao;
import com.zm.provider.entity.TestInsertEntity;
import com.zm.provider.service.TestInsertService;
import com.zm.provider.util.aop.MyAspect;

@Service
public class TestInsertServiceImpl implements TestInsertService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestInsertServiceImpl.class);

    @Autowired
    private TestInsertEntityDao testInsertEntityDao;
    
	@Override
	public void insertTestInfo(String name) {
		TestInsertEntity entity = new TestInsertEntity();
		entity.setName(name);
		entity.setId(UUID.randomUUID().toString().replaceAll("-", ""));
		try {
			testInsertEntityDao.insetTestInfo(entity);
		}catch (DuplicateKeyException e) {
            LOGGER.error("插入数据出现违反唯一索引的数据,此记录可丢弃！", e);
        }
	}

	@Override
	public void deleteInfo() {
		testInsertEntityDao.deleteInfo();
	}

	@Override
	public List<TestInsertEntity> getInsertInfo() {
		return testInsertEntityDao.getInsertInfo();
	}

	@Override
	public void batchDelete(List<TestInsertEntity> list) {
		testInsertEntityDao.batchDelete(list);
	}

	@Override
	@MyAspect(expireTime=5000)
	//@RateLimiter(limit = 2,timeout=6000)
	public TestInsertEntity getCacheStr(String name,String age) {
		System.out.println("要查库了");
		return testInsertEntityDao.getCacheStr();
	}

}
