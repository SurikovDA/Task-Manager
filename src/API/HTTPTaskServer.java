package API;

import managers.task.FileBackedTasksManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import gson.deserialize.DurationJsonDeserializer;
import gson.deserialize.EpicJsonDeserializer;
import gson.deserialize.LocalDateTimeJsonDeserializer;
import gson.deserialize.SubtaskJsonDeserializer;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;

public class HTTPTaskServer extends FileBackedTasksManager {
    private static final int PORT = 8080;
    private static final String PATH_FILE = "src/resources/history.csv";

    private HttpServer server;
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Subtask.class, new SubtaskJsonDeserializer())
            .registerTypeAdapter(Epic.class, new EpicJsonDeserializer())
            .registerTypeAdapter(Duration.class, new DurationJsonDeserializer())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeJsonDeserializer())
            .create();

    public HTTPTaskServer() {
        super(PATH_FILE);
    }

    public void startServer() {
        try {
            server = HttpServer.create();
            server.bind(new InetSocketAddress(PORT), 0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //methods Tasks
        workWithTasks();
        //subtask methods
        workWithSubtask();
        //epic methods
        workWithEpics();
        //get EpicSubTasks(id)
        workWithMethodGetEpicSubTasks();
        //getHistory
        workWithMethodGetHistory();
        //getPrioritizedTasks
        workWithMethodGetPrioritizedTasks();
        server.start();
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

    public void stop() {
        System.out.println("Завершение работы сервера на порту " + PORT);
        System.out.println("Не работает в браузере http://localhost:" + PORT + "/");
        server.stop(0);
    }

    private void workWithTasks() {
        server.createContext("/tasks/task", (httpExchange) -> {
            System.out.println("Началась обработка /tasks/task запроса от клиента.");
            String method = httpExchange.getRequestMethod();
            String response = null;
            String idInfo = null;
            switch (method) {
                case "GET" : {
                    idInfo = httpExchange.getRequestURI().getRawQuery();
                    //if id exists
                    if (idInfo != null) {
                        int idTask = getIdValue(idInfo);
                        response = gson.toJson(findTaskById(idTask));
                    } else {
                        response = gson.toJson(getAllTasks());
                    }
                    printResponse(response, httpExchange);
                    break;
                }
                case "POST" : {
                    InputStream inputStream = httpExchange.getRequestBody();
                    String jsonString = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                    Task newTask = gson.fromJson(jsonString, Task.class);
                    if (tasks.containsKey(newTask.getId())) {
                        updateTask(newTask.getId(), newTask);
                        System.out.println("Успешно обновлена задача id = " + newTask.getId());
                        response = gson.toJson(findTaskById(newTask.getId()));
                    } else {
                        createNewTask(newTask);
                        System.out.println("Успешно добавлена задача id = " + newTask.getId());
                        response = gson.toJson(findTaskById(newTask.getId()));
                    }
                    printResponse(response, httpExchange);
                    break;
                }
                case "DELETE" : {
                    idInfo = httpExchange.getRequestURI().getRawQuery();
                    if (idInfo != null) {
                        int idTask = getIdValue(idInfo);
                        deleteTaskById(idTask);
                        response = gson.toJson(getAllTasks());
                    } else {
                        deleteAllTasks();
                        response = gson.toJson(getAllTasks());
                    }
                    printResponse(response, httpExchange);
                    break;
                }
            }

        });
    }

    private void workWithSubtask() {
        server.createContext("/tasks/subtask", (httpExchange) -> {
            System.out.println("Началась обработка /tasks/subtask запроса от клиента.");
            String method = httpExchange.getRequestMethod();
            String response = null;
            String idInfo = null;

            switch (method) {
                case "GET" : {
                    idInfo = httpExchange.getRequestURI().getRawQuery();
                    //if id exists
                    if (idInfo != null) {
                        int idSubtask = getIdValue(idInfo);
                        response = gson.toJson(findSubtaskById(idSubtask));
                    }
                    printResponse(response, httpExchange);
                    break;
                }
                case "POST" : {
                    InputStream inputStream = httpExchange.getRequestBody();
                    String jsonString = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                    Subtask newSubtask = gson.fromJson(jsonString, Subtask.class);
                    addEpicInSubtaskById(newSubtask, newSubtask.getEpic().getId());
                    if (subtasks.containsKey(newSubtask.getId())) {
                        updateSubtaskById(newSubtask.getId(), newSubtask);
                        System.out.println("Успешно обновлена подзадача id = " + newSubtask.getId());
                        response = gson.toJson(findSubtaskById(newSubtask.getId()));
                    } else {
                        createNewSubtask(newSubtask, newSubtask.getEpic());
                        System.out.println("Успешно добавлена подзадача id = " + newSubtask.getId());
                        response = gson.toJson(findSubtaskById(newSubtask.getId()));
                    }
                    printResponse(response, httpExchange);
                    break;
                }
                case "DELETE" : {
                    idInfo = httpExchange.getRequestURI().getRawQuery();
                    if (idInfo != null) {
                        int idSubtask = getIdValue(idInfo);
                        deleteSubtaskById(idSubtask);
                        response = gson.toJson(getAllSubtasks());
                    } else {
                        deleteAllSubtasks();
                        response = gson.toJson(getAllSubtasks());
                    }
                    printResponse(response, httpExchange);
                    break;
                }
            }
        });
    }

    private void workWithEpics() {
        server.createContext("/tasks/epic", (httpExchange) -> {
            System.out.println("Началась обработка /tasks/epic запроса от клиента.");
            String method = httpExchange.getRequestMethod();
            String response = null;
            String idInfo = null;
            switch (method) {
                case "GET" : {
                    idInfo = httpExchange.getRequestURI().getRawQuery();
                    //if id exists
                    if (idInfo != null) {
                        int idEpic = getIdValue(idInfo);
                        response = gson.toJson(findEpicById(idEpic));
                    } else
                        response = gson.toJson(getAllEpics());
                    printResponse(response, httpExchange);
                    break;
                }
                case "POST" : {
                    InputStream inputStream = httpExchange.getRequestBody();
                    String jsonString = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                    Epic newEpic = gson.fromJson(jsonString, Epic.class);
                    if (epics.containsKey(newEpic.getId())) {
                        addSubtasksInEpic(newEpic);
                        updateEpicById(newEpic.getId(), newEpic);
                        System.out.println("Успешно обновлен эпик id = " + newEpic.getId());
                        response = gson.toJson(newEpic);
                    } else {
                        createNewEpic(newEpic);
                        System.out.println("Успешно добавлен эпик id = " + newEpic.getId());
                        response = gson.toJson(getEpicById(newEpic.getId()));
                    }
                    printResponse(response, httpExchange);
                    break;
                }
                case "DELETE" : {
                    idInfo = httpExchange.getRequestURI().getRawQuery();
                    if (idInfo != null) {
                        int idEpic = getIdValue(idInfo);
                        deleteEpicById(idEpic);
                        response = gson.toJson(getAllEpics());
                    } else {
                        deleteAllEpics();
                        response = gson.toJson(getAllEpics());
                    }
                    printResponse(response, httpExchange);
                    break;
                }
            }
        });
    }

    private void workWithMethodGetEpicSubTasks() {
        server.createContext("/tasks/subtask/epic", (httpExchange) -> {
            System.out.println("Началась обработка /tasks/subtask/epic запроса от клиента.");
            String method = httpExchange.getRequestMethod();
            String response = null;
            String idInfo = null;
            if (method.equals("GET")) {
                idInfo = httpExchange.getRequestURI().getRawQuery();
                //if id exists
                if (idInfo != null) {
                    int idEpic = getIdValue(idInfo);
                    //возращает подзадачи у эпика по id эпика
                    response = gson.toJson(getAllSubtasksById(idEpic));
                }
                printResponse(response, httpExchange);
            }
        });
    }

    private void workWithMethodGetHistory() {
        server.createContext("/tasks/history", (httpExchange) -> {
            System.out.println("Началась обработка /tasks/history запроса от клиента.");
            String method = httpExchange.getRequestMethod();
            String response = null;
            if (method.equals("GET")) {
                response = gson.toJson(getHistory());
            } else {
                response = gson.toJson(String.format("Запрос %s не обрабатывается", method));
            }
            printResponse(response, httpExchange);
        });
    }

    private void workWithMethodGetPrioritizedTasks() {
        server.createContext("/tasks", (httpExchange) -> {
            System.out.println("Началась обработка /tasks запроса от клиента.");
            String method = httpExchange.getRequestMethod();
            String response = null;
            if (method.equals("GET")) {
                response = gson.toJson(getPrioritizedTasks());
            } else {
                response = gson.toJson(String.format("Запрос %s не обрабатывается", method));
            }
            printResponse(response, httpExchange);
        });
    }

    private int getIdValue(String idInfo) {
        String[] idInfoSplit = idInfo.split("=");
        return Integer.parseInt(idInfoSplit[1]);
    }

    private void printResponse(String response, HttpExchange httpExchange) {
        try {
            if (response != null) {
                httpExchange.sendResponseHeaders(200, 0);
                try (OutputStream os = httpExchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            } else {
                httpExchange.sendResponseHeaders(400, 0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}