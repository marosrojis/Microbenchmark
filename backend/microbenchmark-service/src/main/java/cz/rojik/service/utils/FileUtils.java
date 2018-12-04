package cz.rojik.service.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import cz.rojik.service.constants.ProjectContants;
import cz.rojik.service.dto.TemplateDTO;
import cz.rojik.service.exception.ReadFileException;
import cz.rojik.service.properties.PathProperties;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Component
public class FileUtils {

    private static Logger logger = LoggerFactory.getLogger(FileUtils.class);

    private static PathProperties pathProperties;

    @Autowired
    public FileUtils(PathProperties pathProperties) {
        FileUtils.pathProperties = pathProperties;
    }

    public static void saveTemplateToJson(TemplateDTO template, String projectId) {
        try (Writer writer = new FileWriter(pathProperties.getProjects() + projectId + File.separatorChar + "template.json")) {
            Gson gson = new GsonBuilder().create();
            gson.toJson(template, writer);
        } catch (IOException e) {
            logger.error("Cannot save template class from project with ID {}", projectId);
            throw new ReadFileException(projectId);
        }
    }

    public static TemplateDTO getTemplateFromJson(String projectId) {
        TemplateDTO template = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(pathProperties.getProjects() +
                projectId + File.separatorChar + "template.json"))){
            Gson gson = new GsonBuilder().create();
            template = gson.fromJson(reader, TemplateDTO.class);
        } catch (IOException e) {
            logger.error("Cannot open template file from project with ID {}", projectId);
            throw new ReadFileException(projectId);
        }
        return template;
    }

    public static List<String> readSourceFile(String projectId) {
        List<String> sourceCode;
        try {
            sourceCode = Files.readAllLines(Paths.get(pathProperties.getProjects() + projectId +
                    File.separatorChar + ProjectContants.PATH_JAVA_PACKAGE + ProjectContants.JAVA_CLASS_FILE));
        } catch (IOException e) {
            logger.error("Cannot open class from project with ID {}", projectId);
            throw new ReadFileException(projectId);
        }
        return sourceCode;
    }

    public static String readFileFromResource(String file) {
            Resource resource = new ClassPathResource(file);
            String fileContent = null;
            try {
                fileContent = IOUtils.toString(resource.getInputStream(), StandardCharsets.UTF_8);
            } catch (IOException e) {
                e.printStackTrace();
                throw new ReadFileException(file);
            }

            return fileContent;
    }
}
