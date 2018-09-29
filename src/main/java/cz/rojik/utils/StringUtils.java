package cz.rojik.utils;

public class StringUtils {

    public static String insertBracket(String string) {
        return "\\[" + string + "\\]";
    }

    public static String getJMHLibraries() {
        return "import java.util.concurrent.TimeUnit;\n" +
                "import org.openjdk.jmh.annotations.*;\n" +
                "import org.openjdk.jmh.profile.GCProfiler;\n" +
                "import org.openjdk.jmh.profile.StackProfiler;\n" +
                "import org.openjdk.jmh.results.format.ResultFormatType;\n" +
                "import org.openjdk.jmh.runner.Runner;\n" +
                "import org.openjdk.jmh.runner.RunnerException;\n" +
                "import org.openjdk.jmh.runner.options.Options;\n" +
                "import org.openjdk.jmh.runner.options.OptionsBuilder;";
    }
}
