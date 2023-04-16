package tests;

import clients.HTTPTaskClient;
import API.HTTPTaskServer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gson.deserialize.DurationJsonDeserializer;
import gson.deserialize.EpicJsonDeserializer;
import gson.deserialize.LocalDateTimeJsonDeserializer;
import gson.deserialize.SubtaskJsonDeserializer;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

public class HTTPTaskServerTest {
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Subtask.class, new SubtaskJsonDeserializer())
            .registerTypeAdapter(Epic.class, new EpicJsonDeserializer())
            .registerTypeAdapter(Duration.class, new DurationJsonDeserializer())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeJsonDeserializer())
            .create();
    private final String uri = "http://localhost:8080/tasks";
    private final HTTPTaskClient client = new HTTPTaskClient(uri);

    private HTTPTaskServer server;
    private Task task1;
    private Task task2;
    private Subtask subtask1;
    private Subtask subtask2;
    private Epic epic1;
    private Epic epic2;

    @BeforeEach
    void startServer() {
        server = new HTTPTaskServer();
        server.startServer();
    }

    @AfterEach
    void stopServer() {
        server.stop();
    }

    @BeforeEach
    void init() {
        task1 = new Task("task1");
        task2 = new Task("task2");
        subtask1 = new Subtask("subtask1");
        subtask2 = new Subtask("subtask2");
        epic1 = new Epic("epic1");
        epic2 = new Epic("epic2");
    }


    //tests for Task methods
    @Test
    void test1_shouldGetTasksToString() {
        //добавляем на сервер задачи через запрос клиента
        client.addOrUpdateTask(task1);
        client.addOrUpdateTask(task2);
        //ожидание
        String expectedTasks = gson.toJson(server.getAllTasks());
        //реальность
        String actualTasks = client.getTasksToString();

        Assertions.assertEquals(expectedTasks, actualTasks, "Ожидался другой список");
    }

    @Test
    void test2_shouldGetTaskByIdToString() {
        //добавляем на сервер задачи через запрос клиента
        client.addOrUpdateTask(task1);
        //ожидание
        String expectedTask = gson.toJson(server.findTaskById(1));
        //реальность
        String actualTasks = client.getTaskByIdToString(1);

        Assertions.assertEquals(expectedTask, actualTasks, "Ожидалась другая задача");
    }

    @Test
    void test3_shouldAddOrUpdateTask() {
        Task updateTask = new Task("update");
        client.addOrUpdateTask(task1);

        //Присвоим updateTask id = 1, чтобы сервер обновил задачу
        updateTask.setId(1);
        client.addOrUpdateTask(updateTask);

        //ожидание
        String expectedTask = updateTask.toString();
        //реальность
        String actualTasks = server.findTaskById(1).toString();

        Assertions.assertEquals(expectedTask, actualTasks, "Задача не обновлена");
    }

    @Test
    void test4_shouldDeleteTaskById() {
        client.addOrUpdateTask(task1);
        client.addOrUpdateTask(task1);

        client.deleteTaskById(1);

        int expectedSize = 1;

        Assertions.assertEquals(expectedSize, server.getAllTasks().size());
    }

    @Test
    void test5_shouldDeleteAllTask() {
        client.addOrUpdateTask(task1);
        client.addOrUpdateTask(task1);

        client.deleteAllTask();

        int expectedSize = 0;

        Assertions.assertEquals(expectedSize, server.getAllTasks().size());
    }

    //tests subtask methods
    @Test
    void test6_shouldGetSubtaskByIdToString() {
        client.addOrUpdateEpic(epic1);
        client.addOrUpdateSubtask(subtask1, server.findEpicById(1));

        String expectedSubtask = gson.toJson(server.findSubtaskById(2));
        String actualSubtask = client.getSubtaskByIdToString(2);

        Assertions.assertEquals(expectedSubtask, actualSubtask, "Ожидалась другая подзадача");
    }

    @Test
    void test7_shouldAddOrUpdateSubtask() {
        Subtask updateSubtask = new Subtask("update");

        client.addOrUpdateEpic(epic1);
        client.addOrUpdateEpic(epic2);
        client.addOrUpdateSubtask(subtask1, server.findEpicById(1));

        //назначим updateSubtask id = 3, чтобы обновить подзадачу под этим id
        updateSubtask.setId(3);
        client.addOrUpdateSubtask(updateSubtask, server.findEpicById(2));

        updateSubtask.setEpic(server.findEpicById(2));
        String expectedSubtask = gson.toJson(updateSubtask);
        String actualSubtask = server.findSubtaskById(3).toString();
        Assertions.assertEquals(expectedSubtask, actualSubtask, "Подзадача не обновлена");
    }

    @Test
    void test8_shouldDeleteSubtaskById() {
        client.addOrUpdateEpic(epic1);
        client.addOrUpdateSubtask(subtask1, server.getEpicById(1));
        client.addOrUpdateSubtask(subtask2, server.getEpicById(1));

        client.deleteSubtaskById(2);

        int expectedSizeSubtasks = 1;
        Assertions.assertEquals(expectedSizeSubtasks, server.getEpicById(1).getSubtasks().size());
    }

    @Test
    void test9_shouldDeleteAllSubtask() {
        client.addOrUpdateEpic(epic1);
        client.addOrUpdateSubtask(subtask1, server.getEpicById(1));
        client.addOrUpdateSubtask(subtask2, server.getEpicById(1));

        client.deleteAllSubtask();

        Assertions.assertEquals(0, server.getEpicById(1).getSubtasks().size());
    }

    //tests epic methods
    @Test
    void test10_shouldGetEpicsToString() {
        client.addOrUpdateEpic(epic1);
        client.addOrUpdateEpic(epic1);

        String expectedEpics = gson.toJson(server.getAllEpics());
        String actualEpics = client.getEpicsToString();

        Assertions.assertEquals(expectedEpics, actualEpics, "Ожидались одинаковые эпики");
    }

    @Test
    void test11_shouldGetEpicByIdToString() {
        client.addOrUpdateEpic(epic1);

        String expectedEpics = gson.toJson(server.getEpicById(1));
        String actualEpics = client.getEpicByIdToString(1);

        Assertions.assertEquals(expectedEpics, actualEpics, "Различаются эпики");
    }

    @Test
    void test12_shouldAddOrUpdateEpic() {
        Epic updateEpic = new Epic("updateEpic");

        client.addOrUpdateEpic(epic1);

        updateEpic.setId(1);
        client.addOrUpdateEpic(updateEpic);

        String expectedEpic = updateEpic.toString();
        String actualEpic = server.getEpicById(1).toString();

        Assertions.assertEquals(expectedEpic, actualEpic, "Ожидалось обновление эпика");
    }

    @Test
    void test13_shouldDeleteEpicById() {
        client.addOrUpdateEpic(epic1);
        client.addOrUpdateEpic(epic2);

        client.deleteEpicById(1);

        int expectedSize = 1;

        Assertions.assertEquals(1, server.getAllEpics().size());
    }

    @Test
    void test14_shouldDeleteAllEpic() {
        client.addOrUpdateEpic(epic1);
        client.addOrUpdateEpic(epic2);

        client.deleteAllEpic();

        Assertions.assertEquals(0, server.getAllEpics().size());
    }

    @Test
    void test15_shouldGetEpicSubTasksToString() {
        client.addOrUpdateEpic(epic1);
        client.addOrUpdateSubtask(subtask1, server.getEpicById(1));
        client.addOrUpdateSubtask(subtask2, server.getEpicById(1));

        String expectedSubtasks = server.getAllSubtasksById(1).toString();
        String actualSubtasks = client.getEpicSubTasksToString(1);

        Assertions.assertEquals(expectedSubtasks, actualSubtasks);
    }

    @Test
    void test16_shouldGetHistoryToString() {
        client.addOrUpdateTask(task1);

        server.findTaskById(1);

        String expectedHistory = gson.toJson(server.getHistory());
        String actualHistory = client.getHistoryToString();

        Assertions.assertEquals(expectedHistory, actualHistory);
    }

    @Test
    void test17_shouldGetPrioritizedTasksToString() {
        task1.setDuration(60);
        task1.setStartTime("11.04.2022|12:00");

        task2.setDuration(60);
        task2.setStartTime("11.04.2022|14:00");

        client.addOrUpdateTask(task1);
        client.addOrUpdateTask(task2);

        String expectedTasks = gson.toJson(server.getPrioritizedTasks());
        String actualTasks = client.getPrioritizedTasksToString();

        Assertions.assertEquals(expectedTasks, actualTasks);
    }
}