package com.hyb.redis;

/**
 * Created by HuangYibo on 2018/3/26.
 */
public interface UserService {
    /**
     * 新增
     */
     boolean addUser(User entity);

    /**
     * 根据id 查询
     */
     String queryUserByUserId(User user);
}
