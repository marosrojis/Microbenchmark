package cz.rojik.backend.exception;

/**
 * @author Marek Rojik (marek@rojik.cz) on 05. 01. 2019
 */
public class InvalidBearerTokenException extends RuntimeException {

    private String details;

    public InvalidBearerTokenException(String message, String details) {
        super(message);
        this.details = details;
    }

    public String getDetails() {
        return details;
    }
}
