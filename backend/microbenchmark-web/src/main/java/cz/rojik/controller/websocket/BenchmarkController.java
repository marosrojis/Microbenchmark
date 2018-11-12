package cz.rojik.controller.websocket;

import cz.rojik.backend.dto.BenchmarkDTO;
import cz.rojik.backend.service.BenchmarkService;
import cz.rojik.backend.util.SecurityHelper;
import cz.rojik.constants.MappingURLConstants;
import cz.rojik.service.TransformService;
import cz.rojik.service.dto.BenchmarkRunErrorDTO;
import cz.rojik.service.dto.ResultDTO;
import cz.rojik.service.dto.TemplateDTO;
import cz.rojik.service.exception.BenchmarkRunException;
import cz.rojik.service.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.text.SimpleDateFormat;
import java.util.Date;

@Controller("webSocketBenchmarkController")
public class BenchmarkController {

    @Autowired
    private cz.rojik.service.service.BenchmarkService benchmarkService;

    @Autowired
    private TransformService transformService;

    @Autowired
    private BenchmarkService benchmarkServiceBackend;

    @MessageMapping(MappingURLConstants.BENCHMARK_RUN)
    @SendToUser(MappingURLConstants.BENCHMARK_RESULT)
    public String runBenchmark(SimpMessageHeaderAccessor headerAccessor, String projectId) {

        SimpMessageHeaderAccessor messageHeaderAccessor = SimpMessageHeaderAccessor
                .create(SimpMessageType.MESSAGE);
        messageHeaderAccessor.setSessionId(headerAccessor.getSessionId());
        messageHeaderAccessor.setLeaveMutable(true);

        TemplateDTO template = FileUtils.getTemplateFromJson(projectId);

        ResultDTO benchmarkResult = null;
        try {
            benchmarkResult = benchmarkService.runBenchmark(projectId, template, messageHeaderAccessor);
        } catch (BenchmarkRunException e) {
            return new BenchmarkRunErrorDTO(e.getException(), e.getFile()).toString();
        }

        BenchmarkDTO resultToSave = transformService.createResult(projectId, template, benchmarkResult);
        resultToSave = benchmarkServiceBackend.saveResult(resultToSave);

        return new SimpleDateFormat("HH:mm:ss").format(new Date())+" - " + benchmarkResult;
    }

    @MessageExceptionHandler
    @SendToUser(MappingURLConstants.SOCKET_EXCEPTION)
    public String handleException(Throwable exception) {
        return exception.getMessage();
    }

}
