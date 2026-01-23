package com.App.WeatherApp.service;

import com.App.WeatherApp.dto.ForecastDto;
import com.App.WeatherApp.dto.ForecastPointDto;
import com.App.WeatherApp.exception.WeatherApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class WeatherForecastService {
    private final WebClient webClient;

    @Value("${openweathermap.api.key}")
    private String apiKey;

    @Value("${openweathermap.api.base-url}")
    private String baseUrl;

    public WeatherForecastService(WebClient webClient) {
        this.webClient = webClient;
    }
    @Cacheable(value = "forecastCache",key = "#cityCode",cacheManager = "forecastCacheManager")
    public ForecastDto getForecastByCityCode(String cityCode){
        OpenWeatherForecastResponse response = webClient.get()
                .uri(baseUrl + "/forecast?id={id}&appid={key}&units=metric", cityCode, apiKey)
                .retrieve()
                .bodyToMono(OpenWeatherForecastResponse.class)
                .block();
        if(response == null || response.list==null){
            throw new WeatherApiException("Empty forecast response for city" + cityCode);
        }
        Map<String, List<ForecastPointDto>> groupedForecasts =
                response.list.stream()
                        .collect(Collectors.groupingBy(
                                f -> f.dt_txt.substring(0, 10), // YYYY-MM-DD
                                Collectors.mapping(
                                        f -> new ForecastPointDto(
                                                f.dt_txt.substring(11), // HH:mm:ss
                                                f.main.temp
                                        ),
                                        Collectors.toList()
                                )
                        ));
        ForecastDto forecastDto = new ForecastDto();
        forecastDto.setCityCode(cityCode);
        forecastDto.setForecasts(groupedForecasts);
        return forecastDto;
    }
    static class OpenWeatherForecastResponse{
        public List<ForecastItem> list;

        static class ForecastItem{
            public Main main;
            public String dt_txt;

            static class Main{
                public double temp;
            }
        }
    }
}
