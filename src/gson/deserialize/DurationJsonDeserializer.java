package gson.deserialize;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.Duration;

public class DurationJsonDeserializer implements JsonDeserializer<Duration> {
    @Override
    public Duration deserialize(JsonElement json,
                                Type type,
                                JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

        return Duration.ofMinutes(json.getAsLong());
    }
}
