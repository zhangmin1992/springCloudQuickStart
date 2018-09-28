package com.zm.provider.zk;

public class Test {

	public static void main(String[] args) {
		//推荐好用的粉底液
		for (int i = 0; i < 10; i++) {
            Thread t = new Thread(new Runnable() {
				public void run() {
					
					ZkDistributedLock lock = null;
	                try {
	                    lock = new ZkDistributedLock("127.0.0.1:2181", "mylockName");
	                    lock.lock();
	                    System.out.println(Thread.currentThread().getName() + "我正在做某些事");
	                    System.out.println(Thread.currentThread().getName() + "正在运行");
	                } finally {
	                    if (lock != null) {
	                    	System.out.println(Thread.currentThread().getName() +  "我即将释放锁");
	                        lock.unlock();
	                    }
	                }
	                
				}
			});
            t.start();
        }
	}

}
