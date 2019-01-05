package cz.rojik.backend.exception;

/**
 * @author Marek Rojik (marek@rojik.cz) on 05. 01. 2019
 */
public class UserException extends RuntimeException {

    public UserException() {
        super();
    }

    public UserException(String message) {
        super(message);
    }
}
