package cz.rojik.service.utils.pojo;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ImportsResult {

    private Set<String> libraries;
    private Map<String, List<String>> librariesToChoose;

    public ImportsResult() {
        libraries = new HashSet<>();
        librariesToChoose = new HashMap<>();
    }

    public Set<String> getLibraries() {
        return libraries;
    }

    public ImportsResult setLibraries(Set<String> libraries) {
        this.libraries = libraries;
        return this;
    }

    public Map<String, List<String>> getLibrariesToChoose() {
        return librariesToChoose;
    }

    public ImportsResult setLibrariesToChoose(Map<String, List<String>> librariesToChoose) {
        this.librariesToChoose = librariesToChoose;
        return this;
    }
}
