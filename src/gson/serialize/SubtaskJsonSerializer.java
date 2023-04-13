package gson.serialize;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import tasks.Subtask;

import java.lang.reflect.Type;
import java.time.format.DateTimeFormatter;

public class SubtaskJsonSerializer implements JsonSerializer<Subtask> {
    @Override
    public JsonElement serialize(Subtask subtask, Type type, JsonSerializationContext context) {
        JsonObject result = new JsonObject();

        result.addProperty("Name", subtask.getName());
        if (subtask.getDescriptionTask() == null)
            result.addProperty("Specification", "null");
        else
            result.addProperty("Specification", subtask.getDescriptionTask());

        result.addProperty("Id", subtask.getId());

        result.addProperty("Status", String.valueOf(subtask.getStatus()));

        if (subtask.getDuration() == null)
            result.addProperty("Duration", "null");
        else
            result.add("Duration", context.serialize(subtask.getDuration().toMinutes()));

        if (subtask.getStartTime() == null)
            result.addProperty("StartTime", "null");
        else
            result.add("StartTime",
                    context.serialize(subtask.getStartTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy|HH:mm"))));

        result.addProperty("IdEpic", subtask.getEpic().getId());

        return result;
    }
}
