package cz.rojik.backend.util.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import cz.rojik.backend.constants.ConfigConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collection;

public class LocalDateTimeDeserializer extends StdScalarDeserializer<LocalDateTime> {

    private static Logger LOGGER = LoggerFactory.getLogger(LocalDateTimeDeserializer.class);

    //DateTimeFormatter is thread-safe
    private static final Collection<DateTimeFormatter> FORMATTERS = Arrays.asList(
            DateTimeFormatter.ofPattern(ConfigConstants.LOCAL_DATE_PATTERN),
            DateTimeFormatter.ofPattern(ConfigConstants.LOCAL_DATE_TIME_PATTERN)
        );

    private static final String NULL_VALUE = "null";

    public LocalDateTimeDeserializer() {
        super(LocalDateTime.class);
    }

    protected LocalDateTimeDeserializer(Class<LocalDateTime> vc) {
        super(vc);
    }

    @Override
    public LocalDateTime deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {

        while (jp.getCurrentToken() != JsonToken.VALUE_STRING) {
            jp.nextToken();
        }

        String dateStr = jp.getValueAsString();

        return returnParsedLocalDateOrNull(dateStr);
    }

    private LocalDateTime returnParsedLocalDateOrNull(String dateStr) {
        if (dateStr != null && !NULL_VALUE.equals(dateStr)) {
            return parseDateWithProperFormat(dateStr);
        }
        return null;
    }

    private LocalDateTime parseDateWithProperFormat(String dateStr) {
        LocalDateTime date = null;

        for (DateTimeFormatter formatter : FORMATTERS) {
            date = tryParseLocalDate(dateStr, formatter);

            if (date != null) {
                break;
            }
        }

        return date;
    }

    private LocalDateTime tryParseLocalDate(String dateStr, DateTimeFormatter formatter) {
        LocalDateTime date = null;

        try {
            date = LocalDateTime.parse(dateStr, formatter);
        } catch (IllegalArgumentException e) {
            LOGGER.warn("Unable to parse value {} as LocalDate", dateStr);
        }
        return date;
    }
}