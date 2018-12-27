package cz.rojik.backend.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import cz.rojik.backend.dto.user.UserDTO;
import cz.rojik.backend.enums.BenchmarkStateTypeEnum;
import cz.rojik.backend.util.serialization.LocalDateTimeDeserializer;
import cz.rojik.backend.util.serialization.LocalDateTimeSerializer;
import cz.rojik.backend.util.serialization.LocalTimeDeserializer;
import cz.rojik.backend.util.serialization.LocalTimeSerializer;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

public class BenchmarkStateDTO extends BaseDTO {

    private String projectId;
    private String containerId;
    private BenchmarkStateTypeEnum type;
    private int numberOfConnections;
    private UserDTO user;
    private int completed;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime updated;

    @JsonSerialize(using = LocalTimeSerializer.class)
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    private LocalTime timeToEnd;

    public BenchmarkStateDTO() {
        updated = LocalDateTime.now();
        completed = 0;
    }

    public String getProjectId() {
        return projectId;
    }

    public BenchmarkStateDTO setProjectId(String projectId) {
        this.projectId = projectId;
        return this;
    }

    public String getContainerId() {
        return containerId;
    }

    public BenchmarkStateDTO setContainerId(String containerId) {
        this.containerId = containerId;
        return this;
    }

    public BenchmarkStateTypeEnum getType() {
        return type;
    }

    public BenchmarkStateDTO setType(BenchmarkStateTypeEnum type) {
        this.type = type;
        return this;
    }

    public LocalDateTime getUpdated() {
        return updated;
    }

    public BenchmarkStateDTO setUpdated(LocalDateTime updated) {
        this.updated = updated;
        return this;
    }

    public int getNumberOfConnections() {
        return numberOfConnections;
    }

    public BenchmarkStateDTO setNumberOfConnections(int numberOfConnections) {
        this.numberOfConnections = numberOfConnections;
        return this;
    }

    public UserDTO getUser() {
        return user;
    }

    public BenchmarkStateDTO setUser(UserDTO user) {
        this.user = user;
        return this;
    }

    public int getCompleted() {
        return completed;
    }

    public BenchmarkStateDTO setCompleted(int completed) {
        this.completed = completed;
        return this;
    }

    public LocalTime getTimeToEnd() {
        return timeToEnd;
    }

    public BenchmarkStateDTO setTimeToEnd(LocalTime timeToEnd) {
        this.timeToEnd = timeToEnd;
        return this;
    }

    @Override
    public String toString() {
        return "BenchmarkStateDTO{" +
                "projectId='" + projectId + '\'' +
                ", containerId='" + containerId + '\'' +
                ", type=" + type +
                ", numberOfConnections=" + numberOfConnections +
                ", user=" + user +
                ", completed=" + completed +
                ", updated=" + updated +
                ", timeToEnd=" + timeToEnd +
                ", id=" + id +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        BenchmarkStateDTO that = (BenchmarkStateDTO) o;
        return numberOfConnections == that.numberOfConnections &&
                completed == that.completed &&
                Objects.equals(projectId, that.projectId) &&
                Objects.equals(containerId, that.containerId) &&
                type == that.type &&
                Objects.equals(updated, that.updated) &&
                Objects.equals(timeToEnd, that.timeToEnd);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), projectId, containerId, type, numberOfConnections, completed, updated, timeToEnd);
    }
}
