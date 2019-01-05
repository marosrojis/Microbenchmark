package cz.rojik.service.service;

import cz.rojik.backend.dto.PropertyDTO;
import cz.rojik.service.exception.ReadFileException;
import cz.rojik.service.utils.pojo.ImportsResult;

/**
 * @author Marek Rojik (marek@rojik.cz) on 05. 01. 2019
 */
public interface ImporterService {

    ImportsResult getLibrariesToImport(ImportsResult imports, String input);

    PropertyDTO processFolderWithJars(String folder) throws ReadFileException;
}
