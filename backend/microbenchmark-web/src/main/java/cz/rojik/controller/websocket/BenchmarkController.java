package cz.rojik.controller.websocket;

import cz.rojik.constants.MappingURLConstants;
import cz.rojik.dto.ResultDTO;
import cz.rojik.service.BenchmarkService;
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

    @MessageMapping(MappingURLConstants.BENCHMARK_RUN)
    @SendToUser(MappingURLConstants.BENCHMARK_RESULT)
    public String runBenchmark(SimpMessageHeaderAccessor headerAccessor, String projectId) {

        SimpMessageHeaderAccessor messageHeaderAccessor = SimpMessageHeaderAccessor
                .create(SimpMessageType.MESSAGE);
        messageHeaderAccessor.setSessionId(headerAccessor.getSessionId());
        messageHeaderAccessor.setLeaveMutable(true);

        ResultDTO result = benchmarkService.runBenchmark(projectId, messageHeaderAccessor);

        return new SimpleDateFormat("HH:mm:ss").format(new Date())+" - " + result;
    }

}
