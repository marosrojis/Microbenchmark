package cz.rojik.service.service.impl;

import cz.rojik.service.dto.ProcessInfoDTO;
import cz.rojik.service.dto.TemplateDTO;
import cz.rojik.service.enums.Operation;
import cz.rojik.service.service.MessageLogParserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Marek Rojik (marek@rojik.cz) on 05. 01. 2019
 */
@Service
public class MessageLogParserServiceImpl implements MessageLogParserService {

    private static Logger logger = LoggerFactory.getLogger(MessageLogParserServiceImpl.class);

    private static final String MEASUREMENT_REGEX = "Iteration[ \\t]*(\\d+):.*";
    private static final String WARMUP_REGEX = "# Warmup Iteration[ \\t]*(\\d+):.*";
    private static final String RESULT_REGEX = "Result \"cz\\.rojik\\.Microbenchmark\\.benchmarkTest(\\d+)\":[\\s\\S.]*";
    private static final String EXCEPTION_REGEX = ".*Exception[\\s\\S]*";
    private static final String ERROR_REGEX = ".*Error:[\\s\\S]*";
    private static final String COMPLETE_REGEX = "[\\s]?Benchmark result is saved to[\\s\\S.]*";


    @Override
    public ProcessInfoDTO parseMessage(String message, TemplateDTO template) {
        logger.trace("Parse message {} from running docker container", message);
        ProcessInfoDTO info = null;
        final Pattern pMeasurement = Pattern.compile(MEASUREMENT_REGEX);
        final Pattern pWarmup = Pattern.compile(WARMUP_REGEX);
        final Pattern pResult = Pattern.compile(RESULT_REGEX);
        final Pattern pException = Pattern.compile(EXCEPTION_REGEX);
        final Pattern pError = Pattern.compile(ERROR_REGEX);
        final Pattern pComplete = Pattern.compile(COMPLETE_REGEX);

        if (pMeasurement.matcher(message).matches()) {
            info = new ProcessInfoDTO(Operation.MEASUREMENT, getNumberFromRegex(pMeasurement.matcher(message)), template.getMeasurement() + "");
        }
        else if (pWarmup.matcher(message).matches()) {
            info = new ProcessInfoDTO(Operation.WARMUP, getNumberFromRegex(pWarmup.matcher(message)), template.getWarmup() + "");
        }
        else if (pResult.matcher(message).matches()) {
            info = new ProcessInfoDTO(Operation.RESULT, getNumberFromRegex(pResult.matcher(message)), template.getTestMethods().size() + "");
        }
        else if (pException.matcher(message).matches() || pError.matcher(message).matches()) {
            info = new ProcessInfoDTO(Operation.ERROR_BENCHMARK).setNote(message);
        }
        else if (pComplete.matcher(message).matches()) {
            info = new ProcessInfoDTO(Operation.SUCCESS_BENCHMARK);
        }

        return info;
    }

    // PRIVATE

    private int getNumberFromRegex(Matcher matcher) {
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        return -1;
    }
}
