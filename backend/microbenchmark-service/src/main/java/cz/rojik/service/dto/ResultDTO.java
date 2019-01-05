package cz.rojik.service.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import cz.rojik.backend.util.serialization.LocalDateTimeDeserializer;
import cz.rojik.backend.util.serialization.LocalDateTimeSerializer;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * @author Marek Rojik (marek@rojik.cz) on 05. 01. 2019
 */
public class ResultDTO {

    private List<MicrobenchmarkResultDTO> results;
    private int bestScoreIndex;
    private int numberOfConnections;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime time;

    public ResultDTO() {
        this.time = LocalDateTime.now();
    }

    public ResultDTO(LocalDateTime time, List<MicrobenchmarkResultDTO> results, int fasterstIndex) {
        this.time = time;
        this.results = results;
        this.bestScoreIndex = fasterstIndex;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public ResultDTO setTime(LocalDateTime time) {
        this.time = time;
        return this;
    }

    public List<MicrobenchmarkResultDTO> getResults() {
        return results;
    }

    public ResultDTO setResults(List<MicrobenchmarkResultDTO> results) {
        this.results = results;
        return this;
    }

    public int getBestScoreIndex() {
        return bestScoreIndex;
    }

    public ResultDTO setBestScoreIndex(int bestScoreIndex) {
        this.bestScoreIndex = bestScoreIndex;
        return this;
    }

    public int getNumberOfConnections() {
        return numberOfConnections;
    }

    public ResultDTO setNumberOfConnections(int numberOfConnections) {
        this.numberOfConnections = numberOfConnections;
        return this;
    }

    @Override
    public String toString() {
        return "ResultDTO{" +
                "time=" + time +
                ", results=" + results +
                ", bestScoreIndex=" + bestScoreIndex +
                ", numberOfConnections=" + numberOfConnections +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResultDTO resultDTO = (ResultDTO) o;
        return bestScoreIndex == resultDTO.bestScoreIndex &&
                numberOfConnections == resultDTO.numberOfConnections &&
                Objects.equals(time, resultDTO.time) &&
                Objects.equals(results, resultDTO.results);
    }

    @Override
    public int hashCode() {

        return Objects.hash(time, results, bestScoreIndex, numberOfConnections);
    }
}
