package cz.rojik.service.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import cz.rojik.backend.util.serialization.LocalDateTimeDeserializer;
import cz.rojik.backend.util.serialization.LocalDateTimeSerializer;
import cz.rojik.service.enums.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ProcessInfoDTO {

    private static Logger logger = LoggerFactory.getLogger(ProcessInfoDTO.class);

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime time;

    private Operation operation;
    private int number;
    private String note;

    public ProcessInfoDTO() {
        this.time = LocalDateTime.now();
        this.note = "";
    }

    public ProcessInfoDTO(Operation operation) {
        this(LocalDateTime.now(), operation);
    }

    public ProcessInfoDTO(Operation operation, int number, String note) {
        this(LocalDateTime.now(), operation, number, note);
    }

    public ProcessInfoDTO(LocalDateTime time, Operation operation) {
        this(LocalDateTime.now(), operation, -1,"");
    }

    public ProcessInfoDTO(LocalDateTime time, Operation operation, int number) {
        this(LocalDateTime.now(), operation, number,"");
    }

    public ProcessInfoDTO(LocalDateTime time, Operation operation, int number, String note) {
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

    public ProcessInfoDTO setTime(LocalDateTime time) {
        this.time = time;
        return this;
    }

    public Operation getOperation() {
        return operation;
    }

    public ProcessInfoDTO setOperation(Operation operation) {
        this.operation = operation;
        return this;
    }

    public String getNote() {
        return note;
    }

    public ProcessInfoDTO setNote(String note) {
        this.note = note;
        return this;
    }

    public int getNumber() {
        return number;
    }

    public ProcessInfoDTO setNumber(int number) {
        this.number = number;
        return this;
    }
}
