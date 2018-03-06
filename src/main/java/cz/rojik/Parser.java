package cz.rojik;

import cz.rojik.exception.ReadFileException;
import cz.rojik.model.MicrobenchmarkResult;
import cz.rojik.model.Result;
import org.apache.commons.io.FileUtils;
import com.google.gson.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Parser {

    private JsonParser jParser;

    public Parser() {
        jParser = new JsonParser();
    }

    public Result parseResult(String fileName, LocalDateTime time) {
        List<MicrobenchmarkResult> mbResults = new ArrayList<>();
        String resultContent = readResultFile(fileName);

        JsonElement parsed = jParser.parse(resultContent);
        JsonArray jsonResults = parsed.getAsJsonArray();

        for (JsonElement object : jsonResults) {
            mbResults.add(parseBenchmarkResult(object));
        }

        int minIndex = 0;
        double minScore = mbResults.get(0).getScore();
        for (int i = 1; i < mbResults.size(); i++) {
            MicrobenchmarkResult mbResult = mbResults.get(i);
            if (mbResult.getScore() < minScore) {
                minScore = mbResult.getScore();
                minIndex = i;
            }
        }
        mbResults.get(minIndex).setFastest(true);

        Result result = new Result(time, true)
                .setResults(mbResults);
        return result;
    }

    private MicrobenchmarkResult parseBenchmarkResult(JsonElement element) {
        JsonObject object = (JsonObject) element;
        String name = object.get("benchmark").getAsString();
        int warmupIterations = object.get("warmupIterations").getAsInt();
        int measurementIterations = object.get("measurementIterations").getAsInt();

        JsonObject primaryMetric = object.getAsJsonObject("primaryMetric");
        String unit = primaryMetric.get("scoreUnit").getAsString();
        double score = primaryMetric.get("score").getAsDouble();
        double error = primaryMetric.get("scoreError").getAsDouble();
        return new MicrobenchmarkResult(name, warmupIterations, measurementIterations, unit, score, error);
    }

    private String readResultFile(String fileName) {
        File file = new File(fileName);
        String fileContent = "";
        try {
            fileContent = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ReadFileException(fileName);
        }

        return fileContent;
    }
}
