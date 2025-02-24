package cz.rojik.service.exception;

/**
 * Exception is thrown when write to file is failed
 * @author Marek Rojik (marek@rojik.cz) on 05. 01. 2019
 */
public class WriteFileException extends RuntimeException {

    public WriteFileException(String filename) {
        super("Write file " + filename + " failure");
    }

    public WriteFileException(String filename, String error) {
        super(error + " (" + filename + ")");
    }
}
