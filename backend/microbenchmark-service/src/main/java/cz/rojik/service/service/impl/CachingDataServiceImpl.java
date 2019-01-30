package cz.rojik.service.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import cz.rojik.backend.dto.PropertyDTO;
import cz.rojik.backend.exception.EntityNotFoundException;
import cz.rojik.backend.service.PropertyService;
import cz.rojik.service.constants.OtherConstants;
import cz.rojik.service.exception.ReadFileException;
import cz.rojik.service.service.CachingDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Marek Rojik (marek@rojik.cz) on 05. 01. 2019
 */
@Service
public class CachingDataServiceImpl implements CachingDataService {

    private static Logger LOGGER = LoggerFactory.getLogger(ImporterServiceImpl.class);

    private static final String LIBRARIES_FILE = "importer_files/libraries.json";
    private static final String IGNORE_CLASS_FILE = "importer_files/ignore_class.txt";

    @Autowired
    private PropertyService propertiesService;

    @Cacheable(OtherConstants.LIBRARIES_CACHE)
    @Override
    public Map<String, List<String>> getJavaLibraries() {
        LOGGER.trace("Read file with all java classes and packages.");

        Gson gson = new GsonBuilder().create();
        Type type = new TypeToken<HashMap<String, List<String>>>(){}.getType();
        HashMap<String, List<String>> libraries;
        PropertyDTO properties;

        try {
            properties = propertiesService.getByKey(OtherConstants.LIBRARIES_CACHE);
        } catch (EntityNotFoundException e) {
            LOGGER.debug("Property '{}' was not found in database", OtherConstants.LIBRARIES_CACHE);

            libraries = gson.fromJson(cz.rojik.service.utils.FileUtils.readFileFromResource(LIBRARIES_FILE), type);
            return libraries;
        }

        LOGGER.debug("Read libraries properties from database.");
        libraries = gson.fromJson(properties.getValue(), type);
        return libraries;

    }

    @Cacheable(OtherConstants.IGNORE_CLASSES_CACHE)
    @Override
    public Set<String> getIgnoreClasses() {
        LOGGER.trace("Read file with classes to ignore import");

        Set<String> ignoreClasses;
        PropertyDTO properties;

        try {
            properties = propertiesService.getByKey(OtherConstants.IGNORE_CLASSES_CACHE);
        } catch (EntityNotFoundException e) {
            LOGGER.debug("Property '{}' was not found in database", OtherConstants.IGNORE_CLASSES_CACHE);
            LOGGER.debug("Get ignore classes from file.");

            ignoreClasses = readIgnoreClasses();
            return ignoreClasses;
        }

        LOGGER.debug("Get ignore classes properties from database.");
        ignoreClasses = new HashSet<>(Arrays.asList(properties.getValue().split(OtherConstants.PACKAGE_IGNORE_CLASSES_SEPARATOR)));
        return ignoreClasses;
    }

    @CacheEvict(value = OtherConstants.LIBRARIES_CACHE, allEntries = true)
    @Override
    public void evictLibrariesCacheValues() {
        LOGGER.debug("Clear cache '{}'", OtherConstants.LIBRARIES_CACHE);
    }

    @CacheEvict(value = OtherConstants.IGNORE_CLASSES_CACHE, allEntries = true)
    @Override
    public void evictIgnoreClassesCacheValues() {
        LOGGER.debug("Clear cache '{}'", OtherConstants.IGNORE_CLASSES_CACHE);
    }

    // PRIVATE

    /**
     * Get all classes that can be ignore during importing process
     * @return collection of ignore classes
     */
    private Set<String> readIgnoreClasses() {
        Set<String> ignoreClasses;
        Resource resource = new ClassPathResource(IGNORE_CLASS_FILE);
        try {
            ignoreClasses = new BufferedReader(new InputStreamReader(resource.getInputStream()))
                    .lines().collect(Collectors.toSet());

        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new ReadFileException(IGNORE_CLASS_FILE);
        }
        return ignoreClasses;
    }
}
