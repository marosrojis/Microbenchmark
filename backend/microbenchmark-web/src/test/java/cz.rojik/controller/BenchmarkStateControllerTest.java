package cz.rojik.controller;

import cz.rojik.MBMarkApplicationTest;
import cz.rojik.backend.dto.BenchmarkStateDTO;
import cz.rojik.backend.enums.BenchmarkStateTypeEnum;
import cz.rojik.backend.repository.BenchmarkStateRepository;
import cz.rojik.controller.rest.BenchmarkStateController;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static cz.rojik.mock.MockConst.*;

/**
 * Created by Marek Rojik (marek.rojik@inventi.cz) on 03. 02. 2019
 */
public class BenchmarkStateControllerTest extends MBMarkApplicationTest {

    @Autowired
    private BenchmarkStateController benchmarkStateController;

    @Autowired
    private BenchmarkStateRepository benchmarkStateRepository;

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
}
