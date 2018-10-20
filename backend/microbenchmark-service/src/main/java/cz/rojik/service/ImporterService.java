package cz.rojik.service;

import cz.rojik.utils.pojo.ImportsResult;

public interface ImporterService {

    ImportsResult getLibrariesToImport(ImportsResult imports, String input);
}
