package cz.rojik.service.service;

import cz.rojik.service.dto.ResultDTO;

public interface ResultParserService {

    ResultDTO parseResult(String projectId);
}
