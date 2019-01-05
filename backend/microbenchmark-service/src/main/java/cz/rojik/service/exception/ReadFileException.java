package cz.rojik.service.exception;

/**
 * @author Marek Rojik (marek@rojik.cz) on 05. 01. 2019
 */
public class ReadFileException extends RuntimeException {

    public ReadFileException(String filename) {
        super("Read file " + filename + " failure");
    }

    public ReadFileException(String filename, String error) {
        super(error + " (" + filename + ")");
    }

    public ReadFileException(String filename, Throwable throwable) {
        super("Read file " + filename + " failure", throwable);
    }
}
