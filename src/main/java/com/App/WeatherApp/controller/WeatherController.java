package com.App.WeatherApp.controller;

import com.App.WeatherApp.dto.CityRankingDTO;
import com.App.WeatherApp.service.ComfortIndexService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.awt.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/weather")
public class WeatherController {
    private final ComfortIndexService comfortIndexService;

    public WeatherController(ComfortIndexService comfortIndexService) {
        this.comfortIndexService = comfortIndexService;
    }

    @GetMapping("/comfort-index")
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
}
