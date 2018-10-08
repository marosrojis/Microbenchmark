package cz.rojik.service.impl;

import cz.rojik.constants.ProjectContants;
import cz.rojik.exception.ReadFileException;
import cz.rojik.dto.ErrorDTO;
import cz.rojik.dto.ErrorInfoDTO;
import cz.rojik.service.ErrorsParserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ErrorsParserServiceImpl implements ErrorsParserService {

    private static Logger logger = LoggerFactory.getLogger(ErrorsParserServiceImpl.class);

    private static final String ERROR_HELP = "[ERROR] -> [Help 1]";
    private static final String ERROR_COMPILATION = "[ERROR] COMPILATION ERROR : ";
    private static final String ERROR_EMPTY = "[ERROR] ";
    private static final String ERROR_MAVEN_REGEX = ".*maven.*";
    private static final String ERROR_PATH_FILE_REGEX = "\\[ERROR\\].*\\.java:";
    private static final String PARSE_ERROR_REGEX = "\\[(\\d+),\\d+\\] (.*)";
    private static final String PARSE_ERROR_WITHOUT_ABSOLUTE_PATH = "\\[ERROR\\] {3}([a-z]+:.*)"; //e.g. [ERROR]   symbol:   class List

    @Override
    public List<ErrorDTO> getSyntaxErrors(Set<String> errors) {
        errors = removeCertainErrors(errors);
        errors = removeErrorsHelp(errors);
        errors = removeMavenErrors(errors);
        List<ErrorDTO> errorList = getErrorsInfo(errors);

        return errorList;
    }

    @Override
    public List<ErrorInfoDTO> processErrorList(List<ErrorDTO> errors, String projectId) {
        errors = insertCodeToError(projectId, errors);

        List<ErrorInfoDTO> errorInfoList = new ArrayList<>();

        int i = 0;
        while (i < errors.size()) {
            ErrorDTO error;

            ErrorInfoDTO errorInfo = new ErrorInfoDTO();
            do {
                error = errors.get(i);
                errorInfo.getErrors().add(error);
                i++;
            } while (i < errors.size() && (error.getRow() == errors.get(i).getRow() || errors.get(i).getRow() - error.getRow() == 1));

            errorInfoList.add(errorInfo);
        }

        return errorInfoList;
    }

    // PRIVATE

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
            }
            else if (mWithoutAbsolutePath.find()){
                errorList.add(new ErrorDTO(mWithoutAbsolutePath.group(1), row));
            }
        }

        return errorList;
    }

    private List<ErrorDTO> insertCodeToError(String projectId, List<ErrorDTO> errors) {
        List<String> codes;
        try {
            codes = Files.readAllLines(Paths.get(ProjectContants.PATH_ALL_PROJECTS + projectId + "/" + ProjectContants.PATH_JAVA_PACKAGE + ProjectContants.JAVA_CLASS_FILE));
        } catch (IOException e) {
            logger.error("Cannot open class from project witd ID {0}", projectId);
            throw new ReadFileException(projectId);

        }

        for (ErrorDTO error : errors) {
            error.setCode(codes.get(error.getRow() - 1));
        }

        return errors;
    }

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

    private Set<String> removeCertainErrors(Set<String> errors) {
        errors.removeIf(item -> item.equals(ERROR_COMPILATION) || item.equals(ERROR_EMPTY));
        return errors;
    }

    private Set<String> removeMavenErrors(Set<String> errors) {
        Pattern p = Pattern.compile(ERROR_MAVEN_REGEX);
        errors.removeIf(item -> p.matcher(item.toLowerCase()).matches());

        return errors;
    }
}
