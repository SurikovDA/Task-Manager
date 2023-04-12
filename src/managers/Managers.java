package managers;

import managers.history.HistoryManager;
import managers.history.InMemoryHistoryManager;
import managers.task.FileBackedTasksManager;
import managers.task.HTTPTaskManager;
import managers.task.InMemoryTaskManager;
import managers.task.TaskManager;

import java.io.IOException;
import java.net.URISyntaxException;

public class Managers {
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public TaskManager getHttpTaskManager(String uri)  {
        try {
            return new HTTPTaskManager(uri);
        } catch (URISyntaxException | IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public TaskManager getFileBackedTasksManager(String path) {
        return new FileBackedTasksManager(path);

    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
