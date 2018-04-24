package cz.rojik.service;

import cz.rojik.dto.Error;
import cz.rojik.dto.ErrorInfo;

import java.util.List;
import java.util.Set;

public interface ErrorsParserService {

    List<Error> getSyntaxErrors(Set<String> errors);

    List<ErrorInfo> processErrorList(List<Error> errors, String projectId);
}
