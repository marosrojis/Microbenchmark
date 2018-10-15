package cz.rojik.service;

import cz.rojik.dto.ErrorDTO;
import cz.rojik.dto.ErrorInfoWithSourceCodeDTO;

import java.util.List;
import java.util.Set;

public interface ErrorsParserService {

    List<ErrorDTO> getSyntaxErrors(Set<String> errors);

    ErrorInfoWithSourceCodeDTO processErrorList(List<ErrorDTO> errors, String projectId);
}
