package cz.rojik.service.exception;

/**
 * @author Marek Rojik (marek@rojik.cz) on 05. 01. 2019
 */
public class DockerException extends RuntimeException {

    public DockerException() {
        super();
    }

    public DockerException(String message) {
        super(message);
    }

    public DockerException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
