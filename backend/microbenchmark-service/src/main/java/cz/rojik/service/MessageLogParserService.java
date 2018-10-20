package cz.rojik.service;

import cz.rojik.dto.ProcessInfoDTO;
import cz.rojik.dto.TemplateDTO;

public interface MessageLogParserService {

    ProcessInfoDTO parseMessage(String message, TemplateDTO template);
}
