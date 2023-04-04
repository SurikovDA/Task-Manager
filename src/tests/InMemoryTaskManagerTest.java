package tests;

import managers.task.InMemoryTaskManager;
import managers.task.TaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.List;


public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    private InMemoryTaskManager taskManager;
    private final String startTime = "03.04.2023|13:00";
    Epic epic;
    Subtask subtask1;
    Subtask subtask2;

    private Task test1;

    public InMemoryTaskManagerTest() {
        super(new InMemoryTaskManager());
    }

    @BeforeEach
    void createInMemoryTaskManager() {
        taskManager = new InMemoryTaskManager();
    }

    void initEpicAndSubtasks() {
        epic = new Epic("testEpic");
        subtask1 = new Subtask("test1");
        subtask2 = new Subtask("test2");
    }

    void initTask() {
        test1 = new Task("test1");
        test1.setStartTime(startTime);
        test1.setDuration(60);
    }

    @Test
    void test1_shouldReturnLocalDateTimeForTask() {
        initTask();
        Assertions.assertEquals("03.04.2023|14:00", test1.getEndTime().format(Task.formatter));
    }

    @Test
    void test2_shouldReturnLocalDateTimeForSubtask() {
        initTask();
        Assertions.assertEquals("03.04.2023|14:00", test1.getEndTime().format(Task.formatter));
    }

    @Test
    void test3_shouldReturnLocalDateTimeForEpic_WhenSubtasksStartAtSameTime() {
        //проверка корректности времени, когда подзадачи у эпика начинаются в одно время
        TaskManager taskManager = new InMemoryTaskManager();

        initEpicAndSubtasks();
        //Назначаем продолжительность подзадач
        subtask1.setDuration(60);
        subtask2.setDuration(120);
        //назначение начало работы
        subtask1.setStartTime("03.04.2023|13:00");
        subtask2.setStartTime("03.04.2023|14:05");
        //связываем эпики с подзадачами и наоборот
        taskManager.createNewEpic(epic);
        taskManager.createNewSubtask(subtask1, epic);
        taskManager.createNewSubtask(subtask2, epic);

        Assertions.assertEquals("03.04.2023|13:00", epic.getStartTime().format(Task.formatter));
        Assertions.assertEquals("03.04.2023|16:05", epic.getEndTime().format(Task.formatter));
        Assertions.assertEquals(180, epic.getDuration().toMinutes());
    }

    @Test
    void test4_shouldReturnLocalDateTimeForEpic_WhenSubtaskIsNotFullyDefinedInStartTime() {
        //проверка корректности времени, когда подзадачи у эпика начинаются в одно время
        TaskManager taskManager = new InMemoryTaskManager();

        initEpicAndSubtasks();
        //Назначаем продолжительность подзадач
        subtask1.setDuration(60);
        subtask2.setDuration(120);
        //назначение начало работы
        subtask1.setStartTime(startTime);
        //связываем эпики с подзадачами и наоборот
        taskManager.createNewEpic(epic);
        taskManager.createNewSubtask(subtask1, epic);
        taskManager.createNewSubtask(subtask2, epic);

        Assertions.assertEquals(startTime, epic.getStartTime().format(Task.formatter));
        Assertions.assertEquals("03.04.2023|14:00", epic.getEndTime().format(Task.formatter));
        Assertions.assertEquals(180, epic.getDuration().toMinutes());
    }

    @Test
    void test5_shouldReturnLocalDateTimeForEpic_WhenSubtaskIsNotFullyDefinedInDuration() {
        //проверка корректности времени, когда подзадачи у эпика начинаются в одно время
        String startTime = "03.04.2023|13:00";
        TaskManager taskManager = new InMemoryTaskManager();

        initEpicAndSubtasks();
        //Назначаем продолжительность подзадач
        subtask1.setDuration(60);
        //назначение начало работы
        subtask1.setStartTime(startTime);
        subtask2.setStartTime(startTime);
        //связываем эпики с подзадачами и наоборот
        taskManager.createNewEpic(epic);
        taskManager.createNewSubtask(subtask1, epic);
        taskManager.createNewSubtask(subtask2, epic);

        Assertions.assertEquals(startTime, epic.getStartTime().format(Task.formatter));
        Assertions.assertEquals("03.04.2023|14:00", epic.getEndTime().format(Task.formatter));
        Assertions.assertEquals(60, epic.getDuration().toMinutes());
    }

    @Test
    void test6_shouldReturnLocalDateTimeForEpic_WhenSubtasksStartAtDifferentTime() {
        //проверка корректности времени, когда подзадачи у эпика начинаются в разное время
        String startTimeForSubtask1 = "03.04.2023|12:00";
        String startTimeForSubtask2 = "03.04.2023|14:00";
        TaskManager taskManager = new InMemoryTaskManager();

        initEpicAndSubtasks();
        //Назначаем продолжительность подзадач
        subtask1.setDuration(60);
        subtask2.setDuration(120);
        //назначение начало работы
        subtask1.setStartTime(startTimeForSubtask1);
        subtask2.setStartTime(startTimeForSubtask2);
        //связываем эпики с подзадачами и наоборот
        taskManager.createNewEpic(epic);
        taskManager.createNewSubtask(subtask1, epic);
        taskManager.createNewSubtask(subtask2, epic);

        Assertions.assertEquals(startTimeForSubtask1, epic.getStartTime().format(Task.formatter));
        Assertions.assertEquals("03.04.2023|16:00", epic.getEndTime().format(Task.formatter));
        Assertions.assertEquals(180, epic.getDuration().toMinutes());
    }

    @Test
    void test8_shouldReturnSortedListByTime() {
        Task test1 = new Task("test1");
        test1.setStartTime("03.04.2023|17:00");

        Task test2 = new Task("test2");
        test2.setStartTime("03.04.2023|14:00");

        Task test3 = new Task("test3");
        test3.setStartTime("03.04.2023|11:00");

        Task test4 = new Task("test4");
        test4.setStartTime("03.04.2023|16:00");
        //шпион, т к не назначено время
        Task test5 = new Task("test5");

        taskManager.createNewTask(test1);
        taskManager.createNewTask(test2);
        taskManager.createNewTask(test3);
        taskManager.createNewTask(test4);
        taskManager.createNewTask(test5);
        Task[] expectedSortedTasks = new Task[]{test3, test2, test4, test1, test5};

        List<Task> tasks = taskManager.getPrioritizedTasks();
        Assertions.assertArrayEquals(
                expectedSortedTasks,
                tasks.toArray(Task[]::new),
                "не верная сортировка по времени");
    }

    @Test
    void test9_checkingIntersectionWhenThereIsIntersection() {
        Task test1 = new Task("test1");
        test1.setStartTime("03.04.2023|13:00");
        test1.setDuration(60);

        Task test2 = new Task("test2");
        test2.setStartTime("03.04.2023|14:01");
        test2.setDuration(60);

        Task test3 = new Task("test3");
        test3.setStartTime("03.04.2023|15:02");
        test3.setDuration(60);

        Task test4 = new Task("test4");
        test4.setStartTime("03.04.2023|16:03");
        test4.setDuration(60);

        //задача, которая пересекается с test4
        Task test5 = new Task("test5");
        test5.setStartTime("03.04.2023|16:07");
        test5.setDuration(60);

        taskManager.createNewTask(test1);
        taskManager.createNewTask(test2);
        taskManager.createNewTask(test3);
        taskManager.createNewTask(test4);
        taskManager.createNewTask(test5);

        Task[] expectedSortedTasks = new Task[]{test1, test2, test3, test4};

        List<Task> tasks = taskManager.getPrioritizedTasks();
        Assertions.assertArrayEquals(
                expectedSortedTasks,
                tasks.toArray(Task[]::new),
                "не верная сортировка при пересечении");
        Assertions.assertEquals(4, tasks.size(), "Не правильная обработка пересечения");
    }

    @Test
    void test10_checkingIntersectionWhenThereIsIntersection() {
        Task test1 = new Task("test1");
        test1.setStartTime("03.04.2023|13:00");
        test1.setDuration(60);

        Task test2 = new Task("test2");
        test2.setStartTime("03.04.2023|14:01");
        test2.setDuration(60);

        Task test3 = new Task("test3");
        test3.setStartTime("03.04.2023|15:02");
        test3.setDuration(60);

        Task test4 = new Task("test4");
        test4.setStartTime("03.04.2023|16:03");
        test4.setDuration(60);

        //задача, которая пересекается с test3
        Task test5 = new Task("test5");
        test5.setStartTime("03.04.2023|15:02");
        test5.setDuration(60);

        taskManager.createNewTask(test1);
        taskManager.createNewTask(test2);
        taskManager.createNewTask(test3);
        taskManager.createNewTask(test4);
        taskManager.createNewTask(test5);

        Task[] expectedSortedTasks = new Task[]{test1, test2, test3, test4};

        List<Task> tasks = taskManager.getPrioritizedTasks();
        Assertions.assertArrayEquals(
                expectedSortedTasks,
                tasks.toArray(Task[]::new),
                "не верная сортировка при пересечении");
        Assertions.assertEquals(4, tasks.size(), "Не правильная обработка пересечения");
    }
}