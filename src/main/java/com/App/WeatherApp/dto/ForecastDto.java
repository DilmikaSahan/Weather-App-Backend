package com.App.WeatherApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForecastDto {
    private String cityCode;
    private Map<String, List<ForecastPointDto>> forecasts;

}
