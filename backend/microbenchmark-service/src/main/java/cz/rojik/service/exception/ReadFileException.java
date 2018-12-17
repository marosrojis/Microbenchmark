package cz.rojik.service.exception;

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
