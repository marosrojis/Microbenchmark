package cz.rojik.backend.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.Objects;

@Entity
@Table(
        name = "mbmark_properties",
        uniqueConstraints = @UniqueConstraint(columnNames = {"key"}),
        indexes = @Index(columnList = "key"))
public class PropertiesEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "key", nullable = false)
    @Lob
    private String key;

    @Column(name = "value", nullable = false)
    @Lob
    private String value;

    public Long getId() {
        return id;
    }

    public PropertiesEntity setId(Long id) {
        this.id = id;
        return this;
    }

    public String getKey() {
        return key;
    }

    public PropertiesEntity setKey(String key) {
        this.key = key;
        return this;
    }

    public String getValue() {
        return value;
    }

    public PropertiesEntity setValue(String value) {
        this.value = value;
        return this;
    }

    @Override
    public String toString() {
        return "Properties{" +
                "id=" + id +
                ", key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PropertiesEntity that = (PropertiesEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(key, that.key) &&
                Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, key, value);
    }
}
