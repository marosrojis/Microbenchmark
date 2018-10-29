package cz.rojik.backend.exception;

public class InvalidBearerTokenException extends RuntimeException {

    public InvalidBearerTokenException() {
        super();
    }

    public InvalidBearerTokenException(String message) {
        super(message);
    }
}
