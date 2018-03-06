package cz.rojik.model;

import java.util.ArrayList;
import java.util.List;

public class ErrorInfo {

    private List<Error> errors;

    public ErrorInfo() {
        this.errors = new ArrayList<>();
    }

    public ErrorInfo(List<Error> errors) {
        this.errors = errors;
    }

    public List<Error> getErrors() {
        return errors;
    }

    public ErrorInfo setErrors(List<Error> errors) {
        this.errors = errors;
        return this;
    }
}
