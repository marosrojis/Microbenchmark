package cz.rojik.backend.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import cz.rojik.backend.dto.user.UserDTO;
import cz.rojik.backend.util.serialization.LocalDateTimeDeserializer;
import cz.rojik.backend.util.serialization.LocalDateTimeSerializer;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class BenchmarkDTO extends BaseDTO {

    private String projectId;
    private String name;
    private String content;
    private int warmup;
    private int measurement;
    private String init;
    private String declare;
    private String jarUrl;
    private boolean success;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime created;

    private UserDTO user;
    private List<MeasureMethodDTO> measureMethods;

    @Override
    public String toString() {
        return "BenchmarkDTO{" +
                "projectId='" + projectId + '\'' +
                ", name=" + name +
                ", created=" + created +
                ", content='" + content + '\'' +
                ", warmup=" + warmup +
                ", measurement=" + measurement +
                ", init='" + init + '\'' +
                ", declare='" + declare + '\'' +
                ", user=" + user +
                ", measureMethods=" + measureMethods +
                ", jarUrl=" + jarUrl +
                ", success=" + success +
                '}';
    }

    public String getProjectId() {
        return projectId;
    }

    public BenchmarkDTO setProjectId(String projectId) {
        this.projectId = projectId;
        return this;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public BenchmarkDTO setCreated(LocalDateTime created) {
        this.created = created;
        return this;
    }

    public String getContent() {
        return content;
    }

    public BenchmarkDTO setContent(String content) {
        this.content = content;
        return this;
    }

    public int getWarmup() {
        return warmup;
    }

    public BenchmarkDTO setWarmup(int warmup) {
        this.warmup = warmup;
        return this;
    }

    public int getMeasurement() {
        return measurement;
    }

    public BenchmarkDTO setMeasurement(int measurement) {
        this.measurement = measurement;
        return this;
    }

    public String getInit() {
        return init;
    }

    public BenchmarkDTO setInit(String init) {
        this.init = init;
        return this;
    }

    public String getDeclare() {
        return declare;
    }

    public BenchmarkDTO setDeclare(String declare) {
        this.declare = declare;
        return this;
    }

    public UserDTO getUser() {
        return user;
    }

    public BenchmarkDTO setUser(UserDTO user) {
        this.user = user;
        return this;
    }

    public List<MeasureMethodDTO> getMeasureMethods() {
        return measureMethods;
    }

    public BenchmarkDTO setMeasureMethods(List<MeasureMethodDTO> measureMethods) {
        this.measureMethods = measureMethods;
        return this;
    }

    public String getName() {
        return name;
    }

    public BenchmarkDTO setName(String name) {
        this.name = name;
        return this;
    }

    public String getJarUrl() {
        return jarUrl;
    }

    public BenchmarkDTO setJarUrl(String jarUrl) {
        this.jarUrl = jarUrl;
        return this;
    }

    public boolean isSuccess() {
        return success;
    }

    public BenchmarkDTO setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        BenchmarkDTO that = (BenchmarkDTO) o;
        return warmup == that.warmup &&
                measurement == that.measurement &&
                Objects.equals(projectId, that.projectId) &&
                Objects.equals(name, that.name) &&
                Objects.equals(created, that.created) &&
                Objects.equals(content, that.content) &&
                Objects.equals(init, that.init) &&
                Objects.equals(declare, that.declare) &&
                Objects.equals(jarUrl, that.jarUrl) &&
                Objects.equals(success, that.success) &&
                Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), projectId, name, created, content, warmup, measurement, init, declare, user, measureMethods, jarUrl, success);
    }
}
