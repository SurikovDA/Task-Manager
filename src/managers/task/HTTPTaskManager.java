package managers.task;

import clients.KVTaskClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
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
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class HTTPTaskManager extends FileBackedTasksManager {
    Gson gson;
    private final KVTaskClient kvTaskClient;

    public HTTPTaskManager(String path) throws URISyntaxException, IOException, InterruptedException {
        super(path);
        gson = new Gson();
        this.kvTaskClient = new KVTaskClient(path);
    }

    //Сохраняет задачи на сервер
    @Override
    protected void save() {
        for (Task task : tasks.values()) {
            String json = getJsonString(task);
            kvTaskClient.put("task", json);
        }

        for (Subtask subtask : subtasks.values()) {
            String json = getJsonString(subtask);
            kvTaskClient.put("subtask", json);
        }

        for (Epic epic : epics.values()) {
            String json = getJsonString(epic);
            kvTaskClient.put("epic", json);
        }
        List<Integer> idList = Stream.of(
                        this.getAllTasks()
                        , this.getAllEpics()
                        , this.getAllSubtasks())
                .flatMap(Collection::stream)
                .map(Task::getId)
                .collect(Collectors.toList());
        kvTaskClient.put("idList", gson.toJson(idList));
    }

    //Возращает Задачу из сервера
    public void load(String key) {
        String type;
        String json = kvTaskClient.load(key);
        if (json != null) {
            if (key.equals("task")) {
                type = "Task";
                Task task = readJsonString(json, type);
                tasks.put(task.getId(), task);
            } else if (key.equals("subtask")) {
                type = "Subtask";
                Task subtask = readJsonString(json, type);
                ((Subtask) subtask).getEpic().getSubtasks().add((Subtask) subtask);
                subtasks.put(subtask.getId(), (Subtask) subtask);
            } else if (key.equals("epic")) {
                type = "Epic";
                Task epic = readJsonString(json, type);
                epics.put(epic.getId(), (Epic) epic);
            } else {
                System.out.println("Ошибка! Не правильный запрос!");
            }
        }
    }

    //Возращает Задачу из сервера
    public void loadAll() {
        this.load("task");
         this.load("epic");
         this.load("subtask");
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
        } else if (simpleNameTask.equals("Subtask")) {
            Subtask subtask = gson.fromJson(json, Subtask.class);
            addEpicInSubtaskById(subtask, subtask.getIdEpic());
            return subtask;
        } else {
            Epic epic = gson.fromJson(json, Epic.class);
            addSubtasksInEpic(epic);
            return epic;
        }
    }

    private String getSimpleNameTask(int id) {
        if (tasks.containsKey(id))
            return tasks.get(id).getClass().getSimpleName();
        else if (subtasks.containsKey(id))
            return subtasks.get(id).getClass().getSimpleName();
        else
            return epics.get(id).getClass().getSimpleName();
    }

}