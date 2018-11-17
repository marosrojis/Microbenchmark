package cz.rojik.service.service.impl;

import com.spotify.docker.client.exceptions.DockerCertificateException;
import com.spotify.docker.client.exceptions.DockerException;
import cz.rojik.backend.dto.BenchmarkDTO;
import cz.rojik.backend.dto.BenchmarkStateDTO;
import cz.rojik.backend.enums.BenchmarkStateTypeEnum;
import cz.rojik.backend.service.BenchmarkStateService;
import cz.rojik.service.constants.ProjectContants;
import cz.rojik.service.dto.ErrorDTO;
import cz.rojik.service.dto.ErrorInfoWithSourceCodeDTO;
import cz.rojik.service.dto.LibrariesDTO;
import cz.rojik.service.dto.ProcessInfoDTO;
import cz.rojik.service.dto.ResultDTO;
import cz.rojik.service.dto.TemplateDTO;
import cz.rojik.service.enums.Operation;
import cz.rojik.service.exception.BenchmarkRunException;
import cz.rojik.service.exception.DeleteBenchmarkException;
import cz.rojik.service.exception.ImportsToChooseException;
import cz.rojik.service.exception.MavenCompileException;
import cz.rojik.service.exception.ReadFileException;
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
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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

    @Autowired
    private cz.rojik.backend.service.BenchmarkService benchmarkBackendService;

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
        benchmarkStateService.createState(new BenchmarkStateDTO()
                .setProjectId(projectId)
                .setType(BenchmarkStateTypeEnum.COMPILE_START));

        Set<String> errors = runnerService.compileProject(projectId);

        if (errors.size() != 0) {
            logger.error("Compilation is failed!! ({} errors)", errors.size());
            benchmarkStateService.updateState(new BenchmarkStateDTO()
                    .setProjectId(projectId)
                    .setType(BenchmarkStateTypeEnum.COMPILE_ERROR));


            List<ErrorDTO> errorList = errorsParserService.getSyntaxErrors(errors);
            ErrorInfoWithSourceCodeDTO errorInfoList = errorsParserService.processErrorList(errorList, projectId);

            throw new MavenCompileException(errorInfoList);
        }
        logger.info("Compilation is successful.");

        return true;
    }

    @Override
    public ResultDTO runBenchmark(String projectId, TemplateDTO template, SimpMessageHeaderAccessor socketHeader) throws BenchmarkRunException {
        ResultDTO result;
        ProcessInfoDTO processInfo;
        try {
            processInfo = runnerService.runProject(projectId, template, socketHeader);

        } catch (DockerCertificateException | DockerException | InterruptedException e) {
            throw new BenchmarkRunException(projectId, e.getMessage(), readSourceFile(projectId));
        }

        if (processInfo.getOperation().equals(Operation.SUCCESS_BENCHMARK)) {
            result = resultParserService.parseResult(projectId);

            BenchmarkStateDTO state = benchmarkStateService.updateState(new BenchmarkStateDTO()
                    .setProjectId(projectId)
                    .setType(BenchmarkStateTypeEnum.BENCHMARK_SUCCESS));

            result.setNumberOfConnections(state.getNumberOfConnections());
        } else {
            benchmarkStateService.updateState(new BenchmarkStateDTO()
                    .setProjectId(projectId)
                    .setType(BenchmarkStateTypeEnum.BENCHMARK_ERROR));

            throw new BenchmarkRunException(projectId, processInfo.getNote(), readSourceFile(projectId));
        }
        return result;
    }

    @Transactional
    @Override
    public void deleteBenchmark(Long id) {
        BenchmarkDTO benchmark = benchmarkBackendService.delete(id);

        try {
            File projectDirectory = new File(Paths.get(ProjectContants.PROJECTS_FOLDER + benchmark.getProjectId()).toUri().getPath());
            org.apache.commons.io.FileUtils.deleteDirectory(projectDirectory);

            File resultFolder = new File(ProjectContants.PATH_RESULT + benchmark.getProjectId() + ProjectContants.JSON_FILE_FORMAT);
            if (!resultFolder.delete()) { // TODO: repair delete result file
                logger.info(String.format("Result file %s.json was not deleted", benchmark.getProjectId()));
            }
        } catch (IOException e) {
            throw new DeleteBenchmarkException(String.format("Directory or result of project %s (benchmark ID is %s) was not deleted.\n%s", benchmark.getProjectId(), id, e.getMessage()));
        }
    }

    // PRIVATE

    private List<String> readSourceFile(String projectId) {
        List<String> sourceCode;
        try {
            sourceCode = Files.readAllLines(Paths.get(ProjectContants.PROJECTS_FOLDER + projectId + "/" + ProjectContants.PATH_JAVA_PACKAGE + ProjectContants.JAVA_CLASS_FILE));
        } catch (IOException e) {
            logger.error("Cannot open class from project witd ID {0}", projectId);
            throw new ReadFileException(projectId);
        }
        return sourceCode;
    }
}
