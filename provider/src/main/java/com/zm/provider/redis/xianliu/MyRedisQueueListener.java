package com.zm.provider.redis.xianliu;

import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zm.provider.dao.TestInsertEntityDao;
import com.zm.provider.entity.TestInsertEntity;
import com.zm.provider.util.CheckUtils;
import com.zm.provider.util.ThreadFactory;
import com.zm.provider.util.redis.RedisToolUtils;

@Component
public class MyRedisQueueListener {

	private ArrayBlockingQueue<String> blockQueue = new ArrayBlockingQueue<>(50);
	
	@Autowired
	private TestInsertEntityDao testInsertEntityDao;
	
	public void init() {
		ThreadFactory.startNewDaemonThread(new Runnable() {
			
			@Override
			public void run() {
				while(true) {
					try {
						System.out.println("监听开始");
						if(RedisToolUtils.isKeyExist(CommonConstants.key)) {
							System.out.println("redis有数据，我放入到阻塞队列中，先进先出");
							//rpop 弹出最后一个元素后，这个key也就不存在了
							String obj = RedisToolUtils.rpop(CommonConstants.key);
							blockQueue.put(obj);
						}else{
							System.out.println("redis没有数据，休息60秒");
							TimeUnit.SECONDS.sleep(60);
						}
					} catch (Throwable e) {
						System.out.println("发生异常");
					}
				}
			}
		});
		
		ThreadFactory.startNewDaemonThread(new Runnable() {
			
			@Override
			public void run() {
				while(true) {
					try {
						String object = blockQueue.take();
						if(!CheckUtils.isEmpty(object)) {
							System.out.println("阻塞队列发现数据，即将插入数据库");
							TestInsertEntity entity= new TestInsertEntity();
							entity.setName(object);
							entity.setId(UUID.randomUUID().toString().replace("-", ""));
							testInsertEntityDao.insetTestInfo(entity);
						}else {
							System.out.println("阻塞队列没有消息，休息60秒");
							TimeUnit.SECONDS.sleep(60);
						}
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			}
		});
		
	}
}
