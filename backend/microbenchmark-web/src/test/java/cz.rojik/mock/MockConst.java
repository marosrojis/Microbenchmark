package cz.rojik.mock;

import cz.rojik.backend.enums.BenchmarkStateTypeEnum;
import cz.rojik.backend.enums.RoleTypeEnum;

import java.time.LocalDateTime;

/**
 * Created by Marek Rojik (marek.rojik@inventi.cz) on 30. 01. 2019
 */
public class MockConst {

    // BASIC
    public static final LocalDateTime LOOAL_DATE_TIME_1 = LocalDateTime.now();

    // USER
    public static final Long USER_ID_1 = 1L;
    public static final String USER_FIRSTNAME_1 = "firstname-1";
    public static final String USER_LASTNAME_1 = "lastname-1";
    public static final String USER_EMAIL_1 = "email-1@rojik.cz";
    public static final String USER_PASSWORD_1 = "password-1";

    public static final Long USER_ID_2 = 2L;
    public static final String USER_FIRSTNAME_2 = "firstname-2";
    public static final String USER_LASTNAME_2 = "lastname-2";
    public static final String USER_EMAIL_2 = "email-2@rojik.cz";
    public static final String USER_PASSWORD_2 = "password-2";

    public static final Long USER_ID_3 = 3L;
    public static final String USER_FIRSTNAME_3 = "firstname-3";
    public static final String USER_LASTNAME_3 = "lastname-3";
    public static final String USER_EMAIL_3 = "email-3@rojik.cz";
    public static final String USER_PASSWORD_3 = "password-3";

    // ROLE
    public static final Long ROLE_ID_1 = 1L;
    public static final RoleTypeEnum ROLE_TYPE_1 = RoleTypeEnum.ADMIN;

    public static final Long ROLE_ID_2 = 2L;
    public static final RoleTypeEnum ROLE_TYPE_2 = RoleTypeEnum.USER;

    public static final Long ROLE_ID_3 = 3L;
    public static final RoleTypeEnum ROLE_TYPE_3 = RoleTypeEnum.DEMO;

    // BENCHMARK
    public static final Long BENCHMARK_ID_1 = 1L;
    public static final String BENCHMARK_PROJECT_ID_1 = "project-id-1";
    public static final String BENCHMARK_NAME_1 = "name-1";
    public static final String BENCHMARK_CONTENT_1 = "content-1";
    public static final String BENCHMARK_INIT_1 = "init-1";
    public static final String BENCHMARK_DECLARE_1 = "declare-1";
    public static final Boolean BENCHMARK_SUCCESS_1 = true;
    public static final int BENCHMARK_WARMUP_1 = 3;
    public static final int BENCHMARK_MEASUREMENT_1 = 5;

    public static final Long BENCHMARK_ID_2 = 2L;
    public static final String BENCHMARK_PROJECT_ID_2 = "project-id-2";
    public static final String BENCHMARK_NAME_2 = "name-2";
    public static final String BENCHMARK_CONTENT_2 = "content-2";
    public static final String BENCHMARK_INIT_2 = "init-2";
    public static final String BENCHMARK_DECLARE_2 = "declare-2";
    public static final Boolean BENCHMARK_SUCCESS_2 = true;
    public static final int BENCHMARK_WARMUP_2 = 5;
    public static final int BENCHMARK_MEASUREMENT_2 = 8;

    public static final Long BENCHMARK_ID_3 = 3L;
    public static final String BENCHMARK_PROJECT_ID_3 = "project-id-3";
    public static final String BENCHMARK_NAME_3 = "name-3";
    public static final String BENCHMARK_CONTENT_3 = "content-3";
    public static final Boolean BENCHMARK_SUCCESS_3 = false;
    public static final int BENCHMARK_WARMUP_3 = 11;
    public static final int BENCHMARK_MEASUREMENT_3 = 11;

    // MEASURE_METHOD
    public static final Long MEASURE_METHOD_ID_1 = 1L;
    public static final String MEASURE_METHOD_METHOD_1 = "measure_method_content-1";
    public static final int MEASURE_METHOD_ORDER_1 = 1;

    public static final Long MEASURE_METHOD_ID_2 = 2L;
    public static final String MEASURE_METHOD_METHOD_2 = "measure_method_content-2";
    public static final int MEASURE_METHOD_ORDER_2 = 2;

    public static final Long MEASURE_METHOD_ID_3 = 3L;
    public static final String MEASURE_METHOD_METHOD_3 = "measure_method_content-3";
    public static final int MEASURE_METHOD_ORDER_3 = 3;

    public static final Long MEASURE_METHOD_ID_4 = 4L;
    public static final String MEASURE_METHOD_METHOD_4 = "measure_method_content-4";
    public static final int MEASURE_METHOD_ORDER_4 = 4;

    public static final Long MEASURE_METHOD_ID_5 = 5L;
    public static final String MEASURE_METHOD_METHOD_5 = "measure_method_content-5";
    public static final int MEASURE_METHOD_ORDER_5 = 5;

    // BENCHMARK_STATE
    public static final Long BENCHMARK_STATE_ID_1 = 1L;
    public static final String BENCHMARK_STATE_PROJECT_ID_1 = "project-id-1";
    public static final String BENCHMARK_STATE_CONTAINER_ID_1 = "container-id-1";
    public static final BenchmarkStateTypeEnum BENCHMARK_STATE_TYPE_1 = BenchmarkStateTypeEnum.BENCHMARK_RUNNING;
    public static final int BENCHMARK_STATE_NUMBER_OF_CONNECTIONS_1 = 2;
    public static final int BENCHMARK_STATE_COMPLETED_1 = 1;
    public static final LocalDateTime BENCHMARK_STATE_ESTIMATED_TIME_1 = LocalDateTime.now();

    public static final Long BENCHMARK_STATE_ID_2 = 2L;
    public static final String BENCHMARK_STATE_PROJECT_ID_2 = "project-id-2";
    public static final String BENCHMARK_STATE_CONTAINER_ID_2 = "container-id-2";
    public static final BenchmarkStateTypeEnum BENCHMARK_STATE_TYPE_2 = BenchmarkStateTypeEnum.BENCHMARK_RUNNING;
    public static final int BENCHMARK_STATE_NUMBER_OF_CONNECTIONS_2 = 2;
    public static final int BENCHMARK_STATE_COMPLETED_2 = 2;
    public static final LocalDateTime BENCHMARK_STATE_ESTIMATED_TIME_2 = LocalDateTime.now();

    public static final Long BENCHMARK_STATE_ID_3 = 3L;
    public static final String BENCHMARK_STATE_PROJECT_ID_3 = "project-id-3";
    public static final String BENCHMARK_STATE_CONTAINER_ID_3 = "container-id-3";
    public static final BenchmarkStateTypeEnum BENCHMARK_STATE_TYPE_3 = BenchmarkStateTypeEnum.BENCHMARK_START;
    public static final int BENCHMARK_STATE_NUMBER_OF_CONNECTIONS_3 = 3;
    public static final int BENCHMARK_STATE_COMPLETED_3 = 0;
    public static final LocalDateTime BENCHMARK_STATE_ESTIMATED_TIME_3 = LocalDateTime.now();

    public static final Long BENCHMARK_STATE_ID_4 = 4L;
    public static final String BENCHMARK_STATE_PROJECT_ID_4 = "project-id-4";
    public static final String BENCHMARK_STATE_CONTAINER_ID_4 = "container-id-4";
    public static final BenchmarkStateTypeEnum BENCHMARK_STATE_TYPE_4 = BenchmarkStateTypeEnum.BENCHMARK_ERROR;
    public static final int BENCHMARK_STATE_NUMBER_OF_CONNECTIONS_4 = 4;
    public static final int BENCHMARK_STATE_COMPLETED_4 = 4;
    public static final LocalDateTime BENCHMARK_STATE_ESTIMATED_TIME_4 = LocalDateTime.now();

    public static final Long BENCHMARK_STATE_ID_5 = 5L;
    public static final String BENCHMARK_STATE_PROJECT_ID_5 = "project-id-5";
    public static final String BENCHMARK_STATE_CONTAINER_ID_5 = "container-id-5";
    public static final BenchmarkStateTypeEnum BENCHMARK_STATE_TYPE_5 = BenchmarkStateTypeEnum.BENCHMARK_KILL;
    public static final int BENCHMARK_STATE_NUMBER_OF_CONNECTIONS_5 = 5;
    public static final int BENCHMARK_STATE_COMPLETED_5 = 5;
    public static final LocalDateTime BENCHMARK_STATE_ESTIMATED_TIME_5 = LocalDateTime.now();

    public static final Long BENCHMARK_STATE_ID_6 = 6L;
    public static final String BENCHMARK_STATE_PROJECT_ID_6 = "project-id-6";
    public static final String BENCHMARK_STATE_CONTAINER_ID_6 = "container-id-6";
    public static final BenchmarkStateTypeEnum BENCHMARK_STATE_TYPE_6 = BenchmarkStateTypeEnum.BENCHMARK_SUCCESS;
    public static final int BENCHMARK_STATE_NUMBER_OF_CONNECTIONS_6 = 6;
    public static final int BENCHMARK_STATE_COMPLETED_6 = 100;
    public static final LocalDateTime BENCHMARK_STATE_ESTIMATED_TIME_6 = LocalDateTime.now();

    // PROPERTY
    public static final Long PROPERTY_ID_1 = 1L;
    public static final String PROPERTY_KEY_1 = "key-1";
    public static final String PROPERTY_VALUE_1 = "value-1";

    public static final Long PROPERTY_ID_2 = 2L;
    public static final String PROPERTY_KEY_2 = "key-2";
    public static final String PROPERTY_VALUE_2 = "value-2";

    public static final Long PROPERTY_ID_3 = 3L;
    public static final String PROPERTY_KEY_3 = "key-3";
    public static final String PROPERTY_VALUE_3 = "value-3";
}
