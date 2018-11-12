package cz.rojik.backend.exception;

public class BenchmarkNotFoundException extends RuntimeException {

    public BenchmarkNotFoundException() {
        super();
    }

    public BenchmarkNotFoundException(String message) {
        super(message);
    }
}
