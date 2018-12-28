package cz.rojik.backend.entity;

import cz.rojik.backend.enums.BenchmarkStateTypeEnum;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

@Entity
@SQLDelete(sql = "UPDATE mbmark_benchmark_state SET archived = true WHERE id = ?")
@Table(
        name = "mbmark_benchmark_state",
        uniqueConstraints = @UniqueConstraint(columnNames = {"project_id"}),
        indexes = @Index(columnList = "project_id"))
public class BenchmarkStateEntity extends BaseEntity {

    @Column(name = "project_id", nullable = false)
    private String projectId;

    @Column(name = "container_id")
    private String containerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 32)
    private BenchmarkStateTypeEnum type;

    @Column(name = "updated", nullable = false)
    private LocalDateTime updated;

    @Column(name = "number_of_connections", nullable = false)
    private int numberOfConnections;

    @Column(name = "completed", nullable = false)
    private int completed;

    @Column(name = "timeToEnd")
    private LocalTime timeToEnd;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    public String getProjectId() {
        return projectId;
    }

    public BenchmarkStateEntity setProjectId(String projectId) {
        this.projectId = projectId;
        return this;
    }

    public String getContainerId() {
        return containerId;
    }

    public BenchmarkStateEntity setContainerId(String containerId) {
        this.containerId = containerId;
        return this;
    }

    public BenchmarkStateTypeEnum getType() {
        return type;
    }

    public BenchmarkStateEntity setType(BenchmarkStateTypeEnum type) {
        this.type = type;
        return this;
    }

    public LocalDateTime getUpdated() {
        return updated;
    }

    public BenchmarkStateEntity setUpdated(LocalDateTime updated) {
        this.updated = updated;
        return this;
    }

    public int getNumberOfConnections() {
        return numberOfConnections;
    }

    public BenchmarkStateEntity setNumberOfConnections(int numberOfConnections) {
        this.numberOfConnections = numberOfConnections;
        return this;
    }

    public UserEntity getUser() {
        return user;
    }

    public BenchmarkStateEntity setUser(UserEntity user) {
        this.user = user;
        return this;
    }

    public int getCompleted() {
        return completed;
    }

    public BenchmarkStateEntity setCompleted(int completed) {
        this.completed = completed;
        return this;
    }

    public LocalTime getTimeToEnd() {
        return timeToEnd;
    }

    public BenchmarkStateEntity setTimeToEnd(LocalTime timeToEnd) {
        this.timeToEnd = timeToEnd;
        return this;
    }

    @Override
    public String toString() {
        return "BenchmarkStateEntity{" +
                "projectId='" + projectId + '\'' +
                ", containerId='" + containerId + '\'' +
                ", type=" + type +
                ", updated=" + updated +
                ", numberOfConnections=" + numberOfConnections +
                ", completed=" + completed +
                ", timeToEnd=" + timeToEnd +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        BenchmarkStateEntity entity = (BenchmarkStateEntity) o;
        return numberOfConnections == entity.numberOfConnections &&
                completed == entity.completed &&
                Objects.equals(projectId, entity.projectId) &&
                Objects.equals(containerId, entity.containerId) &&
                type == entity.type &&
                Objects.equals(updated, entity.updated) &&
                Objects.equals(timeToEnd, entity.timeToEnd);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), projectId, containerId, type, updated, numberOfConnections, completed, timeToEnd);
    }
}
