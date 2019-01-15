package cz.rojik.service.service.impl;

import com.google.gson.*;
import cz.rojik.service.constants.ProjectContants;
import cz.rojik.service.dto.MicrobenchmarkResultDTO;
import cz.rojik.service.dto.ResultDTO;
import cz.rojik.service.exception.ReadFileException;
import cz.rojik.backend.properties.PathProperties;
import cz.rojik.service.service.ResultParserService;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Marek Rojik (marek@rojik.cz) on 05. 01. 2019
 */
@Service
public class ResultParserServiceImpl implements ResultParserService {

    private static Logger logger = LoggerFactory.getLogger(ResultParserServiceImpl.class);

    @Autowired
    private PathProperties pathProperties;

    @Override
    public ResultDTO parseResult(String projectId) {
        logger.trace("Parse result of benchmark {}", projectId);
        JsonParser jParser = new JsonParser();

        List<MicrobenchmarkResultDTO> mbResults = new ArrayList<>();
        String resultContent = readResultFile(projectId);

        JsonElement parsed = jParser.parse(resultContent);
        JsonArray jsonResults = parsed.getAsJsonArray();

        for (JsonElement object : jsonResults) {
            mbResults.add(parseBenchmarkResult(object));
        }
        logger.debug("Parsed result of project {} is {}", projectId, mbResults);

        logger.trace("Finding the fastest benchmark time for project {}", projectId);
        int minIndex = 0;
        double minScore = mbResults.get(0).getScore();
        for (int i = 1; i < mbResults.size(); i++) {
            MicrobenchmarkResultDTO mbResult = mbResults.get(i);
            if (mbResult.getScore() < minScore) {
                minScore = mbResult.getScore();
                minIndex = i;
            }
        }
        logger.debug("The fastest test method for project {} is {}", projectId, mbResults.get(minIndex));

        ResultDTO result = new ResultDTO()
                .setResults(mbResults)
                .setBestScoreIndex(minIndex);
        return result;
    }

    // PRIVATE

    /**
     * Get result from generated JSON file from JMH project.
     * File with result is in JSON format.
     * Method parses only one given measured method.
     * @param element measured method
     * @return parsed result of one method
     */
    private MicrobenchmarkResultDTO parseBenchmarkResult(JsonElement element) {
        JsonObject object = (JsonObject) element;
        String name = object.get("benchmark").getAsString();
        logger.trace("Get values of test method {}", name);

        int warmupIterations = object.get("warmupIterations").getAsInt();
        int measurementIterations = object.get("measurementIterations").getAsInt();

        JsonObject primaryMetric = object.getAsJsonObject("primaryMetric");
        String unit = primaryMetric.get("scoreUnit").getAsString();
        double score = primaryMetric.get("score").getAsDouble();
        double error = primaryMetric.get("scoreError").getAsDouble();

        List<Double> measureValues = new ArrayList<>();
        primaryMetric.getAsJsonArray("rawData").get(0).getAsJsonArray().forEach(value -> measureValues.add(value.getAsDouble()));

        return new MicrobenchmarkResultDTO(name, warmupIterations, measurementIterations, unit, measureValues, score, error);
    }

    /**
     * Read file with result of microbenchmark project.
     * @param projectId generated project ID
     * @return content of result file
     */
    private String readResultFile(String projectId) {
        logger.trace("Read result file for project {}", projectId);
        File file = new File(pathProperties.getResults() + projectId + ProjectContants.JSON_FILE_FORMAT);
        String fileContent = "";
        try {
            fileContent = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new ReadFileException(projectId);
        }

        logger.trace("Result file of project {} is {}", projectId, fileContent);
        return fileContent;
    }
}
