package cz.rojik.dto;

import java.util.ArrayList;
import java.util.List;

public class ErrorInfoDTO {

    private List<ErrorDTO> errors;

    public ErrorInfoDTO() {
        this.errors = new ArrayList<>();
    }

    public ErrorInfoDTO(List<ErrorDTO> errors) {
        this.errors = errors;
    }

    public List<ErrorDTO> getErrors() {
        return errors;
    }

    public ErrorInfoDTO setErrors(List<ErrorDTO> errors) {
        this.errors = errors;
        return this;
    }

    @Override
    public String toString() {
        return "ErrorInfoDTO{" +
                "errors=" + errors +
                '}';
    }
}
