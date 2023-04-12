package clients;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gson.serialize.DurationJsonSerializer;
import gson.serialize.EpicJsonSerializer;
import gson.serialize.LocalDateTimeJsonSerializer;
import gson.serialize.SubtaskJsonSerializer;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

public class HTTPTaskClient {
    private final HttpClient client = HttpClient.newHttpClient();
    private final String uri;
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Subtask.class, new SubtaskJsonSerializer())
            .registerTypeAdapter(Epic.class, new EpicJsonSerializer())
            .registerTypeAdapter(Duration.class, new DurationJsonSerializer())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeJsonSerializer())
            .create();

    public HTTPTaskClient(String uri) {
        this.uri = uri;
    }

    //Task methods
    public String getTasksToString() {
        try {
            HttpRequest request = getRequest_GET(uri + "/task");
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (IOException | InterruptedException | URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getTaskByIdToString(int idTask) {
        try {
            HttpRequest request = getRequest_GET(uri + "/task?id=" + idTask);
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (IOException | InterruptedException | URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addOrUpdateTask(Task task) {
        String json = getJsonString(task);
        try {
            HttpRequest request = getRequest_POST(uri + "/task", json);
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void deleteTaskById(int idTask) {
        try {
            HttpRequest request = getRequest_DELETE(uri + "/task?id=" + idTask);
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void deleteAllTask() {
        try {
            HttpRequest request = getRequest_DELETE(uri + "/task");
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    //subtask methods
    public String getSubtaskByIdToString(int idSubtask) {
        try {
            HttpRequest request = getRequest_GET(uri + "/subtask?id=" + idSubtask);
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (IOException | InterruptedException | URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addOrUpdateSubtask(Subtask subtask, Epic epicSubtask) {
        //искусственно свяжем подзадачу с эпиком, чтобы не потерялись
        subtask.setEpic(epicSubtask);

        String json = getJsonString(subtask);
        try {
            HttpRequest request = getRequest_POST(uri + "/subtask", json);
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void deleteSubtaskById(int idSubtask) {
        try {
            HttpRequest request = getRequest_DELETE(uri + "/subtask?id=" + idSubtask);
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void deleteAllSubtask() {
        try {
            HttpRequest request = getRequest_DELETE(uri + "/subtask");
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    //epic methods
    public String getEpicsToString() {
        try {
            HttpRequest request = getRequest_GET(uri + "/epic");
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (IOException | InterruptedException | URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getEpicByIdToString(int idEpic) {
        try {
            HttpRequest request = getRequest_GET(uri + "/epic?id=" + idEpic);
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (IOException | InterruptedException | URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addOrUpdateEpic(Epic epic) {
        String json = getJsonString(epic);
        try {
            HttpRequest request = getRequest_POST(uri + "/epic", json);
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void deleteEpicById(int idEpic) {
        try {
            HttpRequest request = getRequest_DELETE(uri + "/epic?id=" + idEpic);
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void deleteAllEpic() {
        try {
            HttpRequest request = getRequest_DELETE(uri + "/epic");
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public String getEpicSubTasksToString(int id) {
        try {
            HttpRequest request = getRequest_GET(uri + "/subtask/epic?id=" + id);
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (IOException | InterruptedException | URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getHistoryToString() {
        try {
            HttpRequest request = getRequest_GET(uri + "/history");
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (IOException | InterruptedException | URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getPrioritizedTasksToString() {
        try {
            HttpRequest request = getRequest_GET(uri);
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (IOException | InterruptedException | URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    //Сериализация Task объекта в Json
    private String getJsonString(Task task) {
        if (task instanceof Subtask)
            return gson.toJson(task, Subtask.class);
        else if (task instanceof Epic)
            return gson.toJson(task, Epic.class);
        else
            return gson.toJson(task, Task.class);
    }

    private HttpRequest getRequest_GET(String uri) throws URISyntaxException {
        return HttpRequest
                .newBuilder()
                .uri(new URI(uri))
                .header("Accept", "text")
                .GET()
                .build();

    }

    private HttpRequest getRequest_POST(String uri, String json) throws URISyntaxException {
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        return HttpRequest
                .newBuilder()
                .uri(new URI(uri))
                .header("Accept", "text/json")
                .POST(body)
                .build();
    }

    private HttpRequest getRequest_DELETE(String uri) throws URISyntaxException {
        return HttpRequest
                .newBuilder()
                .uri(new URI(uri))
                .header("Accept", "text")
                .DELETE()
                .build();
    }
}
