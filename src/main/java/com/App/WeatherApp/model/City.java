package com.App.WeatherApp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class City {
    private String CityCode;
    private String CityName;
    private String Temp;    // Optional, API will overwrite
    private String Status;  // Optional, API will overwrite
}
