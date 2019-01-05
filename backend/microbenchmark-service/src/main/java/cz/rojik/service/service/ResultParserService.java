package cz.rojik.service.service;

import cz.rojik.service.dto.ResultDTO;

/**
 * @author Marek Rojik (marek@rojik.cz) on 05. 01. 2019
 */
public interface ResultParserService {

    ResultDTO parseResult(String projectId);
}
