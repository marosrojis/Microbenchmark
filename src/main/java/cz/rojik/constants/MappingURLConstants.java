package cz.rojik.constants;

public class MappingURLConstants {

    public static final String MAIN_PAGE = "/";

    // REST API
    public static final String BENCHMARK = "benchmark";
    public static final String BENCHMARK_CREATE = "create";
    public static final String BENCHMARK_IMPORT_LIBRARIES = "importLibraries";
    public static final String BENCHMARK_COMPILE = "compile/{projectId}";

    // WEBSOCKET
    public static final String BENCHMARK_RUN = "/benchmark/run";
    public static final String BENCHMARK_RESULT = "/benchmark/result";
    public static final String BENCHMARK_RESULT_STEP = "/benchmark/result/step";
}
