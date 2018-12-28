package cz.rojik.service.exception;

public class DeleteBenchmarkException extends RuntimeException {

    public DeleteBenchmarkException() {
        super();
    }

    public DeleteBenchmarkException(String message) {
        super(message);
    }

    public DeleteBenchmarkException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
