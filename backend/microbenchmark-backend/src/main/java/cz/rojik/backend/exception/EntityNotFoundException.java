package cz.rojik.backend.exception;

public class EntityNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -2632582071879397439L;

    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
