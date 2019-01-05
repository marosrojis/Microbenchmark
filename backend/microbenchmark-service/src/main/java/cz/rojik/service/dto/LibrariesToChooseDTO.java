package cz.rojik.service.dto;

import java.util.List;
import java.util.Map;

/**
 * @author Marek Rojik (marek@rojik.cz) on 05. 01. 2019
 */
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
