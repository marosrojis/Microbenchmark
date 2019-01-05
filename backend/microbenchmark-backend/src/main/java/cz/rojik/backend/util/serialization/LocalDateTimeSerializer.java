package cz.rojik.backend.util.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import cz.rojik.backend.constants.DateTimeConstants;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Marek Rojik (marek@rojik.cz) on 05. 01. 2019
 */
public class LocalDateTimeSerializer extends StdScalarSerializer<LocalDateTime> {

    //DateTimeFormatter is thread-safe
    private final static DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern(DateTimeConstants.LOCAL_DATE_TIME_PATTERN);

    private static final String NULL_VALUE = "null";

    public LocalDateTimeSerializer() {
        super(LocalDateTime.class);
    }

    protected LocalDateTimeSerializer(Class<LocalDateTime> t) {
        super(t);
    }

    @Override
    public void serialize(LocalDateTime value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        if (value != null) {
            jgen.writeString(value.format(DATE_FORMAT));
        } else {
            jgen.writeString(NULL_VALUE);
        }
    }
}