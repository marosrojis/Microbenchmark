package cz.rojik;

import cz.rojik.dto.TemplateDTO;

import java.util.ArrayList;
import java.util.List;

public class Reader {

    public TemplateDTO readInputs() {
        TemplateDTO template = new TemplateDTO();

        template.setWarmup(readWarmup())
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
                "\tRandom random;\n" +
                "\tList<String> temp;";
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

}
