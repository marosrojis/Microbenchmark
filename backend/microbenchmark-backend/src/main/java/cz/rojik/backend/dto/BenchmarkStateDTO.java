package cz.rojik.backend.dto;

import cz.rojik.backend.dto.user.UserDTO;
import cz.rojik.backend.entity.BenchmarkStateType;

import java.time.LocalDateTime;
import java.util.Objects;

public class BenchmarkStateDTO extends BaseDTO {

    private String projectId;
    private String containerId;
    private BenchmarkStateType type;
    private LocalDateTime updated;
    private int numberOfConnections;
    private UserDTO user;

    public BenchmarkStateDTO() {
    }

    public BenchmarkStateDTO(String projectId, String containerId, BenchmarkStateType type, LocalDateTime updated) {
        this.projectId = projectId;
        this.containerId = containerId;
        this.type = type;
        this.updated = updated;
    }

    @Override
    public String toString() {
        return "BechmarkStateDTO{" +
                "projectId='" + projectId + '\'' +
                ", containerId='" + containerId + '\'' +
                ", type=" + type +
                ", updated=" + updated +
                ", numberOfConnections=" + numberOfConnections +
                ", user=" + user +
                ", id=" + id +
                '}';
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

    public BenchmarkStateType getType() {
        return type;
    }

    public BenchmarkStateDTO setType(BenchmarkStateType type) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        BenchmarkStateDTO that = (BenchmarkStateDTO) o;
        return numberOfConnections == that.numberOfConnections &&
                Objects.equals(projectId, that.projectId) &&
                Objects.equals(containerId, that.containerId) &&
                type == that.type &&
                Objects.equals(updated, that.updated) &&
                Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), projectId, containerId, type, updated, numberOfConnections, user);
    }
}
