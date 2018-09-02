package cz.rojik.dto;

import java.time.LocalDateTime;
import java.util.List;

public class ResultDTO {

    private LocalDateTime time;
    private List<MicrobenchmarkResultDTO> results;
    private List<ErrorInfoDTO> errors;
    private boolean success;
    private int bestScoreIndex;

    public ResultDTO(LocalDateTime time, boolean success) {
        this.time = time;
        this.success = success;
    }

    public ResultDTO(LocalDateTime time, List<MicrobenchmarkResultDTO> results, boolean success, int fasterstIndex) {
        this.time = time;
        this.results = results;
        this.success = success;
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

    public boolean isSuccess() {
        return success;
    }

    public ResultDTO setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public List<ErrorInfoDTO> getErrors() {
        return errors;
    }

    public ResultDTO setErrors(List<ErrorInfoDTO> errors) {
        this.errors = errors;
        return this;
    }

    public int getBestScoreIndex() {
        return bestScoreIndex;
    }

    public ResultDTO setBestScoreIndex(int bestScoreIndex) {
        this.bestScoreIndex = bestScoreIndex;
        return this;
    }
}
