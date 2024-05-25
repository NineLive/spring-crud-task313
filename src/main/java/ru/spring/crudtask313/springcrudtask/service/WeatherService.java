package ru.spring.crudtask313.springcrudtask.service;


import ru.spring.crudtask313.springcrudtask.util.Coordinates;

public interface WeatherService {
    boolean checkRain(String address);

    String getPrecipitationTypeByCoordinates(Coordinates coordinates);
}
