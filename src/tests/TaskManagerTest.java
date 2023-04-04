package tests;

import managers.task.TaskManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import static org.junit.jupiter.api.Assertions.*;

abstract public class TaskManagerTest<T extends TaskManager> {
    private final T object;
    TaskManager taskManager;
    Epic epic;
    Subtask subtask;
    Task task;

    public TaskManagerTest(T object) {
        this.object = object;
    }

    @BeforeEach
    void createTaskManager() {
        taskManager = object;
        epic = new Epic("Помыть посуду");
        subtask = new Subtask("Подойти к раковине");
        task = new Task("test");
    }

    @Test
    void test1_shouldGetAllEpics() {
        taskManager.createNewEpic(epic);

        Epic[] expectedAllEpics = new Epic[]{epic};
        Epic[] allEpic = taskManager.getAllEpics().toArray(Epic[]::new);

        Assertions.assertArrayEquals(
                expectedAllEpics,
                allEpic,
                "Массивы не равны"
        );
    }

    @Test
    void test2_shouldFindTaskById() {
        taskManager.createNewTask(task);

        Assertions.assertEquals(task, taskManager.findTaskById(task.getId()));
    }

    @Test
    void test3_shouldFindSubtaskById() {
        taskManager.createNewEpic(epic);
        taskManager.createNewSubtask(subtask, epic);

        Assertions.assertEquals(subtask, taskManager.findSubtaskById(subtask.getId()));
    }

    @Test
    void test4_shouldFindEpicById() {
        taskManager.createNewEpic(epic);

        Assertions.assertEquals(epic, taskManager.findEpicById(epic.getId()));
    }

    @Test
    void test5_shouldCreateNewTask() {
        taskManager.createNewTask(task);

        Task[] expectedAllTasks = new Task[]{task};
        Task[] allTask = taskManager.getAllTasks().toArray(Task[]::new);

        Assertions.assertArrayEquals(
                expectedAllTasks,
                allTask,
                "Массивы не равны"
        );
    }

    @Test
    void test6_shouldDoNotCreateNewTask() {
        taskManager.createNewTask(task);
        //пробуем создать повторную задачу в менеджере
        taskManager.createNewTask(task);
        int expectedSize = 1;
        //проверка на размер словаря
        Assertions.assertEquals(
                expectedSize,
                taskManager.getAllTasks().size(),
                "Создался дубликат задачи"
        );
    }

    @Test
    void test7_shouldCreateNewSubtask() {
        taskManager.createNewEpic(epic);
        taskManager.createNewSubtask(subtask, epic);

        Subtask[] expectedAllSubtasks = new Subtask[]{subtask};
        Subtask[] allSubtasks = taskManager.getAllSubtasks(epic.getId()).toArray(Subtask[]::new);

        Assertions.assertArrayEquals(expectedAllSubtasks, allSubtasks, "Массивы не равны");
    }

    @Test
    void test8_shouldDoNotCreateNewSubtask() {
        taskManager.createNewEpic(epic);
        taskManager.createNewSubtask(subtask, epic);
        //пробуем создать повторную подзадачу в менеджере
        taskManager.createNewSubtask(subtask, epic);
        int expectedSize = 1;
        //проверка на размер словаря
        Assertions.assertEquals(
                expectedSize,
                taskManager.getAllSubtasks(epic.getId()).size(),
                "Создался дубликат подзадачи"
        );
    }

    @Test
    void test9_shouldCreateNewEpic() {
        taskManager.createNewEpic(epic);

        Epic[] expectedAllEpics = new Epic[]{epic};
        Epic[] allEpic = taskManager.getAllEpics().toArray(Epic[]::new);

        Assertions.assertArrayEquals(
                expectedAllEpics,
                allEpic,
                "Массивы не равны"
        );
    }

    @Test
    void test10_shouldDoNotCreateNewEpic() {
        taskManager.createNewEpic(epic);
        //пробуем создать повторный эпик в менеджере
        taskManager.createNewEpic(epic);
        int expectedSize = 1;
        //проверка на размер словаря
        Assertions.assertEquals(
                expectedSize,
                taskManager.getAllEpics().size(),
                "Создался дубликат эпика"
        );
    }

    @Test
    void test11_shouldUpdateTaskById() {
        taskManager.createNewTask(task);

        Task expectedTask = new Task("Убрать посуду");
        expectedTask.setId(task.getId());

        Assertions.assertEquals(expectedTask, taskManager.updateTask(task.getId(), new Task("Убрать посуду")));

    }

    @Test
    void test12_shouldDoNotUpdateTaskById() {
        taskManager.createNewTask(task);

        Assertions.assertNull(taskManager.updateTask(task.getId() + 1, new Task("Убрать посуду")));
    }

    @Test
    void test13_shouldUpdateSubtasksByIdWithoutEpic() {

        taskManager.createNewEpic(epic);
        taskManager.createNewSubtask(subtask, epic);

        Subtask expectedSubtask = new Subtask("Убрать посуду");
        //подготавливаем подзадачу
        expectedSubtask.setEpic(epic);
        expectedSubtask.setId(subtask.getId());

        //проверка без нового эпика
        Assertions.assertEquals(
                expectedSubtask,
                taskManager.updateSubtaskById(subtask.getId(), expectedSubtask),
                "Подзадачи разные"
        );
    }

    @Test
    void test14_shouldUpdateSubtasksByIdWithEpic() {
        taskManager.createNewEpic(epic);
        taskManager.createNewSubtask(subtask, epic);

        //новой эпик и подзадача, которые будут использоваться
        Epic cleanRoom = new Epic("Сдать проект");
        taskManager.createNewEpic(cleanRoom);
        //ожидаемый результат
        Subtask expectedSubtask = new Subtask("Приступить к работе");
        //связываем с эпиком подзадачу
        expectedSubtask.setEpic(cleanRoom);
        //подготавливаем подзадачу для сравнения
        expectedSubtask.setId(subtask.getId());
        //проверка с новым эпиком
        Assertions.assertEquals(
                expectedSubtask,
                taskManager.updateSubtaskById(subtask.getId(), expectedSubtask),
                "Подзадачи разные"
        );
    }

    @Test
    void test15_shouldDoNotUpdateSubtasksById() {
        taskManager.createNewEpic(epic);
        taskManager.createNewSubtask(subtask, epic);
        //проверка на null, если обновить не существующею подзадачу по id
        Assertions.assertNull(
                taskManager.updateSubtaskById(subtask.getId() + 1, new Subtask("Убрать посуду"))
        );
    }

    @Test
    void test16_shouldUpdateEpicById() {
        taskManager.createNewEpic(epic);

        Epic expectedEpic = new Epic("Убрать посуду");

        Assertions.assertEquals(
                expectedEpic,
                taskManager.updateEpicById(epic.getId(), new Epic("Убрать посуду"))
        );

    }

    @Test
    void test17_shouldDoNotUpdateEpicById() {
        taskManager.createNewEpic(epic);
        //проверка на null, если обновить не существующий эпик по id
        Assertions.assertNull(taskManager.updateEpicById(epic.getId() + 1, new Epic("Убрать посуду")));
    }

    @Test
    void test18_shouldDeleteTaskById() {
        //добавили задачу
        taskManager.createNewTask(task);
        //удаляем задачу
        taskManager.deleteTaskById(task.getId());
        //проверка
        Assertions.assertTrue(taskManager.getAllTasks().isEmpty());
    }

    @Test
    void test19_shouldDeleteSubtaskById() {
        //добавили эпик и подзадачу
        taskManager.createNewEpic(epic);
        taskManager.createNewSubtask(subtask, epic);
        //удаляем подзадачу
        taskManager.deleteSubtaskById(subtask.getId());
        //проверка, что удалился у эпика
        Assertions.assertTrue(epic.getSubtasks().isEmpty());
        //проверка, что удалился из словаря менеджера, с помощью обновления, который вернет null, если нет Subtask
        Assertions.assertNull(taskManager.updateSubtaskById(subtask.getId(), new Subtask("Выспаться")));
    }

    @Test
    void test20_shouldDeleteEpicById() {
        //добавили эпик и подзадачу
        taskManager.createNewEpic(epic);
        taskManager.createNewSubtask(subtask, epic);
        //удаляем эпик
        taskManager.deleteEpicById(epic.getId());
        //проверка, что в словаре менеджера эпика не осталось
        Assertions.assertTrue(taskManager.getAllEpics().isEmpty());
    }

    @Test
    void test20_shouldGetHistory() {
        taskManager.createNewTask(task);
        //добавим в историю task
        taskManager.findTaskById(task.getId());
        //ожидаемый результат
        Task[] expectedHistoryTasks = new Task[]{task};
        Task[] historyTasks = taskManager.getHistory().toArray(Task[]::new);
        //проверка
        Assertions.assertArrayEquals(expectedHistoryTasks, historyTasks);
    }
}