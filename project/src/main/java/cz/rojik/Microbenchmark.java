package cz.rojik;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.profile.GCProfiler;
import org.openjdk.jmh.profile.StackProfiler;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@State(Scope.Thread)
@BenchmarkMode({Mode.AverageTime})
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Warmup(iterations = 2)
@Measurement(iterations = 3)
public class Microbenchmark {

	List<Integer> arrayList;
	int[] array;
	Random random;

	@Setup(Level.Trial)
	public void init() {
		random = new Random();
		array = new int[1000];
		arrayList = new ArrayList<>();
		for (int i = 0; i < 1000; i++) {
			int randomNumber = random.nextInt();
			array[i] = randomNumber;
			arrayList.add(new Integer(randomNumber));
		}
	}

	@Benchmark
	public void benchmarkTest1() {
		Arrays.sort(array);
	}
	@Benchmark
	public void benchmarkTest2() {
		Collections.sort(arrayList);
	}


	public static void main(String[] args) throws RunnerException {

		Options options = new OptionsBuilder()
				.include(Microbenchmark.class.getSimpleName()).threads(1)
				.forks(1).shouldFailOnError(true).shouldDoGC(true)
				.result("result/results.json")
				.resultFormat(ResultFormatType.JSON)
				.jvmArgs("-server").build();
		new Runner(options).run();

	}
}