package com.App.WeatherApp.controller;

import com.App.WeatherApp.model.CacheStatus;
import com.github.benmanes.caffeine.cache.stats.CacheStats;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/cache")
public class CacheDebugController {
    private final CacheManager weatherCacheManager;
    private final CacheManager forecastCacheManager;

    public CacheDebugController(
            CacheManager weatherCacheManager,
            @Qualifier("forecastCacheManager") CacheManager forecastCacheManager
    ) {
        this.weatherCacheManager = weatherCacheManager;
        this.forecastCacheManager = forecastCacheManager;
    }

    @GetMapping("/status")
    public List<CacheStatus> getCacheStatus() {
        List<CacheStatus> stats = new ArrayList<>();

        collectStats(weatherCacheManager, stats);
        collectStats(forecastCacheManager, stats);

        return stats;
    }

    private void collectStats(CacheManager manager, List<CacheStatus> stats) {
        manager.getCacheNames().forEach(name -> {
            CaffeineCache cache = (CaffeineCache) manager.getCache(name);
            if (cache != null) {
                CacheStats s = cache.getNativeCache().stats();
                stats.add(new CacheStatus(
                        name,
                        s.hitCount(),
                        s.missCount()
                ));
            }
        });
    }

}
