package sample.weather;

import javafx.util.Pair;
import sample.ChatterController;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class WeatherDownloader {

    private synchronized static File getFileFromResource(String fileName) throws URISyntaxException {

        ClassLoader classLoader = ChatterController.class.getClassLoader();
        URL resource = classLoader.getResource(fileName);

        if (resource == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return new File(resource.toURI());
        }

    }

    public static String getWeather() throws URISyntaxException, IOException, InterruptedException {
        File file = getFileFromResource("api-key.private");
        BufferedReader br = new BufferedReader(new FileReader(file));
        String apikey = br.readLine();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://community-open-weather-map.p.rapidapi.com/weather?q=Nisko%2Cpl&lat=0&lon=0&callback=test&id=2172797&lang=en&units=metric"))
                .header("x-rapidapi-host", "community-open-weather-map.p.rapidapi.com")
                .header("x-rapidapi-key", apikey)
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body().getClass());
        System.out.println(response.body());
        return response.body();

    }

    public static void test2(String json){
        int descriptionBegin = json.indexOf("\"description\":\"");
        int descriptionEnd = json.indexOf("icon")-3;
        String description = json.substring(descriptionBegin + "\"description\":\"".length(), descriptionEnd);
        String temp = json.substring(json.indexOf("temp\":")+6, json.indexOf("feels_like")-2);

        System.out.println(description);
        System.out.println(temp);

    }
    public static Pair<String, String> getDescriptionAndCurrentTemp() throws InterruptedException, IOException, URISyntaxException {
        String weather = getWeather();
        int descriptionBegin = weather.indexOf("\"description\":\"");
        int descriptionEnd = weather.indexOf("icon")-3;
        String description = weather.substring(descriptionBegin + "\"description\":\"".length(), descriptionEnd);
        String temp = weather.substring(weather.indexOf("temp\":")+6, weather.indexOf("feels_like")-2);
        Pair<String, String> weatherPair = new Pair<>(description, temp);
        return weatherPair;
    }
    public static void main(String[] args) throws IOException, URISyntaxException, InterruptedException {
        String json = getWeather();
        test2(json);
    }
}
