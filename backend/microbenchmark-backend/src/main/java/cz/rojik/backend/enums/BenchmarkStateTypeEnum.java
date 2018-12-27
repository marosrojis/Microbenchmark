package cz.rojik.backend.enums;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public enum BenchmarkStateTypeEnum implements Serializable {

    COMPILE_START(Values.COMPILE_START),
    COMPILE_ERROR(Values.COMPILE_ERROR),
    BENCHMARK_START(Values.BENCHMARK_START),
    BENCHMARK_RUNNING(Values.BENCHMARK_RUNNING),
    BENCHMARK_SUCCESS(Values.BENCHMARK_SUCCESS),
    BENCHMARK_ERROR(Values.BENCHMARK_ERROR);

    String benchmarkState;

    private BenchmarkStateTypeEnum(String benchmarkState) {
        this.benchmarkState = benchmarkState;
    }

    public String getBenchmarkStateType() {
        return benchmarkState;
    }

    public static String getStateById(int id) {
        if (id == 1) return BenchmarkStateTypeEnum.COMPILE_START.getBenchmarkStateType();
        if (id == 2) return BenchmarkStateTypeEnum.COMPILE_ERROR.getBenchmarkStateType();
        if (id == 3) return BenchmarkStateTypeEnum.BENCHMARK_START.getBenchmarkStateType();
        if (id == 4) return BenchmarkStateTypeEnum.BENCHMARK_RUNNING.getBenchmarkStateType();
        if (id == 5) return BenchmarkStateTypeEnum.BENCHMARK_SUCCESS.getBenchmarkStateType();
        if (id == 6) return BenchmarkStateTypeEnum.BENCHMARK_ERROR.getBenchmarkStateType();
        return null;
    }

    public static List<BenchmarkStateTypeEnum> runningStates() {
        List<BenchmarkStateTypeEnum> states = new ArrayList<>();
        states.add(BenchmarkStateTypeEnum.COMPILE_START);
        states.add(BenchmarkStateTypeEnum.BENCHMARK_START);
        states.add(BenchmarkStateTypeEnum.BENCHMARK_RUNNING);
        return states;
    }

    public static List<BenchmarkStateTypeEnum> runningBenchmarks() {
        List<BenchmarkStateTypeEnum> states = new ArrayList<>();
        states.add(BenchmarkStateTypeEnum.BENCHMARK_START);
        states.add(BenchmarkStateTypeEnum.BENCHMARK_RUNNING);
        return states;
    }

    public static List<BenchmarkStateTypeEnum> stopStates() {
        List<BenchmarkStateTypeEnum> states = new ArrayList<>();
        states.add(BenchmarkStateTypeEnum.COMPILE_ERROR);
        states.add(BenchmarkStateTypeEnum.BENCHMARK_SUCCESS);
        states.add(BenchmarkStateTypeEnum.BENCHMARK_ERROR);
        return states;
    }

    @Override
    public String toString() {
        return "BenchmarkStateType{" +
                "benchmarkState='" + benchmarkState + '\'' +
                '}';
    }

    public static class Values {
        public static final String COMPILE_START = "COMPILE_START";
        public static final String COMPILE_ERROR = "COMPILE_ERROR";
        public static final String BENCHMARK_START = "BENCHMARK_START";
        public static final String BENCHMARK_RUNNING = "BENCHMARK_RUNNING";
        public static final String BENCHMARK_SUCCESS = "BENCHMARK_SUCCESS";
        public static final String BENCHMARK_ERROR = "BENCHMARK_ERROR";
    }

}
