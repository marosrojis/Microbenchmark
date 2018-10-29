package cz.rojik.service.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import cz.rojik.service.constants.ProjectContants;
import cz.rojik.service.dto.TemplateDTO;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class FileUtils {

    public static void saveTemplateToJson(TemplateDTO template, String projectId) {
        try (Writer writer = new FileWriter(ProjectContants.PROJECTS_FOLDER + projectId + "/" + "template.json")) {
            Gson gson = new GsonBuilder().create();
            gson.toJson(template, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static TemplateDTO getTemplateFromJson(String projectId) {
        BufferedReader reader = null;
        TemplateDTO template = null;
        try {
            reader = new BufferedReader(new FileReader(ProjectContants.PROJECTS_FOLDER + projectId + "/" + "template.json"));
            Gson gson = new GsonBuilder().create();
            template = gson.fromJson(reader, TemplateDTO.class);

            System.out.println("Object mode: " + template);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        return template;
    }
}
