package com.zm.provider.util.redis;

import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.util.Pool;

/**
 * @author: peng.chen-2
 * @Date: 2018:03:21 下午5:35
 * @company: 易宝支付(YeePay)
 */
public class RedisClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisClient.class);

    private static Pool<Jedis> pool;


    static {
        init();
    }

    /**
     * 获取资源
     *
     * @return
     */
    public static Jedis getResource() {
        if (pool == null) {
            throw new RuntimeException("Redis Client not init!");
        }
        return pool.getResource();
    }

    /**
     * 关闭资源
     *
     * @param jedis
     */
    public static void closeResource(Jedis jedis) {
        if (jedis != null) {
            if (jedis.isConnected()) {
                pool.returnResource(jedis);
            } else {
                pool.returnBrokenResource(jedis);
            }
        }
    }

    /**
     * 初始化
     */
    public synchronized static void init() {
        Properties prop = null;
        try {
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("runtimecfg/redis-conf.properties");
            prop = new Properties();
            prop.load(is);
        } catch (Exception e) {
            throw new RuntimeException("read redis config file error", e);
        }
        //初始化redis pool
        try {
            autoDetect(prop);
        } catch (Exception e) {
            throw new RuntimeException("init redis pool fail", e);
        }
    }

    /**
     * masterName 和 sentinelNodes存在则为哨兵模式
     * 否则为单节点
     * @param
     */
    private static void autoDetect(Properties prop) {
        String masterName = prop.getProperty("spring.redis.sentinel.master");
        String sentinelNodes = prop.getProperty("spring.redis.sentinel.nodes");
        if(masterName == null
                || masterName.trim().length() == 0
                || sentinelNodes == null
                || sentinelNodes.trim().length() == 0){
            //单节点 redis pool初始化
            initSingleCommonPool(prop);
        }else{
            //初始化哨兵池
            initSentinelsPool(prop, masterName, sentinelNodes);
        }

    }

    /**
     * 初始化连接池配置
     *
     * @param prop
     * @return
     */
    private static JedisPoolConfig initPoolConfig(Properties prop) {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(toInteger(prop, "spring.redis.pool.max-active"));
        config.setMaxWaitMillis(toInteger(prop, "spring.redis.pool.max-wait"));
        config.setMaxIdle(toInteger(prop, "spring.redis.pool.max-idle"));
        config.setMinIdle(toInteger(prop, "spring.redis.pool.min-idle"));
        return config;
    }

    /**
     * 初始化哨兵池
     *
     * @param prop
     */
    private static void initSentinelsPool(Properties prop, String masterName, String sentinels) {
        LOGGER.info("init sentinel redis pool start... masterName={},sentinels={}",masterName,sentinels);
        JedisPoolConfig config = initPoolConfig(prop);
        String timeoutStr = prop.getProperty("spring.redis.timeout");
        String password = prop.getProperty("spring.redis.password");
        Set<String> sentinelsSet = new HashSet<String>(
                Arrays.asList(getHostAndPorts(sentinels)));
        pool = new JedisSentinelPool(masterName,
                sentinelsSet, config, Integer.parseInt(timeoutStr.trim()),password);
        LOGGER.info("init sentinel redis pool end... masterName={},sentinels={}",masterName,sentinels);
    }


    /**
     * 初始化单节点的redis池
     *
     * @param prop
     */
    private static void initSingleCommonPool(Properties prop) {
        LOGGER.info("init single redis pool start...");
        JedisPoolConfig config = initPoolConfig(prop);
        String host = prop.getProperty("spring.redis.host");
        String portStr = prop.getProperty("spring.redis.port");
        if(host == null || portStr == null
                || host.trim().length() == 0
                || portStr.trim().length() == 0){
            throw new RuntimeException("init single redis pool host and port must be specified ");
        }
        String timeoutStr = prop.getProperty("spring.redis.timeout");
        String password = prop.getProperty("spring.redis.password");
        pool = new JedisPool(config, host, Integer.parseInt(portStr),Integer.parseInt(timeoutStr),password);
        LOGGER.info("init single redis pool success...");
    }

    /**
     * 获取主机与端口号列表
     *
     * @param sentinels redis ip列表
     * @return
     */
    private static String[] getHostAndPorts(String sentinels) {
        return sentinels.trim().split(",");
    }


    /***
     * 处理prop中 integer 类型数据
     * @param prop
     * @param key
     * @return
     */
    private static int toInteger(Properties prop, String key) {
        return Integer.parseInt(prop.getProperty(key).trim());
    }

    /***
     * 处理prop中 String 类型数据
     * @param prop
     * @param key
     * @return
     */
    private static boolean toBoolean(Properties prop, String key) {
        return Boolean.valueOf(prop.getProperty(key).trim());
    }

    /**
     * 模板方法
     *
     * @param rc
     * @return
     */
    public static <T> T call(RedisCall<T> rc) {
        Jedis jedis = null;
        try {
            jedis = RedisClient.getResource();
            return rc.run(jedis);
        } finally {
            if (jedis != null) {
                RedisClient.closeResource(jedis);
            }
        }

    }


}
