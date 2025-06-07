package com.example.footballapi.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching // Enables Spring's annotation-driven cache management capability
public class CacheConfig {
    public static final String COUNTRIES_CACHE = "countriesCache";
    @Value("${app.features.cache-ttl-seconds.leagues:86400}") // Default to 24 hours if not set
    private long leaguesCacheTtlSeconds;

    @Value("${app.features.cache-ttl-seconds.standings:3600}") // Default to 1 hour if not set
    private long standingsCacheTtlSeconds;

    public static final String LEAGUES_CACHE = "leagues";
    public static final String STANDINGS_BY_LEAGUE_CACHE = "standingsByLeague";

    @Bean
    @Primary // Make this the default CacheManager
    public CacheManager caffeineCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();

        // Configure specific caches with different TTLs
        cacheManager.registerCustomCache(LEAGUES_CACHE,
                Caffeine.newBuilder()
                        .expireAfterWrite(leaguesCacheTtlSeconds, TimeUnit.SECONDS)
                        .maximumSize(100) // Max 100 entries for leagues cache
                        .build());

        cacheManager.registerCustomCache(STANDINGS_BY_LEAGUE_CACHE,
                Caffeine.newBuilder()
                        .expireAfterWrite(standingsCacheTtlSeconds, TimeUnit.SECONDS)
                        .maximumSize(200) // Max 200 entries for standings cache (e.g., different leagues)
                        .build());

        return cacheManager;
    }
}