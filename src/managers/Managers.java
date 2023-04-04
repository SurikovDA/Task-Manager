package managers;

import managers.history.HistoryManager;
import managers.history.InMemoryHistoryManager;
import managers.task.FileBackedTasksManager;
import managers.task.InMemoryTaskManager;
import managers.task.TaskManager;

public class Managers {
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public TaskManager getFileBackedTasksManager(String path) {
        return new FileBackedTasksManager(path);

    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
