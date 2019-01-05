package cz.rojik.backend.exception;

/**
 * @author Marek Rojik (marek@rojik.cz) on 05. 01. 2019
 */
public class InvalidBearerTokenException extends RuntimeException {

    public InvalidBearerTokenException() {
        super();
    }

    public InvalidBearerTokenException(String message) {
        super(message);
    }
}
