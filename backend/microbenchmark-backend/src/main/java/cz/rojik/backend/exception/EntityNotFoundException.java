package cz.rojik.backend.exception;

/**
 * @author Marek Rojik (marek@rojik.cz) on 05. 01. 2019
 */
public class EntityNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -2632582071879397439L;

    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
