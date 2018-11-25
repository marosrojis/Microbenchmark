package cz.rojik.service.exception;

public class DockerException extends RuntimeException {

    public DockerException() {
        super();
    }

    public DockerException(String message) {
        super(message);
    }
}
