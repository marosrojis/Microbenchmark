package cz.rojik.service.impl;

import cz.rojik.constants.ProjectContants;
import cz.rojik.exception.ReadFileException;
import cz.rojik.dto.MicrobenchmarkResultDTO;
import cz.rojik.dto.ResultDTO;
import cz.rojik.service.ResultParserService;
import org.apache.commons.io.FileUtils;
import com.google.gson.*;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ResultParserServiceImpl implements ResultParserService {

    @Override
    public ResultDTO parseResult(String projectId, LocalDateTime time) {
        JsonParser jParser = new JsonParser();

        List<MicrobenchmarkResultDTO> mbResults = new ArrayList<>();
        String resultContent = readResultFile(projectId);

        JsonElement parsed = jParser.parse(resultContent);
        JsonArray jsonResults = parsed.getAsJsonArray();

        for (JsonElement object : jsonResults) {
            mbResults.add(parseBenchmarkResult(object));
        }

        int minIndex = 0;
        double minScore = mbResults.get(0).getScore();
        for (int i = 1; i < mbResults.size(); i++) {
            MicrobenchmarkResultDTO mbResult = mbResults.get(i);
            if (mbResult.getScore() < minScore) {
                minScore = mbResult.getScore();
                minIndex = i;
            }
        }
        mbResults.get(minIndex).setFastest(true);

        ResultDTO result = new ResultDTO(time, true)
                .setResults(mbResults);
        return result;
    }

    // PRIVATE

    private MicrobenchmarkResultDTO parseBenchmarkResult(JsonElement element) {
        JsonObject object = (JsonObject) element;
        String name = object.get("benchmark").getAsString();
        int warmupIterations = object.get("warmupIterations").getAsInt();
        int measurementIterations = object.get("measurementIterations").getAsInt();

        JsonObject primaryMetric = object.getAsJsonObject("primaryMetric");
        String unit = primaryMetric.get("scoreUnit").getAsString();
        double score = primaryMetric.get("score").getAsDouble();
        double error = primaryMetric.get("scoreError").getAsDouble();
        return new MicrobenchmarkResultDTO(name, warmupIterations, measurementIterations, unit, score, error);
    }

    private String readResultFile(String projectId) {
        File file = new File(ProjectContants.PATH_RESULT + projectId + ProjectContants.JSON_FILE_FORMAT);
        String fileContent = "";
        try {
            fileContent = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ReadFileException(projectId);
        }

        return fileContent;
    }
}
