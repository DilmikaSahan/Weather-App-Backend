package com.App.WeatherApp.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ComfortIndexCalculator {
    @Value("${comfort.index.temperature.weight}")
    private double tempWeight;

    @Value("${comfort.index.humidity.weight}")
    private double humidityWeight;

    @Value("${comfort.index.wind.weight}")
    private double windWeight;

    @Value("${comfort.index.cloudiness.weight}")
    private double cloudWeight;

    /**
     * Computes Comfort Index from parameters
     *
     * @param temperature Celsius
     * @param humidity percentage 0-100
     * @param windSpeed m/s
     * @param cloudiness percentage 0-100
     * @return Comfort Index score 0-100
     */

    public double calculateComfortIndex(double temperature, double humidity, double windSpeed, double cloudiness) {
        // Normalize parameters to 0-100 scale
        double tempScore = normalizeTemperature(temperature);
        double humidityScore = 100 - humidity; // lower humidity = more comfortable
        double windScore = normalizeWind(windSpeed);
        double cloudScore = 100 - cloudiness; // less clouds = more comfortable

        // Weighted sum
        double comfortIndex = (tempScore * tempWeight) +
                (humidityScore * humidityWeight) +
                (windScore * windWeight) +
                (cloudScore * cloudWeight);

        // Clamp to 0-100
        return Math.max(0, Math.min(100, comfortIndex));

    }
    private double normalizeTemperature(double temp) {
        // Assuming ideal temperature = 22°C, penalize deviation
        double ideal = 22.0;
        double diff = Math.abs(temp - ideal);
        double score = 100 - (diff * 3); // every degree off reduces score
        return Math.max(0, Math.min(100, score));
    }

    private double normalizeWind(double wind) {
        // Ideal wind 0-3 m/s, penalize higher wind
        double score = 100;
        if (wind > 3) {
            score -= (wind - 3) * 15; // each m/s above 3 reduces score
        }
        return Math.max(0, Math.min(100, score));
    }

}
