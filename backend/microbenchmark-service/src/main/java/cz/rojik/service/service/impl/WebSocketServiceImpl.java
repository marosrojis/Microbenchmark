package cz.rojik.service.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import cz.rojik.service.dto.ProcessInfoDTO;
import cz.rojik.service.service.WebSocketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

/**
 * @author Marek Rojik (marek@rojik.cz) on 05. 01. 2019
 */
@Service
public class WebSocketServiceImpl implements WebSocketService {

    private static Logger LOGGER = LoggerFactory.getLogger(WebSocketServiceImpl.class);

    private static final String BENCHMARK_RESULT_STEP = "/benchmark/result/step";

    @Autowired
    private SimpMessagingTemplate template;

    @Override
    public void sendProcessInfo(ProcessInfoDTO processInfo, SimpMessageHeaderAccessor headerAccessor) {
        LOGGER.trace("Send websocket with process info {} to user with session ID {}", processInfo, headerAccessor.getSessionId());
        this.template.convertAndSendToUser(headerAccessor.getSessionId(), BENCHMARK_RESULT_STEP, processInfo, headerAccessor.getMessageHeaders());
    }
}
