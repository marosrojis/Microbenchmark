package cz.rojik.service.service;

import cz.rojik.service.dto.ProcessInfoDTO;
import cz.rojik.service.dto.TemplateDTO;

/**
 * @author Marek Rojik (marek@rojik.cz) on 05. 01. 2019
 */
public interface MessageLogParserService {

    /**
     * Parse important messages from JMH log (e.g iterate, warmup, measurement, result).
     * @param message log message
     * @param template code to measure
     * @return object contains code to measure
     */
    ProcessInfoDTO parseMessage(String message, TemplateDTO template);
}
