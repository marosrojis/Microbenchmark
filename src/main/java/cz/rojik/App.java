package cz.rojik;

import com.spotify.docker.client.exceptions.DockerCertificateException;
import com.spotify.docker.client.exceptions.DockerException;
import cz.rojik.enums.Operation;
import cz.rojik.dto.ErrorInfoDTO;
import cz.rojik.dto.ProcessInfoDTO;
import cz.rojik.dto.ResultDTO;
import cz.rojik.dto.ErrorDTO;
import cz.rojik.dto.TemplateDTO;
import cz.rojik.service.impl.ErrorsParserServiceImpl;
import cz.rojik.service.impl.GeneratorServiceImpl;
import cz.rojik.service.impl.ImporterServiceImpl;
import cz.rojik.service.impl.ResultParserServiceImpl;
import cz.rojik.service.impl.RunnerServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class App {

    private static Logger logger = LoggerFactory.getLogger(App.class);

    private Reader reader;
    private GeneratorServiceImpl generator;
    private ImporterServiceImpl importer;
    private RunnerServiceImpl runner;
    private ResultParserServiceImpl resultParser;
    private ErrorsParserServiceImpl errorsParser;
    private GeneratorHTML generatorHTML;

    public App() {
        reader = new Reader();
        generator = new GeneratorServiceImpl();
        importer = new ImporterServiceImpl();
        runner = new RunnerServiceImpl();
        resultParser = new ResultParserServiceImpl();
        errorsParser = new ErrorsParserServiceImpl();
        generatorHTML = new GeneratorHTML();

        LocalDateTime now = LocalDateTime.now();

        TemplateDTO input = reader.readInputs();
        input.setLibraries(getAllImports(input));
        String projectId = generator.generateJavaClass(input);
        Set<String> errors = runner.compileProject(projectId);

        if (errors.size() == 0) {
            try {
                runner.runProject(projectId, input);
            } catch (DockerCertificateException | DockerException | InterruptedException e) {
                logger.error("Throw exception during run docker container with project.");
            }
            ResultDTO result = resultParser.parseResult(projectId, now);
            generatorHTML.generateHTMLFile(result, projectId, input);
        }
        else {
            ProcessInfoDTO processInfo = new ProcessInfoDTO(Operation.ERROR_COMPILE);

            List<ErrorDTO> errorList = errorsParser.getSyntaxErrors(errors);
            List<ErrorInfoDTO> errorInfoList = errorsParser.processErrorList(errorList, projectId);

            ResultDTO result = new ResultDTO(now, false)
                    .setErrors(errorInfoList);
            generatorHTML.generateHTMLFile(result, projectId, input);
        }
    }

    private String getAllImports(TemplateDTO template) {
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

//    public static void main(String[] args) {
//        new App();
//    }
}
