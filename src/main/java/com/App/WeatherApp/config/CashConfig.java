package com.App.WeatherApp.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CashConfig {
    public  static final String WEATHER_CACHE = "weatherCache";
    public  static final String COMFORT_INDEX_CACHE = "comfortIndexCache";
    public static final int CACHE_TTL_MINUTES = 5;

    //Configure Caffeine cache manager with 5-minute TTL
    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager(
                WEATHER_CACHE,
                COMFORT_INDEX_CACHE
        );
        cacheManager.setCaffeine(caffeineCacheBuilder());
        return cacheManager;
    }
    @Bean
    public Caffeine<Object, Object> caffeineCacheBuilder() {
        return Caffeine.newBuilder()
                .expireAfterWrite(CACHE_TTL_MINUTES, TimeUnit.MINUTES)
                .maximumSize(1000)
                .recordStats();
    }

}
