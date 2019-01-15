package cz.rojik.service.exception;

import java.util.List;
import java.util.Map;

/**
 * Exception is thrown when something is wrong during running benchmark
 * @author Marek Rojik (marek@rojik.cz) on 05. 01. 2019
 */
public class BenchmarkRunException extends RuntimeException {

    private String projectId;
    private String exception;
    private List<String> file;

    public BenchmarkRunException(String projectId, String exception, List<String> file) {
        super("An error occurred during the microbenchmark.");
        this.projectId = projectId;
        this.exception = exception;
        this.file = file;
    }

    public String getProjectId() {
        return projectId;
    }

    public String getException() {
        return exception;
    }

    public List<String> getFile() {
        return file;
    }
}
