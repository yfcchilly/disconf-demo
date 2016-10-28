package com.clcc.config.demo;

import com.baidu.disconf.client.common.annotations.DisconfUpdateService;
import com.baidu.disconf.client.common.update.IDisconfUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by fanchao on 2016/10/28.
 */
@Service
@DisconfUpdateService(classes = {DisconfDemoTask.class}, itemKeys = {DisconfDemoTask.max_discount_key})
public class DiscountUpdateCallback implements IDisconfUpdate {


    private static final Logger LOGGER = LoggerFactory
            .getLogger(DiscountUpdateCallback.class);

    @Autowired
    private DisconfDemoTask disconfDemoTask;

    public void reload() throws Exception {
        LOGGER.info(DisconfDemoTask.max_discount_key + "be updated, new value=" + disconfDemoTask.getMaxDiscount());
    }
}
