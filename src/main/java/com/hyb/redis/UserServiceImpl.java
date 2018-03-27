package com.hyb.redis;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 业务层接口实现
 * Created by HuangYibo on 2018/3/26.
 */
@Service
public class UserServiceImpl implements UserService {
    private static final String  cacheKey  ="userEntity";
    /**
     * 缓存存储
     */
    private RedisCacheStorageImpl<User> storageCache;

    public void setStorageCache(RedisCacheStorageImpl<User> storageCache) {
        this.storageCache = storageCache;
    }
    /**
     * 新增
     * @param user
     * @return
     */
    @Override
    public boolean addUser(User user) {
        //非空
        if(user ==null || StringUtils.isEmpty(user.getUserId())){
            return false;
        }
        /**
         * 做数据库持久化，这里就无需再申明了
         */
        System.out.println("先插入数据库中,.........");
        //然后接下来做非关系型数据库持久化
        return storageCache.hset(cacheKey, user.getUserId(), user);
    }

    /**
     * 根据id 查询
     * @return
     */
    @Override
    public String queryUserByUserId(User user) {
        //非空
        if(user ==null || StringUtils.isEmpty(user.getUserId())){
            return null;
        }
        //先去缓存中查询 是否存在，不存在在查询
        String result = JSONUtils.toJsonString(storageCache.hget(cacheKey, user.getUserId())) ;
        if(result != null){
            return result ;
        }else{
            //查询数据库
            System.out.println("查询数据库");
        }
        return null;
    }
}
