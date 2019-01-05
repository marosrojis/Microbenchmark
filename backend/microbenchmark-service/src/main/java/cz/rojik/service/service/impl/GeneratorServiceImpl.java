package cz.rojik.service.service.impl;

import cz.rojik.backend.dto.PropertiesDTO;
import cz.rojik.backend.exception.EntityNotFoundException;
import cz.rojik.backend.service.PropertiesService;
import cz.rojik.service.constants.OtherConstants;
import cz.rojik.service.constants.ProjectContants;
import cz.rojik.service.constants.TemplateConstants;
import cz.rojik.service.dto.LibrariesDTO;
import cz.rojik.service.dto.TemplateDTO;
import cz.rojik.service.exception.ImportsToChooseException;
import cz.rojik.service.exception.ReadFileException;
import cz.rojik.service.exception.WriteFileException;
import cz.rojik.backend.properties.PathProperties;
import cz.rojik.service.service.GeneratorService;
import cz.rojik.service.utils.StringUtils;
import cz.rojik.service.utils.pojo.ImportsResult;
import cz.rojik.service.service.ImporterService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
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

    @Autowired
    private PathProperties pathProperties;

    @Autowired
    private PropertiesService propertiesService;

    @Override
    public String generateJavaClass(TemplateDTO template) throws ImportsToChooseException {
        logger.trace("Generate java class with template {}", template);
        String projectID = copyProjectFolder();

        ImportsResult imports = getAllImports(template);
        template.setLibraries(generateImports(imports.getLibraries()));
        cz.rojik.service.utils.FileUtils.saveTemplateToJson(template, projectID);

        if (imports.getLibrariesToChoose().size() != 0) {
            logger.debug("Code contains libraries that can not be imported automatically." + projectID + "\n" + imports.getLibrariesToChoose());
            throw new ImportsToChooseException(projectID, imports.getLibrariesToChoose());
        }

        String fileContent = cz.rojik.service.utils.FileUtils.readFileFromResource(ProjectContants.DEFAULT_JAVA_FILE);
        String newContent = generateContent(template, fileContent);
        saveFile(projectID, newContent);

        logger.trace("Generate java class was successful for project {}", projectID);

        return projectID;
    }

    @Override
    public String importLibraries(LibrariesDTO libraries) {
        logger.trace("Import libraries {} to project {}", libraries.getLibraries(), libraries.getProjectId());
        String projectId = libraries.getProjectId();

        TemplateDTO template = cz.rojik.service.utils.FileUtils.getTemplateFromJson(projectId);
        String generatedImports = generateImports(libraries.getLibraries());

        StringBuilder sb = new StringBuilder(template.getLibraries());
        sb.append(generatedImports);
        String librariesFinal = sb.toString();
        logger.trace("Generated libraries for project {} are {}", projectId, librariesFinal);
        template.setLibraries(librariesFinal);

        cz.rojik.service.utils.FileUtils.saveTemplateToJson(template, projectId);
        String fileContent = cz.rojik.service.utils.FileUtils.readFileFromResource(ProjectContants.DEFAULT_JAVA_FILE);
        String newContent = generateContent(template, fileContent);
        saveFile(projectId, newContent);

        return projectId;
    }

    // PRIVATE

    private String generateContent(TemplateDTO template, String content) {
        logger.trace("Generate content of file from template {}", template);
        content = replaceTemplateMark(content, TemplateConstants.LIBRARIES, template.getLibraries());
        content = replaceTemplateMark(content, TemplateConstants.WARMUP, template.getWarmup() + "");
        content = replaceTemplateMark(content, TemplateConstants.MEASUREMENT, template.getMeasurement() + "");
        content = replaceTemplateMark(content, TemplateConstants.CLASS_NAME, ProjectContants.JAVA_CLASS);
        content = replaceTemplateMark(content, TemplateConstants.DECLARE, template.getDeclare());
        content = replaceTemplateMark(content, TemplateConstants.INIT, template.getInit());
        content = replaceTemplateMark(content, TemplateConstants.RESULT_JSON_FILE, ProjectContants.RESULT_JSON_FILE);
        content = replaceTestMethods(content, template.getTestMethods());

        logger.trace("Generated file {}", content);
        return content;
    }

    private ImportsResult getAllImports(TemplateDTO template) {
        logger.trace("Find all libraries to import {}", template);
        ImportsResult imports = new ImportsResult();

        imports = importerService.getLibrariesToImport(imports, template.getDeclare());
        logger.trace("Libraries for import (declare section) {}", template);
        imports = importerService.getLibrariesToImport(imports, template.getInit());
        logger.trace("Libraries for import (init section) {}", template);

        for (String method : template.getTestMethods()) {
            imports = importerService.getLibrariesToImport(imports, method);
        }
        logger.trace("Libraries for import (test methods section) {}", template);

        return imports;
    }

    private boolean saveFile(String projectID, String content) {
        logger.trace("Save file with content {} for project {}", content, projectID);
        try {
            File file = new File(pathProperties.getProjects() + projectID + File.separatorChar + ProjectContants.PATH_JAVA_PACKAGE + ProjectContants.JAVA_CLASS_FILE);
            FileUtils.writeStringToFile(file, content, StandardCharsets.UTF_8, false);
        } catch (IOException e) {
            logger.error("Save file failure");
            return false;
        }
        logger.trace("Saving file {} was successful", projectID);
        return true;
    }

    private String copyProjectFolder() {
        File projectsFolder = new File(pathProperties.getProjects());
        if (!projectsFolder.exists()) {
            projectsFolder.mkdirs();
        }

        String generatedID = UUID.randomUUID().toString();
        logger.info("Generated unique project ID = {}", generatedID);

        Resource resource = new ClassPathResource(ProjectContants.PATH_DEFAULT_PROJECT + ProjectContants.PROJECT_POM);
        String fileContent = null;
        try {
            fileContent = IOUtils.toString(resource.getInputStream(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new ReadFileException(ProjectContants.PATH_DEFAULT_PROJECT + ProjectContants.PROJECT_POM);
        }

        File destDir = new File(pathProperties.getProjects() + generatedID);
        destDir.mkdirs();

        File finalFile = new File(destDir.getPath() + File.separatorChar + "pom.xml");

        fileContent = replaceVariablesInProjectPOM(fileContent);
        try {
            logger.debug("Copy default project folder to new folder {}", generatedID);
            FileUtils.writeStringToFile(finalFile, fileContent, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new WriteFileException(finalFile.getAbsolutePath());
        }

        return generatedID;
    }

    private String generateImports(Set<String> libraries) {
        logger.trace("Generate imports of libraries {}", libraries);
        StringBuilder sb = new StringBuilder();
        libraries.forEach(value -> sb.append("import ")
                .append(value.replaceAll("\\s+", ""))
                .append(";\n"));

        String output = sb.toString();

        return output;
    }

    private String replaceTestMethods(String content, List<String> testMethods) {
        logger.trace("Replace test methods {}", testMethods);
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

    private String replaceVariablesInProjectPOM(String fileContent) {
        logger.trace("Replace variable in project POM.xml file.");
        String javaVersion = getProperty(OtherConstants.JAVA_VERSION, ProjectContants.DEFAULT_JAVA_VERSION);
        String jmhVersion = getProperty(OtherConstants.JMH_VERSION, ProjectContants.DEFAULT_JMH_VERSION);

        fileContent = replaceTemplateMark(fileContent, TemplateConstants.JAVA_VERSION, javaVersion);
        fileContent = replaceTemplateMark(fileContent, TemplateConstants.JMH_VERSION, jmhVersion);

        return fileContent;
    }

    private String replaceTemplateMark(String content, String templateMark, String text) {
        logger.trace("Replace template mark {} with content {}", templateMark, text);
        content = content.replaceAll(StringUtils.insertBracket(templateMark), text);
        return content;
    }

    private String getProperty(String keyProperty, String defaultValue) {
        String value;
        try {
            PropertiesDTO property = propertiesService.getProperties(keyProperty);
            value = property.getValue();
        } catch (EntityNotFoundException e) {
            logger.debug("Property {} is not in database, set default value {}.", keyProperty, defaultValue);
            value = defaultValue;
        }
        return value;
    }
}
