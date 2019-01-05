package cz.rojik.service.dto;

import java.util.List;
import java.util.Objects;

/**
 * @author Marek Rojik (marek@rojik.cz) on 05. 01. 2019
 */
public class BenchmarkRunErrorDTO {

    private String exception;
    private List<String> sourceCode;

    public BenchmarkRunErrorDTO(String exception, List<String> sourceCode) {
        this.exception = exception;
        this.sourceCode = sourceCode;
    }

    @Override
    public String toString() {
        return "BenchmarkRunErrorDTO{" +
                "exception='" + exception + '\'' +
                ", sourceCode=" + sourceCode +
                '}';
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public List<String> getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(List<String> sourceCode) {
        this.sourceCode = sourceCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BenchmarkRunErrorDTO that = (BenchmarkRunErrorDTO) o;
        return Objects.equals(exception, that.exception) &&
                Objects.equals(sourceCode, that.sourceCode);
    }

    @Override
    public int hashCode() {

        return Objects.hash(exception, sourceCode);
    }
}
