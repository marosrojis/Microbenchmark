package cz.rojik.service;

import cz.rojik.dto.ProcessInfoDTO;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

public interface WebSocketService {

    void sendProcessInfo(ProcessInfoDTO processInfo, SimpMessageHeaderAccessor headerAccessor);
}
