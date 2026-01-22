package com.App.WeatherApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CityRankingDTO {
    private String cityCode;
    private String cityName;
    private String description;
    private double temperature;
    private double comfortScore;
    private int humidity;
    private double windSpeed;
    private int visibility;
    private int rank;
    private String icon;
}
