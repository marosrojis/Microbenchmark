package cz.rojik.service.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Marek Rojik (marek@rojik.cz) on 05. 01. 2019
 */
public interface CachingDataService {

    /**
     * Get map contains all classes with packages from file or database
     * @return map contains classes with packages
     */
    Map<String, List<String>> getJavaLibraries();

    /**
     * Get list contains all classes which is no needed to import
     * @return classes which is no needed to import
     */
    Set<String> getIgnoreClasses();

    /**
     * Clear libraries from cache
     */
    void evictLibrariesCacheValues();

    /**
     * Clear ignore classes from cache
     */
    void evictIgnoreClassesCacheValues();
}
