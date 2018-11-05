package cz.rojik.backend.entity;

import java.io.Serializable;

public enum BenchmarkStateType implements Serializable {

    IMPORT(Values.IMPORT),
    COMPILE(Values.COMPILE),
    RUN(Values.RUN),
    SUCCESS(Values.SUCCESS),
    ERROR(Values.ERROR);

    String benchmarkState;

    private BenchmarkStateType(String benchmarkState) {
        this.benchmarkState = benchmarkState;
    }

    public String getBenchmarkStateType() {
        return benchmarkState;
    }

    public static String getRoleById(int id) {
        if (id == 1) return BenchmarkStateType.IMPORT.getBenchmarkStateType();
        if (id == 2) return BenchmarkStateType.COMPILE.getBenchmarkStateType();
        if (id == 3) return BenchmarkStateType.RUN.getBenchmarkStateType();
        if (id == 4) return BenchmarkStateType.SUCCESS.getBenchmarkStateType();
        if (id == 5) return BenchmarkStateType.ERROR.getBenchmarkStateType();
        return null;
    }

    @Override
    public String toString() {
        return "BenchmarkStateType{" +
                "benchmarkState='" + benchmarkState + '\'' +
                '}';
    }

    public static class Values {
        public static final String IMPORT = "IMPORT";
        public static final String COMPILE = "COMPILE";
        public static final String RUN = "RUN";
        public static final String SUCCESS = "SUCCESS";
        public static final String ERROR = "ERROR";
    }

}
