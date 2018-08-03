package com.zm.provider.util.redis;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;

import com.alibaba.fastjson.JSON;
import com.zm.provider.util.CheckUtils;

/**
 * @author: peng.chen-2
 * @Date: 2018:03:21 下午7:42
 * @company: 易宝支付(YeePay)
 */
public class RedisToolUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisToolUtils.class);

    /**
     * 是否存在key
     * @param key
     * @return
     */
    public static boolean isKeyExist(final String key) {
        return RedisClient.call(new RedisCall<Boolean>() {
            @Override
            public Boolean run(Jedis jedis) {
                return jedis.exists(key);
            }
        });
    }

    /**
     * 对整数值（或整数字符串）加1
     *
     * @param key
     * @return
     */
    public static Long decr(final String key) {
        return RedisClient.call(new RedisCall<Long>() {

            @Override
            public Long run(Jedis jedis) {
                Long result = jedis.decr(key);
                return result;
            }

        });
    }


    /**
     * 对整数值（或整数字符串）加1
     *
     * @param key
     * @return
     */
    public static Long incr(final String key) {
        return RedisClient.call(new RedisCall<Long>() {

            @Override
            public Long run(Jedis jedis) {
                Long incr = jedis.incr(key);
                return incr;
            }

        });
    }

    /**
     * 对整数值（或整数字符串）加1,存在过期时间，秒级别
     *
     * @param key
     * @return
     */
    public static Long incr(final String key, final int seconds) {
        return RedisClient.call(new RedisCall<Long>() {

            @Override
            public Long run(Jedis jedis) {
                Long incr = jedis.incr(key);
                if (incr == 1l) {
                    jedis.expire(key, seconds);
                    return 1l;
                }
                return incr;

            }

        });
    }


    /**
     * 删除某个键
     *
     * @param key
     * @return
     */
    public static Boolean remove(final String key) {
        return RedisClient.call(new RedisCall<Boolean>() {

            @Override
            public Boolean run(Jedis jedis) {
                Long del = jedis.del(key);
                if (del > 0) {
                    return true;
                } else {
                    return false;
                }
            }

        });
    }


    /**
     * 添加一个元素，字符串
     *
     * @param key
     * @param value
     */
    public static void set(final String key, String value) {
        RedisClient.call(new RedisCall<Boolean>() {

            @Override
            public Boolean run(Jedis jedis) {
                String resule = jedis.set(key, value);
                if ("ok".equalsIgnoreCase(resule)) {
                    return true;
                } else {
                    return false;
                }
            }

        });
    }

    /**
     * 添加一个元素，实体
     *
     * @param key
     * @param value
     */
    public static void set(final String key, Object value) {
        String objStr = JSON.toJSONString(value);
        RedisClient.call(new RedisCall<Boolean>() {

            @Override
            public Boolean run(Jedis jedis) {
                String resule = jedis.set(key, objStr);
                if ("ok".equalsIgnoreCase(resule)) {
                    return true;
                } else {
                    return false;
                }
            }

        });
    }


    /**
     * 得到某个单一值，字符串
     *
     * @param key
     * @return
     */
    public static String get(final String key) {
        String str = null;
        try {
            str = RedisClient.call(new RedisCall<String>() {

                @Override
                public String run(Jedis jedis) {
                    String result = jedis.get(key);
                    if (CheckUtils.isNotEmpty(result)) {
                        return result;
                    } else {
                        return null;
                    }
                }

            });

        } catch (Throwable e) {
            LOGGER.error("get data from redis error!", e);
        }
        return str;
    }

    /**
     * 得到某个单一值,实体
     *
     * @param key
     * @param clazz
     * @return
     */
    public static <T> T get(final String key, Class<T> clazz) {
        T parseObject = null;
        try {
            String jsonStr = RedisClient.call(new RedisCall<String>() {

                @Override
                public String run(Jedis jedis) {
                    String result = jedis.get(key);
                    if (CheckUtils.isNotEmpty(result)) {
                        return result;
                    } else {
                        return null;
                    }
                }

            });

            parseObject = JSON.parseObject(jsonStr, clazz);
        } catch (Throwable e) {
            LOGGER.error("get data from redis error!", e);
        }
        return parseObject;
    }

    /**
     * 存有寿命的值
     *
     * @param key
     * @param surviveTimes
     * @param value
     */
    public static void setex(final String key, final int surviveTimes, final String value) {
        RedisClient.call(new RedisCall<Boolean>() {

            @Override
            public Boolean run(Jedis jedis) {
                String resule = jedis.setex(key, surviveTimes, value);
                if ("ok".equalsIgnoreCase(resule)) {
                    return true;
                } else {
                    return false;
                }
            }

        });
    }


    /**
     * 存有寿命的值不覆盖原值
     *
     * @param key
     * @param surviveTimes
     * @param value
     */
    public static void setexNotReTry(final String key, final int surviveTimes, final String value) {
        RedisClient.call(new RedisCall<Boolean>() {

            @Override
            public Boolean run(Jedis jedis) {
                String string = jedis.get(key);
                if (string == null) {
                    String resule = jedis.setex(key, surviveTimes, value);
                    if ("ok".equalsIgnoreCase(resule)) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    // 已有值时不会覆盖值
                    return true;
                }
            }

        });
    }


    /**
     * 在集合中添加一个元素
     *
     * @param key
     * @param value
     */
    public static void lpush(final String key, final String value) {
        RedisClient.call(new RedisCall<Boolean>() {

            @Override
            public Boolean run(Jedis jedis) {
                Long lpush = jedis.lpush(key, value);
                return true;
            }

        });
    }

    /**
     * 在集合中弹出一个元素
     *
     * @param key
     * @param clazz
     */
    public static <T> T rpop(final String key, final Class<T> clazz) throws Throwable {
        T parseObject = null;
        try {
            String str = RedisClient.call(new RedisCall<String>() {

                @Override
                public String run(Jedis jedis) {
                    String result = jedis.rpop(key);
                    if (CheckUtils.isNotEmpty(result)) {
                        return result;
                    } else {
                        return null;
                    }
                }

            });
            if (CheckUtils.isNotEmpty(str)) {
                parseObject = JSON.parseObject(str, clazz);
            }
            return parseObject;
        } catch (Throwable e) {
            throw e;
        }
    }


    public static String rpop(final String key) throws Throwable {
        try {
            return RedisClient.call(new RedisCall<String>() {
                @Override
                public String run(Jedis jedis) {
                    return jedis.rpop(key);
                }
            });
        } catch (Throwable e) {
            throw e;
        }
    }

    public static Long llen(final String key) throws Throwable {
        try {
            return RedisClient.call(new RedisCall<Long>() {
                @Override
                public Long run(Jedis jedis) {
                    return jedis.llen(key);
                }
            });
        } catch (Throwable e) {
            throw e;
        }
    }

    public static Long del(final String key){
        try {
            return RedisClient.call(new RedisCall<Long>() {
                @Override
                public Long run(Jedis jedis) {
                    return jedis.del(key);
                }
            });
        }catch (Throwable e){
        	return null;
        }
    }

    /**
     * 获取服务器时间(时间戳)
     * @return
     */
    public static Long time(){
        try {
            return RedisClient.call(new RedisCall<Long>() {
                @Override
                public Long run(Jedis jedis) {
                    return Long.valueOf(jedis.time().get(0));
                }
            });
        }catch (Throwable e){
        	return null;
        }
    }

    /**
     * 添加一个元素，实体
     * @param nxxx 只能取NX或者XX
     *             如果取NX，则只有当key不存在时才进行set;如果取XX，则只有当key已经存在时才进行set
     * @param expx 只能取EX或者PX
     *             代表数据过期时间的单位
     *             EX代表秒;PX代表毫秒
     */
    public static Boolean set(final String key, final String value, final String nxxx, final String expx, final int aliveTimes) {
        return RedisClient.call(new RedisCall<Boolean>() {
            @Override
            public Boolean run(Jedis jedis) {

                String resule = jedis.set(key, value, nxxx, expx, aliveTimes);

                if ("ok".equalsIgnoreCase(resule)) {
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    /**
     * 脚本执行
     */
    public static Object evel(final String script, List<String> keys, List<String> args) {

        return RedisClient.call(new RedisCall<Object>() {

            @Override
            public Object run(Jedis jedis) {

                return jedis.eval(script, keys, args);

            }
        });
    }
}
