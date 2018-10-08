package cz.rojik.exception;

import cz.rojik.dto.ErrorInfoDTO;

import java.util.List;

public class MavenCompileException extends RuntimeException {

    public MavenCompileException() {
        super("Failure maven compilation");
    }

    public MavenCompileException(List<ErrorInfoDTO> errors) {
        super("Failure maven compilation");
        this.errors = errors;
    }

    public MavenCompileException(String error) {
        super(error);
    }

    private List<ErrorInfoDTO> errors;

    public List<ErrorInfoDTO> getErrors() {
        return errors;
    }
}
