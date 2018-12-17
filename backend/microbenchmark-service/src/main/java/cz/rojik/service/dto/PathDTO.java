package cz.rojik.service.dto;

import java.io.Serializable;
import java.util.Objects;

public class PathDTO implements Serializable {

    private String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "PathDTO{" +
                "path='" + path + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PathDTO pathDTO = (PathDTO) o;
        return Objects.equals(path, pathDTO.path);
    }

    @Override
    public int hashCode() {

        return Objects.hash(path);
    }
}
