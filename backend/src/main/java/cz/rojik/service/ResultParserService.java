package cz.rojik.service;

import cz.rojik.dto.ResultDTO;

import java.time.LocalDateTime;

public interface ResultParserService {

    ResultDTO parseResult(String projectId);
}
