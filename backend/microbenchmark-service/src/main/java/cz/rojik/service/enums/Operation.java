package cz.rojik.service.enums;

/**
 * @author Marek Rojik (marek@rojik.cz) on 05. 01. 2019
 */
public enum Operation {

    MEASUREMENT("MEASUREMENT"),

    WARMUP("WARMUP"),

    RESULT("RESULT"),

    ERROR_BENCHMARK("ERROR_BENCHMARK"),

    SUCCESS_BENCHMARK("SUCCESS_BENCHMARK");

    private String type;

    private Operation(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

}
