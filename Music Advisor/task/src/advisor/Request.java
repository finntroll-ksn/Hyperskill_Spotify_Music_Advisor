package advisor;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

class Request {
    private HttpRequest request;
    private static Request req;

    private Request() {
    }

    static Request getInstance() {
        if (req == null) {
            req = new Request();
        }

        return req;
    }

    void webGetToken() {
        request = HttpRequest.newBuilder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .uri(URI.create(Config.SERVER_PATH + "/api/token"))
                .POST(HttpRequest.BodyPublishers.ofString(
                        "grant_type=authorization_code" +
                                "&code=" + Config.AUTH_CODE +
                                "&client_id=" + Config.CLIENT_ID +
                                "&client_secret=" + Config.CLIENT_SECRET +
                                "&redirect_uri=" + Config.RETURN_URI
                ))
                .build();

        try {
            HttpClient client = HttpClient.newBuilder().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response != null && response.body().contains("access_token")) {
                parseAccessToken(response.body());
            }

        } catch (InterruptedException | IOException e) {
            System.out.println("Error response");
        }
    }

    private void parseAccessToken(String body) {
        JsonObject jo = JsonParser.parseString(body).getAsJsonObject();
        Config.ACCESS_TOKEN = jo.get("access_token").getAsString();
    }
}