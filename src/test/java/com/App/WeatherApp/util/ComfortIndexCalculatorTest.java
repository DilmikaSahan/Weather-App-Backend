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
    @Test
    void shouldReturnLowScoreForExtremeHeat() {
        double score = calculator.calculateComfortIndex(45, 80, 1, 10); // very hot
        assertTrue(score < 50, "Comfort score should be low for extreme temperature");
    }

    @Test
    void shouldReturnLowScoreForExtremeCold() {
        double score = calculator.calculateComfortIndex(-10, 15, 2, 30);// very cold
        assertTrue(score < 50, "Comfort score should be low for extreme cold");
    }

    @Test
    void shouldReturnLowerScoreForHighHumidity() {
        double score = calculator.calculateComfortIndex(25, 90, 2, 80); // very humid
        assertTrue(score < 70, "Comfort score should drop with high humidity");
    }

    @Test
    void shouldReturnLowerScoreForStrongWind() {
        double score = calculator.calculateComfortIndex(23, 20, 15, 60); // very windy
        assertTrue(score < 70, "Comfort score should drop with strong wind");
    }

    @Test
    void shouldReturnLowerScoreForOvercastConditions() {
        double score = calculator.calculateComfortIndex(22, 80, 3, 100); // very cloudy
        System.out.println(score);
        assertTrue(score < 90, "Comfort score should drop for heavy clouds");
    }

    @Test
    void shouldHandleZeroValuesGracefully() {
        double score = calculator.calculateComfortIndex(0, 0, 0, 0);
        assertTrue(score >= 0, "Score should not be negative");
    }

    @Test
    void shouldHandleNegativeValuesGracefully() {
        double score = calculator.calculateComfortIndex(-5, -20, -3, -50);
        assertTrue(score >= 0, "Score should not be negative even for negative input");
    }

    @Test
    void shouldReturnRoundedScore() {
        double score = calculator.calculateComfortIndex(22, 45, 3, 40);
        long rounded = Math.round(score);
        assertTrue(rounded >= 90, "Rounded score should still be high for ideal weather");
    }
}
