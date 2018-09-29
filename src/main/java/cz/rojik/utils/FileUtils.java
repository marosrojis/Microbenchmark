package cz.rojik.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import cz.rojik.constants.ProjectContants;
import cz.rojik.dto.TemplateDTO;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class FileUtils {

    public static void saveTemplateToJson(TemplateDTO template, String projectId) {
        try (Writer writer = new FileWriter(ProjectContants.PATH_ALL_PROJECTS + projectId + "/" + "template.json")) {
            Gson gson = new GsonBuilder().create();
            gson.toJson(template, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
