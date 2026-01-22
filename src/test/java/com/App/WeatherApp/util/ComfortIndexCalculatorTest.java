package com.App.WeatherApp.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ComfortIndexCalculatorTest {
    private ComfortIndexCalculator calculator;

    @BeforeEach
    public void setUp() {
        calculator = new ComfortIndexCalculator();

        ReflectionTestUtils.setField(calculator, "tempWeight",0.4);
        ReflectionTestUtils.setField(calculator, "humidityWeight",0.3);
        ReflectionTestUtils.setField(calculator, "windWeight",0.2);
        ReflectionTestUtils.setField(calculator, "cloudWeight",0.1);
    }

    @Test
    void shouldReturnHighScoreForIdealConditions(){
        double temp = 22;
        double humidity = 45;
        double windSpeed = 3;
        double cloudiness = 40;

        double score = calculator.calculateComfortIndex(temp, humidity, windSpeed, cloudiness);

        assertTrue(score >= 90,"Comfort score should be high for ideal weather");
    }
}
