package cz.rojik.service.service;

import cz.rojik.backend.dto.PropertyDTO;
import cz.rojik.service.exception.ReadFileException;
import cz.rojik.service.utils.pojo.ImportsResult;

/**
 * @author Marek Rojik (marek@rojik.cz) on 05. 01. 2019
 */
public interface ImporterService {

    /**
     * Analyze code from user and find all libraries to import and libraries needs to choose
     * @param imports already found libraries to import
     * @param input code to analyze
     * @return libraries to import
     */
    ImportsResult getLibrariesToImport(ImportsResult imports, String input);

    /**
     * Find all classes with package name from JAR files.
     * Save libraries and list of ignore classes to database.
     * @param folder folder contains all JAR files to analyze
     * @return classes with packages found in JAR files
     * @throws ReadFileException problem with reading JAR file in specified folder
     */
    PropertyDTO processFolderWithJars(String folder) throws ReadFileException;
}
