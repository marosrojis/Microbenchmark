package cz.rojik.constants;

public class MappingURLConstants {

    public static final String API_PREFIX = "/api";
    public static final String MAIN_PAGE = "/";

    // REST API
    public static final String BENCHMARK = "benchmark";
    public static final String BENCHMARK_CREATE = "create";
    public static final String BENCHMARK_IMPORT_LIBRARIES = "importLibraries";
    public static final String BENCHMARK_COMPILE = "compile/{projectId}";

    public static final String LOGIN = API_PREFIX + "/login";

    // WEBSOCKET
    public static final String BENCHMARK_RUN = "/benchmark/run";
    public static final String BENCHMARK_RESULT = "/benchmark/result";
    public static final String BENCHMARK_RESULT_STEP = "/benchmark/result/step";

    public static final String TEST = API_PREFIX + "/test";

}
