package cz.rojik.mock;

import cz.rojik.backend.dto.user.UserDTO;
import cz.rojik.backend.entity.BenchmarkEntity;
import cz.rojik.backend.entity.BenchmarkStateEntity;
import cz.rojik.backend.entity.MeasureMethodEntity;
import cz.rojik.backend.entity.PropertyEntity;
import cz.rojik.backend.entity.RoleEntity;
import cz.rojik.backend.entity.UserEntity;
import cz.rojik.backend.repository.BenchmarkRepository;
import cz.rojik.backend.repository.BenchmarkStateRepository;
import cz.rojik.backend.repository.MeasureMethodRepository;
import cz.rojik.backend.repository.PropertyRepository;
import cz.rojik.backend.repository.RoleRepository;
import cz.rojik.backend.repository.UserRepository;
import cz.rojik.backend.util.SecurityHelper;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import static cz.rojik.mock.MockConst.*;

/**
 * Created by Marek Rojik (marek.rojik@inventi.cz) on 30. 01. 2019
 */
@Component
public class MockDataFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(MockDataFactory.class);

    @Autowired
    private BenchmarkRepository benchmarkRepository;

    @Autowired
    private MeasureMethodRepository measureMethodRepository;

    @Autowired
    private BenchmarkStateRepository benchmarkStateRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Transactional
    public void createMockData() {
        // USER
        UserEntity user1 = new UserEntity(USER_FIRSTNAME_1, USER_LASTNAME_1, USER_EMAIL_1, USER_PASSWORD_1).setEnabled(true);
        user1.setId(USER_ID_1);
        UserEntity user2 = new UserEntity(USER_FIRSTNAME_2, USER_LASTNAME_2, USER_EMAIL_2, USER_PASSWORD_2).setEnabled(true);
        user2.setId(USER_ID_2);
        UserEntity user3 = new UserEntity(USER_FIRSTNAME_3, USER_LASTNAME_3, USER_EMAIL_3, USER_PASSWORD_3).setEnabled(true);
        user3.setId(USER_ID_3);

        // ROLE
        RoleEntity role1 = new RoleEntity(ROLE_TYPE_1);
        role1.setId(ROLE_ID_1);
        RoleEntity role2 = new RoleEntity(ROLE_TYPE_2);
        role2.setId(ROLE_ID_2);
        RoleEntity role3 = new RoleEntity(ROLE_TYPE_3);
        role3.setId(ROLE_ID_3);

        roleRepository.saveAndFlush(role1);
        roleRepository.saveAndFlush(role2);
        roleRepository.saveAndFlush(role3);

        user1.getRoles().add(role1);
        user1.getRoles().add(role2);
        user2.getRoles().add(role2);
        user3.getRoles().add(role3);
        userRepository.saveAndFlush(user1);
        userRepository.saveAndFlush(user2);
        userRepository.saveAndFlush(user3);

        // BENCHMARK
        BenchmarkEntity benchmark1 = new BenchmarkEntity(BENCHMARK_PROJECT_ID_1, BENCHMARK_NAME_1, LocalDateTime.now(),
                BENCHMARK_CONTENT_1, BENCHMARK_WARMUP_1, BENCHMARK_MEASUREMENT_1, BENCHMARK_INIT_1, BENCHMARK_DECLARE_1, BENCHMARK_SUCCESS_1)
                .setUser(user1);
        benchmark1.setId(BENCHMARK_ID_1);
        BenchmarkEntity benchmark2 = new BenchmarkEntity(BENCHMARK_PROJECT_ID_2, BENCHMARK_NAME_2, LocalDateTime.now(),
                BENCHMARK_CONTENT_2, BENCHMARK_WARMUP_2, BENCHMARK_MEASUREMENT_2, BENCHMARK_INIT_2, BENCHMARK_DECLARE_2, BENCHMARK_SUCCESS_2)
                .setUser(user1);
        benchmark2.setId(BENCHMARK_ID_2);
        BenchmarkEntity benchmark3 = new BenchmarkEntity(BENCHMARK_PROJECT_ID_3, BENCHMARK_NAME_3, LocalDateTime.now(),
                BENCHMARK_CONTENT_3, BENCHMARK_WARMUP_3, BENCHMARK_MEASUREMENT_3, null, null, BENCHMARK_SUCCESS_3);
        benchmark3.setId(BENCHMARK_ID_3);

        benchmark1 = benchmarkRepository.saveAndFlush(benchmark1);
        benchmark2 = benchmarkRepository.saveAndFlush(benchmark2);
        benchmark3 = benchmarkRepository.saveAndFlush(benchmark3);

        // MEASURE_METHOD
        MeasureMethodEntity method1 = new MeasureMethodEntity(MEASURE_METHOD_ORDER_1, MEASURE_METHOD_METHOD_1, benchmark1);
        method1.setId(MEASURE_METHOD_ID_1);
        MeasureMethodEntity method2 = new MeasureMethodEntity(MEASURE_METHOD_ORDER_2, MEASURE_METHOD_METHOD_2, benchmark1);
        method2.setId(MEASURE_METHOD_ID_2);
        MeasureMethodEntity method3 = new MeasureMethodEntity(MEASURE_METHOD_ORDER_3, MEASURE_METHOD_METHOD_3, benchmark2);
        method3.setId(MEASURE_METHOD_ID_3);
        MeasureMethodEntity method4 = new MeasureMethodEntity(MEASURE_METHOD_ORDER_4, MEASURE_METHOD_METHOD_4, benchmark2);
        method4.setId(MEASURE_METHOD_ID_4);
        MeasureMethodEntity method5 = new MeasureMethodEntity(MEASURE_METHOD_ORDER_5, MEASURE_METHOD_METHOD_5, benchmark3);
        method5.setId(MEASURE_METHOD_ID_5);

        method1 = measureMethodRepository.saveAndFlush(method1);
        method2 = measureMethodRepository.saveAndFlush(method2);
        method3 = measureMethodRepository.saveAndFlush(method3);
        method4 = measureMethodRepository.saveAndFlush(method4);
        method5 = measureMethodRepository.saveAndFlush(method5);

        benchmark1.setMeasureMethods(Arrays.asList(method1, method2));
        benchmark2.setMeasureMethods(Arrays.asList(method3, method4));
        benchmark3.setMeasureMethods(Collections.singletonList(method5));
        benchmarkRepository.flush();

        // BENCHMARK_STATE
        BenchmarkStateEntity benchmarkState1 = new BenchmarkStateEntity(BENCHMARK_STATE_PROJECT_ID_1, BENCHMARK_STATE_CONTAINER_ID_1,
                BENCHMARK_STATE_TYPE_1, LocalDateTime.now(), BENCHMARK_STATE_NUMBER_OF_CONNECTIONS_1, BENCHMARK_STATE_COMPLETED_1, BENCHMARK_STATE_ESTIMATED_TIME_1)
                .setUser(user1);
        benchmarkState1.setId(BENCHMARK_STATE_ID_1);
        BenchmarkStateEntity benchmarkState2 = new BenchmarkStateEntity(BENCHMARK_STATE_PROJECT_ID_2, BENCHMARK_STATE_CONTAINER_ID_2,
                BENCHMARK_STATE_TYPE_2, LocalDateTime.now(), BENCHMARK_STATE_NUMBER_OF_CONNECTIONS_2, BENCHMARK_STATE_COMPLETED_2, BENCHMARK_STATE_ESTIMATED_TIME_2)
                .setUser(user2);
        benchmarkState2.setId(BENCHMARK_STATE_ID_2);
        BenchmarkStateEntity benchmarkState3 = new BenchmarkStateEntity(BENCHMARK_STATE_PROJECT_ID_3, BENCHMARK_STATE_CONTAINER_ID_3,
                BENCHMARK_STATE_TYPE_3, LocalDateTime.now(), BENCHMARK_STATE_NUMBER_OF_CONNECTIONS_3, BENCHMARK_STATE_COMPLETED_3, BENCHMARK_STATE_ESTIMATED_TIME_3)
                .setUser(user3);
        benchmarkState3.setId(BENCHMARK_STATE_ID_3);
        BenchmarkStateEntity benchmarkState4 = new BenchmarkStateEntity(BENCHMARK_STATE_PROJECT_ID_4, BENCHMARK_STATE_CONTAINER_ID_4,
                BENCHMARK_STATE_TYPE_4, LocalDateTime.now(), BENCHMARK_STATE_NUMBER_OF_CONNECTIONS_4, BENCHMARK_STATE_COMPLETED_4, BENCHMARK_STATE_ESTIMATED_TIME_4);
        benchmarkState4.setId(BENCHMARK_STATE_ID_4);
        BenchmarkStateEntity benchmarkState5 = new BenchmarkStateEntity(BENCHMARK_STATE_PROJECT_ID_5, BENCHMARK_STATE_CONTAINER_ID_5,
                BENCHMARK_STATE_TYPE_5, LocalDateTime.now(), BENCHMARK_STATE_NUMBER_OF_CONNECTIONS_5, BENCHMARK_STATE_COMPLETED_5, BENCHMARK_STATE_ESTIMATED_TIME_5)
                .setUser(user1);
        benchmarkState5.setId(BENCHMARK_STATE_ID_5);
        BenchmarkStateEntity benchmarkState6 = new BenchmarkStateEntity(BENCHMARK_STATE_PROJECT_ID_6, BENCHMARK_STATE_CONTAINER_ID_6,
                BENCHMARK_STATE_TYPE_6, LocalDateTime.now(), BENCHMARK_STATE_NUMBER_OF_CONNECTIONS_6, BENCHMARK_STATE_COMPLETED_6, BENCHMARK_STATE_ESTIMATED_TIME_6);
        benchmarkState6.setId(BENCHMARK_STATE_ID_6);

        benchmarkStateRepository.saveAndFlush(benchmarkState1);
        benchmarkStateRepository.saveAndFlush(benchmarkState2);
        benchmarkStateRepository.saveAndFlush(benchmarkState3);
        benchmarkStateRepository.saveAndFlush(benchmarkState4);
        benchmarkStateRepository.saveAndFlush(benchmarkState5);
        benchmarkStateRepository.saveAndFlush(benchmarkState6);

        // PROPERTY
        PropertyEntity property1 = new PropertyEntity(PROPERTY_KEY_1, PROPERTY_VALUE_1);
        property1.setId(PROPERTY_ID_1);
        PropertyEntity property2 = new PropertyEntity(PROPERTY_KEY_2, PROPERTY_VALUE_2);
        property2.setId(PROPERTY_ID_2);
        PropertyEntity property3 = new PropertyEntity(PROPERTY_KEY_3, PROPERTY_VALUE_3);
        property3.setId(PROPERTY_ID_3);
        propertyRepository.saveAndFlush(property1);
        propertyRepository.saveAndFlush(property2);
        propertyRepository.saveAndFlush(property3);
    }
}
