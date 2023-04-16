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
    private final Managers managers = new Managers();
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
        taskManager = managers.getHttpTaskManager(uri);
        task = new Task("Task1", "task");
        epic = new Epic("Epic1", "epic");
        subtask = new Subtask("Subtask1", "subtask");
    }

    @AfterEach
    void serverStop() {
        kvServer.stop();
    }

    @Test
    void loadFromServerTest() {

        taskManager.createNewTask(task);

        taskManager.createNewEpic(epic);

        taskManager.createNewSubtask(subtask, epic);

        taskManager.createNewSubtask(
                new Subtask("Subtask2", "Subtask2D"), epic);



        taskManager.getAllTasks();
        taskManager.getAllEpics();
        taskManager.getAllSubtasks();

        ((HTTPTaskManager)taskManager).load("task");
        ((HTTPTaskManager)taskManager).load("epic");
        ((HTTPTaskManager)taskManager).load("subtask");
        ((HTTPTaskManager)taskManager).loadAll();

        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllSubtasks());

    }
}