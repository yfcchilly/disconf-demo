package com.clcc.config.demo;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

/**
 * Created by fanchao on 2016/10/28.
 */
@Service
@Scope("singleton")
public class SimpleRedisService implements InitializingBean, DisposableBean {

    // jedis 实例
    private Jedis jedis = null;

    /**
     * 分布式配置
     */
    @Autowired
    private JedisConfig jedisConfig;

    /**
     * 关闭
     */
    public void destroy() throws Exception {

        if (jedis != null) {
            jedis.disconnect();
        }
    }

    /**
     * 进行连接
     */
    public void afterPropertiesSet() throws Exception {
        jedis = new Jedis(jedisConfig.getHost(), jedisConfig.getPort());
    }

    /**
     * 获取一个值
     *
     * @param key
     * @return
     */
    public String getKey(String key) {
        if (jedis != null) {
//            return jedis.get(key);
        }

        return null;
    }
}
