package cz.rojik.service;

import cz.rojik.dto.ProcessInfo;
import cz.rojik.dto.Template;

public interface MessageLogParserService {

    ProcessInfo parseMessage(String message, Template template);
}
