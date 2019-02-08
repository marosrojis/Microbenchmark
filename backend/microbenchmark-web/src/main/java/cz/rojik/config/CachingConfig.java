package cz.rojik.config;

import cz.rojik.backend.constants.PropertyConstants;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/**
 * @author Marek Rojik (marek@rojik.cz) on 05. 01. 2019
 */
@Configuration
@EnableCaching
public class CachingConfig {

    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(Arrays.asList(
                new ConcurrentMapCache(PropertyConstants.LIBRARIES_CACHE),
                new ConcurrentMapCache(PropertyConstants.IGNORE_CLASSES_CACHE)));
        return cacheManager;
    }
}
