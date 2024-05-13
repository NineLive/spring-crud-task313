package ru.spring.crudtask313.springcrudtask.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Coordinates {
    private String latitude;
    private String longitude;
}
