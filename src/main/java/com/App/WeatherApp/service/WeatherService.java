package com.App.WeatherApp.service;

import com.App.WeatherApp.exception.WeatherApiException;
import com.App.WeatherApp.model.City;
import com.App.WeatherApp.model.WeatherData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class WeatherService {
    private final WebClient webClient;

    public WeatherService(WebClient webClient) {
        this.webClient = webClient;
    }
    @Value("${openweathermap.api.key}")
    private String apiKey;

    @Value("${openweathermap.api.base-url}")
    private String baseUrl;

    @Cacheable(value = "weatherCache",key="#city.cityCode")
    public WeatherData fetchWeather(City city){
        try {
            var response = webClient.get()
                    .uri(baseUrl+"/weather?id={id}&appid={key}&units=metric", city.getCityCode(),apiKey)
                    .retrieve()
                    .bodyToMono(OpenWeatherResponse.class)
                    .block();
            if(response == null){
                throw new WeatherApiException("Empty response from Weather API");
            }
            return mapToWeatherData(response);
        }catch (Exception e){
            throw new WeatherApiException("Failed to fetch weather for "+city.getCityName(),e);

        }
    }
    private WeatherData mapToWeatherData(OpenWeatherResponse r){
        WeatherData data = new WeatherData();
        data.setTemperature(r.main.temp);
        data.setHumidity(r.main.humidity);
        data.setPressure(r.main.pressure);
        data.setVisibility(r.visibility);
        data.setWindSpeed(r.wind.speed);
        data.setCloudiness(r.clouds.all);
        data.setDescription(r.weather.get(0).description);
        return data;
    }
    static class OpenWeatherResponse {
        public Main main;
        public Wind wind;
        public Clouds clouds;
        public int visibility;
        public java.util.List<Weather> weather;

        static class Main {
            public double temp;
            public int humidity;
            public int pressure;
        }

        static class Wind {
            public double speed;
        }

        static class Clouds {
            public int all;
        }

        static class Weather {
            public String description;
        }
    }
}
