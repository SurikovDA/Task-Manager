
import managers.Managers;
import managers.task.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;


/**
 * Теститорование программы
 */

public class Main {
    public static void main(String[] args) {

        //Создание объекта менеджера:
        TaskManager manager = Managers.getDefault();
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
        subtask2Epic1.setDuration(120);
        subtask2Epic1.setStartTime("30.03.2023|11:40");
        System.out.println("\n");

        //Добавляем подзадачу в список подзадач 2 эпика
        System.out.println("Добавляем в список подзадач 2 эпика: ");
        manager.createNewSubtask(subtask1Epic2, epic2);
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

        //Удаляем все задачи
        manager.deleteAllTasks();
        System.out.println(manager.findTaskById(task1.getId()));
        System.out.println("\n");


    }
}
