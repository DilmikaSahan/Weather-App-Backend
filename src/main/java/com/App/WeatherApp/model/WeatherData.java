package com.App.WeatherApp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeatherData {
    private double temperature;  // Celsius
    private int humidity;        // %
    private double windSpeed;    // m/s
    private int cloudiness;      // %
    private int pressure;        // hPa
    private int visibility;      // meters
    private String description;  // Weather description
}
