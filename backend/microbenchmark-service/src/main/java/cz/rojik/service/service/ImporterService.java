package cz.rojik.service.service;

import cz.rojik.service.utils.pojo.ImportsResult;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ImporterService {

    ImportsResult getLibrariesToImport(ImportsResult imports, String input);
}
