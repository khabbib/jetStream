package application.model;

import com.sun.javafx.fxml.BeanAdapter;
import com.sun.mail.iap.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class WeatherAPI {
    public static void main(String[] args) throws IOException, InterruptedException {
       WeatherAPI weatherAPI = new WeatherAPI();
       weatherAPI.weatherForecast("Stockholm");

    }

    public void weatherForecast(String city) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://community-open-weather-map.p.rapidapi.com/weather?q=" + city + "&lat=0&lon=0&callback=test&id=2172797&lang=null&units=metric&mode=xml"))
                .header("X-RapidAPI-Host", "community-open-weather-map.p.rapidapi.com")
                .header("X-RapidAPI-Key", "6df4a5018dmsh154dd0e84d60e4cp1fa50bjsn13abfed7560e")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        List<String> forecast = new ArrayList<String>();

        String responseString = response.body().replace("\"", " ");
        List<Integer> integers = new ArrayList<Integer>();
        for (int i = 0; i < responseString.length(); i++) {
            if (responseString.charAt(i) == ' ') {
                integers.add(i);
            }
        }

        System.out.println(responseString);

        if (responseString.length() > 120) {
            forecast.add(responseString.substring(integers.get(12) + 1, integers.get(13)));
            forecast.add(responseString.substring(integers.get(30) + 2, integers.get(31) - 1));
            forecast.add(responseString.substring(integers.get(32) + 2, integers.get(33) -1));
            forecast.add(responseString.substring(integers.get(34) + 2, integers.get(35) - 1));
            forecast.add(responseString.substring(integers.get(36) + 2, integers.get(37) - 1));

            System.out.println(city);
            System.out.println("Weather: " + forecast.get(0));
            System.out.println("Temperature: " + Math.round(Double.parseDouble(forecast.get(1))));
            System.out.println("Feels like: " + Math.round(Double.parseDouble(forecast.get(2))));
            System.out.println("Minimum: " + Math.round(Double.parseDouble(forecast.get(3))));
            System.out.println("Maximum: " + Math.round(Double.parseDouble(forecast.get(4)))+"\n");
        }
    }
}
