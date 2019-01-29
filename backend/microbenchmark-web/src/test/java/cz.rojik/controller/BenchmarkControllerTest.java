package cz.rojik.controller;

import cz.rojik.MBMarkApplicationTest;
import cz.rojik.backend.dto.BenchmarkDTO;
import cz.rojik.backend.entity.BenchmarkEntity;
import cz.rojik.backend.exception.EntityNotFoundException;
import cz.rojik.backend.repository.BenchmarkRepository;
import cz.rojik.backend.service.BenchmarkService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by Marek Rojik (marek.rojik@inventi.cz) on 29. 01. 2019
 */
public class BenchmarkControllerTest extends MBMarkApplicationTest {

    @Autowired
    private BenchmarkRepository benchmarkRepository;

    @Autowired
    private BenchmarkService benchmarkService;

    @Test(expected = EntityNotFoundException.class)
    public void getOneTest() {
        List<BenchmarkEntity> entities = benchmarkRepository.findAll();
        BenchmarkDTO benchmark = benchmarkService.getOne(1L);
    }
}
