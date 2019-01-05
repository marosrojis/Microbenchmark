package cz.rojik.service.exception;

import cz.rojik.service.dto.ErrorInfoWithSourceCodeDTO;

/**
 * @author Marek Rojik (marek@rojik.cz) on 05. 01. 2019
 */
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
