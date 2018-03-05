package cz.rojik;

import cz.rojik.constant.ProjectContants;
import cz.rojik.model.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Set;

public class App {

    private static Logger logger = LoggerFactory.getLogger(App.class);

    private Reader reader;
    private Generator generator;
    private Runner runner;
    private Parser parser;
    private ErrorsParser errorsParser;
    private GeneratorHTML generatorHTML;

    public App() {
        reader = new Reader();
        generator = new Generator();
        runner = new Runner();
        parser = new Parser();
        errorsParser = new ErrorsParser();
        generatorHTML = new GeneratorHTML();

        LocalDateTime now = LocalDateTime.now();

        Template input = reader.readInputs();
        String className = generator.generateJavaClass(input);
        Set<String> errors = runner.compileProject();

        if (errors.size() == 0) {
            runner.runProject(className);
            Result result = parser.parseResult(ProjectContants.RESULT_JSON_FILE, now);
            generatorHTML.generateHTMLFile(result, input);
        }
        else {
            errorsParser.getSyntaxErrors(errors);
        }

    }

    public static void main(String[] args) {
        new App();
    }
}
