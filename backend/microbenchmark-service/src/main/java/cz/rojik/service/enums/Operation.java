package cz.rojik.service.enums;

/**
 * @author Marek Rojik (marek@rojik.cz) on 05. 01. 2019
 */
public enum Operation {

    MEASUREMENT("MEASUREMENT"),

    WARMUP("WARMUP"),

    RESULT("RESULT"),

    START_COMPILE("START_COMPILE"),

    ERROR_COMPILE("ERROR_COMPILE"),

    END_COMPILE("END_COMPILE"),

    START_BENCHMARK("START_BENCHMARK"),

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
