package cz.rojik.backend.dto;

import cz.rojik.backend.entity.MeasureMethodEntity;

import java.util.Objects;

/**
 * @author Marek Rojik (marek@rojik.cz) on 05. 01. 2019
 */
public class MeasureMethodDTO extends BaseDTO {

    private int order;
    private String method;

    public MeasureMethodDTO(int order, String method) {
        this.order = order;
        this.method = method;
    }

    public MeasureMethodDTO(MeasureMethodEntity entity) {
        this.id = entity.getId();
        this.order = entity.getOrder();
        this.method = entity.getMethod();
    }

    @Override
    public String toString() {
        return "MeasureMethodDTO{" +
                "order=" + order +
                ", method='" + method + '\'' +
                ", id=" + id +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        MeasureMethodDTO that = (MeasureMethodDTO) o;
        return order == that.order &&
                Objects.equals(method, that.method);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), order, method);
    }

    public int getOrder() {

        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
