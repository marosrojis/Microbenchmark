package cz.rojik.backend.entity;

import org.hibernate.annotations.SQLDelete;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Objects;

/**
 * @author Marek Rojik (marek@rojik.cz) on 05. 01. 2019
 */
@Entity
@SQLDelete(sql = "UPDATE mbmark_measure_method SET archived = true WHERE id = ?")
@Table(name = "mbmark_measure_method")
public class MeasureMethodEntity extends BaseEntity {

    @Column(name = "order_position", nullable = false)
    private int order;

    @Column(name = "method", nullable = false, length = 4096)
    private String method;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "benchmark_id", nullable = false)
    private BenchmarkEntity benchmark;

    public MeasureMethodEntity() {}

    public MeasureMethodEntity(int order, String method, BenchmarkEntity benchmark) {
        this.order = order;
        this.method = method;
        this.benchmark = benchmark;
    }

    public int getOrder() {
        return order;
    }

    public MeasureMethodEntity setOrder(int order) {
        this.order = order;
        return this;
    }

    public String getMethod() {
        return method;
    }

    public MeasureMethodEntity setMethod(String method) {
        this.method = method;
        return this;
    }

    public BenchmarkEntity getBenchmark() {
        return benchmark;
    }

    public MeasureMethodEntity setBenchmark(BenchmarkEntity benchmark) {
        this.benchmark = benchmark;
        return this;
    }

    @Override
    public String toString() {
        return "MeasureMethodEntity{" +
                "order=" + order +
                ", method='" + method + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        MeasureMethodEntity that = (MeasureMethodEntity) o;
        return order == that.order &&
                Objects.equals(method, that.method) &&
                Objects.equals(benchmark, that.benchmark);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), order, method, benchmark);
    }
}
