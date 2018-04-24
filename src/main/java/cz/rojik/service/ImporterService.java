package cz.rojik.service;

import java.util.Set;

public interface ImporterService {

    Set<String> getLibrariesToImport(String input);
}
