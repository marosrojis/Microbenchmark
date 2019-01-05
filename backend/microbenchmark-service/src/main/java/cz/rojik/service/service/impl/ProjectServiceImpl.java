package cz.rojik.service.service.impl;

import com.spotify.docker.client.exceptions.DockerCertificateException;
import com.spotify.docker.client.exceptions.DockerException;
import cz.rojik.backend.dto.BenchmarkStateDTO;
import cz.rojik.backend.enums.BenchmarkStateTypeEnum;
import cz.rojik.backend.properties.PathProperties;
import cz.rojik.backend.service.BenchmarkStateService;
import cz.rojik.service.constants.ProjectContants;
import cz.rojik.service.dto.ErrorDTO;
import cz.rojik.service.dto.ErrorInfoWithSourceCodeDTO;
import cz.rojik.service.dto.LibrariesDTO;
import cz.rojik.service.dto.ResultDTO;
import cz.rojik.service.dto.TemplateDTO;
import cz.rojik.service.exception.BenchmarkRunException;
import cz.rojik.service.exception.ImportsToChooseException;
import cz.rojik.service.exception.KillContainerException;
import cz.rojik.service.exception.MavenCompileException;
import cz.rojik.service.service.ProjectService;
import cz.rojik.service.service.GeneratorService;
import cz.rojik.service.service.ResultParserService;
import cz.rojik.service.service.ErrorsParserService;
import cz.rojik.service.service.DockerService;
import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.Invoker;
import org.apache.maven.shared.invoker.MavenInvocationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * @author Marek Rojik (marek@rojik.cz) on 05. 01. 2019
 */
@Service
public class ProjectServiceImpl implements ProjectService {

    private static Logger logger = LoggerFactory.getLogger(ProjectServiceImpl.class);
    private static final String REGEX_ERROR = "\\[ERROR\\].*";

    @Autowired
    private GeneratorService generatorService;

    @Autowired
    private DockerService runnerService;

    @Autowired
    private ResultParserService resultParserService;

    @Autowired
    private ErrorsParserService errorsParserService;

    @Autowired
    private BenchmarkStateService benchmarkStateService;

    @Autowired
    private PathProperties pathProperties;

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

        Set<String> errors = runCompileMavenProject(projectId);

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

    @Override
    public void kill(String projectId) {
        logger.debug("Kill running benchmark with project ID {}", projectId);
        BenchmarkStateDTO runningBenchmark = benchmarkStateService.getByProjectId(projectId);
        if (!BenchmarkStateTypeEnum.runningBenchmarks().contains(runningBenchmark.getType())) {
            logger.error("Benchmark for kill is not in running state: {}", runningBenchmark);
            throw new KillContainerException(String.format("Benchmark with project ID %s for kill is not in running state.", projectId));
        }

        runnerService.killContainer(runningBenchmark.getContainerId());
        runningBenchmark.setType(BenchmarkStateTypeEnum.BENCHMARK_KILL)
                .setUpdated(LocalDateTime.now());
        benchmarkStateService.updateState(runningBenchmark);
    }

    private Set<String> runCompileMavenProject(String projectId) {
        Set<String> output = new LinkedHashSet<>();
        final Pattern p = Pattern.compile(REGEX_ERROR);

        InvocationRequest request = new DefaultInvocationRequest();
        request.setPomFile(new File(pathProperties.getProjects() + projectId + File.separatorChar + ProjectContants.PROJECT_POM));
        request.setGoals(Arrays.asList("clean", "package", "-Dmaven.test.skip=true"));

        Invoker invoker = new DefaultInvoker();
        invoker.setOutputHandler(text -> {
            if (p.matcher(text).matches()) {
                output.add(text);
            }
        });
        try {
            logger.info("Start compiling project {}", projectId);
            invoker.execute(request);
        } catch (MavenInvocationException e) {
            logger.error("Maven invocation exception: {}", e.getMessage());
            throw new MavenCompileException();
        }

        logger.debug("Compiling project {} is completed", projectId);
        return output;
    }
}
