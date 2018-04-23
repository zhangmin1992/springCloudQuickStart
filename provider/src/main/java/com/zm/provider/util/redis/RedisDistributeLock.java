package com.zm.provider.util.redis;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zm.provider.util.CheckUtils;

/**
 * redis分布式锁工具类
 * @author: chenliang.wang
 * @Date: 2018年04月22日 PM2:18
 * @company: 易宝支付(YeePay)
 */
public class RedisDistributeLock {
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisDistributeLock.class);

    /**
     * 尝试加锁
     *
     * 1.NX/XX: NX，则只有当key不存在时才进行set;
     *          XX，则只有当key已经存在时才进行set;
     * 2.EX/PX: 代表数据过期时间的单位
     *          EX代表秒;
     *          PX代表毫秒
     * @param lockKey
     * @return
     */
    public static boolean tryLock(String lockKey) {
        CheckUtils.notEmpty(lockKey,"lockKey");
        return RedisToolUtils.set(lockKey, LocalDateTime.now().toString(), "NX", "PX", 2000 * 60);
    }

    /**
     * 释放锁
     * @param lockKey
     */
    public static void unlock(String lockKey) {
        CheckUtils.notEmpty(lockKey,"lockKey");
        RedisToolUtils.del(lockKey);
    }

    /**
     * 生成lockKey
     * @param clazz
     * @param describe
     * @param params
     * @return
     */
    public static String getLockKey(Object clazz, String describe, String... params){
        CheckUtils.notEmpty(params,"params");
        CheckUtils.notEmpty(clazz,"clazz");
        CheckUtils.notEmpty(describe,"describe");

        StringBuilder sb = new StringBuilder().append(clazz.getClass().getName()).append(describe);
        for (Object param : params) {
            sb.append(param);
        }

        return sb.toString();
    }
}


