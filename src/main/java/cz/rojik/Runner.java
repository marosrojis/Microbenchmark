package cz.rojik;

import cz.rojik.constant.ProjectContants;
import cz.rojik.enums.Operation;
import cz.rojik.exception.MavenCompileException;
import cz.rojik.model.ProcessInfo;
import cz.rojik.model.Template;
import org.apache.maven.shared.invoker.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class Runner {

    private static Logger logger = LoggerFactory.getLogger(Runner.class);
    private static final String REGEX_ERROR = "\\[ERROR\\].*";

    private ProcessParser processParser;

    public Runner() {
        this.processParser = new ProcessParser();
    }

    public Set<String> compileProject() {
        Set<String> output = new LinkedHashSet<>();
        ProcessInfo processInfo;
        final Pattern p = Pattern.compile(REGEX_ERROR);

        InvocationRequest request = new DefaultInvocationRequest();
        request.setPomFile(new File(ProjectContants.PROJECT_POM));
        request.setGoals( Arrays.asList("clean", "install", "-Dmaven.test.skip=true"));

        Invoker invoker = new DefaultInvoker();
        invoker.setOutputHandler(text -> {
            if (p.matcher(text).matches()) {
                output.add(text);
            }
        });
        try {
            processInfo = new ProcessInfo(Operation.START_COMPILE);
            invoker.execute(request);
        } catch (MavenInvocationException e) {
            processInfo = new ProcessInfo(Operation.ERROR_COMPILE);
            throw new MavenCompileException();
        }

        processInfo = new ProcessInfo(Operation.END_COMPILE);
        return output;
    }

    public boolean runProject(String fileName, Template template) {
        Runtime rt = Runtime.getRuntime();
        String[] commands = {"java","-jar", ProjectContants.PATH_GENERATE_JAR + fileName + ".jar"};
        ProcessInfo processInfo;

        try {
            Process proc = rt.exec(commands);

            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(proc.getInputStream()));

            String s = null;
            while ((s = stdInput.readLine()) != null) {
                processInfo = processParser.parseMessage(s, template);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        processInfo = new ProcessInfo(Operation.FINISH_BENCHMARKS);
        return true;
    }

}
