package com.example.weatherapp.data;

import com.example.weatherapp.model.Weather;
import com.example.weatherapp.util.WeatherMapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class WeatherRepository {

    public Weather getWeather(String city) throws Exception {
        JSONObject location = fetchLocation(city);
        double lat = location.getDouble("latitude");
        double lon = location.getDouble("longitude");

        return fetchWeather(lat, lon);
    }

    private JSONObject fetchLocation(String city) throws Exception {
        String encodedCity = URLEncoder.encode(
                city,
                StandardCharsets.UTF_8.name()
        );

        String url =
                "https://geocoding-api.open-meteo.com/v1/search?name=" +
                        encodedCity + "&count=1";

        JSONObject json = new JSONObject(request(url));

        if (!json.has("results")) {
            throw new Exception("City not found");
        }

        JSONArray results = json.getJSONArray("results");
        if (results.length() == 0) {
            throw new Exception("City not found");
        }

        return results.getJSONObject(0);
    }

    private Weather fetchWeather(double lat, double lon) throws Exception {
        String url =
                "https://api.open-meteo.com/v1/forecast" +
                        "?latitude=" + lat +
                        "&longitude=" + lon +
                        "&current_weather=true";

        JSONObject json = new JSONObject(request(url));
        JSONObject current = json.getJSONObject("current_weather");

        double temp = current.getDouble("temperature");
        int code = current.getInt("weathercode");

        return new Weather(
                temp,
                code,
                WeatherMapper.getDescription(code)
        );
    }

    private String request(String urlString) throws Exception {
        HttpURLConnection conn = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(urlString);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(10_000);
            conn.setReadTimeout(10_000);

            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new Exception("HTTP error" + conn.getResponseCode());
            }

            reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream())
            );

            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            return result.toString();
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
    }
}
