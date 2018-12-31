package cz.rojik.service.dto;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.Set;

public class LibrariesDTO {

    @NotNull
    @NotEmpty
    String projectId;

    @NotNull
    @NotEmpty
    Set<String> libraries;

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public Set<String> getLibraries() {
        return libraries;
    }

    public void setLibraries(Set<String> libraries) {
        this.libraries = libraries;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LibrariesDTO that = (LibrariesDTO) o;
        return Objects.equals(projectId, that.projectId) &&
                Objects.equals(libraries, that.libraries);
    }

    @Override
    public int hashCode() {

        return Objects.hash(projectId, libraries);
    }

    @Override
    public String toString() {
        return "LibrariesDTO{" +
                "projectId='" + projectId + '\'' +
                ", libraries=" + libraries +
                '}';
    }
}
