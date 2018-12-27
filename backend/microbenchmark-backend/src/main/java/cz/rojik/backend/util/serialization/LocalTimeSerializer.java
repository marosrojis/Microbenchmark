package cz.rojik.backend.util.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import cz.rojik.backend.constants.ConfigConstants;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class LocalTimeSerializer extends StdScalarSerializer<LocalTime> {

    //DateTimeFormatter is thread-safe
    private final static DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern(ConfigConstants.LOCAL_TIME_PATTERN);

    private static final String NULL_VALUE = "null";

    public LocalTimeSerializer() {
        super(LocalTime.class);
    }

    protected LocalTimeSerializer(Class<LocalTime> t) {
        super(t);
    }

    @Override
    public void serialize(LocalTime value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        if (value != null) {
            jgen.writeString(value.format(DATE_FORMAT));
        } else {
            jgen.writeString(NULL_VALUE);
        }
    }
}