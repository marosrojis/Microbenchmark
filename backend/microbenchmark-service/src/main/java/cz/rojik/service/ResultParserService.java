package cz.rojik.service;

import cz.rojik.dto.ResultDTO;

public interface ResultParserService {

    ResultDTO parseResult(String projectId);
}
