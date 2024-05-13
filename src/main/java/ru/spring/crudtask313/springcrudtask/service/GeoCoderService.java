package ru.spring.crudtask313.springcrudtask.service;

import ru.spring.crudtask313.springcrudtask.util.Coordinates;

public interface GeoCoderService {
    Coordinates getCoordinatesByAddress(String address);
}
