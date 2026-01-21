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
    private double comfortScore;
    private int rank;
}
