package com.App.WeatherApp.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CashConfig {
    public  static final String WEATHER_CACHE = "weatherCache";
    public  static final String COMFORT_INDEX_CACHE = "comfortIndexCache";
    public static final String FORECAST_CACHE =  "forecastCache";

    public static final int WEATHER_CACHE_TTL_MINUTES = 5;
    public static final int FORECAST_CACHE_TTL_HOURS = 3;   // 3 hour cache for FORECAST_CACHE (openWeatherMap API forecut weather in 3-hour intervals )

    @Bean
    @Primary
    public CacheManager weatherCacheManager() {
        CaffeineCacheManager manager = new CaffeineCacheManager(WEATHER_CACHE,COMFORT_INDEX_CACHE);
        manager.setCaffeine(Caffeine.newBuilder()
                .expireAfterWrite(WEATHER_CACHE_TTL_MINUTES, TimeUnit.MINUTES)
                .maximumSize(1000)
                .recordStats());
        return manager;
    }
    @Bean(name = "forecastCacheManager")
    public CacheManager forecastCacheManager() {
        CaffeineCacheManager manager = new CaffeineCacheManager(FORECAST_CACHE);
        manager.setCaffeine(Caffeine.newBuilder()
                .expireAfterWrite(FORECAST_CACHE_TTL_HOURS, TimeUnit.HOURS)
                .maximumSize(1000)
                .recordStats());
        return manager;
    }

}
