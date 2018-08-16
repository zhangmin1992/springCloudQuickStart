package com.zm.provider.redis.xianliu;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.zm.provider.entity.Pay;
import com.zm.provider.service.PayEntityService;
import com.zm.provider.util.CheckUtils;
import com.zm.provider.util.ThreadFactory;
import com.zm.provider.util.redis.RedisToolUtils;

public class RedisQueueListener {

	@Autowired
	private PayEntityService payEntityService;

    private static final int BILL_QUEUE_SIZE = 500;

	private static BlockingQueue<Object> billQueue = new ArrayBlockingQueue<Object>(BILL_QUEUE_SIZE);
	
	public void init() {
		
		final Thread queueListenerThread = new Thread(new QueueListener());
        queueListenerThread.setDaemon(true);
        queueListenerThread.start();
        
		//1.开启一个守护线程，为啥开启是为了这个等待队列刚开始为空，你就要当前启动线程等待60秒
        ThreadFactory.startNewDaemonThread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						//2.如果等待队列没有数据就休息
						if (!RedisToolUtils.isKeyExist(CommonConstants.REDIS_BOLCK_QUEUE_FOR_PAY)) {
							System.out.println("等待队列没有线程,休息60秒");
							TimeUnit.SECONDS.sleep(60);
						} else {
							//3.找到redis数据就插入等待队列，redis格式是json字符串
							String billId = RedisToolUtils.rpop(CommonConstants.REDIS_BOLCK_QUEUE_FOR_PAY);
							System.out.println("redis中弹出数据"+JSONObject.toJSONString(billId));
							if (!CheckUtils.isEmpty(billId)) {
								billQueue.put(billId);
							}
						}
					}  catch (Throwable e) {
						System.out.println("发生异常了");
					}
				}
			}
		});
		
	}
	
	public class QueueListener implements Runnable {
		@Override
		public void run() {
			//while(true) 一旦启动就会持续执行，直到非守护线程全部终止
			while(true) {
				try {
					//从阻塞队列中取数据
					String obj = (String) billQueue.peek();
					if (!CheckUtils.isEmpty(obj)) {
						System.out.println("等待队列发现数据"+ JSONObject.toJSONString(obj));
						billQueue.remove();
						Pay pay = JSONObject.parseObject(obj, Pay.class);
						payEntityService.insertPay(pay);
					}else {
						System.out.println("billQueue中无数据,休眠60s！");
						TimeUnit.SECONDS.sleep(60);
					}
				}catch (Exception e) {
					System.out.println("QueueListener 发生异常");
				}
			}
			
		}
	}
}
