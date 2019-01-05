package cz.rojik.service.service;

import cz.rojik.service.dto.ProcessInfoDTO;
import cz.rojik.service.dto.TemplateDTO;

/**
 * @author Marek Rojik (marek@rojik.cz) on 05. 01. 2019
 */
public interface MessageLogParserService {

    ProcessInfoDTO parseMessage(String message, TemplateDTO template);
}
