package cz.rojik.service.service;

import cz.rojik.service.dto.LibrariesDTO;
import cz.rojik.service.dto.TemplateDTO;
import cz.rojik.service.exception.ImportsToChooseException;

/**
 * @author Marek Rojik (marek@rojik.cz) on 05. 01. 2019
 */
public interface GeneratorService {

    /**
     * Generate java class with JMH anotations and methods for start microbenchmark
     * @param template code to measure
     * @return Generated project ID
     * @throws ImportsToChooseException Code needs to choose from which package want to import class
     */
    String generateJavaClass(TemplateDTO template) throws ImportsToChooseException;

    /**
     * Generate libraries from methods to measure and generate content of JMH class
     * @param libraries selected libraries to import
     * @return project ID
     */
    String importLibraries(LibrariesDTO libraries);
}
