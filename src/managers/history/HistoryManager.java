package managers.history;


import tasks.Task;

import java.util.List;

public interface HistoryManager {
    /**
     * Интерфейс менеджера хранения истории
     */
    //для добавления нового просмотра задачи
    void add(Task task);

    //для удаления задачи из просмотров
    void remove(int id);

    //для получения истории последних просмотров
    List<Task> getHistory();
}
