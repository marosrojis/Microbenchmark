package cz.rojik.service.service;

import cz.rojik.service.dto.ProcessInfoDTO;
import cz.rojik.service.dto.TemplateDTO;

public interface MessageLogParserService {

    ProcessInfoDTO parseMessage(String message, TemplateDTO template);
}
