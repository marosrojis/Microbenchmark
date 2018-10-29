package cz.rojik.backend.service;

import cz.rojik.backend.dto.ResultDTO;

import java.time.LocalDateTime;

public interface ResultService {

    ResultDTO saveResult(ResultDTO result);
}
