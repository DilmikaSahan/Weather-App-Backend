package com.App.WeatherApp.controller;

import com.App.WeatherApp.model.CacheStatus;
import com.github.benmanes.caffeine.cache.stats.CacheStats;
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
    private final CacheManager cacheManager;

    public CacheDebugController(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @GetMapping("/status")
    public List<CacheStatus> getCacheStatus() {
        List<CacheStatus> stats = new ArrayList<>();

        cacheManager.getCacheNames().forEach(name -> {
            CaffeineCache cache = (CaffeineCache) cacheManager.getCache(name);
            if (cache != null) {
                CacheStats s = cache.getNativeCache().stats();
                stats.add(new CacheStatus(name, s.hitCount(), s.missCount()));
            }
        });
        return stats;
    }

}
