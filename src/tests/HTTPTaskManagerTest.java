package tests;

import API.HTTPTaskServer;
import KVServer.KVServer;
import managers.task.HTTPTaskManager;
import managers.Managers;
import managers.task.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class HTTPTaskManagerTest {
    //private final Managers managers = new Managers();
    //private final KVServer kvServer = new KVServer();


    private TaskManager taskManager;
    private Task task;
    private Subtask subtask;
    private Epic epic;

    protected KVServer kvServer;
    /*
    @BeforeEach
    public void loadInitialConditions() throws IOException {

        kvServer = new KVServer();
        kvServer.start();

       Managers manager = new Managers();

    }*/

    @BeforeEach
    void start()throws IOException {
        kvServer = new KVServer();
        kvServer.start();
        String uri = "http://localhost:8088";
        taskManager = new HTTPTaskManager(uri);
        task = new Task("Test", "Test1");
        epic = new Epic("Eat", "soup");
        subtask = new Subtask("cook", "soup");
    }

    @AfterEach
    void serverStop() {
        kvServer.stop();
    }

    @Test
    void test1_shouldSavedTaskToServer() {
        task.setDuration(60);
        task.setStartTime("09.04.2022|13:00");
        taskManager.createNewTask(task);
        Assertions.assertEquals(
                task,
                ((HTTPTaskManager) taskManager).load(String.valueOf(task.getId())),
                "Задачи различаются");
    }

    @Test
    void test2_shouldSavedSubtaskToServer() {
        taskManager.createNewEpic(epic);
        taskManager.createNewSubtask(subtask, epic);
        Task task = ((HTTPTaskManager) taskManager).load(String.valueOf(subtask.getId()));
        Assertions.assertEquals(
                subtask.toString(),
                ((HTTPTaskManager) taskManager).load(String.valueOf(subtask.getId())).toString(),
                "Подзадачи разные ");

    }

    @Test
    void test3_shouldSavedEpicToServer() {
        taskManager.createNewEpic(epic);
        taskManager.createNewSubtask(subtask, epic);
        Assertions.assertEquals(
                epic.toString(),
                ((HTTPTaskManager) taskManager).load(String.valueOf(epic.getId())).toString(),
                "Эпики разные"
        );

    }
}