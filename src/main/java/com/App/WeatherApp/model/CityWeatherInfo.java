package com.App.WeatherApp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CityWeatherInfo {
    private String cityCode;
    private String cityName;
    private WeatherData weatherData;
    private double comfortScore;
    private int rank;
}
