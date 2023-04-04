package managers.task;

import exceptions.ManagerSaveException;
import managers.history.HistoryManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.Status;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class FileBackedTasksManager extends InMemoryTaskManager {
    protected final String path;
    public static final String START_LINE = "id,type,name,status,description,startTime,durationMinutes,epic\n";


    public FileBackedTasksManager(String path) {
        this.path = path;
    }

    @Override
    public ArrayList<Task> getAllTasks() {
        var allTasks = super.getAllTasks();
        save();
        return allTasks;
    }

    @Override
    public ArrayList<Epic> getAllEpics() {
        var allEpics = super.getAllEpics();
        save();
        return allEpics;
    }

    @Override
    public ArrayList<Task> getAllSubtasks(int idEpic) {
        var allSubtasks = super.getAllSubtasks(idEpic);
        save();
        return allSubtasks;
    }

    @Override
    public Task findTaskById(int id) {
        var task = super.findTaskById(id);
        save();
        return task;
    }

    @Override
    public Subtask findSubtaskById(int id) {
        var subtask = super.findSubtaskById(id);
        save();
        return subtask;
    }

    @Override
    public Epic findEpicById(int id) {
        Epic epic = super.findEpicById(id);
        save();
        return epic;
    }

    @Override
    public void createNewTask(Task task) {
        super.createNewTask(task);
        save();
    }

    @Override
    public void createNewSubtask(Subtask subtask, Epic epic) {
        super.createNewSubtask(subtask, epic);
        save();
    }

    @Override
    public void createNewEpic(Epic epic) {
        super.createNewEpic(epic);
        save();
    }

    @Override
    public Task updateTask(int id, Task task) {
        var newTask = super.updateTask(id, task);
        save();
        return newTask;
    }

    @Override
    public Subtask updateSubtaskById(int id, Subtask subtask) {
        var newSubtask = super.updateSubtaskById(id, subtask);
        save();
        return newSubtask;
    }

    @Override
    public Epic updateEpicById(int id, Epic epic) {
        var newEpic = super.updateEpicById(id, epic);
        save();
        return newEpic;
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteSubtaskById(int id) {
        super.deleteSubtaskById(id);
        save();
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public List<Task> getHistory() {
        var history = super.getHistory();
        save();
        return history;
    }

    // Возвращает эпик
    public Epic getEpicById(int id) {
        return epics.get(id);
    }

    //Сохраняет текущие состояние менеджера в указанный файл
    protected void save() {
        try (Writer fileWriterStart = new FileWriter(path)) {
            fileWriterStart.write(START_LINE);

            for (Task task : tasks.values()) {
                fileWriterStart.write(toString(task) + "\n");
            }
            for (Epic epic : epics.values()) {
                fileWriterStart.write(toString(epic) + "\n");
            }
            for (Subtask subtask : subtasks.values()) {
                fileWriterStart.write(toString(subtask) + "\n");
            }
            if (taskHistory.getHistory() != null)
                fileWriterStart.write(historyToString(taskHistory));
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка во время записи файла.");
        }
    }


    //Сохраняет задачу в строку
    private String toString(Task task) {
        String typeTask = task.getClass().getSimpleName();
        String result;
        String startTime;
        long durationMinutes;

        if (task.getStartTime() != null) {
            startTime = task.getStartTime().format(Task.formatter);
        } else {
            startTime = "null";
        }

        if (task.getDuration() != null) {
            durationMinutes = task.getDuration().toMinutes();
        } else {
            durationMinutes = 0;
        }

        if (task instanceof Subtask) {
            result = String.format("%d,%s,%s,%s,%s,%s,%d,%d",
                    task.getId(),
                    typeTask.toUpperCase(),
                    task.getName(),
                    task.getStatus(),
                    task.getDescriptionTask(),
                    startTime,
                    durationMinutes,
                    ((Subtask) task).getEpic().getId()
            );
        } else {
            result = String.format("%d,%s,%s,%s,%s,%s,%d",
                    task.getId(),
                    typeTask.toUpperCase(),
                    task.getName(),
                    task.getStatus(),
                    task.getDescriptionTask(),
                    startTime,
                    durationMinutes
            );
        }
        return result;
    }

    //Создание задачи из строки
    private Task fromString(String value) {
        /*parameters[0] - id,
         parameters[1] - type,
         parameters[2] - name,
         parameters[3] - status,
         parameters[4] - descriptionTask,
         parameters[5] - startTime,
         parameters[6] - duration,
         parameters[7] - epic
         */

        //var idEpic = Integer.parseInt(values[7]);
        var values = value.split(",");
        var id = Integer.parseInt(values[0]);
        var type = values[1];
        var name = values[2];
        var status = Status.valueOf(values[3]);
        var descriptionTask = values[4];


        if (TypeTask.valueOf(type).equals(TypeTask.TASK)) {
            Task task = new Task(id, name, descriptionTask, status);
            // инициализируем время и продолжительность
            if (!values[5].equals("null")) {
                task.setStartTime(values[5]);
            }
            if (!values[6].equals("0")) {
                task.setDuration(Long.parseLong(values[6]));
            }
            return task;
        }

        if (TypeTask.valueOf(type).equals(TypeTask.EPIC)) {
            Epic task = new Epic(id, name, descriptionTask, status);
            // инициализируем время и продолжительность
            if (!values[5].equals("null")) {
                task.setStartTime(values[5]);
            }
            if (!values[6].equals("0")) {
                task.setDuration(Long.parseLong(values[6]));
            }
            return task;
        }

        if (TypeTask.valueOf(type).equals(TypeTask.SUBTASK)) {
            int idEpic = Integer.parseInt(values[7]);
            Subtask task = new Subtask(id, name, descriptionTask, status, findEpicById(idEpic));
            // инициализируем время и продолжительность
            if (!values[5].equals("null")) {
                task.setStartTime(values[5]);
            }
            if (!values[6].equals("0")) {
                task.setDuration(Long.parseLong(values[6]));
            }
            return task;
        } else {
            throw new IllegalArgumentException("Данный формат таска не поддерживается");
        }
    }


    //Сохранение менеджера истории
    private static String historyToString(HistoryManager manager) {
        List<Task> tasksHistory = manager.getHistory();
        StringBuilder idHistory = new StringBuilder();
        idHistory.append("\n");
        for (int i = 0; i < tasksHistory.size(); i++) {
            //проверка на последний элемент массива
            if (i == tasksHistory.size() - 1)
                idHistory.append(String.format("%d", tasksHistory.get(i).getId()));
            else
                idHistory.append(String.format("%d,", tasksHistory.get(i).getId()));
        }

        return idHistory.toString();
    }

    //Восстановление менеджера истории из CSV
    private static List<Integer> historyFromString(String value) {
        List<Integer> historyId = new ArrayList<>();
        String[] idHistoryTasks = value.split(",");
        for (String id : idHistoryTasks) {
            historyId.add(Integer.parseInt(id));
        }
        return historyId;
    }

    //считывание информации из файла
    public void loadFromFile(String path) {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            while (br.ready()) {
                String line = br.readLine();
                if (!line.isBlank()) {
                    if (!line.equals("id,type,name,status,description,startTime,durationMinutes,epic")) {
                        Task task = fromString(line);
                        if (task instanceof Epic) {
                            epics.put(task.getId(), (Epic) task);
                        } else if (task instanceof Subtask) {
                            ((Subtask) task).getEpic().getSubtasks().add((Subtask) task);
                            subtasks.put(task.getId(), (Subtask) task);
                        } else {
                            tasks.put(task.getId(), task);
                        }
                    }
                } else {
                    String newLine = br.readLine();
                    List<Integer> idHistory = historyFromString(newLine);
                    Task task = null;
                    for (Integer id : idHistory) {
                        if (tasks.containsKey(id)) {
                            task = tasks.get(id);
                            taskHistory.add(task);
                        } else if (epics.containsKey(id)) {
                            task = epics.get(id);
                            taskHistory.add(task);
                        } else if (subtasks.containsKey(id)) {
                            task = subtasks.get(id);
                            taskHistory.add(task);
                        }
                    }
                    if (task != null)
                        taskHistory.add(task);
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка во время чтения файла.");
        }
    }

    //Проверка
    public static void main(String[] args) {
        //Создание объекта менеджера:
        TaskManager manager = new FileBackedTasksManager("src/resources/results.csv");

        //Создаем две задачи
        System.out.println("Создание 2х задач: ");
        Task task1 = new Task("Пройти теорию");
        Task task2 = new Task("Сделать проект");
        manager.createNewTask(task1);
        manager.createNewTask(task2);
        System.out.println("\n");

        //создаем первый эпик
        System.out.println("Создаем первый эпик: ");
        Epic removal = new Epic("Переезд");
        manager.createNewEpic(removal);
        System.out.println("\n");

        //Создаем подзадачи для первого эпика
        System.out.println("Создаем подзадачи для 1го эпика: ");
        Subtask subtask1Epic1 = new Subtask("Собрать коробки");
        Subtask subtask2Epic1 = new Subtask("Упаковать кошку");
        Subtask subtask3Epic1 = new Subtask("Сказать слова прощания");

        //Добавляем подзадачи в список подзадач к первому эпику
        System.out.println("Добавляем подзадачи в список подзадач к 1му эпику: ");
        manager.createNewSubtask(subtask1Epic1, removal);
        manager.createNewSubtask(subtask2Epic1, removal);
        manager.createNewSubtask(subtask3Epic1, removal);
        System.out.println("\n");

        //создаем второй эпик, у которого будет 1 подзадача
        System.out.println("Создаем 2й эпик: ");
        Epic epic2 = new Epic("Важный эпик 2");
        manager.createNewEpic(epic2);
        System.out.println("\n");

        //Создаем подзадачу для второго эпика
        System.out.println("Создаем подзадачу для 2го эпика: ");
        Subtask subtask1Epic2 = new Subtask("Задача1");
        System.out.println("\n");

        //Добавляем подзадачу в список подзадач 2 эпика
        System.out.println("Добавляем в список подзадач 2 эпика: ");
        manager.createNewSubtask(subtask1Epic2, epic2);
        subtask2Epic1.setDuration(120);
        subtask2Epic1.setStartTime("30.03.2023|11:40");
        System.out.println("\n");

        //Получение списка всех задач
        System.out.println("Список всех задач: " + manager.getAllTasks());

        //Получение списка всех эпиков
        System.out.println("Список всех эпиков: " + manager.getAllEpics());

        //Получение списка всех подзадач определённого эпика.
        System.out.println("Получение списка всех подзадач определенного эпика: ");

        System.out.println("Список всех подзадач " + manager.findEpicById(removal.getId()) + ": ");
        System.out.println(manager.getAllSubtasks(removal.getId()));

        System.out.println("Список всех подзадач " + manager.findEpicById(epic2.getId()) + ": ");
        System.out.println(manager.getAllSubtasks(epic2.getId()));
        System.out.println("\n");

        //Запрашиваем созданные задачи и проверяем историю
        System.out.println("Запрашиваем созданные задачи и проверяем историю: \n");

        System.out.println("Эпик: " + manager.findEpicById(removal.getId()));
        System.out.println(manager.getHistory());

        System.out.println("Подзадача: " + manager.findSubtaskById(subtask1Epic1.getId()));
        System.out.println(manager.getHistory());
        System.out.println("Подзадача: " + manager.findSubtaskById(subtask2Epic1.getId()));
        System.out.println(manager.getHistory());
        System.out.println("Подзадача: " + manager.findSubtaskById(subtask3Epic1.getId()));
        System.out.println(manager.getHistory());

        System.out.println("Задача: " + manager.findTaskById(task1.getId()));
        System.out.println(manager.getHistory());

        System.out.println("Эпик: " + manager.findEpicById(epic2.getId()));
        System.out.println(manager.getHistory());
        System.out.println("Задача: " + manager.findTaskById(task2.getId()));
        System.out.println(manager.getHistory());
        System.out.println("Подзадача: " + manager.findSubtaskById(subtask1Epic2.getId()));
        System.out.println(manager.getHistory());

        System.out.println("Проверка истории по вызову");

        System.out.println(manager.findEpicById(removal.getId()));
        System.out.println(manager.getHistory());
        System.out.println(manager.findSubtaskById(subtask1Epic2.getId()));
        System.out.println(manager.getHistory());
        System.out.println(manager.getHistory().size());


        //Удаляем задачу и убеждаемся, что она удалилась
        manager.deleteTaskById(task2.getId());
        System.out.println(manager.findTaskById(task2.getId()));
        System.out.println("\n");

       /*Удаляем эпик с тремя подзадачами и убеждаемся,
                 что удалился как сам эпик, так и всего его подзадачи*/
        //manager.deleteEpicById(removal.getId());
        System.out.println(manager.findTaskById(removal.getId()));
        System.out.println(manager.findSubtaskById(subtask1Epic1.getId()));
        System.out.println(manager.findSubtaskById(subtask2Epic1.getId()));
        System.out.println("\n");
        //Распечатать по приоритету
        System.out.println("Печать по приоритету:");
        for (Task task : manager.getPrioritizedTasks()) {
            System.out.println(task);
        }
        System.out.println("\n");

        FileBackedTasksManager secondManager = new FileBackedTasksManager("src/resources/results.csv");
        secondManager.loadFromFile("src/resources/results.csv");


        System.out.println(secondManager.getAllSubtasks(7));

        /*Удаляем все задачи*/
        secondManager.deleteAllTasks();
        System.out.println(secondManager.findTaskById(task1.getId()));
        System.out.println("\n");

        //Распечатать по приоритету
        System.out.println("Печать по приоритету:");
        for (Task task : secondManager.getPrioritizedTasks()) {
            System.out.println(task);
        }
    }
}


