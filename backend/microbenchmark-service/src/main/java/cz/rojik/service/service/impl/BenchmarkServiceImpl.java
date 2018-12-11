package cz.rojik.service.service.impl;

import com.spotify.docker.client.exceptions.DockerCertificateException;
import com.spotify.docker.client.exceptions.DockerException;
import cz.rojik.backend.dto.BenchmarkDTO;
import cz.rojik.backend.dto.BenchmarkStateDTO;
import cz.rojik.backend.enums.BenchmarkStateTypeEnum;
import cz.rojik.backend.service.BenchmarkStateService;
import cz.rojik.service.dto.ErrorDTO;
import cz.rojik.service.dto.ErrorInfoWithSourceCodeDTO;
import cz.rojik.service.dto.LibrariesDTO;
import cz.rojik.service.dto.ResultDTO;
import cz.rojik.service.dto.TemplateDTO;
import cz.rojik.service.exception.BenchmarkRunException;
import cz.rojik.service.exception.ImportsToChooseException;
import cz.rojik.service.exception.MavenCompileException;
import cz.rojik.service.service.BenchmarkService;
import cz.rojik.service.service.GeneratorService;
import cz.rojik.service.service.ResultParserService;
import cz.rojik.service.service.ErrorsParserService;
import cz.rojik.service.service.RunnerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
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
    private ResultParserService resultParserService;

    @Autowired
    private ErrorsParserService errorsParserService;

    @Autowired
    private BenchmarkStateService benchmarkStateService;

    @Override
    public String createProject(TemplateDTO template) throws ImportsToChooseException {
        String projectId = generatorService.generateJavaClass(template);
        return projectId;
    }

    @Override
    public String importLibraries(LibrariesDTO libraries) {
        String projectId = generatorService.importLibraries(libraries);
        return projectId;
    }

    @Override
    public boolean compile(String projectId) {
        logger.trace("Compile project {}", projectId);
        benchmarkStateService.createState(new BenchmarkStateDTO()
                .setProjectId(projectId)
                .setType(BenchmarkStateTypeEnum.COMPILE_START));

        Set<String> errors = runnerService.compileProject(projectId);

        if (errors.size() != 0) {
            logger.error("Compilation is failed!!\n {}", errors);
            benchmarkStateService.updateState(new BenchmarkStateDTO()
                    .setProjectId(projectId)
                    .setType(BenchmarkStateTypeEnum.COMPILE_ERROR));


            List<ErrorDTO> errorList = errorsParserService.getSyntaxErrors(errors);
            ErrorInfoWithSourceCodeDTO errorInfoList = errorsParserService.processErrorList(errorList, projectId);
            logger.error("Throw exception with errors {} to project {}", errorInfoList, projectId);
            throw new MavenCompileException(errorInfoList);
        }
        logger.info("Compilation is successful.");

        return true;
    }

    @Override
    public ResultDTO runBenchmark(String projectId, TemplateDTO template, SimpMessageHeaderAccessor socketHeader) throws BenchmarkRunException {
        logger.debug("Run benchmark for project {} with template {}", projectId, template);
        BenchmarkStateDTO state;

        try {
            state = runnerService.runProject(projectId, template, socketHeader);
        } catch (DockerCertificateException | DockerException | InterruptedException e) {
            logger.error(e.getMessage(), e);
            throw new cz.rojik.service.exception.DockerException(e.getMessage());
        }

        ResultDTO result = resultParserService.parseResult(projectId);
        result.setNumberOfConnections(state.getNumberOfConnections());
        return result;
    }
}
