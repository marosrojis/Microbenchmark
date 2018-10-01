package cz.rojik.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import cz.rojik.constants.MappingURLConstants;
import cz.rojik.dto.ProcessInfoDTO;
import cz.rojik.service.WebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketServiceImpl implements WebSocketService {

    @Autowired
    private SimpMessagingTemplate template;

    @Override
    public void sendProcessInfo(ProcessInfoDTO processInfo, SimpMessageHeaderAccessor headerAccessor) {
        Gson gson = new GsonBuilder().create();
        String output = gson.toJson(processInfo);

        this.template.convertAndSendToUser(headerAccessor.getSessionId(), MappingURLConstants.BENCHMARK_RESULT_STEP, processInfo.toString(), headerAccessor.getMessageHeaders());
    }
}
