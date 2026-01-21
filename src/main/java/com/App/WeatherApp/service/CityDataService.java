package com.App.WeatherApp.service;

import com.App.WeatherApp.exception.CityNotFoundException;
import com.App.WeatherApp.model.City;
import com.App.WeatherApp.util.JsonParser;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityDataService {
    private final JsonParser jsonParser;
    private static final String CITY_FILE = "cities.json";

    public CityDataService(JsonParser jsonParser) {
        this.jsonParser = jsonParser;
    }

    public List<City> getAllCities(){
        try {
            return jsonParser.parseCitiesJson(CITY_FILE);
        } catch (Exception e){
            throw new CityNotFoundException("Unable to load cities.json",e);
        }
    }
}
