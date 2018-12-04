package cz.rojik.service.exception;

public class WriteFileException extends RuntimeException {

    public WriteFileException(String filename) {
        super("Write file " + filename + " failure");
    }

    public WriteFileException(String filename, String error) {
        super(error + " (" + filename + ")");
    }
}
