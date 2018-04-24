package cz.rojik.dto;

public class Error {

    private String message;
    private int row;
    private String code;

    public Error(String message, int row) {
        this.message = message;
        this.row = row;
    }

    public String getMessage() {
        return message;
    }

    public Error setMessage(String message) {
        this.message = message;
        return this;
    }

    public int getRow() {
        return row;
    }

    public Error setRow(int row) {
        this.row = row;
        return this;
    }

    public String getCode() {
        return code;
    }

    public Error setCode(String code) {
        this.code = code;
        return this;
    }
}
