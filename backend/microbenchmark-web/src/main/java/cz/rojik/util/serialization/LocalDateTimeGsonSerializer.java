package cz.rojik.util.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import cz.rojik.backend.constants.DateTimeConstants;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Marek Rojik (marek@rojik.cz) on 05. 01. 2019
 */
public class LocalDateTimeGsonSerializer implements JsonSerializer<LocalDateTime> {

    //DateTimeFormatter is thread-safe
    private final static DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern(DateTimeConstants.LOCAL_DATE_TIME_PATTERN);

    private static final String NULL_VALUE = "null";

    @Override
    public JsonElement serialize(LocalDateTime time, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(time.format(DATE_FORMAT));
    }
}