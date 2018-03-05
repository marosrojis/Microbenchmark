package cz.rojik;

import cz.rojik.model.Error;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ErrorsParser {

    private static Logger logger = LoggerFactory.getLogger(ErrorsParser.class);

    private static final String ERROR_HELP = "[ERROR] -> [Help 1]";
    private static final String ERROR_COMPILATION = "[ERROR] COMPILATION ERROR : ";
    private static final String ERROR_EMPTY = "[ERROR] ";
    private static final String ERROR_MAVEN_REGEX = ".*maven.*";
    private static final String ERROR_PATH_FILE_REGEX = "\\[ERROR\\].*\\.java:";
    private static final String PARSE_ERROR_REGEX = "\\[(\\d+),\\d+\\] (.*)";
    private static final String PARSE_ERROR_WITHOUT_ABSOLUTE_PATH = "\\[ERROR\\] {3}([a-z]+:.*)"; //e.g. [ERROR]   symbol:   class List

    public void getSyntaxErrors(Set<String> errors) {
        errors = removeCertainErrors(errors);
        errors = removeErrorsHelp(errors);
        errors = removeMavenErrors(errors);
        List<Error> errorList = getErrorsInfo(errors);
    }

    private List<Error> getErrorsInfo(Set<String> errors) {
        Pattern regexParseError = Pattern.compile(PARSE_ERROR_REGEX);
        Pattern regexWithoutAbsolutePath = Pattern.compile(PARSE_ERROR_WITHOUT_ABSOLUTE_PATH);
        List<Error> errorList = new ArrayList<>();
        int row = -1;

        for (String error : errors) {
            error = error.replaceAll(ERROR_PATH_FILE_REGEX, "");
            Matcher mParseError = regexParseError.matcher(error);
            Matcher mWithoutAbsolutePath = regexWithoutAbsolutePath.matcher(error);

            if (mParseError.find()) {
                row = Integer.parseInt(mParseError.group(1));
                errorList.add(new Error(mParseError.group(2), row));
            }
            else if (mWithoutAbsolutePath.find()){
                errorList.add(new Error(mWithoutAbsolutePath.group(1), row));
            }
        }

        return errorList;
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
