package cz.rojik.service.service.impl;

import cz.rojik.backend.service.PropertiesService;
import cz.rojik.service.constants.OtherConstants;
import cz.rojik.service.service.CachingDataService;
import cz.rojik.service.utils.pojo.ImportsResult;
import cz.rojik.service.service.ImporterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ImporterServiceImpl implements ImporterService {

    private static Logger logger = LoggerFactory.getLogger(ImporterServiceImpl.class);

    private final Pattern JAVA_PACKAGE_CLASS_REGEX = Pattern.compile(OtherConstants.JAVA_PACKAGE_CLASS_REGEX);

    @Autowired
    private CachingDataService cachingDataService;

    @Override
    public ImportsResult getLibrariesToImport(ImportsResult imports, String input) {
        logger.trace("Start finding all classes in project to import in file {}", input);

        Map<String, List<String>> javaLibraries = cachingDataService.getJavaLibraries();
        Set<String> ignoreClasses = cachingDataService.getIgnoreClasses();

        String[] inputs = input.split("[ =<>();\n\t]");
        Set<String> values = new HashSet<>(Arrays.asList(inputs));
        Set<String> libraries = new HashSet<>();
        Map<String, List<String>> selectImports = new HashMap<>();

        for (String value : values) {
            value = value.trim();
            Matcher m = JAVA_PACKAGE_CLASS_REGEX.matcher(value);
            if (m.find()) {
                // e.g. java.util.List, static class like Math (m.group(0) from Math.abs() => Math)
                if (!Character.isUpperCase(value.charAt(0)) && value.contains(".") && !ignoreClasses.contains(m.group(0))) {
                    libraries.add(m.group(0));
                }
                else { // input contains only potential class name
                    String className = m.group(2);
                    if (!ignoreClasses.contains(className) && javaLibraries.containsKey(className)) {
                        List<String> packages = javaLibraries.get(className);
                        if (packages.size() == 1) {
                            String library = new StringBuilder().append(packages.get(0)).append(".").append(className).toString();
                            libraries.add(library);
                        }
                        else {
                            selectImports.put(className, packages);
                        }
                    }
                }
            }
        }

        imports.getLibraries().addAll(libraries);
        logger.debug("All classes to automatic import {}", libraries);

        imports.getLibrariesToChoose().putAll(selectImports);
        logger.debug("All classes to choose which to import {}", selectImports);

        return imports;
    }

}
