package cz.rojik.backend.util.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import cz.rojik.backend.constants.ConfigConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class LocalTimeDeserializer extends StdScalarDeserializer<LocalTime> {

    private static Logger LOGGER = LoggerFactory.getLogger(LocalTimeDeserializer.class);

    //DateTimeFormatter is thread-safe
    private static final Collection<DateTimeFormatter> FORMATTERS = Collections.singletonList(
            DateTimeFormatter.ofPattern(ConfigConstants.LOCAL_TIME_PATTERN)
    );

    private static final String NULL_VALUE = "null";

    public LocalTimeDeserializer() {
        super(LocalTime.class);
    }

    protected LocalTimeDeserializer(Class<LocalTime> vc) {
        super(vc);
    }

    @Override
    public LocalTime deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {

        while (jp.getCurrentToken() != JsonToken.VALUE_STRING) {
            jp.nextToken();
        }

        String dateStr = jp.getValueAsString();

        return returnParsedLocalDateOrNull(dateStr);
    }

    private LocalTime returnParsedLocalDateOrNull(String dateStr) {
        if (dateStr != null && !NULL_VALUE.equals(dateStr)) {
            return parseDateWithProperFormat(dateStr);
        }
        return null;
    }

    private LocalTime parseDateWithProperFormat(String dateStr) {
        LocalTime date = null;

        for (DateTimeFormatter formatter : FORMATTERS) {
            date = tryParseLocalDate(dateStr, formatter);

            if (date != null) {
                break;
            }
        }

        return date;
    }

    private LocalTime tryParseLocalDate(String dateStr, DateTimeFormatter formatter) {
        LocalTime date = null;

        try {
            date = LocalTime.parse(dateStr, formatter);
        } catch (IllegalArgumentException e) {
            LOGGER.warn("Unable to parse value {} as LocalDate", dateStr);
        }
        return date;
    }
}