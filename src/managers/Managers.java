package managers;

import exceptions.ManagerSaveException;
import managers.history.HistoryManager;
import managers.history.InMemoryHistoryManager;
import managers.task.FileBackedTasksManager;
import managers.task.HTTPTaskManager;
import managers.task.InMemoryTaskManager;
import managers.task.TaskManager;

import java.io.IOException;
import java.net.URISyntaxException;

public class Managers {
    public static TaskManager getDefault(String uri) {
        return new HTTPTaskManager(uri);
    }

    public TaskManager getHttpTaskManager(String uri)  {
            return new HTTPTaskManager(uri);
    }

    public static TaskManager getFileBackedTasksManager(String path) {
        return new FileBackedTasksManager(path);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
