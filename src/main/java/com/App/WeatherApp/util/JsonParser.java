package com.App.WeatherApp.util;

import com.App.WeatherApp.model.City;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
public class JsonParser {
    private final ObjectMapper mapper = new ObjectMapper();

    public List<City> parseCitiesJson(String path) throws Exception {

        ClassPathResource resource = new ClassPathResource(path);

        if (!resource.exists()) {
            throw new Exception("File not found: " + path);
        }

        try (InputStream is = resource.getInputStream()) {

            JsonNode rootNode = mapper.readTree(is);
            JsonNode listNode = rootNode.get("List");

            if (listNode == null || !listNode.isArray()) {
                throw new Exception("Invalid JSON structure: missing 'List' array");
            }

            List<City> cities = new ArrayList<>();
            for (JsonNode node : listNode) {
                cities.add(mapper.treeToValue(node, City.class));
            }

            return cities;
        }
    }
}
