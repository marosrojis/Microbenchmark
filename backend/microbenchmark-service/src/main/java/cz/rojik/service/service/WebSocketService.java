package cz.rojik.service.service;

import cz.rojik.service.dto.ProcessInfoDTO;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

/**
 * @author Marek Rojik (marek@rojik.cz) on 05. 01. 2019
 */
public interface WebSocketService {

    void sendProcessInfo(ProcessInfoDTO processInfo, SimpMessageHeaderAccessor headerAccessor);
}
