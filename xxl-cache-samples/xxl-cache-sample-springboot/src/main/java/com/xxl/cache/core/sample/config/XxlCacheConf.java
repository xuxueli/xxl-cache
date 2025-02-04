package com.xxl.cache.core.sample.config;

import com.xxl.cache.core.factory.XxlCacheFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author xuxueli 2025-02-03
 */
@Configuration
public class XxlCacheConf {

    @Value("${xxl.cache.l1.provider}")
    private String l1Provider;
    @Value("${xxl.cache.l2.provider}")
    private String l2Provider;
    @Value("${xxl.cache.nodes}")
    private String nodes;
    @Value("${xxl.cache.password}")
    private String password;

    @Bean(initMethod = "start", destroyMethod = "stop")
    public XxlCacheFactory xxlCacheFactory() {
        XxlCacheFactory xxlCacheFactory = new XxlCacheFactory();
        xxlCacheFactory.setL1Provider(l1Provider);
        xxlCacheFactory.setL2Provider(l2Provider);
        xxlCacheFactory.setNodes(nodes);
        xxlCacheFactory.setPassword(password);
        return xxlCacheFactory;
    }

}
