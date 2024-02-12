package io.kenji.seckill.infrastructure.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.IOException;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2023/5/27
 **/
public class JodaDateJsonDeserializer extends JsonDeserializer {
    /**
     * @param jsonParser
     * @param deserializationContext
     * @return
     * @throws IOException
     * @throws
     */
    @Override
    public Object deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String dataString = jsonParser.readValueAs(String.class);
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        return DateTime.parse(dataString, formatter);
    }
}
