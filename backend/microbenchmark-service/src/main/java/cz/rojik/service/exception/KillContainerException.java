package cz.rojik.service.exception;

public class KillContainerException extends RuntimeException {

    public KillContainerException(String message) {
        super(message);
    }

    public KillContainerException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
