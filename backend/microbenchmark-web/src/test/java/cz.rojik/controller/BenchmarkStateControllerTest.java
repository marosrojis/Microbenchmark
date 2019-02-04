package cz.rojik.controller;

import cz.rojik.EmptyDbConfig;
import cz.rojik.backend.dto.BenchmarkStateDTO;
import cz.rojik.backend.enums.BenchmarkStateTypeEnum;
import cz.rojik.backend.repository.BenchmarkStateRepository;
import cz.rojik.backend.service.BenchmarkStateService;
import cz.rojik.backend.service.impl.BenchmarkStateServiceImpl;
import cz.rojik.controller.rest.BenchmarkStateController;
import cz.rojik.mock.MockDataFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static cz.rojik.mock.MockConst.*;
import static org.mockito.ArgumentMatchers.any;

/**
 * Created by Marek Rojik (marek.rojik@inventi.cz) on 03. 02. 2019
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BenchmarkStateControllerTest.TestConfiguration.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DataJpaTest
public class BenchmarkStateControllerTest {

    @Autowired
    private MockDataFactory mockDataFactory;

    @Autowired
    private BenchmarkStateController benchmarkStateController;

    @Autowired
    private BenchmarkStateRepository benchmarkStateRepository;

    @Autowired
    private BenchmarkStateService benchmarkStateService;

    @Before
    public void setUp() {
        mockDataFactory.createMockData();
    }

    @Test
    public void getAllTest() {
        Optional<Boolean> running = Optional.empty();
        ResponseEntity<List<BenchmarkStateDTO>> response = benchmarkStateController.getAll(running);
        List<BenchmarkStateDTO> states = response.getBody();

        Assert.assertEquals(states.size(), benchmarkStateRepository.count());
        Assert.assertTrue(states.stream().allMatch(s -> s.getId().equals(BENCHMARK_STATE_ID_1) ||
                s.getId().equals(BENCHMARK_STATE_ID_2) ||
                s.getId().equals(BENCHMARK_STATE_ID_3) ||
                s.getId().equals(BENCHMARK_STATE_ID_4) ||
                s.getId().equals(BENCHMARK_STATE_ID_5) ||
                s.getId().equals(BENCHMARK_STATE_ID_6)
        ));
    }

    @Test
    public void getAllRunningTest() {
        Mockito.when(benchmarkStateService.getAllByState(any())).thenCallRealMethod();
        Mockito.when(benchmarkStateService.getAll()).thenCallRealMethod();
        Mockito.when(benchmarkStateService.getBenchmarksState(any())).thenCallRealMethod();
        Mockito.doNothing().when(benchmarkStateService).synchronizeContainersWithRunningBenchmarks();

        Optional<Boolean> running = Optional.of(true);
        ResponseEntity<List<BenchmarkStateDTO>> response = benchmarkStateController.getAll(running);
        List<BenchmarkStateDTO> states = response.getBody();

        Assert.assertNotEquals(states.size(), 0);
        Assert.assertTrue(states.stream().allMatch(s -> BenchmarkStateTypeEnum.runningStates().contains(s.getType())));
    }

    @Test
    public void getAllNonRunningTest() {
        Optional<Boolean> running = Optional.of(false);
        ResponseEntity<List<BenchmarkStateDTO>> response = benchmarkStateController.getAll(running);
        List<BenchmarkStateDTO> states = response.getBody();

        Assert.assertNotEquals(states.size(), 0);
        Assert.assertTrue(states.stream().allMatch(s -> BenchmarkStateTypeEnum.stopStates().contains(s.getType())));
    }

    @Configuration
    protected static class TestConfiguration extends EmptyDbConfig {

        @Bean
        @Primary
        public BenchmarkStateService benchmarkStateService() {
            return Mockito.mock(BenchmarkStateServiceImpl.class);
        }
    }
}
