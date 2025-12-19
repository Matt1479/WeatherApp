package com.example.weatherapp;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.example.weatherapp.data.WeatherRepository;
import com.example.weatherapp.model.Weather;
import com.example.weatherapp.util.WeatherMapper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private LinearLayout rootLayout;
    private EditText cityEditText;
    private TextView temperatureTextView, descriptionTextView;
    private ImageView weatherImageView;
    private Button getWeatherButton;
    private ProgressBar loadingProgressBar;

    private final ExecutorService executor =
            Executors.newSingleThreadExecutor();
    private final WeatherRepository repo =
            new WeatherRepository();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        getWeatherButton.setOnClickListener((view) -> {
            loadWeather();
        });
    }

    private void initViews() {
        rootLayout = findViewById(R.id.rootLayout);
        cityEditText = findViewById(R.id.cityEditText);
        temperatureTextView = findViewById(R.id.temperatureTextView);
        descriptionTextView = findViewById(R.id.descriptionTextView);
        weatherImageView = findViewById(R.id.weatherImageView);
        getWeatherButton = findViewById(R.id.getWeatherButton);
        loadingProgressBar = findViewById(R.id.loadingProgressBar);
    }

    private void showLoading() {
        loadingProgressBar.setVisibility(View.VISIBLE);
        getWeatherButton.setEnabled(false);
    }

    private void hideLoading() {
        loadingProgressBar.setVisibility(View.INVISIBLE);
        getWeatherButton.setEnabled(true);
    }

    private void loadWeather() {
        String city = cityEditText.getText().toString().trim();

        if (city.isEmpty()) {
            showToast("Please enter a city");
            return;
        }

        showLoading();

        executor.execute(() -> {
            try {
                Weather weather = repo.getWeather(city);

                runOnUiThread(() -> {
                    updateUI(weather);
                });

            } catch (Exception e) {
                runOnUiThread(() -> {
                    showError(e);
                });
            } finally {
                runOnUiThread(() -> {
                    hideLoading();
                });
            }
        });
    }

    private void updateUI(Weather weather) {
        temperatureTextView.setText(weather.temperature + " °C");
        descriptionTextView.setText(weather.description);

        rootLayout.setBackgroundColor(
                WeatherMapper.getBackgroundColor(weather.code)
        );

        weatherImageView.setImageResource(
                WeatherMapper.getImage(weather.code)
        );
    }

    private void showError(Exception e) {
        resetUI();
        showToast(e.getMessage());
    }

    private void resetUI() {
        temperatureTextView.setText("");
        descriptionTextView.setText("");
        rootLayout.setBackgroundColor(Color.WHITE);
        weatherImageView.setImageResource(R.drawable.weather);
    }

    private void showToast(String message) {
        Toast.makeText(MainActivity.this, message,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdown();
    }
}