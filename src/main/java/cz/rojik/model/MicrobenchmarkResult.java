package cz.rojik.model;

public class MicrobenchmarkResult {

    private String name;
    private int warmupIterations;
    private int measurementIterations;
    private String unit;

    private double score;
    private double error;
    private boolean fastest;

    public MicrobenchmarkResult(String name, int warmupIterations, int measurementIterations, String unit, double score, double error) {
        this.name = name;
        this.warmupIterations = warmupIterations;
        this.measurementIterations = measurementIterations;
        this.unit = unit;
        this.score = score;
        this.error = error;
        this.fastest = false;
    }

    public String getName() {
        return name;
    }

    public MicrobenchmarkResult setName(String name) {
        this.name = name;
        return this;
    }

    public int getWarmupIterations() {
        return warmupIterations;
    }

    public MicrobenchmarkResult setWarmupIterations(int warmupIterations) {
        this.warmupIterations = warmupIterations;
        return this;
    }

    public int getMeasurementIterations() {
        return measurementIterations;
    }

    public MicrobenchmarkResult setMeasurementIterations(int measurementIterations) {
        this.measurementIterations = measurementIterations;
        return this;
    }

    public String getUnit() {
        return unit;
    }

    public MicrobenchmarkResult setUnit(String unit) {
        this.unit = unit;
        return this;
    }

    public double getScore() {
        return score;
    }

    public MicrobenchmarkResult setScore(double score) {
        this.score = score;
        return this;
    }

    public double getError() {
        return error;
    }

    public MicrobenchmarkResult setError(double error) {
        this.error = error;
        return this;
    }

    public boolean isFastest() {
        return fastest;
    }

    public void setFastest(boolean fastest) {
        this.fastest = fastest;
    }
}
