package ru.spring.crudtask313.springcrudtask.service;


import java.util.Map;

public interface WeatherService {
    boolean checkRain(String address);
    Map<String, String> getCoordinates(String address);
    String getPrecipitationTypeByCoordinates(Map<String, String> coordinates);
}
