package cz.rojik;

import cz.rojik.model.Template;

import java.util.ArrayList;
import java.util.List;

public class Reader {

    public Template readInputs() {
        Template template = new Template();

        template.setLibraries(readLibraries())
                .setWarmup(readWarmup())
                .setMeasurement(readMeasurement())
                .setDeclare(readDeclare())
                .setInit(readInit())
                .setTestMethods(readTestMethods());

        return template;
    }

    private int readWarmup() {
        return 2;
    }

    private int readMeasurement() {
        return 3;
    }

    private String readDeclare() {
        return "List<Integer> arrayList;\n" +
                "\tint[] array;\n" +
                "\tRandom random;";
    }

    private String readInit() {
        return "random = new Random();\n" +
                "\t\tarray = new int[1000];\n" +
                "\t\tarrayList = new ArrayList<>();\n" +
                "\t\tfor (int i = 0; i < 1000; i++) {\n" +
                "\t\t\tint randomNumber = random.nextInt();\n" +
                "\t\t\tarray[i] = randomNumber;\n" +
                "\t\t\tarrayList.add(new Integer(randomNumber));\n" +
                "\t\t}";
    }

    private List<String> readTestMethods() {
        List<String> output = new ArrayList<>();
        output.add(readTestMethod1());
        output.add(readTestMethod2());
        return output;
    }

    private String readTestMethod1() {
        return "Arrays.sort(array);";
    }

    private String readTestMethod2() {
        return "Collections.sort(arrayList);";
    }

    private String readLibraries() {
        return "import java.util.ArrayList;\n" +
                "import java.util.Arrays;\n" +
                "import java.util.Collections;\n" +
                "import java.util.List;\n" +
                "import java.util.Random;\n" +
                "import java.util.concurrent.TimeUnit;\n" +
                "\n" +
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
