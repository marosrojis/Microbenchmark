package cz.rojik.service.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import cz.rojik.service.constants.ProjectContants;
import cz.rojik.service.dto.TemplateDTO;
import cz.rojik.service.exception.ReadFileException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class FileUtils {

    private static Logger logger = LoggerFactory.getLogger(FileUtils.class);

    public static void saveTemplateToJson(TemplateDTO template, String projectId) {
        try (Writer writer = new FileWriter(ProjectContants.PROJECTS_FOLDER + projectId + "/" + "template.json")) {
            Gson gson = new GsonBuilder().create();
            gson.toJson(template, writer);
        } catch (IOException e) {
            logger.error("Cannot save template class from project witd ID {0}", projectId);
            throw new ReadFileException(projectId);
        }
    }

    public static TemplateDTO getTemplateFromJson(String projectId) {
        TemplateDTO template = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(ProjectContants.PROJECTS_FOLDER + projectId + "/" + "template.json"))){
            Gson gson = new GsonBuilder().create();
            template = gson.fromJson(reader, TemplateDTO.class);
        } catch (IOException e) {
            logger.error("Cannot open template file from project witd ID {0}", projectId);
            throw new ReadFileException(projectId);
        }
        return template;
    }

    public static List<String> readSourceFile(String projectId) {
        List<String> sourceCode;
        try {
            sourceCode = Files.readAllLines(Paths.get(ProjectContants.PROJECTS_FOLDER + projectId + "/" + ProjectContants.PATH_JAVA_PACKAGE + ProjectContants.JAVA_CLASS_FILE));
        } catch (IOException e) {
            logger.error("Cannot open class from project witd ID {0}", projectId);
            throw new ReadFileException(projectId);
        }
        return sourceCode;
    }
}
