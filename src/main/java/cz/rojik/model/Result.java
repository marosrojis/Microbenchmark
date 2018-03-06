package cz.rojik.model;

import java.time.LocalDateTime;
import java.util.List;

public class Result {

    private LocalDateTime time;
    private List<MicrobenchmarkResult> results;
    private List<ErrorInfo> errors;
    private boolean success;

    public Result(LocalDateTime time, boolean success) {
        this.time = time;
        this.success = success;
    }

    public Result(LocalDateTime time, List<MicrobenchmarkResult> results, boolean success) {
        this.time = time;
        this.results = results;
        this.success = success;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public Result setTime(LocalDateTime time) {
        this.time = time;
        return this;
    }

    public List<MicrobenchmarkResult> getResults() {
        return results;
    }

    public Result setResults(List<MicrobenchmarkResult> results) {
        this.results = results;
        return this;
    }

    public boolean isSuccess() {
        return success;
    }

    public Result setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public List<ErrorInfo> getErrors() {
        return errors;
    }

    public Result setErrors(List<ErrorInfo> errors) {
        this.errors = errors;
        return this;
    }
}
