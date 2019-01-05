package cz.rojik.backend.exception;

/**
 * @author Marek Rojik (marek@rojik.cz) on 05. 01. 2019
 */
public class ObjectNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ObjectNotFoundException(String message) {
        super(message);
    }

    public ObjectNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
