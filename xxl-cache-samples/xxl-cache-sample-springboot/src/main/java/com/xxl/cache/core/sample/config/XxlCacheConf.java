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
    @Value("${xxl.cache.l1.maxSize}")
    private int maxSize;
    @Value("${xxl.cache.l1.expireAfterWrite}")
    private long expireAfterWrite;
    @Value("${xxl.cache.l2.provider}")
    private String l2Provider;
    @Value("${xxl.cache.l2.serializer}")
    private String serializer;
    @Value("${xxl.cache.l2.nodes}")
    private String nodes;
    @Value("${xxl.cache.l2.user}")
    private String user;
    @Value("${xxl.cache.l2.password}")
    private String password;

    @Bean(initMethod = "start", destroyMethod = "stop")
    public XxlCacheFactory xxlCacheFactory() {
        XxlCacheFactory xxlCacheFactory = new XxlCacheFactory();
        xxlCacheFactory.setL1Provider(l1Provider);
        xxlCacheFactory.setMaxSize(maxSize);
        xxlCacheFactory.setExpireAfterWrite(expireAfterWrite);
        xxlCacheFactory.setL2Provider(l2Provider);
        xxlCacheFactory.setSerializer(serializer);
        xxlCacheFactory.setNodes(nodes);
        xxlCacheFactory.setUser(user);
        xxlCacheFactory.setPassword(password);
        return xxlCacheFactory;
    }

}
