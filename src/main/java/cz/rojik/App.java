package cz.rojik;

import com.spotify.docker.client.exceptions.DockerCertificateException;
import com.spotify.docker.client.exceptions.DockerException;
import cz.rojik.constant.ProjectContants;
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
    private Runner runner;
    private ResultParser resultParser;
    private ErrorsParser errorsParser;
    private GeneratorHTML generatorHTML;

    public App() {
        reader = new Reader();
        generator = new Generator();
        runner = new Runner();
        resultParser = new ResultParser();
        errorsParser = new ErrorsParser();
        generatorHTML = new GeneratorHTML();

        LocalDateTime now = LocalDateTime.now();

        Template input = reader.readInputs();
        String className = generator.generateJavaClass(input);
        Set<String> errors = runner.compileProject();

        if (errors.size() == 0) {
            try {
                runner.runProject(className, input);
            } catch (DockerCertificateException | DockerException | InterruptedException e) {
                e.printStackTrace();
            }
            Result result = resultParser.parseResult(ProjectContants.RESULT_JSON_FILE, now);
            generatorHTML.generateHTMLFile(result, input);
        }
        else {
            ProcessInfo processInfo = new ProcessInfo(Operation.ERROR_COMPILE);

            List<Error> errorList = errorsParser.getSyntaxErrors(errors);
            List<ErrorInfo> errorInfoList = errorsParser.processErrorList(errorList, className);

            Result result = new Result(now, false)
                    .setErrors(errorInfoList);
            generatorHTML.generateHTMLFile(result, input);
        }

    }

    public static void main(String[] args) {
        new App();
        logger.info("koneeeeeeeeeeeeec");
    }
}
