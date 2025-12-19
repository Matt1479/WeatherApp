package com.example.weatherapp.util;

import android.graphics.Color;
import com.example.weatherapp.R;

public class WeatherMapper {

    public static String getDescription(int code) {
        switch (code) {
            case 0:
                return "Clear sky";
            case 1:
                return "Mainly clear";
            case 2:
            case 3:
                return "Cloudy";
            case 45:
            case 48:
                return "Fog";
            case 51:
            case 53:
            case 55:
            case 56:
            case 57:
            case 61:
            case 63:
            case 65:
            case 66:
            case 67:
                return "Rain";
            case 71:
            case 73:
            case 75:
            case 77:
                return "Snow";
            default:
                return "Unknown weather";
        }
    }

    public static int getImage(int code) {
        switch (code) {
            case 0:
                return R.drawable.clearsky;
            case 1:
                return R.drawable.mainlyclear;
            case 2:
            case 3:
                return R.drawable.cloudy;
            case 45:
            case 48:
                return R.drawable.fog;
            case 51:
            case 53:
            case 55:
            case 56:
            case 57:
            case 61:
            case 63:
            case 65:
            case 66:
            case 67:
                return R.drawable.rain;
            case 71:
            case 73:
            case 75:
            case 77:
                return R.drawable.snow;
            default:
                return R.drawable.weather;
        }
    }

    public static int getBackgroundColor(int code) {
        if (code <= 1) {
            // Light-gray
            return Color.argb(32, 128, 128, 128);
        } else if (code <= 48) {
            // Gray
            return Color.argb(64, 128, 128, 128);
        } else {
            // Light-blue
            return Color.argb(64, 0, 128, 128);
        }
    }
}
