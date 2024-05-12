package ru.spring.crudtask313.springcrudtask.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeatherDTO {
    private String address;
    private Boolean hasRain;
}
