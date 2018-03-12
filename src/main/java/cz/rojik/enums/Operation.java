package cz.rojik.enums;

public enum Operation {

    MEASUREMENT("MEASUREMENT"),

    WARMUP("WARMUP"),

    RESULT("RESULT"),

    START_COMPILE("START_COMPILE"),

    ERROR_COMPILE("ERROR_COMPILE"),

    END_COMPILE("END_COMPILE"),

    START_BENCHMARK("START_BENCHMARK"),

    FINISH_BENCHMARKS("FINISH_BENCHMARKS");

    private String type;

    private Operation(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

}
