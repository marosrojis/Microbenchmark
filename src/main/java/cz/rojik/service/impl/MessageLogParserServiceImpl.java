package cz.rojik.service.impl;

import cz.rojik.enums.Operation;
import cz.rojik.dto.ProcessInfoDTO;
import cz.rojik.dto.TemplateDTO;
import cz.rojik.service.MessageLogParserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class MessageLogParserServiceImpl implements MessageLogParserService {

    private static Logger logger = LoggerFactory.getLogger(MessageLogParserServiceImpl.class);

    private static final String MEASUREMENT_REGEX = "Iteration[ \\t]*(\\d+):.*";
    private static final String WARMUP_REGEX = "# Warmup Iteration[ \\t]*(\\d+):.*";
    private static final String RESULT_REGEX = "Result \"cz\\.rojik\\.Microbenchmark\\.benchmarkTest(\\d+)\":";

    @Override
    public ProcessInfoDTO parseMessage(String message, TemplateDTO template) {
        ProcessInfoDTO info = null;
        final Pattern pMeasurement = Pattern.compile(MEASUREMENT_REGEX);
        final Pattern pWarmup = Pattern.compile(WARMUP_REGEX);
        final Pattern pResult = Pattern.compile(RESULT_REGEX);

        if (pMeasurement.matcher(message).matches()) {
            info = new ProcessInfoDTO(Operation.MEASUREMENT, getNumberFromRegex(pMeasurement.matcher(message)), template.getMeasurement() + "");
        }
        else if (pWarmup.matcher(message).matches()) {
            info = new ProcessInfoDTO(Operation.WARMUP, getNumberFromRegex(pWarmup.matcher(message)), template.getWarmup() + "");
        }
        else if (pResult.matcher(message).matches()) {
            info = new ProcessInfoDTO(Operation.RESULT, getNumberFromRegex(pResult.matcher(message)), template.getTestMethods().size() + "");
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
