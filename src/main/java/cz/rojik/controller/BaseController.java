package cz.rojik.controller;

import cz.rojik.Reader;
import cz.rojik.constants.MappingURLConstants;
import cz.rojik.dto.Result;
import cz.rojik.dto.Template;
import cz.rojik.service.GeneratorService;
import cz.rojik.service.RunnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class BaseController {

    @Autowired
    private GeneratorService generatorService;

    @Autowired
    private RunnerService runnerService;

    @RequestMapping(MappingURLConstants.MAIN_PAGE)
    public ResponseEntity<?> homepage() {
        Reader reader = new Reader();
        Template input = reader.readInputs();

        LocalDateTime now = LocalDateTime.now();
        String projectId = generatorService.generateJavaClass(input);

        Result result = runnerService.compileAndStartProject(projectId, input, now);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
