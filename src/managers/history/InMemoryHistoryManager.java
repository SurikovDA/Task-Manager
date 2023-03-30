package managers.history;



import tasks.Task;

import java.util.*;


public class InMemoryHistoryManager implements HistoryManager {
    /**
     * Имплемент HistoryManager
     */
    //Мапа для хранения узлов
    Map<Integer, Node> tasksHistory = new HashMap<>();
    CustomLinkedList customLinkedList = new CustomLinkedList();

    //для добавления нового просмотра задачи
    @Override
    public void add(Task task) {
        if (task != null) {
            if (tasksHistory.containsKey(task.getId())) {
                customLinkedList.removeNode(tasksHistory.get(task.getId()));
            }
            Node node = customLinkedList.linkLast(task);
            tasksHistory.put(task.getId(), node);
        }
    }

    //для удаления задач из истории просмотров
    @Override
    public void remove(int id) {
        if (tasksHistory.containsKey(id)) {
            customLinkedList.removeNode(tasksHistory.get(id));
        }
        tasksHistory.remove(id);
    }

    //для получения истории последних просмотров
    @Override
    public List<Task> getHistory() {
        return customLinkedList.getTasks();
    }
}


class CustomLinkedList {
    /**
     * Класс реализиции двусвязного списка
     */
    //Указатель на первый элемент списка
    private Node first;
    //указатель на последний элемент списка
    private Node last;
    int size = 0;

    //Метод добавления задачи в конец списка
    public Node linkLast(Task element) {
        final Node oldLast = last;
        final Node newNode = new Node(oldLast, element, null);
        last = newNode;
        if (oldLast == null) {
            first = newNode;
        } else {
            oldLast.next = newNode;
        }
        size++;
        return newNode;
    }

    //удаление узла
    public void removeNode(Node node) {
        if (node.equals(first)) {
            first = node.next;
            if (node.next != null) {
                node.next.prev = null;
            }
        } else {
            node.prev.next = node.next;
            if (node.next != null) {
                node.next.prev = node.prev;
            }
        }
        size--;
    }


    //Собрать задачи в ArrayList
    public List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        Node element = first;
        while (element != null) {
            tasks.add(element.data);
            element = element.next;
        }
        return tasks;
    }
}



