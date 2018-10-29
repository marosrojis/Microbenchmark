package cz.rojik.entity;

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
@Table(
        name = "result",
        uniqueConstraints = @UniqueConstraint(columnNames = {"project_id"}),
        indexes = @Index(columnList = "project_id"))
public class ResultEntity extends BaseEntity {

    @Column(name = "project_id", nullable = false)
    private String projectId;

    @Column(name = "created", nullable = false)
    private LocalDateTime created;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "warmup", nullable = false)
    private int warmup;

    @Column(name = "measurement", nullable = false)
    private int measurement;

    @Column(name = "init")
    private String init;

    @Column(name = "declare")
    private String declare;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY,mappedBy = "result")
    List<MeasureMethodEntity> measureMethods;

    @Override
    public String toString() {
        return "ResultEntity{" +
                "projectId='" + projectId + '\'' +
                ", created=" + created +
                ", content='" + content + '\'' +
                ", warmup=" + warmup +
                ", measurement=" + measurement +
                ", init='" + init + '\'' +
                ", declare='" + declare + '\'' +
                ", measureMethods=" + measureMethods +
                '}';
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getWarmup() {
        return warmup;
    }

    public void setWarmup(int warmup) {
        this.warmup = warmup;
    }

    public int getMeasurement() {
        return measurement;
    }

    public void setMeasurement(int measurement) {
        this.measurement = measurement;
    }

    public String getInit() {
        return init;
    }

    public void setInit(String init) {
        this.init = init;
    }

    public String getDeclare() {
        return declare;
    }

    public void setDeclare(String declare) {
        this.declare = declare;
    }

    public List<MeasureMethodEntity> getMeasureMethods() {
        return measureMethods;
    }

    public void setMeasureMethods(List<MeasureMethodEntity> measureMethods) {
        this.measureMethods = measureMethods;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ResultEntity that = (ResultEntity) o;
        return warmup == that.warmup &&
                measurement == that.measurement &&
                Objects.equals(projectId, that.projectId) &&
                Objects.equals(created, that.created) &&
                Objects.equals(content, that.content) &&
                Objects.equals(init, that.init) &&
                Objects.equals(declare, that.declare) &&
                Objects.equals(user, that.user) &&
                Objects.equals(measureMethods, that.measureMethods);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), projectId, created, content, warmup, measurement, init, declare, user, measureMethods);
    }
}
