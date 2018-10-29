package cz.rojik.backend.dto;

import cz.rojik.backend.dto.user.UserDTO;

import java.time.LocalDateTime;
import java.util.List;

public class ResultDTO extends BaseDTO {

    private String projectId;
    private LocalDateTime created;
    private String content;
    private int warmup;
    private int measurement;
    private String init;
    private String declare;

    private UserDTO user;
    private List<MeasureMethodDTO> measureMethods;

    @Override
    public String toString() {
        return "ResultDTO{" +
                "projectId='" + projectId + '\'' +
                ", created=" + created +
                ", content='" + content + '\'' +
                ", warmup=" + warmup +
                ", measurement=" + measurement +
                ", init='" + init + '\'' +
                ", declare='" + declare + '\'' +
                ", user=" + user +
                ", measureMethods=" + measureMethods +
                '}';
    }

    public String getProjectId() {
        return projectId;
    }

    public ResultDTO setProjectId(String projectId) {
        this.projectId = projectId;
        return this;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public ResultDTO setCreated(LocalDateTime created) {
        this.created = created;
        return this;
    }

    public String getContent() {
        return content;
    }

    public ResultDTO setContent(String content) {
        this.content = content;
        return this;
    }

    public int getWarmup() {
        return warmup;
    }

    public ResultDTO setWarmup(int warmup) {
        this.warmup = warmup;
        return this;
    }

    public int getMeasurement() {
        return measurement;
    }

    public ResultDTO setMeasurement(int measurement) {
        this.measurement = measurement;
        return this;
    }

    public String getInit() {
        return init;
    }

    public ResultDTO setInit(String init) {
        this.init = init;
        return this;
    }

    public String getDeclare() {
        return declare;
    }

    public ResultDTO setDeclare(String declare) {
        this.declare = declare;
        return this;
    }

    public UserDTO getUser() {
        return user;
    }

    public ResultDTO setUser(UserDTO user) {
        this.user = user;
        return this;
    }

    public List<MeasureMethodDTO> getMeasureMethods() {
        return measureMethods;
    }

    public ResultDTO setMeasureMethods(List<MeasureMethodDTO> measureMethods) {
        this.measureMethods = measureMethods;
        return this;
    }
}
