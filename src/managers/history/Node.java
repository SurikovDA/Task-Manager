package managers.history;

import tasks.Task;

//Класс реализации узла
class Node {
    Task data;
    Node next;
    Node prev;

    //Конструктор узла
    public Node(Node prev, Task data, Node next) {
        this.data = data;
        this.next = next;
        this.prev = prev;
    }
}
