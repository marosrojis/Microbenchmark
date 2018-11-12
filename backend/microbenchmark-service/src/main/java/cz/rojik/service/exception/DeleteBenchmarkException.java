package cz.rojik.service.exception;

public class DeleteBenchmarkException extends RuntimeException {

    public DeleteBenchmarkException() {
        super();
    }

    public DeleteBenchmarkException(String message) {
        super(message);
    }
}
