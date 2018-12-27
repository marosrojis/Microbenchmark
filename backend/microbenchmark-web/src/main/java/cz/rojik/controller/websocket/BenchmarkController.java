package cz.rojik.controller.websocket;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import cz.rojik.backend.dto.BenchmarkDTO;
import cz.rojik.backend.service.BenchmarkService;
import cz.rojik.constants.MappingURLConstants;
import cz.rojik.service.TransformService;
import cz.rojik.service.dto.BenchmarkRunErrorDTO;
import cz.rojik.service.dto.ResultDTO;
import cz.rojik.service.dto.TemplateDTO;
import cz.rojik.service.exception.BenchmarkRunException;
import cz.rojik.service.service.ProjectService;
import cz.rojik.service.utils.FileUtils;
import cz.rojik.util.serialization.LocalDateTimeGsonSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller("webSocketBenchmarkController")
public class BenchmarkController {

    private static Logger logger = LoggerFactory.getLogger(BenchmarkController.class);

    @Autowired
    private ProjectService benchmarkService;

    @Autowired
    private TransformService transformService;

    @Autowired
    private BenchmarkService benchmarkServiceBackend;

    @MessageMapping(MappingURLConstants.BENCHMARK_RUN)
    @SendToUser(MappingURLConstants.BENCHMARK_RESULT)
    public String runBenchmark(SimpMessageHeaderAccessor headerAccessor, String projectId) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeGsonSerializer())
                .create();

        SimpMessageHeaderAccessor messageHeaderAccessor = SimpMessageHeaderAccessor
                .create(SimpMessageType.MESSAGE);
        messageHeaderAccessor.setSessionId(headerAccessor.getSessionId());
        messageHeaderAccessor.setLeaveMutable(true);

        logger.trace("Read template for project {}", projectId);
        TemplateDTO template = FileUtils.getTemplateFromJson(projectId);

        ResultDTO benchmarkResult;
        try {
            benchmarkResult = benchmarkService.runBenchmark(projectId, template, messageHeaderAccessor);
        } catch (BenchmarkRunException e) {
            logger.error(e.getException(), e);

            logger.trace("Create error result {} of benchmark to DB for project {}", e.getException(), projectId);
            BenchmarkDTO resultToSave = transformService.createErrorResult(projectId, template, e.getException());
            benchmarkServiceBackend.saveResult(resultToSave);

            BenchmarkRunErrorDTO error =  new BenchmarkRunErrorDTO(e.getException(), e.getFile());
            return gson.toJson(error);
        }

        logger.trace("Create result {} of benchmark to DB for project {}", benchmarkResult, projectId);
        BenchmarkDTO resultToSave = transformService.createResult(projectId, template, benchmarkResult);
        benchmarkServiceBackend.saveResult(resultToSave);

        return gson.toJson(benchmarkResult);
    }

    @MessageExceptionHandler
    @SendToUser(MappingURLConstants.SOCKET_EXCEPTION)
    public String handleException(Throwable exception) {
        return exception.getMessage();
    }

}
