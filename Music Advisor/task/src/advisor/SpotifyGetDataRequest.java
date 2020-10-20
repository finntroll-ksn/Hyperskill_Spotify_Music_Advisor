package advisor;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

class SpotifyGetDataRequest {
    private HttpRequest getRequest(String path) {
        return HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + Config.ACCESS_TOKEN)
                .uri(URI.create(path))
                .GET()
                .build();

    }

    private List<SpotifyResponseData> parsePlaylists(HttpResponse<String> response) {
        JsonObject jo = JsonParser.parseString(response.body()).getAsJsonObject();
        JsonObject playlists = jo.getAsJsonObject("playlists");
        List<SpotifyResponseData> data = new ArrayList<>();

        for (JsonElement item : playlists.getAsJsonArray("items")) {
            SpotifyResponseData elem = new SpotifyResponseData();

            elem.setAlbum(item.getAsJsonObject().get("name").toString());
            elem.setLink(item.getAsJsonObject().get("external_urls").getAsJsonObject().get("spotify").toString());

            data.add(elem);
        }

        return data;
    }

    List<SpotifyResponseData> getCategories() {
        String path = Config.API_PATH + "/v1/browse/categories";
        List<SpotifyResponseData> data = new ArrayList<>();

        HttpRequest request = getRequest(path);

        try {
            HttpClient client = HttpClient.newBuilder().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            JsonObject jo = JsonParser.parseString(response.body()).getAsJsonObject();
            JsonObject categories = jo.getAsJsonObject("categories");

            for (JsonElement item : categories.getAsJsonArray("items")) {
                SpotifyResponseData elem = new SpotifyResponseData();
                elem.setCategory(item.getAsJsonObject().get("name").toString());
                data.add(elem);
            }

        } catch (InterruptedException | IOException e) {
            System.out.println("Error response");
        }

        return data;
    }

    List<SpotifyResponseData> getFeatured() {
        String path = Config.API_PATH + "/v1/browse/featured-playlists";
        HttpRequest request = getRequest(path);
        List<SpotifyResponseData> data = null;

        try {
            HttpClient client = HttpClient.newBuilder().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            data = parsePlaylists(response);
        } catch (InterruptedException | IOException e) {
            System.out.println("Error response");
        }

        assert data != null;

        return data;
    }

    List<SpotifyResponseData> getNew() {
        List<SpotifyResponseData> data = new ArrayList<>();
        String path = Config.API_PATH + "/v1/browse/new-releases";
        HttpRequest request = getRequest(path);

        try {
            HttpClient client = HttpClient.newBuilder().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            JsonObject jo = JsonParser.parseString(response.body()).getAsJsonObject();
            JsonObject categories = jo.getAsJsonObject("albums");

            for (JsonElement j : categories.getAsJsonArray("items")) {
                SpotifyResponseData elem = new SpotifyResponseData();

                elem.setAlbum(j.getAsJsonObject().get("name").toString()); // album name
                ArrayList<String> artistNames = new ArrayList<>();

                for (JsonElement name : j.getAsJsonObject().getAsJsonArray("artists")) {
                    artistNames.add(name.getAsJsonObject().get("name").getAsString());
                }

                elem.setArtists(artistNames.toString());
                elem.setLink(j.getAsJsonObject().get("external_urls").getAsJsonObject().get("spotify").toString());
                data.add(elem);
            }
        } catch (InterruptedException | IOException e) {
            System.out.println("Error response");
        }

        return data;
    }

    JsonObject getCategoriesForPlaylist() throws IOException, InterruptedException {

        HttpRequest request = getRequest(Config.API_PATH + "/v1/browse/categories");
        HttpClient client = HttpClient.newBuilder().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return JsonParser.parseString(response.body()).getAsJsonObject();

    }


    List<SpotifyResponseData> getPlaylist(String categoryName) throws Exception {

        String categoryID = "";

        var json = getCategoriesForPlaylist();

        for (int i = 0; i < json.size(); i++) {
            var cat = json.get("categories").getAsJsonObject().get("items").getAsJsonArray();

            for (var element : cat) {
                var category = element.getAsJsonObject();

                if (categoryName.equals(category.get("name").getAsString())) {
                    categoryID = category.get("id").getAsString();
                    break;
                }
            }

        }

        String path = Config.API_PATH + "/v1/browse/categories/" + categoryID + "/playlists";
        HttpRequest request = getRequest(path);
        List<SpotifyResponseData> data = new ArrayList<>();

        try {
            HttpClient client = HttpClient.newBuilder().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.body().contains("404")) {
                System.out.println(response.body());
            } else {
                data = parsePlaylists(response);
            }
        } catch (InterruptedException | IOException e) {
            System.out.println("Error response");
        }

        return data;
    }
}
