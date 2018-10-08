package cz.rojik.dto;

import java.util.List;
import java.util.Map;

public class LibrariesToChooseDTO {

    private String projectId;
    private Map<String, List<String>> imports;

    public LibrariesToChooseDTO(String projectId, Map<String, List<String>> imports) {
        this.projectId = projectId;
        this.imports = imports;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public Map<String, List<String>> getImports() {
        return imports;
    }

    public void setImports(Map<String, List<String>> imports) {
        this.imports = imports;
    }
}
