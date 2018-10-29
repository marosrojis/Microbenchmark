package cz.rojik.service.service;

import cz.rojik.service.dto.ProcessInfoDTO;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

public interface WebSocketService {

    void sendProcessInfo(ProcessInfoDTO processInfo, SimpMessageHeaderAccessor headerAccessor);
}
