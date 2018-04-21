package cz.rojik;

import com.spotify.docker.client.exceptions.DockerCertificateException;
import com.spotify.docker.client.exceptions.DockerException;
import cz.rojik.enums.Operation;
import cz.rojik.model.ErrorInfo;
import cz.rojik.model.ProcessInfo;
import cz.rojik.model.Result;
import cz.rojik.model.Error;
import cz.rojik.model.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class App {

    private static Logger logger = LoggerFactory.getLogger(App.class);

    private Reader reader;
    private Generator generator;
    private Importer importer;
    private Runner runner;
    private ResultParser resultParser;
    private ErrorsParser errorsParser;
    private GeneratorHTML generatorHTML;

    public App() {
        reader = new Reader();
        generator = new Generator();
        importer = new Importer();
        runner = new Runner();
        resultParser = new ResultParser();
        errorsParser = new ErrorsParser();
        generatorHTML = new GeneratorHTML();

        LocalDateTime now = LocalDateTime.now();

        Template input = reader.readInputs();
        input.setLibraries(getAllImports(input));
        String projectId = generator.generateJavaClass(input);
        Set<String> errors = runner.compileProject(projectId);

        if (errors.size() == 0) {
            try {
                runner.runProject(projectId, input);
            } catch (DockerCertificateException | DockerException | InterruptedException e) {
                logger.error("Throw exception during run docker container with project.");
            }
            Result result = resultParser.parseResult(projectId, now);
            generatorHTML.generateHTMLFile(result, projectId, input);
        }
        else {
            ProcessInfo processInfo = new ProcessInfo(Operation.ERROR_COMPILE);

            List<Error> errorList = errorsParser.getSyntaxErrors(errors);
            List<ErrorInfo> errorInfoList = errorsParser.processErrorList(errorList, projectId);

            Result result = new Result(now, false)
                    .setErrors(errorInfoList);
            generatorHTML.generateHTMLFile(result, projectId, input);
        }
    }

    private String getAllImports(Template template) {
        Set<String> imports = importer.getLibrariesToImport(template.getDeclare());
        imports.addAll(importer.getLibrariesToImport(template.getInit()));
        template.getTestMethods().forEach(method -> imports.addAll(importer.getLibrariesToImport(method)));

        StringBuilder sb = new StringBuilder();
        imports.forEach(value -> sb.append("import ")
                .append(value)
                .append(";\n"));

        String output = sb.toString();
        return output;
    }

    public static void main(String[] args) {
        new App();
    }
}
