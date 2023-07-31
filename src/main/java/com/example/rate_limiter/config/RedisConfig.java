package com.example.rate_limiter.config;

import io.github.bucket4j.distributed.proxy.ProxyManager;
import io.github.bucket4j.grid.jcache.JCacheProxyManager;
import org.redisson.config.Config;
import org.redisson.jcache.configuration.RedissonConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.cache.CacheManager;
import javax.cache.Caching;

@Configuration
@EnableCaching
public class RedisConfig {

    private static final String RATE_LIMITER_CACHE = "rate_limiter_cache";

    @Bean
    public Config config() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://localhost:6379");
        return config;
    }

    @Bean
    public CacheManager rateLimiterCache(Config config) {
        CacheManager cacheManager = Caching.getCachingProvider().getCacheManager();
        cacheManager.createCache(RATE_LIMITER_CACHE, RedissonConfiguration.fromConfig(config));
        return cacheManager;
    }

    @Bean
    ProxyManager<String> proxyManager(CacheManager rateLimiterCache) {
        return new JCacheProxyManager<>(rateLimiterCache.getCache(RATE_LIMITER_CACHE));
    }

}