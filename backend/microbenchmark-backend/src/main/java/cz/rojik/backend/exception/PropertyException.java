package cz.rojik.backend.exception;

/**
 * @author Marek Rojik (marek@rojik.cz) on 05. 01. 2019
 */
public class PropertyException extends RuntimeException {

    public PropertyException() {
        super();
    }

    public PropertyException(String message) {
        super(message);
    }
}
