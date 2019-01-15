package cz.rojik.service.constants;

/**
 * @author Marek Rojik (marek@rojik.cz) on 05. 01. 2019
 */
public class OtherConstants {

    /**
     * REGEX for read class or package name from string
     */
    public static final String JAVA_PACKAGE_CLASS_REGEX = "^([a-zA-Z_$][a-zA-Z\\d_$]*\\.)*([A-Z_$][a-zA-Z\\d_$]*)";

    /**
     * Folder separator
     */
    public static final String LINUX_FILE_SEPARATOR = "/";

    /**
     * KEY for save libraries to cache or database
     */
    public static final String LIBRARIES_CACHE = "libraries";

    /**
     * KEY for save ignore classes to cache or database
     */
    public static final String IGNORE_CLASSES_CACHE = "ignoreClasses";

    /**
     * Package with java classes that don't need to import
     */
    public static final String PACKAGE_IGNORE_CLASSES = "java.lang";

    /**
     * Ignore classes separator
     */
    public static final String PACKAGE_IGNORE_CLASSES_SEPARATOR = ";";

    /**
     * KEY for save/read JMH version from database
     */
    public static final String JMH_VERSION = "jmhVersion";

    /**
     * KEY for save/read java version from database
     */
    public static final String JAVA_VERSION = "javaVersion";
}
