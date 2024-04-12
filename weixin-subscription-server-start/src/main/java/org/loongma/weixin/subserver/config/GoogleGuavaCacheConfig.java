package org.loongma.weixin.subserver.config;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class GoogleGuavaCacheConfig {

    @Bean(name = "rateLimitCache")
    public Cache<String, String> rateLimitCache() {
        // 缓存某个用户的访问记录，过期时间 10 min
        return CacheBuilder.newBuilder()
                .expireAfterAccess(10, TimeUnit.MINUTES)
                .build();
    }
}
