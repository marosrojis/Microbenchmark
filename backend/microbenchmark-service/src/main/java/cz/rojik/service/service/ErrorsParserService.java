package cz.rojik.service.service;

import cz.rojik.service.dto.ErrorDTO;
import cz.rojik.service.dto.ErrorInfoWithSourceCodeDTO;

import java.util.List;
import java.util.Set;

/**
 * @author Marek Rojik (marek@rojik.cz) on 05. 01. 2019
 */
public interface ErrorsParserService {

    /**
     * Find errors occur during compiling generated maven project based on codes from user.
     * @param errors found errors
     * @return extended found errors
     */
    List<ErrorDTO> getSyntaxErrors(Set<String> errors);

    /**
     * Generated list of errors occur during compiling generated maven project.
     * Assign the same errors from <code>List<ErrorDTO></code> to same error.
     * Add to {@link ErrorInfoWithSourceCodeDTO} generated source code.
     * @param errors found errors
     * @param projectId project ID
     * @return object contains all occur errors with source code
     */
    ErrorInfoWithSourceCodeDTO processErrorList(List<ErrorDTO> errors, String projectId);
}
