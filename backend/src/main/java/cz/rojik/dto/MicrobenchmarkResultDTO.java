package cz.rojik.dto;

public class MicrobenchmarkResultDTO {

    private String name;
    private int warmupIterations;
    private int measurementIterations;
    private String unit;

    private double score;
    private double error;

    public MicrobenchmarkResultDTO(String name, int warmupIterations, int measurementIterations, String unit, double score, double error) {
        this.name = name;
        this.warmupIterations = warmupIterations;
        this.measurementIterations = measurementIterations;
        this.unit = unit;
        this.score = score;
        this.error = error;
    }

    public String getName() {
        return name;
    }

    public MicrobenchmarkResultDTO setName(String name) {
        this.name = name;
        return this;
    }

    public int getWarmupIterations() {
        return warmupIterations;
    }

    public MicrobenchmarkResultDTO setWarmupIterations(int warmupIterations) {
        this.warmupIterations = warmupIterations;
        return this;
    }

    public int getMeasurementIterations() {
        return measurementIterations;
    }

    public MicrobenchmarkResultDTO setMeasurementIterations(int measurementIterations) {
        this.measurementIterations = measurementIterations;
        return this;
    }

    public String getUnit() {
        return unit;
    }

    public MicrobenchmarkResultDTO setUnit(String unit) {
        this.unit = unit;
        return this;
    }

    public double getScore() {
        return score;
    }

    public MicrobenchmarkResultDTO setScore(double score) {
        this.score = score;
        return this;
    }

    public double getError() {
        return error;
    }

    public MicrobenchmarkResultDTO setError(double error) {
        this.error = error;
        return this;
    }

    @Override
    public String toString() {
        return "MicrobenchmarkResultDTO{" +
                "name='" + name + '\'' +
                ", warmupIterations=" + warmupIterations +
                ", measurementIterations=" + measurementIterations +
                ", unit='" + unit + '\'' +
                ", score=" + score +
                ", error=" + error +
                '}';
    }
}
