package managers.task;
import clients.KVTaskClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import exceptions.ManagerSaveException;
import gson.deserialize.DurationJsonDeserializer;
import gson.deserialize.EpicJsonDeserializer;
import gson.deserialize.LocalDateTimeJsonDeserializer;
import gson.deserialize.SubtaskJsonDeserializer;
import gson.serialize.DurationJsonSerializer;
import gson.serialize.EpicJsonSerializer;
import gson.serialize.LocalDateTimeJsonSerializer;
import gson.serialize.SubtaskJsonSerializer;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Duration;
import java.time.LocalDateTime;

public class HTTPTaskManager extends FileBackedTasksManager {
    private final KVTaskClient kvTaskClient;

    public HTTPTaskManager(String path)  {
            super(path);
            this.kvTaskClient = new KVTaskClient(path);
    }

    //Сохраняет задачи на сервер
    @Override
    protected void save() {
        for (Task task : tasks.values()) {
            String json = getJsonString(task);
            kvTaskClient.put(String.valueOf(task.getId()), json);
        }

        for (Subtask subtask : subtasks.values()) {
            String json = getJsonString(subtask);
            kvTaskClient.put(String.valueOf(subtask.getId()), json);
        }

        for (Epic epic : epics.values()) {
            String json = getJsonString(epic);
            kvTaskClient.put(String.valueOf(epic.getId()), json);
        }
    }

    //Возращает Задачу из сервера
    public Task load(String key) {
        String json = kvTaskClient.load(key);
        if (json != null)
            return readJsonString(json, getSimpleNameTask(Integer.parseInt(key)));
        else
            return null;
    }

    //Сериализация Task объекта в Json
    private String getJsonString(Task task) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Epic.class, new EpicJsonSerializer())
                .registerTypeAdapter(Subtask.class, new SubtaskJsonSerializer())
                .registerTypeAdapter(Duration.class, new DurationJsonSerializer())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeJsonSerializer())
                .create();
        if (task instanceof Subtask)
            return gson.toJson(task, Subtask.class);
        else if (task instanceof Epic)
            return gson.toJson(task, Epic.class);
        else
            return gson.toJson(task, Task.class);
    }

    //Десериализация Json в объект Task
    private Task readJsonString(String json, String simpleNameTask) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Epic.class, new EpicJsonDeserializer())
                .registerTypeAdapter(Subtask.class, new SubtaskJsonDeserializer())
                .registerTypeAdapter(Duration.class, new DurationJsonDeserializer())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeJsonDeserializer())
                .create();
        if (simpleNameTask.equals("Task")) {
            return gson.fromJson(json, Task.class);
        } else if (simpleNameTask.equals("Subtask")){
            Subtask subtask = gson.fromJson(json, Subtask.class);
            addEpicInSubtaskById(subtask, subtask.getIdEpic());
            return subtask;
        } else{
            Epic epic = gson.fromJson(json, Epic.class);
            addSubtasksInEpic(epic);
            return epic;
        }
    }

    private String getSimpleNameTask(int id){
        if (tasks.containsKey(id))
            return tasks.get(id).getClass().getSimpleName();
        else if (subtasks.containsKey(id))
            return subtasks.get(id).getClass().getSimpleName();
        else
            return epics.get(id).getClass().getSimpleName();
    }

}
