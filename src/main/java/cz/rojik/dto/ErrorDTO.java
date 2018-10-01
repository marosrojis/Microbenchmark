package cz.rojik.dto;

public class ErrorDTO {

    private String message;
    private int row;
    private String code;

    public ErrorDTO(String message, int row) {
        this.message = message;
        this.row = row;
    }

    public String getMessage() {
        return message;
    }

    public ErrorDTO setMessage(String message) {
        this.message = message;
        return this;
    }

    public int getRow() {
        return row;
    }

    public ErrorDTO setRow(int row) {
        this.row = row;
        return this;
    }

    public String getCode() {
        return code;
    }

    public ErrorDTO setCode(String code) {
        this.code = code;
        return this;
    }

    @Override
    public String toString() {
        return "ErrorDTO{" +
                "message='" + message + '\'' +
                ", row=" + row +
                ", code='" + code + '\'' +
                '}';
    }
}
