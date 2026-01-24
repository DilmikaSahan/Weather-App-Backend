package com.App.WeatherApp.controller;

import com.App.WeatherApp.dto.CityRankingDTO;
import com.App.WeatherApp.dto.ForecastDto;
import com.App.WeatherApp.service.CityDataService;
import com.App.WeatherApp.service.ComfortIndexService;
import com.App.WeatherApp.service.WeatherForecastService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.awt.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/weather")
public class WeatherController {
    private final ComfortIndexService comfortIndexService;
    private final WeatherForecastService weatherForecastService;
    private final CityDataService cityDataService;

    public WeatherController(ComfortIndexService comfortIndexService, WeatherForecastService weatherForecastService, CityDataService cityDataService) {
        this.comfortIndexService = comfortIndexService;
        this.weatherForecastService = weatherForecastService;
        this.cityDataService = cityDataService;
    }

    @GetMapping("/weather-info")
    public List<CityRankingDTO> getComfortIndexRanking() {
        return comfortIndexService.getRankedCities()
                .stream()
                .map(city -> {
                    CityRankingDTO dto = new CityRankingDTO();
                    dto.setCityCode(city.getCityCode());
                    dto.setCityName(city.getCityName());
                    dto.setComfortScore(city.getComfortScore());
                    dto.setRank(city.getRank());
                    dto.setDescription(city.getWeatherData().getDescription());
                    dto.setTemperature(city.getWeatherData().getTemperature());
                    dto.setHumidity(city.getWeatherData().getHumidity());
                    dto.setWindSpeed(city.getWeatherData().getWindSpeed());
                    dto.setVisibility(city.getWeatherData().getVisibility()/1000); //divide by 1000 for convert m to Km
                    dto.setIcon(city.getWeatherData().getIcon());
                    return dto;
                })
                .toList();
    }

    @GetMapping("getForecast/{cityCode}")
    public ForecastDto getForecast(@PathVariable String cityCode) {
        return weatherForecastService.getForecastByCityCode(cityCode);
    }
}
