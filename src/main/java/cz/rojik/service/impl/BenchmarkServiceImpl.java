package cz.rojik.service.impl;

import cz.rojik.dto.ErrorDTO;
import cz.rojik.dto.ErrorInfoDTO;
import cz.rojik.dto.ResultDTO;
import cz.rojik.dto.TemplateDTO;
import cz.rojik.exception.ImportsToChooseException;
import cz.rojik.exception.MavenCompileException;
import cz.rojik.service.BenchmarkService;
import cz.rojik.service.ErrorsParserService;
import cz.rojik.service.GeneratorService;
import cz.rojik.service.RunnerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class BenchmarkServiceImpl implements BenchmarkService {

    private static Logger logger = LoggerFactory.getLogger(BenchmarkServiceImpl.class);


    @Autowired
    private GeneratorService generatorService;

    @Autowired
    private RunnerService runnerService;

    @Autowired
    private ErrorsParserService errorsParserService;

    @Override
    public ResultDTO runBenchmark(TemplateDTO template) {
//        LocalDateTime now = LocalDateTime.now();
//        String projectId = generatorService.generateJavaClass(template);
//
//        ResultDTO result = runnerService.compileAndStartProject(projectId, template, now);
//        return result;
        return new ResultDTO(false);
    }

    @Override
    public String createProject(TemplateDTO template) throws ImportsToChooseException {
        String projectId = generatorService.generateJavaClass(template);
        return projectId;
    }

    @Override
    public boolean compile(String projectId) {
        Set<String> errors = runnerService.compileProject(projectId);

        if (errors.size() != 0) {
            logger.error("Compilation is failed!! ({} errors)", errors.size());
            List<ErrorDTO> errorList = errorsParserService.getSyntaxErrors(errors);
            List<ErrorInfoDTO> errorInfoList = errorsParserService.processErrorList(errorList, projectId);

            throw new MavenCompileException(errorInfoList);
        }
        logger.info("Compilation is successful.");

        return true;
    }
}
