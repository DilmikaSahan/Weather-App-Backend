package com.App.WeatherApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeatherResponseDTO {
    private String cityCode;
    private String cityName;
    private double temperature;
    private int humidity;
    private double windSpeed;
    private int cloudiness;
    private String description;
}
