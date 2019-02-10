package cz.rojik.backend.constants;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Marek Rojik (marek.rojik@inventi.cz) on 08. 02. 2019
 */
public class PropertyConstants {

    /**
     * KEY for save libraries to cache or database
     */
    public static final String LIBRARIES_CACHE = "libraries";

    /**
     * KEY for save ignore classes to cache or database
     */
    public static final String IGNORE_CLASSES_CACHE = "ignoreClasses";

    /**
     * KEY for save/read JMH version from database
     */
    public static final String JMH_VERSION = "jmhVersion";

    /**
     * KEY for save/read java version from database
     */
    public static final String JAVA_VERSION = "javaVersion";

    /**
     * KEY for save/read java version from database
     */
    public static final String BLIND_COPY_EMAIL = "blindCopyEmail";

    public static List<String> getAllExist() {
        return Arrays.asList(LIBRARIES_CACHE, IGNORE_CLASSES_CACHE, JMH_VERSION, JAVA_VERSION, BLIND_COPY_EMAIL);
    }

    public static boolean isPropertyExist(String key) {
        return getAllExist().contains(key);
    }
}
