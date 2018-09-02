package cz.rojik.service;

import cz.rojik.dto.ErrorDTO;
import cz.rojik.dto.ErrorInfoDTO;

import java.util.List;
import java.util.Set;

public interface ErrorsParserService {

    List<ErrorDTO> getSyntaxErrors(Set<String> errors);

    List<ErrorInfoDTO> processErrorList(List<ErrorDTO> errors, String projectId);
}
