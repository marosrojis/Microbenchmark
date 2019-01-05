package cz.rojik.service.dto;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Marek Rojik (marek@rojik.cz) on 05. 01. 2019
 */
public class TemplateDTO {

    private String name;
    private String libraries;

    @NotNull
    @Min(1)
    private Integer warmup;

    @NotNull
    @Min(1)
    private Integer measurement;
    private String declare;
    private String init;

    @NotNull
    @NotEmpty
    private List<String> testMethods;

    public TemplateDTO() {
        this.name = "";
        this.libraries = "";
        this.declare = "";
        this.init = "";
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
