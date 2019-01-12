package cz.rojik.service.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import cz.rojik.backend.dto.PropertyDTO;
import cz.rojik.backend.service.PropertyService;
import cz.rojik.service.constants.OtherConstants;
import cz.rojik.service.exception.ReadFileException;
import cz.rojik.service.service.CachingDataService;
import cz.rojik.service.utils.pojo.ImportsResult;
import cz.rojik.service.service.ImporterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;

/**
 * @author Marek Rojik (marek@rojik.cz) on 05. 01. 2019
 */
@Service
public class ImporterServiceImpl implements ImporterService {

    private static Logger logger = LoggerFactory.getLogger(ImporterServiceImpl.class);

    private final Pattern JAVA_PACKAGE_CLASS_REGEX = Pattern.compile(OtherConstants.JAVA_PACKAGE_CLASS_REGEX);
    private static final int REMOVE_CLASS = 6;

    @Autowired
    private CachingDataService cachingDataService;

    @Autowired
    private PropertyService propertyService;

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
                } else { // input contains only potential class name
                    String className = m.group(2);
                    if (!ignoreClasses.contains(className) && javaLibraries.containsKey(className)) {
                        List<String> packages = javaLibraries.get(className);
                        if (packages.size() == 1) {
                            String library = new StringBuilder().append(packages.get(0)).append(".").append(className).toString();
                            libraries.add(library);
                        } else {
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

    @Override
    public PropertyDTO processFolderWithJars(String folder) {
        Map<String, List<String>> classes = readFolderWithJar(folder);

        Gson gson = new GsonBuilder().create();
        String result = gson.toJson(classes);

        PropertyDTO properties = propertyService.updateProperty(new PropertyDTO()
                .setKey(OtherConstants.LIBRARIES_CACHE)
                .setValue(result)
        );

        String ignoreClasses = getIgnoreClasses(classes);
        propertyService.updateProperty(new PropertyDTO()
                .setKey(OtherConstants.IGNORE_CLASSES_CACHE)
                .setValue(ignoreClasses)
        );

        cachingDataService.evictLibrariesCacheValues();

        return properties;
    }

    private Map<String, List<String>> readFolderWithJar(String folder) {
        JarFile jarFile;
        Map<String, List<String>> classes = new HashMap<>();

        File directory = new File(folder);
        if (!directory.exists()) {
            logger.error("Read folder {} with JAR files is fail.", folder);
            throw new ReadFileException(folder);
        }

        logger.debug("Read all files from folder {}", folder);
        File[] fList = directory.listFiles();
        for (File file : fList) {
            try {
                jarFile = new JarFile(file);
            } catch (IOException e) {
                logger.error("Read JAR file {} is fail.", file.getAbsoluteFile());
                throw new ReadFileException(folder, e);
            }

            Enumeration<? extends JarEntry> enumeration = jarFile.entries();
            while (enumeration.hasMoreElements()) {
                ZipEntry zipEntry = enumeration.nextElement();

                if (zipEntry.getName().contains(".class") && !zipEntry.getName().contains("$")) {
                    String fileName = zipEntry.getName().substring(0, zipEntry.getName().length() - REMOVE_CLASS);
                    String path = fileName.substring(0, fileName.lastIndexOf("/"));
                    fileName = fileName.substring(fileName.lastIndexOf("/") + 1, fileName.length());
                    path = path.replaceAll("/", ".");

                    if (classes.containsKey(fileName)) {
                        classes.get(fileName).add(path);
                    } else {
                        List<String> tmp = new ArrayList<>();
                        tmp.add(path);
                        classes.put(fileName, tmp);
                    }
                }
            }

        }
        return classes;
    }

    private String getIgnoreClasses(Map<String, List<String>> classes) {
        Set<String> ignoreClasses = new HashSet<>();
        classes.forEach((key, value) -> {
            if (value.contains(OtherConstants.PACKAGE_IGNORE_CLASSES)) {
                ignoreClasses.add(key);
            }
        });

        String result = ignoreClasses.stream().collect(Collectors.joining(OtherConstants.PACKAGE_IGNORE_CLASSES_SEPARATOR));
        return result;
    }
}
