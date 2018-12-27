package cz.rojik.constants;

public class MappingURLConstants {

    public static final String API_PREFIX = "/api";
    public static final String ID_PARAM = "{id}";
    public static final String MAIN_PAGE = "/";

    // REST API //

    // BENCHMARK
    public static final String BENCHMARK = API_PREFIX + "/benchmark";
    public static final String BENCHMARK_ASSIGN_TO_USER = "{id}/user/{userId}";

    // PROJECT
    public static final String PROJECT = API_PREFIX + "/project";
    public static final String PROJECT_CREATE = "create";
    public static final String PROJECT_IMPORT_LIBRARIES = "importLibraries";
    public static final String PROJECT_COMPILE = "compile/{projectId}";

    // USERS
    public static final String USERS = API_PREFIX + "/users";
    public static final String USERS_NON_ENABLED = "nonEnabled";

    // BENCHMARK_STATE
    public static final String BENCHMARK_STATE = API_PREFIX + "/benchmarkState";

    // IMPORT LIBRARIES
    public static final String LIBRARY_LIBRARIES = API_PREFIX + "/library";

    // LOGIN
    public static final String LOGIN = API_PREFIX + "/login";

    // WEBSOCKET //
    public static final String APP_WEBSOCKET = "/app";
    public static final String BENCHMARK_WEBSOCKET = "/benchmark";

    public static final String BENCHMARK_RUN = BENCHMARK_WEBSOCKET + "/run";
    public static final String BENCHMARK_RESULT = BENCHMARK_WEBSOCKET + "/result";
    public static final String BENCHMARK_RESULT_STEP = BENCHMARK_RESULT + "/step";

    public static final String SOCKET = "/socket";
    public static final String SOCKET_EXCEPTION = BENCHMARK_WEBSOCKET + "/errors";
    public static final String TEST = API_PREFIX + "/test";

}
