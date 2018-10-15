package cz.rojik.exception;

import cz.rojik.dto.ErrorInfoWithSourceCodeDTO;

public class MavenCompileException extends RuntimeException {

    public MavenCompileException() {
        super("Failure maven compilation");
    }

    public MavenCompileException(ErrorInfoWithSourceCodeDTO errors) {
        super("Failure maven compilation");
        this.errors = errors;
    }

    public MavenCompileException(String error) {
        super(error);
    }

    private ErrorInfoWithSourceCodeDTO errors;

    public ErrorInfoWithSourceCodeDTO getErrors() {
        return errors;
    }
}
