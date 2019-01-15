package cz.rojik.service.service;

import cz.rojik.service.dto.ResultDTO;

/**
 * @author Marek Rojik (marek@rojik.cz) on 05. 01. 2019
 */
public interface ResultParserService {

    /**
     * Parse result from generated JSON file from JMH microbenchmark
     * @param projectId completed benchmark with result
     * @return result from generated file
     */
    ResultDTO parseResult(String projectId);
}
