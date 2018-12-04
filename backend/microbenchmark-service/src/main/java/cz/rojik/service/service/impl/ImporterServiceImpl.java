package cz.rojik.service.service.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import cz.rojik.service.constants.OtherConstants;
import cz.rojik.service.exception.ReadFileException;
import cz.rojik.service.utils.pojo.ImportsResult;
import cz.rojik.service.service.ImporterService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class ImporterServiceImpl implements ImporterService {

    private static Logger logger = LoggerFactory.getLogger(ImporterServiceImpl.class);


    private static final String LIBRARIES_FILE = "importer_files/libraries.json";
    private static final String IGNORE_CLASS_FILE = "importer_files/ignore_class.txt";

    private final Pattern JAVA_PACKAGE_CLASS_REGEX = Pattern.compile(OtherConstants.JAVA_PACKAGE_CLASS_REGEX);

    private Map<String, List<String>> javaLibraries;
    private Set<String> ignoreClasses;

    public ImporterServiceImpl() {
        javaLibraries = readJavaLibraries();
        ignoreClasses = readIgnoreClasses();
    }

    @Override
    public ImportsResult getLibrariesToImport(ImportsResult imports, String input) {
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
        imports.getLibrariesToChoose().putAll(selectImports);

        return imports;
    }

    // PRIVATE

    private Map<String, List<String>> readJavaLibraries() {
        Gson gson = new Gson();
        Type type = new TypeToken<HashMap<String, List<String>>>(){}.getType();
        HashMap<String, List<String>> libraries = gson.fromJson(cz.rojik.service.utils.FileUtils.readFileFromResource(LIBRARIES_FILE), type);

        return libraries;
    }

    private Set<String> readIgnoreClasses() {
        Resource resource = new ClassPathResource(IGNORE_CLASS_FILE);
        Set<String> ignoreClasses = null;
        try {
            ignoreClasses = new BufferedReader(new InputStreamReader(resource.getInputStream()))
                    .lines().collect(Collectors.toSet());

        } catch (IOException e) {
            throw new ReadFileException(IGNORE_CLASS_FILE);
        }
        return ignoreClasses;
    }
}
