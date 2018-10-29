package cz.rojik.service.dto;

import java.util.List;
import java.util.Objects;

public class ErrorInfoWithSourceCodeDTO {

    private List<ErrorInfoDTO> errors;
    private List<String> sourceCode;

    public ErrorInfoWithSourceCodeDTO(List<ErrorInfoDTO> errors, List<String> sourceCode) {
        this.errors = errors;
        this.sourceCode = sourceCode;
    }

    public List<ErrorInfoDTO> getErrors() {
        return errors;
    }

    public ErrorInfoWithSourceCodeDTO setErrors(List<ErrorInfoDTO> errors) {
        this.errors = errors;
        return this;
    }

    public List<String> getSourceCode() {
        return sourceCode;
    }

    public ErrorInfoWithSourceCodeDTO setSourceCode(List<String> sourceCode) {
        this.sourceCode = sourceCode;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ErrorInfoWithSourceCodeDTO that = (ErrorInfoWithSourceCodeDTO) o;
        return Objects.equals(errors, that.errors) &&
                Objects.equals(sourceCode, that.sourceCode);
    }

    @Override
    public int hashCode() {

        return Objects.hash(errors, sourceCode);
    }

    @Override
    public String toString() {
        return "ErrorInfoWithSourceCodeDTO{" +
                "errors=" + errors +
                ", sourceCode=" + sourceCode +
                '}';
    }
}
