package cz.rojik.service.service;

import cz.rojik.service.utils.pojo.ImportsResult;

public interface ImporterService {

    ImportsResult getLibrariesToImport(ImportsResult imports, String input);
}
