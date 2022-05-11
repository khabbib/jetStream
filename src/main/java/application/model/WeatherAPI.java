package application.model;

import java.io.IOException;
import java.net.URI;
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

    public ArrayList<String> weatherForecast(String country) throws IOException, InterruptedException {
        System.out.println(country);
        switch (country) {
            case "United States of America" : country = "USA"; break;
            case "United Kingdom" : country = "England"; break;
            case "New Zealand" : country = "Wellington"; break;
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://community-open-weather-map.p.rapidapi.com/weather?q=" + country.replace(" ","") + "&lat=0&lon=0&callback=test&id=2172797&lang=null&units=metric&mode=xml"))
                .header("X-RapidAPI-Host", "community-open-weather-map.p.rapidapi.com")
                .header("X-RapidAPI-Key", "6df4a5018dmsh154dd0e84d60e4cp1fa50bjsn13abfed7560e")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

        ArrayList<String> forecast = new ArrayList<String>();

        String responseString = response.body().replace("\"", " ");
        if (!response.body().contains("city not found")) {
            List<Integer> integers = new ArrayList<Integer>();
            for (int i = 0; i < responseString.length(); i++) {
                if (responseString.charAt(i) == ' ') {
                    integers.add(i);
                }
            }

            System.out.println(responseString);

            if (responseString.length() > 120) {
                String weather = null;
                if (responseString.contains("rain") && !responseString.contains("light") || responseString.contains("snow") && !responseString.contains("light") || responseString.contains("mist")) {
                    weather = responseString.substring(integers.get(16) + 1, integers.get(17));
                    forecast.add(convert(weather));
                    forecast.add(weather.substring(0, 1).toUpperCase() + weather.substring(1));
                    forecast.add(responseString.substring(integers.get(31) + 2, integers.get(32) - 1));
                    forecast.add(responseString.substring(integers.get(33) + 2, integers.get(34) - 1));
                    forecast.add(responseString.substring(integers.get(35) + 2, integers.get(36) - 1));
                    forecast.add(responseString.substring(integers.get(37) + 2, integers.get(38) - 1));
                } else {
                    weather = responseString.substring(integers.get(16) + 1, integers.get(18));
                    forecast.add(convert(weather));
                    forecast.add(weather.substring(0, 1).toUpperCase() + weather.substring(1));
                    forecast.add(responseString.substring(integers.get(30) + 2, integers.get(31) - 1));
                    forecast.add(responseString.substring(integers.get(32) + 2, integers.get(33) - 1));
                    forecast.add(responseString.substring(integers.get(34) + 2, integers.get(35) - 1));
                    forecast.add(responseString.substring(integers.get(36) + 2, integers.get(37) - 1));
                }
            }
        } else {
            forecast.add("N/A");
            forecast.add("N/A");
            forecast.add("N/A");
            forecast.add("N/A");
            forecast.add("N/A");
            forecast.add("N/A");
        }
        return forecast;
    }

    private String convert(String weather){

        switch (weather) {
            case "clear sky" : weather = "01d"; break;
            case "few clouds" : weather = "02d"; break;
            case "scattered clouds" : weather = "03d"; break;
            case "broken clouds" : weather = "04d"; break;
            case "overcast clouds" : weather = "04d"; break;
            case "shower rain" : weather = "09d"; break;
            case "rain" : weather = "10d"; break;
            case "light rain" : weather = "10d"; break;
            case "thunderstorm" : weather = "11d"; break;
            case "snow" : weather = "13d"; break;
            case "light snow" : weather = "13d"; break;
            case "mist" : weather = "50d"; break;
        }

       return weather;
    }
}
