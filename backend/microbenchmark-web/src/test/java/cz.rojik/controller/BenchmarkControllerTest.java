package cz.rojik.controller;

import cz.rojik.MBMarkApplicationTest;
import cz.rojik.backend.dto.BenchmarkDTO;
import cz.rojik.backend.entity.BenchmarkEntity;
import cz.rojik.backend.exception.EntityNotFoundException;
import cz.rojik.backend.repository.BenchmarkRepository;
import cz.rojik.backend.service.BenchmarkService;
import cz.rojik.controller.rest.BenchmarkController;
import cz.rojik.mock.SecurityHelperMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * Created by Marek Rojik (marek.rojik@inventi.cz) on 29. 01. 2019
 */
public class BenchmarkControllerTest extends MBMarkApplicationTest {

    @Autowired
    private BenchmarkController benchmarkController;

    private SecurityHelperMock securityHelperMock;

    @Before
    public void setUp() {
        super.setUp();
        securityHelperMock = new SecurityHelperMock(securityHelper);
    }

    @Test(expected = EntityNotFoundException.class)
    public void getOneEntityNotFoundTest() {
        securityHelperMock.mockUser();

        ResponseEntity<BenchmarkDTO> response = benchmarkController.getOne(1L);
    }
}
