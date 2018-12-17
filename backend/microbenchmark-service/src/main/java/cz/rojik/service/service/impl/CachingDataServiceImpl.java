package cz.rojik.service.service.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import cz.rojik.service.constants.OtherConstants;
import cz.rojik.service.exception.ReadFileException;
import cz.rojik.service.service.CachingDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CachingDataServiceImpl implements CachingDataService {

    private static Logger logger = LoggerFactory.getLogger(ImporterServiceImpl.class);

    private static final String LIBRARIES_FILE = "importer_files/libraries.json";
    private static final String IGNORE_CLASS_FILE = "importer_files/ignore_class.txt";

    @Cacheable(OtherConstants.LIBRARIES_CACHE)
    @Override
    public Map<String, List<String>> getJavaLibraries() {
        logger.trace("Read file with all java classes and packages.");

        Gson gson = new Gson();
        Type type = new TypeToken<HashMap<String, List<String>>>(){}.getType();
        HashMap<String, List<String>> libraries = gson.fromJson(cz.rojik.service.utils.FileUtils.readFileFromResource(LIBRARIES_FILE), type);

        return libraries;
    }

    @Cacheable(OtherConstants.IGNORE_CLASSES_CACHE)
    @Override
    public Set<String> getIgnoreClasses() {
        logger.trace("Read file with classes to ignore import");
        Resource resource = new ClassPathResource(IGNORE_CLASS_FILE);
        Set<String> ignoreClasses = null;
        try {
            ignoreClasses = new BufferedReader(new InputStreamReader(resource.getInputStream()))
                    .lines().collect(Collectors.toSet());

        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new ReadFileException(IGNORE_CLASS_FILE);
        }
        return ignoreClasses;
    }

    @CacheEvict(value = OtherConstants.LIBRARIES_CACHE, allEntries = true)
    @Override
    public void evictLibrariesCacheValues() {
    }

    @CacheEvict(value = OtherConstants.IGNORE_CLASSES_CACHE, allEntries = true)
    @Override
    public void evictIgnoreClassesCacheValues() {
    }
}
