package cz.rojik.backend.entity;

import org.hibernate.annotations.SQLDelete;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@SQLDelete(sql = "UPDATE mbmark_benchmark SET archived = true WHERE id = ?")
@Table(
        name = "mbmark_benchmark",
        uniqueConstraints = @UniqueConstraint(columnNames = {"project_id"}),
        indexes = @Index(columnList = "project_id"))
public class BenchmarkEntity extends BaseEntity {

    @Column(name = "project_id", nullable = false)
    private String projectId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "created", nullable = false)
    private LocalDateTime created;

    @Column(name = "content", nullable = false, length = 8192)
    private String content;

    @Column(name = "warmup", nullable = false)
    private int warmup;

    @Column(name = "measurement", nullable = false)
    private int measurement;

    @Column(name = "init", length = 4096)
    private String init;

    @Column(name = "declare", length = 4096)
    private String declare;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "result")
    private List<MeasureMethodEntity> measureMethods;

    @Column(name = "success", nullable = false)
    private boolean success;

    @Override
    public String toString() {
        return "ResultEntity{" +
                "projectId='" + projectId + '\'' +
                ", name=" + name +
                ", created=" + created +
                ", content='" + content + '\'' +
                ", warmup=" + warmup +
                ", measurement=" + measurement +
                ", init='" + init + '\'' +
                ", declare='" + declare + '\'' +
                ", measureMethods=" + measureMethods +
                ", success=" + success +
                '}';
    }

    public String getProjectId() {
        return projectId;
    }

    public BenchmarkEntity setProjectId(String projectId) {
        this.projectId = projectId;
        return this;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public BenchmarkEntity setCreated(LocalDateTime created) {
        this.created = created;
        return this;
    }

    public String getContent() {
        return content;
    }

    public BenchmarkEntity setContent(String content) {
        this.content = content;
        return this;
    }

    public int getMeasurement() {
        return measurement;
    }

    public BenchmarkEntity setMeasurement(int measurement) {
        this.measurement = measurement;
        return this;
    }

    public String getInit() {
        return init;
    }

    public BenchmarkEntity setInit(String init) {
        this.init = init;
        return this;
    }

    public String getDeclare() {
        return declare;
    }

    public BenchmarkEntity setDeclare(String declare) {
        this.declare = declare;
        return this;
    }

    public List<MeasureMethodEntity> getMeasureMethods() {
        return measureMethods;
    }

    public BenchmarkEntity setMeasureMethods(List<MeasureMethodEntity> measureMethods) {
        this.measureMethods = measureMethods;
        return this;
    }

    public UserEntity getUser() {
        return user;
    }

    public BenchmarkEntity setUser(UserEntity user) {
        this.user = user;
        return this;
    }

    public int getWarmup() {
        return warmup;
    }

    public BenchmarkEntity setWarmup(int warmup) {
        this.warmup = warmup;
        return this;
    }

    public String getName() {
        return name;
    }

    public BenchmarkEntity setName(String name) {
        this.name = name;
        return this;
    }

    public boolean isSuccess() {
        return success;
    }

    public BenchmarkEntity setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        BenchmarkEntity that = (BenchmarkEntity) o;
        return warmup == that.warmup &&
                measurement == that.measurement &&
                Objects.equals(projectId, that.projectId) &&
                Objects.equals(name, that.name) &&
                Objects.equals(created, that.created) &&
                Objects.equals(content, that.content) &&
                Objects.equals(init, that.init) &&
                Objects.equals(declare, that.declare) &&
                Objects.equals(user, that.user) &&
                Objects.equals(success, that.success) &&
                Objects.equals(measureMethods, that.measureMethods);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), projectId, name, created, content, warmup, measurement, init, declare, user, measureMethods, success);
    }
}
