package cz.rojik.service;

import cz.rojik.dto.Result;

import java.time.LocalDateTime;

public interface ResultParserService {

    Result parseResult(String projectId, LocalDateTime time);
}
