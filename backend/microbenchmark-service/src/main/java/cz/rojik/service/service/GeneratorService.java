package cz.rojik.service.service;

import cz.rojik.service.dto.LibrariesDTO;
import cz.rojik.service.dto.TemplateDTO;
import cz.rojik.service.exception.ImportsToChooseException;

/**
 * @author Marek Rojik (marek@rojik.cz) on 05. 01. 2019
 */
public interface GeneratorService {

    String generateJavaClass(TemplateDTO template) throws ImportsToChooseException;

    String importLibraries(LibrariesDTO libraries);
}
