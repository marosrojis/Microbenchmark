package cz.rojik;

import cz.rojik.constant.ProjectContants;
import cz.rojik.model.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

public class App {

    private static Logger logger = LoggerFactory.getLogger(App.class);
    private Reader reader;
    private Generator generator;
    private Runner runner;
    private Parser parser;
    private GeneratorHTML generatorHTML;

    public App() {
        reader = new Reader();
        generator = new Generator();
        runner = new Runner();
        parser = new Parser();
        generatorHTML = new GeneratorHTML();

        LocalDateTime now = LocalDateTime.now();

        Template input = reader.readInputs();
        String className = generator.generateJavaClass(input);
        runner.compileProject();
        runner.runProject(className);

        Result result = parser.parseResult(ProjectContants.RESULT_JSON_FILE, now);
        generatorHTML.generateHTMLFile(result, input);
    }

    public static void main(String[] args) {
        new App();
    }
}
