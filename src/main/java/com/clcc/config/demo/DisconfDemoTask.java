package com.clcc.config.demo;

import com.baidu.disconf.client.common.annotations.DisconfItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by fanchao on 2016/10/28.
 */
@Service
public class DisconfDemoTask {

    protected static final Logger LOGGER = LoggerFactory
            .getLogger(DisconfDemoTask.class);

    @Autowired
    private SimpleRedisService simpleRedisService;

    @Autowired
    private JedisConfig jedisConfig;

    private static final String REDIS_KEY = "disconf_key";


    public static final String max_discount_key = "maxDiscount";

    private Integer maxDiscount = 100;

    /**
     *
     */
    public int run() {
        try {
            while (true) {
                Thread.sleep(5000);
                LOGGER.info("redis( " + jedisConfig.getHost() + ","
                        + jedisConfig.getPort() + ")  get key: " + REDIS_KEY
                        + simpleRedisService.getKey(REDIS_KEY));
                LOGGER.info("max_discount:" + getMaxDiscount());
            }
        } catch (Exception e) {
            LOGGER.error(e.toString(), e);
        }
        return 0;
    }

    @DisconfItem(key = max_discount_key)
    public Integer getMaxDiscount() {
        return maxDiscount;
    }

    public void setMaxDiscount(Integer maxDiscount) {
        this.maxDiscount = maxDiscount;
    }
}
