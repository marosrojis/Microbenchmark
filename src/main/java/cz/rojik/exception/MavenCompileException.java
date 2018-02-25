package cz.rojik.exception;

public class MavenCompileException extends RuntimeException {

    public MavenCompileException() {
        super("Failure maven compilation");
    }

    public MavenCompileException(String error) {
        super(error);
    }
}
