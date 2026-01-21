package com.App.WeatherApp.service;

import com.App.WeatherApp.model.CityWeatherInfo;
import com.App.WeatherApp.model.WeatherData;
import com.App.WeatherApp.util.ComfortIndexCalculator;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

@Service
public class ComfortIndexService {
    private final CityDataService cityDataService;
    private final WeatherService weatherService;
    private final ComfortIndexCalculator calculator;

    public ComfortIndexService(CityDataService cityDataService, WeatherService weatherService, ComfortIndexCalculator calculator) {
        this.cityDataService = cityDataService;
        this.weatherService = weatherService;
        this.calculator = calculator;
    }

    @Cacheable(value = "comfortIndexCache")
    public List<CityWeatherInfo> getComfortIndex() {
        List<CityWeatherInfo> results = cityDataService.getAllCities()
                .stream()
                .map(city -> {
                    WeatherData weather = weatherService.fetchWeather(city);
                    double score = calculator.calculateComfortIndex(
                            weather.getTemperature(),
                            weather.getHumidity(),
                            weather.getWindSpeed(),
                            weather.getCloudiness()
                    );
                    CityWeatherInfo info = new CityWeatherInfo();
                    info.setCityCode(city.getCityCode());
                    info.setCityName(city.getCityName());
                    info.setWeatherData(weather);
                    info.setComfortScore(score);
                    return info;
                })
                .sorted(Comparator.comparingDouble(CityWeatherInfo::getComfortScore).reversed()).toList();

        IntStream.range(0, results.size()).forEach(i -> results.get(i).setRank(i + 1));
        return results;
    }
}
