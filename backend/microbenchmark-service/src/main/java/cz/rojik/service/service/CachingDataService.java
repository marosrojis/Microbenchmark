package cz.rojik.service.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Marek Rojik (marek@rojik.cz) on 05. 01. 2019
 */
public interface CachingDataService {

    Map<String, List<String>> getJavaLibraries();
    Set<String> getIgnoreClasses();

    void evictLibrariesCacheValues();
    void evictIgnoreClassesCacheValues();
}
