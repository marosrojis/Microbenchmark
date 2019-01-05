package cz.rojik.service.exception;

/**
 * @author Marek Rojik (marek@rojik.cz) on 05. 01. 2019
 */
public class KillContainerException extends RuntimeException {

    public KillContainerException(String message) {
        super(message);
    }

    public KillContainerException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
