package cz.rojik.model;

import java.util.ArrayList;
import java.util.List;

public class Template {

    private String libraries;
    private String JMHLibraries;
    private int warmup;
    private int measurement;
    private String declare;
    private String init;
    private List<String> testMethods;

    public Template() {
        this.libraries = "";
        this.JMHLibraries = "";
        this.warmup = 1;
        this.measurement = 1;
        this.declare = "";
        this.init = "";
        this.testMethods = new ArrayList<>();
    }

    public int getWarmup() {
        return warmup;
    }

    public Template setWarmup(int warmup) {
        this.warmup = warmup;
        return this;
    }

    public int getMeasurement() {
        return measurement;
    }

    public Template setMeasurement(int measurement) {
        this.measurement = measurement;
        return this;
    }

    public String getDeclare() {
        return declare;
    }

    public Template setDeclare(String declare) {
        this.declare = declare;
        return this;
    }

    public String getInit() {
        return init;
    }

    public Template setInit(String init) {
        this.init = init;
        return this;
    }

    public List<String> getTestMethods() {
        return testMethods;
    }

    public Template setTestMethods(List<String> testMethods) {
        this.testMethods = testMethods;
        return this;
    }

    public String getLibraries() {
        return libraries;
    }

    public Template setLibraries(String libraries) {
        this.libraries = libraries;
        return this;
    }

    public String getJMHLibraries() {
        return JMHLibraries;
    }

    public Template setJMHLibraries(String JMHLibraries) {
        this.JMHLibraries = JMHLibraries;
        return this;
    }
}
