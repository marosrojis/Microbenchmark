package cz.rojik.exception;

import java.util.Map;
import java.util.List;

public class ImportsToChooseException extends Exception {

    private String projectId;
    private Map<String, List<String>> importsToChoose;

    public ImportsToChooseException(String projectId, Map<String, List<String>> importsToChoose) {
        super("Total " + importsToChoose.size() + " imports have to been chosen which library user want.");
        this.projectId = projectId;
        this.importsToChoose = importsToChoose;
    }

    public Map<String, List<String>> getImportsToChoose() {
        return importsToChoose;
    }

    public String getProjectId() {
        return projectId;
    }
}
