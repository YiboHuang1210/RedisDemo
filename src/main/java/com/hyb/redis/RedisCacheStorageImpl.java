package com.hyb.redis;

import org.apache.commons.logging.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisException;
import java.util.HashMap;
import java.util.Map;

/**
 * redis 缓存存储器实现
 * Created by HuangYibo on 2018/3/26.
 */
public class RedisCacheStorageImpl<V> implements RedisCacheStorage<String, V> {

    /**
     * 日志记录
     */
    public static final Log LOG = LogFactory.getLog(RedisCacheStorageImpl.class);

    /**
     * redis 客户端
     */
    @Autowired
    private RedisClient redisClient;

    /**
     * 默认过时时间
     */
    private static final int EXPRIE_TIME =3600*24;

    public void setRedisClient(RedisClient redisClient) {
        this.redisClient = redisClient;
    }

    /**
     * 在redis数据库中插入 key  和value
     * @param key
     * @param value
     * @return
     */
    @Override
    public boolean set(String key, V value) {
        //设置默认过时时间
        return set(key, value, EXPRIE_TIME);
    }

    /**
     * 在redis数据库中插入 key  和value 并且设置过期时间
     * @param key
     * @param value
     * @param exp 过期时间 s
     * @return
     */
    @SuppressWarnings("finally")
    @Override
    public boolean set(String key, V value, int exp) {
        Jedis jedis =null;
        //将key 和value  转换成 json 对象
        String jKey = JSONUtils.toJsonString(key);
        String jValue = JSONUtils.toJsonString(value);
        //操作是否成功
        boolean isSucess =true;
        if(!StringUtils.isEmpty(jKey)){
            LOG.info("key is empty");
            return false;
        }
        try {
            //获取客户端对象
            jedis =redisClient.getResource();
            //执行插入
            jedis.setex(jKey, exp, jValue);
        } catch (JedisException e) {
            LOG.info("client can't connect server");
            isSucess =false;
            if(null !=jedis){
                //释放jedis对象
                redisClient.brokenResource(jedis);
            }
            return false;
        }finally{
            if(isSucess){
                //返还连接池
                redisClient.returnResource(jedis);
            }
            return true;
        }
    }

    /**
     * 根据key 去redis 中获取value
     * @param key
     * @return
     */
    @SuppressWarnings("unchecked")
    @Override
    public V get(String key) {
        Jedis jedis =null;
        //将key 和value  转换成 json 对象
        String jKey = JSONUtils.toJsonString(key);
        V jValue =null;
        //key 不能为空
        if(StringUtils.isEmpty(jKey)){
            LOG.info("key is empty");
            return null;
        }
        try {
            //获取客户端对象
            jedis =redisClient.getResource();
            //执行查询
            String value =  jedis.get(jKey);
            //判断值是否非空
            if(StringUtils.isEmpty(value)){
                return null;
            }else{
                jValue= (V) JSONUtils.jsonParseObject(value);
            }
            //返还连接池
            redisClient.returnResource(jedis);
        } catch (JedisException e) {
            LOG.info("client can't connect server");
            if(null !=jedis){
                //释放jedis 对象
                redisClient.brokenResource(jedis);
            }
        }
        return jValue;
    }

    /**
     * 删除redis库中的数据
     * @param key
     * @return
     */
    @SuppressWarnings("finally")
    @Override
    public boolean remove(String key) {
        Jedis jedis =null;
        //将key 和value  转换成 json 对象
        String jKey = JSONUtils.toJsonString(key);
        //操作是否成功
        boolean isSucess =true;
        if(StringUtils.isEmpty(jKey)){
            LOG.info("key is empty");
            return false;
        }
        try {
            jedis =redisClient.getResource();
            //执行删除
            jedis.del(jKey);
        } catch (JedisException e) {
            LOG.info("client can't connect server");
            isSucess =false;
            if(null !=jedis){
                //释放jedis 对象
                redisClient.brokenResource(jedis);
            }
            return false;
        }finally{
            if (isSucess) {
                //返还连接池
                redisClient.returnResource(jedis);
            }
            return true;
        }
    }

    /**
     * 设置哈希类型数据到redis 数据库
     * @param cacheKey 可以看做一张表
     * @param key   表字段
     * @param value
     * @return
     */
    @SuppressWarnings("finally")
    @Override
    public boolean hset(String cacheKey, String key, V value) {
        Jedis jedis =null;
        //将key 和value  转换成 json 对象
        String jKey = JSONUtils.toJsonString(key);
        String jCacheKey = JSONUtils.toJsonString(cacheKey);
        String jValue = JSONUtils.toJsonString(value);
        //操作是否成功
        boolean isSucess =true;
        if(StringUtils.isEmpty(jCacheKey)){
            LOG.info("cacheKey is empty");
            return false;
        }
        try {
            jedis =redisClient.getResource();
            //执行插入哈希
            jedis.hset(jCacheKey, jKey, jValue);
        } catch (JedisException e) {
            LOG.info("client can't connect server");
            isSucess =false;
            if(null !=jedis){
                //释放jedis 对象
                redisClient.brokenResource(jedis);
            }
            return false;
        }finally{
            if (isSucess) {
                //返还连接池
                redisClient.returnResource(jedis);
            }
            return true;
        }
    }

    /**
     * 获取哈希表数据类型的值
     * @param cacheKey
     * @param key
     * @return
     */
    @SuppressWarnings("unchecked")
    @Override
    public V hget(String cacheKey, String key) {
        Jedis jedis =null;
        //将key 和value  转换成 json 对象
        String jKey = JSONUtils.toJsonString(key);
        String jCacheKey = JSONUtils.toJsonString(cacheKey);
        V jValue =null;
        if(StringUtils.isEmpty(jCacheKey)){
            LOG.info("cacheKey is empty");
            return null;
        }
        try {
            //获取客户端对象
            jedis =redisClient.getResource();
            //执行查询
            String value =  jedis.hget(jCacheKey, jKey);
            //判断值是否非空
            if(StringUtils.isEmpty(value)){
                return null;
            }else{
                jValue= (V) JSONUtils.jsonParseObject(value);
            }
            //返还连接池
            redisClient.returnResource(jedis);
        } catch (JedisException e) {
            LOG.info("client can't connect server");
            if(null !=jedis){
                //释放jedis 对象
                redisClient.brokenResource(jedis);
            }
        }
        return jValue;
    }

    /**
     * 获取哈希类型的数据
     * @param cacheKey
     * @return
     */
    @Override
    public Map<String, V> hget(String cacheKey) {
        String jCacheKey = JSONUtils.toJsonString(cacheKey);
        //非空校验
        if(StringUtils.isEmpty(jCacheKey)){
            LOG.info("cacheKey is empty!");
            return null;
        }
        Jedis jedis =null;
        Map<String,V> result =null;
        try {
            jedis =redisClient.getResource();
            //获取列表集合
            Map<String,String> map = jedis.hgetAll(jCacheKey);

            if(null !=map){
                for(Map.Entry<String, String> entry : map.entrySet()){
                    if(result ==null){
                        result =new HashMap<String,V>();
                    }
                    result.put((String) JSONUtils.jsonParseObject(entry.getKey()), (V) JSONUtils.jsonParseObject(entry.getValue()));
                }
            }
        } catch (JedisException e) {
            LOG.info("client can't connect server");
            if(null !=jedis){
                //释放jedis 对象
                redisClient.brokenResource(jedis);
            }
        }
        return result;
    }

}
