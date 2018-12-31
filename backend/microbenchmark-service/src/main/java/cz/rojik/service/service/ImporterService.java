package cz.rojik.service.service;

import cz.rojik.backend.dto.PropertiesDTO;
import cz.rojik.service.exception.ReadFileException;
import cz.rojik.service.utils.pojo.ImportsResult;

public interface ImporterService {

    ImportsResult getLibrariesToImport(ImportsResult imports, String input);

    PropertiesDTO processFolderWithJars(String folder) throws ReadFileException;
}
