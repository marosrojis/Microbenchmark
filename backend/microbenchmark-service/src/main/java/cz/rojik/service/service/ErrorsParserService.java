package cz.rojik.service.service;

import cz.rojik.service.dto.ErrorDTO;
import cz.rojik.service.dto.ErrorInfoWithSourceCodeDTO;

import java.util.List;
import java.util.Set;

/**
 * @author Marek Rojik (marek@rojik.cz) on 05. 01. 2019
 */
public interface ErrorsParserService {

    List<ErrorDTO> getSyntaxErrors(Set<String> errors);

    ErrorInfoWithSourceCodeDTO processErrorList(List<ErrorDTO> errors, String projectId);
}
