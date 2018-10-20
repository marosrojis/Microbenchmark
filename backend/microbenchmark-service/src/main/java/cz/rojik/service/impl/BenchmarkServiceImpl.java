package cz.rojik.service.impl;

import com.spotify.docker.client.exceptions.DockerCertificateException;
import com.spotify.docker.client.exceptions.DockerException;
import cz.rojik.dto.ErrorDTO;
import cz.rojik.dto.ErrorInfoWithSourceCodeDTO;
import cz.rojik.dto.LibrariesDTO;
import cz.rojik.dto.ResultDTO;
import cz.rojik.dto.TemplateDTO;
import cz.rojik.exception.ImportsToChooseException;
import cz.rojik.exception.MavenCompileException;
import cz.rojik.service.BenchmarkService;
import cz.rojik.service.ErrorsParserService;
import cz.rojik.service.GeneratorService;
import cz.rojik.service.ResultParserService;
import cz.rojik.service.RunnerService;
import cz.rojik.utils.FileUtils;
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
        Set<String> errors = runnerService.compileProject(projectId);

        if (errors.size() != 0) {
            logger.error("Compilation is failed!! ({} errors)", errors.size());
            List<ErrorDTO> errorList = errorsParserService.getSyntaxErrors(errors);
            ErrorInfoWithSourceCodeDTO errorInfoList = errorsParserService.processErrorList(errorList, projectId);

            throw new MavenCompileException(errorInfoList);
        }
        logger.info("Compilation is successful.");

        return true;
    }

    @Override
    public ResultDTO runBenchmark(String projectId, SimpMessageHeaderAccessor socketHeader) {
        TemplateDTO template = FileUtils.getTemplateFromJson(projectId);

        ResultDTO result = null;
        try {
            projectId = runnerService.runProject(projectId, template, socketHeader);
            result = resultParserService.parseResult(projectId);
        } catch (DockerCertificateException | DockerException | InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }
}
