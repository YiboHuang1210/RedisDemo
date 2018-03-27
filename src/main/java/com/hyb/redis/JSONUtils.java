package com.hyb.redis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;


/**
 * Created by HuangYibo on 2018/3/26.
 */
public class JSONUtils {

     public static String toJsonString(Object object)
    {
        return JSON.toJSONString(object);
    }

    public static Object jsonParseObject(String jsonString)
    {
        return JSON.parseObject(jsonString, Object.class);
    }

}
