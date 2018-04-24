package cz.rojik.service.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import cz.rojik.constants.OtherConstants;
import cz.rojik.service.ImporterService;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ImporterServiceImpl implements ImporterService {

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
    public Set<String> getLibrariesToImport(String input) {
        String[] inputs = input.split("[ =<>()\n\t]");
        Set<String> values = new HashSet<>(Arrays.asList(inputs));
        Set<String> libraries = new HashSet<>();
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
                            System.out.println("Select which package you want to use with class: " + className);
                            for (int i = 1; i <= packages.size(); i++) {
                                System.out.println(i + ") " + packages.get(i - 1));
                            }
                            Scanner sc = new Scanner(System.in);
                            int choice = sc.nextInt();
                            String library = new StringBuilder().append(packages.get(choice - 1)).append(".").append(className).toString();
                            libraries.add(library);
                        }
                    }
                }
            }
        }

        return libraries;
    }

    // PRIVATE

    private Map<String, List<String>> readJavaLibraries() {
        Gson gson = new Gson();
        Type type = new TypeToken<HashMap<String, List<String>>>(){}.getType();
        HashMap<String, List<String>> libraries = gson.fromJson(readLibrariesFile(), type);

        return libraries;
    }

    private String readLibrariesFile() {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(LIBRARIES_FILE).getFile());

        String fileContent = "";
        try {
            fileContent = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileContent;
    }

    private Set<String> readIgnoreClasses() {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(IGNORE_CLASS_FILE).getFile());

        List<String> fileContent = new ArrayList<>();
        try {
            fileContent = FileUtils.readLines(file, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Set<String> ignoreClasses = new HashSet<>(fileContent);
        return ignoreClasses;
    }
}
