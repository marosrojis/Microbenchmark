package cz.rojik;

import cz.rojik.enums.Operation;
import cz.rojik.model.ProcessInfo;
import cz.rojik.model.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageLogParser {

    private static Logger logger = LoggerFactory.getLogger(MessageLogParser.class);

    private static final String MEASUREMENT_REGEX = "Iteration[ \\t]*(\\d+):.*";
    private static final String WARMUP_REGEX = "# Warmup Iteration[ \\t]*(\\d+):.*";
    private static final String RESULT_REGEX = "Result \"cz\\.rojik\\.Microbenchmark\\.benchmarkTest(\\d+)\":";


    public MessageLogParser() {}

    public ProcessInfo parseMessage(String message, Template template) {
        ProcessInfo info = null;
        final Pattern pMeasurement = Pattern.compile(MEASUREMENT_REGEX);
        final Pattern pWarmup = Pattern.compile(WARMUP_REGEX);
        final Pattern pResult = Pattern.compile(RESULT_REGEX);

        if (pMeasurement.matcher(message).matches()) {
            info = new ProcessInfo(Operation.MEASUREMENT, getNumberFromRegex(pMeasurement.matcher(message)), template.getMeasurement() + "");
        }
        else if (pWarmup.matcher(message).matches()) {
            info = new ProcessInfo(Operation.WARMUP, getNumberFromRegex(pWarmup.matcher(message)), template.getWarmup() + "");
        }
        else if (pResult.matcher(message).matches()) {
            info = new ProcessInfo(Operation.RESULT, getNumberFromRegex(pResult.matcher(message)), template.getTestMethods().size() + "");
        }

        return info;
    }

    private int getNumberFromRegex(Matcher matcher) {
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        return -1;
    }
}
