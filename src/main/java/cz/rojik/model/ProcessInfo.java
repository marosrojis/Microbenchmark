package cz.rojik.model;

import cz.rojik.Runner;
import cz.rojik.enums.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ProcessInfo {

    private static Logger logger = LoggerFactory.getLogger(ProcessInfo.class);

    private LocalDateTime time;
    private Operation operation;
    private int number;
    private String note;

    public ProcessInfo() {
        this.time = LocalDateTime.now();
        this.note = "";
    }

    public ProcessInfo(Operation operation) {
        this(LocalDateTime.now(), operation);
    }

    public ProcessInfo(Operation operation, int number, String note) {
        this(LocalDateTime.now(), operation, number, note);
    }

    public ProcessInfo(LocalDateTime time, Operation operation) {
        this(LocalDateTime.now(), operation, -1,"");
    }

    public ProcessInfo(LocalDateTime time, Operation operation, int number) {
        this(LocalDateTime.now(), operation, number,"");
    }

    public ProcessInfo(LocalDateTime time, Operation operation, int number, String note) {
        this.time = time;
        this.operation = operation;
        this.number = number;
        this.note = note;

        logger.info(this.toString());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .append(" - ")
                .append(operation.getType());

        if (number != -1) {
            sb.append(" ")
                    .append(number);
        }
        if (!note.equals("")) {
            sb.append(" (")
                    .append(note)
                    .append(")");
        }
        return sb.toString();
    }

    public LocalDateTime getTime() {
        return time;
    }

    public ProcessInfo setTime(LocalDateTime time) {
        this.time = time;
        return this;
    }

    public Operation getOperation() {
        return operation;
    }

    public ProcessInfo setOperation(Operation operation) {
        this.operation = operation;
        return this;
    }

    public String getNote() {
        return note;
    }

    public ProcessInfo setNote(String note) {
        this.note = note;
        return this;
    }

    public int getNumber() {
        return number;
    }

    public ProcessInfo setNumber(int number) {
        this.number = number;
        return this;
    }
}
