package com.zm.provider.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.zm.provider.dao.TestInsertEntityDao;
import com.zm.provider.entity.TestInsertEntity;
import com.zm.provider.service.TestInsertService2;


@Service("testInsertService2")
public class TestInsertServiceImpl2 implements TestInsertService2 {

	@Autowired
    private KafkaTemplate kafkaTemplate;
	
    @Autowired
    private TestInsertEntityDao testInsertEntityDao;
	
    /**
	 * @Cacheable表示插入缓存 CacheEvict表示删除缓存 CachePut表示更新缓存
	 * @Cacheable key 单个对象
	 * 组合对象
	 * 对象某字段
	 * 自定义key 都支持
	 */
    //@Cacheable(value="content")
    //@Cacheable(value="content", key="#id")
    //@Cacheable(value="content", key="T(String).valueOf(#id).concat('-').concat(#name)")
    @Cacheable(value="content", keyGenerator = "baseEnCacheKeyGenerator")
	@Override
	public TestInsertEntity getEnCacheStr(String id,String name) {
		System.out.println("getEnCacheStr 要查库了"+id);
		return testInsertEntityDao.getCacheStr();
	}
    
    public void testKafka() {
    	System.out.println("准备发送消息");
    	kafkaTemplate.send("test3", "8745678");
    }
}
