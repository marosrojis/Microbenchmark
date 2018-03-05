package cz.rojik;

import cz.rojik.constant.ProjectContants;
import cz.rojik.exception.MavenCompileException;
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
import java.util.Set;
import java.util.regex.Pattern;

public class Runner {

    private static Logger logger = LoggerFactory.getLogger(Runner.class);
    private static final String REGEX_ERROR = "\\[ERROR\\].*";

    public Runner() {
    }

    public Set<String> compileProject() {
        Set<String> output = new LinkedHashSet<>();
        final Pattern p = Pattern.compile(REGEX_ERROR);

        InvocationRequest request = new DefaultInvocationRequest();
        request.setPomFile(new File(ProjectContants.PROJECT_POM));
        request.setGoals( Arrays.asList("clean", "install", "-Dmaven.test.skip=true"));

        Invoker invoker = new DefaultInvoker();
        invoker.setOutputHandler(text -> {
            if (p.matcher(text).matches()) {
                output.add(text);
            }
            logger.info(text);
        });
        try {
            invoker.execute(request);
        } catch (MavenInvocationException e) {
            logger.error("Compile project failure");
            throw new MavenCompileException();
        }

        return output;
    }

    public boolean runProject(String fileName) {
        Runtime rt = Runtime.getRuntime();
        String[] commands = {"java","-jar", ProjectContants.PATH_GENERATE_JAR + fileName + ".jar"};

        try {
            Process proc = rt.exec(commands);

            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(proc.getInputStream()));

            BufferedReader stdError = new BufferedReader(new
                    InputStreamReader(proc.getErrorStream()));

            System.out.println("Here is the standard output of the command:\n");
            String s = null;
            while ((s = stdInput.readLine()) != null) {
                System.out.println(s);
            }

            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

}
