package com.example.weatherapp.model;

public class Weather {
    public final double temperature;
    public final int code;
    public final String description;

    public Weather(double temperature, int code, String description) {
        this.temperature = temperature;
        this.code = code;
        this.description = description;
    }
}
