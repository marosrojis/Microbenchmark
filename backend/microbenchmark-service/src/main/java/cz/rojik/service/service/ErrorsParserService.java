package cz.rojik.service.service;

import cz.rojik.service.dto.ErrorDTO;
import cz.rojik.service.dto.ErrorInfoWithSourceCodeDTO;

import java.util.List;
import java.util.Set;

public interface ErrorsParserService {

    List<ErrorDTO> getSyntaxErrors(Set<String> errors);

    ErrorInfoWithSourceCodeDTO processErrorList(List<ErrorDTO> errors, String projectId);
}
