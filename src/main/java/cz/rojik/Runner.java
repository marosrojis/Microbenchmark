package cz.rojik;

import cz.rojik.constant.ProjectContants;
import cz.rojik.exception.MavenCompileException;
import org.apache.maven.shared.invoker.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

public class Runner {

    private static Logger logger = LoggerFactory.getLogger(Runner.class);

    public Runner() {
    }

    public Set<String> compileProject() {
        Set<String> output = new LinkedHashSet<>();
        InvocationRequest request = new DefaultInvocationRequest();
        request.setPomFile(new File(ProjectContants.PROJECT_POM));
        request.setGoals( Arrays.asList("clean", "install -Dmaven.test.skip=true"));

        Invoker invoker = new DefaultInvoker();
        invoker.setOutputHandler(text -> {
            logger.info(text);
            output.add(text);
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
        Process proc = null;
        try {
            proc = Runtime.getRuntime().exec("java -jar " + ProjectContants.PATH_GENERATE_JAR + fileName + ".jar");
            proc.waitFor();

            InputStream in = proc.getInputStream();
            InputStream err = proc.getErrorStream();

            byte b[]=new byte[in.available()];
            in.read(b,0,b.length);
            System.out.println(new String(b));

            byte c[]=new byte[err.available()];
            err.read(c,0,c.length);
            System.err.println(new String(c));
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }


        return true;
    }
}
