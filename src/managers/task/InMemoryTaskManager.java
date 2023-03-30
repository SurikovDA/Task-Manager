package managers.task;

import managers.Managers;
import managers.history.HistoryManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.*;


/**
 * Класс для хранения задач и работы с ними
 */

public class InMemoryTaskManager implements TaskManager {
    /**
     * generationId - Уникальный номер, для присвоения задачам
     * tasks - HashMap для хранения простых задач
     * subtasks - HashMap для хранения подзадач
     * epics  - HashMap для хранения эпиков.
     */

    private int generationId = 0;
    //Создание таблиц с задачами
     final Map<Integer, Task> tasks = new HashMap<>(); //Таблица простых задач
     final Map<Integer, Subtask> subtasks = new HashMap<>(); //Таблица подзадач
     final Map<Integer, Epic> epics = new HashMap<>(); //Таблица Эпиков

    //История просмотренных задач
    HistoryManager taskHistory = Managers.getDefaultHistory();

    //Получение списка всех задач
    @Override
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    //Получение списка всех эпиков.
    @Override
    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    //Получение списка всех подзадач определённого эпика.
    @Override
    public ArrayList<Task> getAllSubtasks(int idEpic) {
        return new ArrayList<>(epics.get(idEpic).getSubtasks());
    }

    //Получение задачи любого типа по идентификатору.
    @Override
    public Task findTaskById(int id) {
        //добавление в историю просмотренных задач
        taskHistory.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public Subtask findSubtaskById(int id) {
        //добавление в историю просмотренных задач
        taskHistory.add(subtasks.get(id));
        return subtasks.get(id);
    }

    @Override
    public Epic findEpicById(int id) {
        //добавление в историю просмотренных задач
        taskHistory.add(epics.get(id));
        return epics.get(id);
    }

    //Добавление новой задачи, эпика и подзадачи. Сам объект должен передаваться в качестве параметра.
    @Override
    public void createNewTask(Task task) {
        if (task.getId() != 0)
            System.out.println("Пользователю нельзя передавать id задачи! id устанавливается автоматически");
        else {
            int idTask = ++generationId;
            //Присваиваем индивидуальное id к объекту
            task.setId(idTask);
            //Добавляем в таблицу
            tasks.put(idTask, task);
            System.out.println("Успешно создана задача;");
        }
    }

    //Создание новой подзадачи
    @Override
    public void createNewSubtask(Subtask subtask, Epic epic) {
        if (subtask.getId() != 0)
            System.out.println("Такая подзадача уже существует, ее id = " + subtask.getId());
        else {
            int idSubtask = ++generationId;
            //Присваиваем индивидуальный id объекту
            subtask.setId(idSubtask);
            //Привязываем к подзадаче ссылку на эпик
            subtask.setEpic(epic);
            //Добавляем в таблицу
            subtasks.put(idSubtask, subtask);
            //По id эпика добавим в массив объект Subtask
            epics.get(epic.getId()).getSubtasks().add(subtask);
            epics.get(epic.getId()).setStatusEpic();

            System.out.println("Успешно создана подзадача;");
        }
    }

    @Override
    public void createNewEpic(Epic epic) {
        if (epic.getId() != 0)
            System.out.println("Такой эпик уже существует, ее id = " + epic.getId());
        else {
            int idEpic = ++generationId;
            //Присваиваем индивидуальное id к объекту
            epic.setId(idEpic);
            epics.put(idEpic, epic);
            System.out.println("Успешно создан эпик;");
        }
    }

    //Обновление задачи любого типа по идентификатору. Новая версия объекта передаётся в виде параметра:

    //Обновление задачи:
    @Override
    public Task updateTask(Task task) {

        tasks.put(task.getId(), task);
        System.out.println("задача по id " + task.getId() + " успешно обновлена");
        return tasks.get(task.getId());
    }

    //Обновление подзадачи
    @Override
    public Subtask updateSubtaskById(int id, Subtask subtask) {
        if (subtasks.containsKey(id)) {
            Subtask value = subtasks.get(id);
            //Если в объекте subtask обновленная ссылка на epic, то мы меняем ссылку и обновляем массив
            if (!value.getEpic().equals(subtask.getEpic()) && subtask.getEpic() != null) {
                //По старому id удалили подзадачу из старого списка эпика
                epics.get(value.getEpic().getId()).getSubtasks().remove(value);
                //По новому id добавили подзадачу в список эпика
                epics.get(subtask.getEpic().getId()).getSubtasks().add(subtask);
                //Обновляем ссылку на эпик в объекте подзадача
                value.setEpic(subtask.getEpic());
            }
            //обновление объекта
            value.setName(subtask.getName());
            value.setDescriptionTask(subtask.getDescriptionTask());
            value.setStatus(subtask.getStatus());
            System.out.println("Подзадача по id = " + id + " успешно обновлено");
            //Устанавливаем статус эпика
            epics.get(value.getEpic().getId()).setStatusEpic();
            return value;
        } else {
            System.out.println("По id = " + id + " такой подзадачи не существует;");
            return null;
        }
    }

    //Обновление эпика
    @Override
    public Epic updateEpicById(int id, Epic epic) {
        if (epics.containsKey(id)) {
            Epic value = epics.get(id);
            //обновление объекта
            epics.put(value.getId(), epic);
            epics.get(value.getId()).setStatusEpic();
            System.out.println("Эпик по id = " + id + " успешно обновлено");
            return epics.get(value.getId());
        } else {
            System.out.println("По id = " + id + " такого эпика не существует;");
            return null;
        }
    }

    //Удаление ранее добавленных задач — всех и по идентификатору.
    @Override
    public void deleteTaskById(int id) {
        //удаляем из списка задач определенную задачу по id
        tasks.remove(id);
        //удаление из истории задач
        taskHistory.remove(id);
        System.out.println("Успешное удаление Задачи id = " + id);
    }

    //Удаление подзадачи эпика по id
    @Override
    public void deleteSubtaskById(int id) {
        //Удаляем у списка Эпика подзадачу
        epics.get(subtasks.get(id).getEpic().getId()).getSubtasks().remove(subtasks.get(id));
        //удаляем из таблицы подзадач определенную подзадачу по id
        subtasks.remove(id);
        //Удаление из истории задач
        taskHistory.remove(id);
        epics.get(subtasks.get(id).getEpic().getId()).setStatusEpic();
        System.out.println("Успешное удаление Подзадачи id = " + id);
    }

    //Удаление эпика по id
    @Override
    public void deleteEpicById(int id) {
        //удалить подзадачи эпика из HashMap
        for (Subtask subtask : epics.get(id).getSubtasks()) {
            //удаление подзадач из истории
            taskHistory.remove(subtask.getId());
            //удаление подзадачи
            subtasks.remove(subtask.getId());
        }
        //Удалить из HashMap эпик по id
        epics.remove(id);
        //Удаление из истории просмотров
        taskHistory.remove(id);
        System.out.println("Успешное удаление Эпика id = " + id);
    }

    //Удалить все задачи
    @Override
    public void deleteAllTasks() {
        ArrayList<Integer> idTasks = new ArrayList<>(tasks.keySet());
        //Удаляем в цикле все задачи по id
        for (Integer idTask : idTasks) {
            deleteTaskById(idTask);
        }

        System.out.println("Все задачи удалены");
    }

    //Удаление подзадач
    @Override
    public void deleteAllSubtasks() {
        ArrayList<Integer> idSubtasks = new ArrayList<>(subtasks.keySet());
        //Удаляем в цикле все задачи по id в списке
        for (Integer idSubtask : idSubtasks) {
            deleteSubtaskById(idSubtask);
            epics.get(subtasks.get(idSubtask).getEpic().getId()).setStatusEpic();
        }

        System.out.println("Все подзадачи удалены");
    }

    //Удалить все эпики
    @Override
    public void deleteAllEpics() {
        ArrayList<Integer> idEpics = new ArrayList<>(epics.keySet());
        //Удаляем в цикле все задачи по id
        for (Integer idEpic : idEpics) {
            deleteEpicById(idEpic);
        }

        System.out.println("Все эпики удалены");
    }

    //Получение истории задач
    @Override
    public List<Task> getHistory() {
        return taskHistory.getHistory();
    }
}
