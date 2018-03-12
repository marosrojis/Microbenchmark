package cz.rojik;

import cz.rojik.constant.ProjectContants;
import cz.rojik.constant.TemplateConstants;
import cz.rojik.exception.ReadFileException;
import cz.rojik.model.Template;
import cz.rojik.utils.StringUtils;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Generator {

    private static Logger logger = LoggerFactory.getLogger(Generator.class);

    public Generator() {
    }

    public String generateJavaClass(Template template) {
        String fileContent = readDefaultFile();
        String newContent = generateContent(template, fileContent);
        saveFile(ProjectContants.JAVA_CLASS_FILE, newContent);

        return ProjectContants.JAVA_CLASS;
    }

    private String generateContent(Template template, String content) {
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

    private String readDefaultFile() {
        File file = new File(ProjectContants.DEFAULT_JAVA_FILE);
        String fileContent = "";
        try {
            fileContent = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ReadFileException(ProjectContants.DEFAULT_JAVA_FILE);
        }

        return fileContent;
    }

    private boolean saveFile(String fileName, String content) {
        try {
            File file = new File(ProjectContants.PATH_JAVA_PACKAGE + fileName);
            FileUtils.writeStringToFile(file, content, StandardCharsets.UTF_8, false);
        } catch (IOException e) {
            logger.error("Save file failure");
            return false;
        }
        return true;
    }
}
