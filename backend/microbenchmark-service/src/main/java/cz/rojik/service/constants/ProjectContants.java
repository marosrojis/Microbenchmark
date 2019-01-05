package cz.rojik.service.constants;

/**
 * @author Marek Rojik (marek@rojik.cz) on 05. 01. 2019
 */
public class ProjectContants {

    public static final String PATH_DEFAULT_PROJECT     = "empty_project/";

    public static final String PROJECT_POM              = "pom.xml";

    public static final String PATH_MAIN                = "src/main/";

    public static final String PATH_JAVA_PACKAGE        = PATH_MAIN + "java/cz/rojik/";

    public static final String JAVA_CLASS               = "Microbenchmark";

    public static final String JAVA_CLASS_FILE          = JAVA_CLASS + ".java";

    public static final String TARGET_FOLDER_JAR        = "target/";

    public static final String DEFAULT_JAVA_FILE        = "default_class.txt";

    public static final String RESULT_JSON_FILE         = "result/results.json";

    public static final String JSON_FILE_FORMAT         = ".json";

    public static final String JAR_FILE_FORMAT          = ".jar";

    public static final String GENERATED_PROJECT_JAR    = JAVA_CLASS + JAR_FILE_FORMAT;

    public static final String DOCKER_BENCHMARK_FOLDER  = "benchmark/";

    public static final String DOCKER_RESULT_FILE       = DOCKER_BENCHMARK_FOLDER + RESULT_JSON_FILE;

    public static final String DEFAULT_JAVA_VERSION     = "1.8";

    public static final String DEFAULT_JMH_VERSION      = "1.20";

}
