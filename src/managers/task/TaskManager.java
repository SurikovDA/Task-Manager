package managers.task;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;


public interface TaskManager {

    /**
     * Интерфейс InMemoryTaskManager
     */

    //Получение списка всех задач
    List<Task> getAllTasks();

    //Получение списка всех эпиков.
    List<Epic> getAllEpics();
    //Получение всех подзадач
    ArrayList<Subtask> getAllSubtasks();

    //Получение списка всех подзадач определённого эпика.
    List<Subtask> getAllSubtasksById(int idEpic);


    //Получение задачи любого типа по идентификатору.
    Task findTaskById(int id);


    Subtask findSubtaskById(int id);


    Epic findEpicById(int id);


    //Добавление новой задачи, эпика и подзадачи. Сам объект должен передаваться в качестве параметра.
    void createNewTask(Task task);

    //Создание новой подзадачи
    void createNewSubtask(Subtask subtask, Epic epic);

    void createNewEpic(Epic epic);

    //Обновление задачи любого типа по идентификатору. Новая версия объекта передаётся в виде параметра:

    //Обновление задачи:
    Task updateTask(int id, Task task);

    //Обновление подзадачи
    Subtask updateSubtaskById(int id, Subtask subtask);

    //Обновление эпика
    Epic updateEpicById(int id, Epic epic);

    //Удаление ранее добавленных задач — всех и по идентификатору.
    void deleteTaskById(int id);

    //Удаление подзадачи эпика по id
    void deleteSubtaskById(int id);

    //Удаление эпика по id
    void deleteEpicById(int id);

    //Удалить все задачи
    void deleteAllTasks();

    //Удаление подзадач
    void deleteAllSubtasks();

    //Удалить все эпики
    void deleteAllEpics();

    //Последние просмотренные пользователем задачи
    List<Task> getHistory();

    //возвращающий список задач и подзадач в отсортированном порядке по приоритету - то есть по startTime
    List<Task> getPrioritizedTasks();
}

