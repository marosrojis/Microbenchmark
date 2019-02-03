package cz.rojik.controller;

import cz.rojik.MBMarkApplicationTest;
import cz.rojik.backend.dto.BenchmarkDTO;
import cz.rojik.backend.dto.user.UserDTO;
import cz.rojik.backend.entity.BenchmarkEntity;
import cz.rojik.backend.exception.EntityNotFoundException;
import cz.rojik.backend.repository.BenchmarkRepository;
import cz.rojik.controller.rest.BenchmarkController;
import cz.rojik.mock.SecurityHelperMock;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static cz.rojik.mock.MockConst.*;

/**
 * Created by Marek Rojik (marek.rojik@inventi.cz) on 29. 01. 2019
 */
public class BenchmarkControllerTest extends MBMarkApplicationTest {

    @Autowired
    private BenchmarkController benchmarkController;

    @Autowired
    private BenchmarkRepository benchmarkRepository;

    private SecurityHelperMock securityHelperMock;

    @Override
    public void setUp() {
        super.setUp();
        securityHelperMock = new SecurityHelperMock(securityHelper);
    }

    @Test
    public void getOneMockAdminTest() {
        securityHelperMock.mockAdmin();
        ResponseEntity<BenchmarkDTO> response = benchmarkController.getOne(1L);
        BenchmarkDTO benchmark = response.getBody();

        assertBenchmark(BENCHMARK_ID_1, BENCHMARK_PROJECT_ID_1, BENCHMARK_CONTENT_1, BENCHMARK_SUCCESS_1, USER_ID_1, benchmark);
    }

    @Test
    public void getOneMockAdminTest2() {
        UserDTO mockUser = securityHelperMock.mockAdmin();
        ResponseEntity<BenchmarkDTO> response = benchmarkController.getOne(5L);
        BenchmarkDTO benchmark = response.getBody();

        assertBenchmark(BENCHMARK_ID_5, BENCHMARK_PROJECT_ID_5, BENCHMARK_CONTENT_5, BENCHMARK_SUCCESS_5, USER_ID_3, benchmark);
        Assert.assertNotEquals(mockUser.getId(), benchmark.getUser().getId());
    }

    @Test
    public void getOneMockUserTest() {
        UserDTO mockUser = securityHelperMock.mockUser();
        ResponseEntity<BenchmarkDTO> response = benchmarkController.getOne(4L);
        BenchmarkDTO benchmark = response.getBody();

        assertBenchmark(BENCHMARK_ID_4, BENCHMARK_PROJECT_ID_4, BENCHMARK_CONTENT_4, BENCHMARK_SUCCESS_4, USER_ID_2, benchmark);
        Assert.assertEquals(mockUser.getId(), benchmark.getUser().getId());
    }

    @Test
    public void getOneMockDemoTest() {
        UserDTO mockUser = securityHelperMock.mockDemo();
        ResponseEntity<BenchmarkDTO> response = benchmarkController.getOne(5L);
        BenchmarkDTO benchmark = response.getBody();

        assertBenchmark(BENCHMARK_ID_5, BENCHMARK_PROJECT_ID_5, BENCHMARK_CONTENT_5, BENCHMARK_SUCCESS_5, USER_ID_3, benchmark);
        Assert.assertEquals(mockUser.getId(), benchmark.getUser().getId());
    }

    @Test(expected = EntityNotFoundException.class)
    public void getOneMockUserExceptionTest() {
        securityHelperMock.mockUser();
        benchmarkController.getOne(1L);
    }

    @Test(expected = EntityNotFoundException.class)
    public void getOneMockDemoExceptionTest() {
        securityHelperMock.mockDemo();
        benchmarkController.getOne(1L);
    }

    @Test
    public void getAllMockAdminTest() {
        UserDTO mockUser = securityHelperMock.mockAdmin();
        Optional<Boolean> success = Optional.empty();
        Optional<Long> user = Optional.empty();
        ResponseEntity<List<BenchmarkDTO>> response = benchmarkController.getAll(success, user);
        List<BenchmarkDTO> benchmarks = response.getBody();

        Assert.assertEquals(benchmarks.size(), benchmarkRepository.count());
        Assert.assertTrue(benchmarks.stream().anyMatch(b -> b.getUser() != null && b.getUser().getId().equals(mockUser.getId())));
        Assert.assertTrue(benchmarks.stream().anyMatch(b -> b.getUser() != null && b.getUser().getId().equals(USER_ID_2)));
        Assert.assertTrue(benchmarks.stream().anyMatch(b -> b.getUser() != null && b.getUser().getId().equals(USER_ID_3)));
        Assert.assertTrue(benchmarks.stream().anyMatch(b -> b.getUser() == null));
    }

    @Test
    public void getAllMockUserTest() {
        UserDTO mockUser = securityHelperMock.mockUser();
        Optional<Boolean> success = Optional.empty();
        Optional<Long> user = Optional.empty();
        ResponseEntity<List<BenchmarkDTO>> response = benchmarkController.getAll(success, user);
        List<BenchmarkDTO> benchmarks = response.getBody();

        Assert.assertTrue(benchmarks.size() != 0);
        Assert.assertTrue(benchmarks.stream().allMatch(b -> b.getUser().getId().equals(mockUser.getId())));
    }

    @Test
    public void getAllMockDemoTest() {
        UserDTO mockUser = securityHelperMock.mockDemo();
        Optional<Boolean> success = Optional.empty();
        Optional<Long> user = Optional.empty();
        ResponseEntity<List<BenchmarkDTO>> response = benchmarkController.getAll(success, user);
        List<BenchmarkDTO> benchmarks = response.getBody();

        Assert.assertTrue(benchmarks.size() != 0);
        Assert.assertTrue(benchmarks.stream().allMatch(b -> b.getUser().getId().equals(mockUser.getId())));
    }

    @Test
    public void getAllMockAdminOnlySuccessTest() {
        securityHelperMock.mockAdmin();
        Optional<Boolean> success = Optional.of(true);
        Optional<Long> user = Optional.empty();
        ResponseEntity<List<BenchmarkDTO>> response = benchmarkController.getAll(success, user);
        List<BenchmarkDTO> benchmarks = response.getBody();

        Assert.assertTrue(benchmarks.size() != 0);
        Assert.assertTrue(benchmarks.stream().allMatch(BenchmarkDTO::isSuccess));
    }

    @Test
    public void getAllMockAdminOnlyNonSuccessTest() {
        securityHelperMock.mockAdmin();
        Optional<Boolean> success = Optional.of(false);
        Optional<Long> user = Optional.empty();
        ResponseEntity<List<BenchmarkDTO>> response = benchmarkController.getAll(success, user);
        List<BenchmarkDTO> benchmarks = response.getBody();

        Assert.assertTrue(benchmarks.size() != 0);
        Assert.assertTrue(benchmarks.stream().noneMatch(BenchmarkDTO::isSuccess));
    }

    @Test
    public void getAllMockAdminOnlyUserTest() {
        securityHelperMock.mockAdmin();
        Optional<Boolean> success = Optional.empty();
        Optional<Long> user = Optional.of(USER_ID_1);
        ResponseEntity<List<BenchmarkDTO>> response = benchmarkController.getAll(success, user);
        List<BenchmarkDTO> benchmarks = response.getBody();

        Assert.assertTrue(benchmarks.size() != 0);
        Assert.assertTrue(benchmarks.stream().allMatch(b -> b.getUser().getId().equals(USER_ID_1)));
    }

    @Test
    public void getAllMockAdminOnlySuccessAndUserTest() {
        securityHelperMock.mockAdmin();
        Optional<Boolean> success = Optional.of(true);
        Optional<Long> user = Optional.of(USER_ID_1);
        ResponseEntity<List<BenchmarkDTO>> response = benchmarkController.getAll(success, user);
        List<BenchmarkDTO> benchmarks = response.getBody();

        Assert.assertTrue(benchmarks.size() != 0);
        Assert.assertTrue(benchmarks.stream().allMatch(BenchmarkDTO::isSuccess));
        Assert.assertTrue(benchmarks.stream().allMatch(b -> b.getUser().getId().equals(USER_ID_1)));
    }

    @Test(expected = EntityNotFoundException.class)
    public void getAllMockUserExceptionTest() {
        securityHelperMock.mockUser();
        Optional<Boolean> success = Optional.empty();
        Optional<Long> user = Optional.of(1L);
        benchmarkController.getAll(success, user);
    }

    @Test(expected = EntityNotFoundException.class)
    public void getAllMockDemoExceptionTest() {
        securityHelperMock.mockDemo();
        Optional<Boolean> success = Optional.empty();
        Optional<Long> user = Optional.of(1L);
        benchmarkController.getAll(success, user);
    }

    @Test
    public void deleteMockAdminTest() {
        securityHelperMock.mockAdmin();
        benchmarkController.delete(BENCHMARK_ID_1);

        List<BenchmarkEntity> benchmarks = benchmarkRepository.findAll();
        Assert.assertEquals(benchmarks.size(), 4);
        Assert.assertTrue(benchmarks.stream().noneMatch(b -> b.getId().equals(BENCHMARK_ID_1)));
    }

    @Test
    public void deleteMockUserTest() {
        UserDTO mockUser = securityHelperMock.mockUser();
        benchmarkController.delete(BENCHMARK_ID_4);

        List<BenchmarkEntity> benchmarks = benchmarkRepository.findAll().stream().filter(b -> b.getUser() != null && b.getUser().getId().equals(mockUser.getId())).collect(Collectors.toList());
        Assert.assertEquals(benchmarks.size(), 0);
    }

    @Test
    public void deleteMockDemoTest() {
        UserDTO mockUser = securityHelperMock.mockDemo();
        benchmarkController.delete(BENCHMARK_ID_5);

        List<BenchmarkEntity> benchmarks = benchmarkRepository.findAll().stream().filter(b -> b.getUser() != null && b.getUser().getId().equals(mockUser.getId())).collect(Collectors.toList());
        Assert.assertEquals(benchmarks.size(), 0);
    }

    @Test(expected = EntityNotFoundException.class)
    public void deleteMockUserExceptionTest() {
        securityHelperMock.mockUser();
        benchmarkController.delete(BENCHMARK_ID_1);
    }

    @Test(expected = EntityNotFoundException.class)
    public void deleteMockDemoExceptionTest() {
        securityHelperMock.mockDemo();
        benchmarkController.delete(BENCHMARK_ID_1);
    }

    @Test
    public void assignToUserTest() {
        securityHelperMock.mockAdmin();
        ResponseEntity<BenchmarkDTO> response = benchmarkController.assignToUser(1L, USER_ID_3);
        BenchmarkDTO benchmark = response.getBody();

        Assert.assertEquals(benchmark.getUser().getId(), USER_ID_3);
    }

    @Test
    public void assignToUserTest2() {
        securityHelperMock.mockAdmin();
        ResponseEntity<BenchmarkDTO> response = benchmarkController.assignToUser(3L, USER_ID_3);
        BenchmarkDTO benchmark = response.getBody();

        Assert.assertEquals(benchmark.getUser().getId(), USER_ID_3);
    }

    @Test(expected = EntityNotFoundException.class)
    public void assignToUserUserNotFoundExceptionTest() {
        securityHelperMock.mockAdmin();
        benchmarkController.assignToUser(3L, 0L);
    }

    @Test(expected = EntityNotFoundException.class)
    public void assignToUserBenchmarkNotFoundExceptionTest() {
        securityHelperMock.mockAdmin();
        benchmarkController.assignToUser(0L, USER_ID_3);
    }

    private void assertBenchmark(Long id, String projectId, String content, boolean success, Long userId, BenchmarkDTO benchmark) {
        Assert.assertEquals(id, benchmark.getId());
        Assert.assertEquals(content, benchmark.getContent());
        Assert.assertEquals(projectId, benchmark.getProjectId());
        Assert.assertEquals(success, benchmark.isSuccess());
        Assert.assertEquals(userId, benchmark.getUser().getId());
    }
}
