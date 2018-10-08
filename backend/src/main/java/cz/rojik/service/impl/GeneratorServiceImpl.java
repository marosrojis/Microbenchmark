package cz.rojik.service.impl;

import cz.rojik.constants.ProjectContants;
import cz.rojik.constants.TemplateConstants;
import cz.rojik.dto.LibrariesDTO;
import cz.rojik.utils.pojo.ImportsResult;
import cz.rojik.exception.ImportsToChooseException;
import cz.rojik.exception.ReadFileException;
import cz.rojik.dto.TemplateDTO;
import cz.rojik.service.GeneratorService;
import cz.rojik.service.ImporterService;
import cz.rojik.utils.StringUtils;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class GeneratorServiceImpl implements GeneratorService {

    private static Logger logger = LoggerFactory.getLogger(GeneratorServiceImpl.class);

    @Autowired
    private ImporterService importerService;

    @Override
    public String generateJavaClass(TemplateDTO template) throws ImportsToChooseException {
        String projectID = copyProjectFolder();

        ImportsResult imports = getAllImports(template);
        template.setLibraries(generateImports(imports.getLibraries()));
        cz.rojik.utils.FileUtils.saveTemplateToJson(template, projectID);

        if (imports.getLibrariesToChoose().size() != 0) {
            logger.info("Code contains libraries that can not be imported automatically.");
            throw new ImportsToChooseException(projectID, imports.getLibrariesToChoose());
        }

        String fileContent = readDefaultFile();
        String newContent = generateContent(template, fileContent);
        saveFile(projectID, newContent);

        return projectID;
    }

    @Override
    public String importLibraries(LibrariesDTO libraries) {
        String projectId = libraries.getProjectId();

        TemplateDTO template = cz.rojik.utils.FileUtils.getTemplateFromJson(projectId);
        String generatedImports = generateImports(libraries.getLibraries());

        StringBuilder sb = new StringBuilder(template.getLibraries());
        sb.append(generatedImports);
        template.setLibraries(sb.toString());

        cz.rojik.utils.FileUtils.saveTemplateToJson(template, projectId);
        String fileContent = readDefaultFile();
        String newContent = generateContent(template, fileContent);
        saveFile(projectId, newContent);

        return projectId;
    }

    // PRIVATE

    private String generateContent(TemplateDTO template, String content) {
        content = replaceTemplateMark(content, TemplateConstants.LIBRARIES, template.getLibraries());
        content = replaceTemplateMark(content, TemplateConstants.WARMUP, template.getWarmup() + "");
        content = replaceTemplateMark(content, TemplateConstants.MEASUREMENT, template.getMeasurement() + "");
        content = replaceTemplateMark(content, TemplateConstants.CLASS_NAME, ProjectContants.JAVA_CLASS);
        content = replaceTemplateMark(content, TemplateConstants.DECLARE, template.getDeclare());
        content = replaceTemplateMark(content, TemplateConstants.INIT, template.getInit());
        content = replaceTemplateMark(content, TemplateConstants.RESULT_JSON_FILE, ProjectContants.RESULT_JSON_FILE);
        content = replaceTestMethods(content, template.getTestMethods());

        return content;
    }

    private String replaceTemplateMark(String content, String templateMark, String text) {
        content = content.replaceAll(StringUtils.insertBracket(templateMark), text);
        return content;
    }

    private String replaceTestMethods(String content, List<String> testMethods) {
        StringBuilder sb = new StringBuilder();
        int i = 1;
        for (String method : testMethods) {
            sb.append("\t")
                .append(TemplateConstants.BENCHMARK_ANOTATE)
                .append("\n\t")
                .append(TemplateConstants.DECLARE_METHOD)
                .append(i)
                .append("() {\n\t\t")
                .append(method)
                .append("\n\t}\n");
            i++;
        }

        content = replaceTemplateMark(content, TemplateConstants.TEST_METHODS, sb.toString());
        return content;
    }

    private ImportsResult getAllImports(TemplateDTO template) {
        ImportsResult imports = new ImportsResult();

        imports = importerService.getLibrariesToImport(imports, template.getDeclare());
        imports = importerService.getLibrariesToImport(imports, template.getInit());

        for (String method : template.getTestMethods()) {
            imports = importerService.getLibrariesToImport(imports, method);
        }

        return imports;
    }

    private String readDefaultFile() {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(ProjectContants.DEFAULT_JAVA_FILE).getFile());
        String fileContent = "";
        try {
            fileContent = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ReadFileException(ProjectContants.DEFAULT_JAVA_FILE);
        }

        return fileContent;
    }

    private boolean saveFile(String projectID, String content) {
        try {
            File file = new File(ProjectContants.PATH_ALL_PROJECTS + projectID + "/" + ProjectContants.PATH_JAVA_PACKAGE + ProjectContants.JAVA_CLASS_FILE);
            FileUtils.writeStringToFile(file, content, StandardCharsets.UTF_8, false);
        } catch (IOException e) {
            logger.error("Save file failure");
            return false;
        }
        return true;
    }

    private String copyProjectFolder() {
        String generatedID = UUID.randomUUID().toString();
        logger.info("Generated unique project ID = {}", generatedID);

        ClassLoader classLoader = getClass().getClassLoader();
        File srcDir = new File(classLoader.getResource(ProjectContants.PATH_DEFAULT_PROJECT).getFile());
        File destDir = new File(ProjectContants.PATH_ALL_PROJECTS + generatedID);

        try {
            logger.info("Copy default project folder to new folder {}", generatedID);
            FileUtils.copyDirectory(srcDir, destDir);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return generatedID;
    }

    private String generateImports(Set<String> libraries) {
        StringBuilder sb = new StringBuilder();
        libraries.forEach(value -> sb.append("import ")
                .append(value.replaceAll("\\s+",""))
                .append(";\n"));

        String output = sb.toString();

        return output;
    }
}
