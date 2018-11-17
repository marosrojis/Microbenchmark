package cz.rojik.util.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import cz.rojik.backend.constants.ConfigConstants;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeGsonSerializer implements JsonSerializer<LocalDateTime> {

    //DateTimeFormatter is thread-safe
    private final static DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern(ConfigConstants.LOCAL_DATE_TIME_PATTERN);

    private static final String NULL_VALUE = "null";

    @Override
    public JsonElement serialize(LocalDateTime time, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(time.format(DATE_FORMAT));
    }
}