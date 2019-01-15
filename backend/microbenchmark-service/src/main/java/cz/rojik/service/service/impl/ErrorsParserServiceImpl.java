package cz.rojik.service.service.impl;

import cz.rojik.service.constants.ProjectContants;
import cz.rojik.service.dto.ErrorDTO;
import cz.rojik.service.dto.ErrorInfoDTO;
import cz.rojik.service.dto.ErrorInfoWithSourceCodeDTO;
import cz.rojik.service.exception.ReadFileException;
import cz.rojik.backend.properties.PathProperties;
import cz.rojik.service.service.ErrorsParserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Marek Rojik (marek@rojik.cz) on 05. 01. 2019
 */
@Service
public class ErrorsParserServiceImpl implements ErrorsParserService {

    private static Logger logger = LoggerFactory.getLogger(ErrorsParserServiceImpl.class);

    @Autowired
    private PathProperties pathProperties;

    private static final String ERROR_HELP = "[ERROR] -> [Help 1]";
    private static final String ERROR_COMPILATION = "[ERROR] COMPILATION ERROR : ";
    private static final String ERROR_EMPTY = "[ERROR] ";
    private static final String ERROR_MAVEN_REGEX = ".*maven.*";
    private static final String ERROR_PATH_FILE_REGEX = "\\[ERROR\\].*\\.java:";
    private static final String PARSE_ERROR_REGEX = "\\[(\\d+),\\d+\\] (.*)";
    private static final String PARSE_ERROR_WITHOUT_ABSOLUTE_PATH = "\\[ERROR\\] {3}([a-z]+:.*)"; //e.g. [ERROR]   symbol:   class List

    @Override
    public List<ErrorDTO> getSyntaxErrors(Set<String> errors) {
        logger.trace("Parse errors from maven project: {}", errors);
        errors = removeCertainErrors(errors);
        errors = removeErrorsHelp(errors);
        errors = removeMavenErrors(errors);
        List<ErrorDTO> errorList = getErrorsInfo(errors);

        logger.debug("Parse errors is completed.\n{}", errorList);
        return errorList;
    }

    @Override
    public ErrorInfoWithSourceCodeDTO processErrorList(List<ErrorDTO> errors, String projectId) {
        logger.trace("Processing error list {} to project {}", errors, projectId);
        List<String> sourceCode = insertSourceCodeToError(projectId, errors);

        List<ErrorInfoDTO> errorInfoList = new ArrayList<>();

        int i = 0;
        while (i < errors.size()) {
            ErrorDTO error;

            ErrorInfoDTO errorInfo = new ErrorInfoDTO();
            do {
                error = errors.get(i);
                errorInfo.getErrors().add(error);
                i++;
            }
            while (i < errors.size() && (error.getRow() == errors.get(i).getRow() || errors.get(i).getRow() - error.getRow() == 1));

            errorInfoList.add(errorInfo);
        }

        ErrorInfoWithSourceCodeDTO errorsWithSourceCode = new ErrorInfoWithSourceCodeDTO(errorInfoList, sourceCode);
        return errorsWithSourceCode;
    }

    // PRIVATE

    /**
     *  Parse error from maven log using REGEX expressions
     * @param errors found errors
     * @return extended found errors
     */
    private List<ErrorDTO> getErrorsInfo(Set<String> errors) {
        Pattern regexParseError = Pattern.compile(PARSE_ERROR_REGEX);
        Pattern regexWithoutAbsolutePath = Pattern.compile(PARSE_ERROR_WITHOUT_ABSOLUTE_PATH);
        List<ErrorDTO> errorList = new ArrayList<>();
        int row = -1;

        for (String error : errors) {
            error = error.replaceAll(ERROR_PATH_FILE_REGEX, "");
            Matcher mParseError = regexParseError.matcher(error);
            Matcher mWithoutAbsolutePath = regexWithoutAbsolutePath.matcher(error);

            if (mParseError.find()) {
                row = Integer.parseInt(mParseError.group(1));
                errorList.add(new ErrorDTO(mParseError.group(2), row));
            } else if (mWithoutAbsolutePath.find()) {
                errorList.add(new ErrorDTO(mWithoutAbsolutePath.group(1), row));
            }
        }

        return errorList;
    }

    /**
     * Read source code from generated maven project file and parse file on rows.
     * Add to {@link ErrorDTO} to each error paste source code with found error.
     * @param projectId generated project ID
     * @param errors found errors
     * @return generated source code
     */
    private List<String> insertSourceCodeToError(String projectId, List<ErrorDTO> errors) {
        List<String> sourceCode;
        try {
            sourceCode = Files.readAllLines(Paths.get(pathProperties.getProjects() + projectId + File.separatorChar + ProjectContants.PATH_JAVA_PACKAGE + ProjectContants.JAVA_CLASS_FILE));
        } catch (IOException e) {
            logger.error("Cannot open class from project witd ID {0}", projectId);
            throw new ReadFileException(projectId);

        }

        for (ErrorDTO error : errors) {
            error.setCode(sourceCode.get(error.getRow() - 1));
        }

        return sourceCode;
    }

    /**
     * Log from maven project contains unnecessary HELP errors.
     * Method removes unnecessary errors from list of errors
     * @param errors found errors
     * @return list of errors without unnecessary errors
     */
    private Set<String> removeErrorsHelp(Set<String> errors) {
        Iterator<String> i = errors.iterator();
        while (i.hasNext()) {
            String item = i.next();

            if (item.equals(ERROR_HELP)) {
                i.remove();
                break;
            }
        }
        while (i.hasNext()) {
            i.next();
            i.remove();
        }

        return errors;
    }

    /**
     * Log from maven project contains unnecessary COMPILATION or EMPTY errors.
     * Method removes unnecessary errors from list of errors
     * @param errors found errors
     * @return list of errors without unnecessary errors
     */
    private Set<String> removeCertainErrors(Set<String> errors) {
        errors.removeIf(item -> item.equals(ERROR_COMPILATION) || item.equals(ERROR_EMPTY));
        return errors;
    }

    /**
     * Log from maven project contains unnecessary MAVEN errors.
     * Method removes unnecessary errors from list of errors
     * @param errors found errors
     * @return list of errors without unnecessary errors
     */
    private Set<String> removeMavenErrors(Set<String> errors) {
        Pattern p = Pattern.compile(ERROR_MAVEN_REGEX);
        errors.removeIf(item -> p.matcher(item.toLowerCase()).matches());

        return errors;
    }
}
