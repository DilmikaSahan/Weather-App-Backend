package com.App.WeatherApp.util;

import com.App.WeatherApp.model.City;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
public class JsonParser {
    private final ObjectMapper mapper = new ObjectMapper();

    public List<City> parseCitiesJson (String filename) throws Exception {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(filename);
        if (is == null) {
            throw new Exception("File not found" + filename);
        }
        JsonNode rootNode = mapper.readTree(is);
        JsonNode listNode = rootNode.get("List");

        if (listNode == null || !listNode.isArray()) {
            throw new Exception("Invalid JSON structure: missing 'List' array");
        }

        List<City> cities = new ArrayList<>();
        for (JsonNode node : listNode) {
            City city = mapper.treeToValue(node, City.class);
            cities.add(city);
        }
        return cities;
    }
}
