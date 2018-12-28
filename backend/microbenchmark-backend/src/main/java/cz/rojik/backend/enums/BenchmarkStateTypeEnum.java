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
    BENCHMARK_ERROR(Values.BENCHMARK_ERROR),
    BENCHMARK_KILL(Values.BENCHMARK_KILL);

    String benchmarkState;

    private BenchmarkStateTypeEnum(String benchmarkState) {
        this.benchmarkState = benchmarkState;
    }

    public String getBenchmarkStateType() {
        return benchmarkState;
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
        states.add(BenchmarkStateTypeEnum.BENCHMARK_KILL);
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
        public static final String BENCHMARK_KILL = "BENCHMARK_KILL";
    }

}
