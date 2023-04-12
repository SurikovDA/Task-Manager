package clients;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private final HttpClient client = HttpClient.newHttpClient();
    private final String uri;
    private final String API_KEY;

    public KVTaskClient(String uri) throws IOException, InterruptedException, URISyntaxException {
        this.uri = uri;
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(new URI(this.uri + "/register"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        API_KEY = response.body();
    }

    //сохраняет состояние менеджера задач через запрос
    public void put(String key, String json) {
        try {
            HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
            HttpRequest request = HttpRequest
                    .newBuilder()
                    .uri(new URI(uri + "/save/" + key + "?API_KEY=" + API_KEY))
                    .POST(body)
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (URISyntaxException | IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    //возвращает состояние менеджера задач через запрос
    public String load(String key) {
        try {
            HttpRequest request = HttpRequest
                    .newBuilder()
                    .uri(new URI(uri + "/load/" + key + "?API_KEY=" + API_KEY))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.body() != null)
                return response.body();
        } catch (URISyntaxException | IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
