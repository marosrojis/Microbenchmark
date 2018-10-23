package cz.rojik.exception;

public class InvalidBearerTokenException extends RuntimeException {

    public InvalidBearerTokenException() {
        super();
    }

    public InvalidBearerTokenException(String message) {
        super(message);
    }
}
