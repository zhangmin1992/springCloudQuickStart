package com.zm.provider.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zm.provider.dao.PayEntityDao;
import com.zm.provider.entity.Pay;
import com.zm.provider.redis.xianliu.RateLimiter;
import com.zm.provider.service.PayEntityService;

@Service
public class PayEntityServiceImpl implements PayEntityService {

	@Autowired
	private PayEntityDao payEntityDao;
	
	@RateLimiter(limit = 2,timeout=6000)
	public void insertPay(Pay pay) {
		payEntityDao.insertPay(pay);
	}
}
