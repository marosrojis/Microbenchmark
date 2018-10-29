package cz.rojik.controller.websocket;

import cz.rojik.backend.service.ResultService;
import cz.rojik.constants.MappingURLConstants;
import cz.rojik.controller.rest.util.converter.ResultConverter;
import cz.rojik.service.TransformService;
import cz.rojik.service.dto.ResultDTO;
import cz.rojik.service.dto.TemplateDTO;
import cz.rojik.service.service.BenchmarkService;
import cz.rojik.service.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
    private SimpMessagingTemplate template;

    @Autowired
    private BenchmarkService benchmarkService;

    @Autowired
    private TransformService transformService;

    @Autowired
    private ResultService resultService;

    @MessageMapping(MappingURLConstants.BENCHMARK_RUN)
    @SendToUser(MappingURLConstants.BENCHMARK_RESULT)
    public String runBenchmark(SimpMessageHeaderAccessor headerAccessor, String projectId) {

        SimpMessageHeaderAccessor messageHeaderAccessor = SimpMessageHeaderAccessor
                .create(SimpMessageType.MESSAGE);
        messageHeaderAccessor.setSessionId(headerAccessor.getSessionId());
        messageHeaderAccessor.setLeaveMutable(true);

        TemplateDTO template = FileUtils.getTemplateFromJson(projectId);
        ResultDTO benchmarkResult = benchmarkService.runBenchmark(projectId, template, messageHeaderAccessor);

        cz.rojik.backend.dto.ResultDTO resultToSave = transformService.createResult(projectId, template, benchmarkResult);
        resultToSave = resultService.saveResult(resultToSave);

        return new SimpleDateFormat("HH:mm:ss").format(new Date())+" - " + benchmarkResult;
    }

}
