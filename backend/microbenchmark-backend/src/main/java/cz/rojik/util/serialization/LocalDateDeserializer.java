package cz.rojik.util.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import cz.rojik.constants.ConfigConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collection;

public class LocalDateDeserializer extends StdScalarDeserializer<LocalDate> {

    private static Logger LOGGER = LoggerFactory.getLogger(LocalDateDeserializer.class);

    //DateTimeFormatter is thread-safe
    private static final Collection<DateTimeFormatter> FORMATTERS = Arrays.asList(
            DateTimeFormatter.ofPattern(ConfigConstants.LOCAL_DATE_PATTERN),
            DateTimeFormatter.ofPattern(ConfigConstants.LOCAL_DATE_TIME_PATTERN)
        );

    private static final String NULL_VALUE = "null";

    public LocalDateDeserializer() {
        super(LocalDate.class);
    }

    protected LocalDateDeserializer(Class<LocalDate> vc) {
        super(vc);
    }

    @Override
    public LocalDate deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {

        while (jp.getCurrentToken() != JsonToken.VALUE_STRING) {
            jp.nextToken();
        }

        String dateStr = jp.getValueAsString();

        return returnParsedLocalDateOrNull(dateStr);
    }

    private LocalDate returnParsedLocalDateOrNull(String dateStr) {
        if (dateStr != null && !NULL_VALUE.equals(dateStr)) {
            return parseDateWithProperFormat(dateStr);
        }
        return null;
    }

    private LocalDate parseDateWithProperFormat(String dateStr) {
        LocalDate date = null;

        for (DateTimeFormatter formatter : FORMATTERS) {
            date = tryParseLocalDate(dateStr, formatter);

            if (date != null) {
                break;
            }
        }

        return date;
    }

    private LocalDate tryParseLocalDate(String dateStr, DateTimeFormatter formatter) {
        LocalDate date = null;

        try {
            date = LocalDate.parse(dateStr, formatter);
        } catch (IllegalArgumentException e) {
            LOGGER.warn("Unable to parse value {} as LocalDate", dateStr);
        }
        return date;
    }
}