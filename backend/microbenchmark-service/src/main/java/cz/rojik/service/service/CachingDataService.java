package cz.rojik.service.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface CachingDataService {

    Map<String, List<String>> getJavaLibraries();
    Set<String> getIgnoreClasses();

    void evictLibrariesCacheValues();
    void evictIgnoreClassesCacheValues();
}
