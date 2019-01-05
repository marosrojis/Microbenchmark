package cz.rojik.backend.dto;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * @author Marek Rojik (marek@rojik.cz) on 05. 01. 2019
 */
public class PropertyDTO {

    private Long id;

    @NotNull
    @NotBlank
    private String key;

    @NotNull
    @NotBlank
    private String value;

    public Long getId() {
        return id;
    }

    public PropertyDTO setId(Long id) {
        this.id = id;
        return this;
    }

    public String getKey() {
        return key;
    }

    public PropertyDTO setKey(String key) {
        this.key = key;
        return this;
    }

    public String getValue() {
        return value;
    }

    public PropertyDTO setValue(String value) {
        this.value = value;
        return this;
    }

    @Override
    public String toString() {
        return "PropertiesDTO{" +
                "id=" + id +
                ", key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PropertyDTO that = (PropertyDTO) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(key, that.key) &&
                Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, key, value);
    }
}
