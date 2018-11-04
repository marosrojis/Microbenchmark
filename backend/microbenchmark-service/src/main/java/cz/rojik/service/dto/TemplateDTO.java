package cz.rojik.service.dto;

import java.util.ArrayList;
import java.util.List;

public class TemplateDTO {

    private String name;
    private String libraries;
    private int warmup;
    private int measurement;
    private String declare;
    private String init;
    private List<String> testMethods;

    public TemplateDTO() {
        this.name = "";
        this.libraries = "";
        this.warmup = 1;
        this.measurement = 1;
        this.declare = "";
        this.init = "";
        this.testMethods = new ArrayList<>();
    }

    public int getWarmup() {
        return warmup;
    }

    public TemplateDTO setWarmup(int warmup) {
        this.warmup = warmup;
        return this;
    }

    public int getMeasurement() {
        return measurement;
    }

    public TemplateDTO setMeasurement(int measurement) {
        this.measurement = measurement;
        return this;
    }

    public String getDeclare() {
        return declare;
    }

    public TemplateDTO setDeclare(String declare) {
        this.declare = declare;
        return this;
    }

    public String getInit() {
        return init;
    }

    public TemplateDTO setInit(String init) {
        this.init = init;
        return this;
    }

    public List<String> getTestMethods() {
        return testMethods;
    }

    public TemplateDTO setTestMethods(List<String> testMethods) {
        this.testMethods = testMethods;
        return this;
    }

    public String getLibraries() {
        return libraries;
    }

    public TemplateDTO setLibraries(String libraries) {
        this.libraries = libraries;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
