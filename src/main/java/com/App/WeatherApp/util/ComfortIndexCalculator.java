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
     * Formular of Comfort Index Score,
     * -If these conditions are true score will be 100 for each parameter,
     *      Temperature: 22.2°C, score = 100
     *      Humidity : in range 30% - 60%, score = 100
     *      WindSpeed : in range 1.5 - 6.0, score = 100
     *      Cloudiness : in range 30% - 50%, score = 100
     * Comfort_Index_Score = (100 - (Td*4))Tw + (100 - (Hd*4))Hw + (100 - (Wd*4))Ww + (100 - (Cd*4))Cw
     * Td - Temperature difference
     * Hd - Humidity difference
     * Wd - WindSpeed difference
     * Cd - Cloudiness difference
     * Tw - temp Weight
     * Hw - humidity Weight
     * Ww - wind Weight
     * Cw - cloud Weight
     */

    public double calculateComfortIndex(double temperature, double humidity, double windSpeed, double cloudiness) {
        // Normalize parameters to 0-100 scale
        double tempScore = normalizeTemperature(temperature);
        double humidityScore = normalizeHumidity(humidity); // 30%-60% comfortable range
        double windScore = normalizeWind(windSpeed);
        double cloudScore = normalizeCloudiness(cloudiness); // 30–50% Coverage

        // Weighted sum
        double comfortIndex = (tempScore * tempWeight) +
                (humidityScore * humidityWeight) +
                (windScore * windWeight) +
                (cloudScore * cloudWeight);

        // Clamp to 0-100
        return round(Math.max(0, Math.min(100, comfortIndex)));

    }
    private double normalizeTemperature(double temp) {
        // Assuming ideal temperature = 22°C, penalize deviation
        double ideal = 22.0;
        double diff = Math.abs(temp - ideal);
        double score = 100 - (diff * 4);    //Penalty: -5 points for every 1 degree away from 22°C
        return Math.max(0, Math.min(100, score));
    }
    private double normalizeWind(double wind) {
        if(wind>=1.5  && wind<=6.0) return 100;        // 1.5 - 6.0 m/s comfortable range
        double diff = (wind<1.5)?(1.5-wind):(6.0-wind);
        return Math.max(0, 100 - (diff * 12));
    }
    private double normalizeHumidity(double humidity) {
        if(humidity>=30  && humidity<=60) return 100;      // 30%-60% comfortable range
        double diff = (humidity<30)?(30-humidity):(60-humidity);
        return Math.max(0, 100 - (diff * 2.5));
    }
    private double normalizeCloudiness(double cloudiness) {
        if(cloudiness>=30  && cloudiness<=50) return 100;   // 30%-50% comfortable range
        double diff = (cloudiness<30)?(30-cloudiness):(50-cloudiness);
        return Math.max(0, 100 - (diff * 1.5));
    }
    private double round(double value){
        return Math.round(value * 10.0) / 10.0;
    }
}
